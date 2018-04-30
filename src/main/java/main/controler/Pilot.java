package main.controler;

import main.Constants;
import main.reading.AnticipatedThread;
import main.reading.GuidedThread;
import main.reading.HighlightThread;
import main.reading.ReadThread;
import main.reading.SegmentedThread;
import main.view.FenetreParametre;
import main.view.Panneau;

public class Pilot {

	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;
	private Panneau p;
	private ControlerText controler;
	
	public Pilot(Panneau p) {
		this.p = p;
		controler = p.controlerGlobal;
	}
	
	/**
	 * Se place sur le segment de numero n et d�marre le lecteur.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.param.premierSegment - 1 || n >= p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Num�ro de segment invalide : " + n);
		}
		/// d�sactive les boutons de contr�le pour �viter le spam ///
		p.controlFrame.disableAll(Constants.DISABLE_TIME);
		//vire le surlignagerouge
		p.editorPane.enleverSurlignageRouge();
		
		/// emp�che le redimensionnement de la fen�tre lors de la premi�re lecture ///
		p.fenetre.setResizable(false);
		
		//met a jour la barre de progression
		p.progressBar.setValue(n);
		p.progressBar.setString(n+"/"+(p.textHandler.getPhrasesCount()-1));
		
		if (activeThread != null) {
			activeThread.doStop();
		}
		activeThread = getReadThread(n);
		activeThread.onPhraseEnd.add(new Runnable() {
			public void run() {
				/// fin du dernier segment du texte ///
				if (n == p.textHandler.getPhrasesCount() - 2) {
					p.afficherCompteRendu();
				}
				/// passe au segment suivant ///
				else {
					goTo(n + 1);
				}
			}
		});
		activeThread.start();
	}
	
	/**
	 * Essaye de passer au segment suivant, passe � la page suivante
	 * si c'�tait le dernier segment de la page.
	 * D�clenche une erreur si on �tait au dernier segment du texte.
	 */
	public void doNext() {
		goTo(p.player.getCurrentPhraseIndex() + 1);
	}

	/**
	 * Essaye de passer au segment pr�c�dent. D�clenche une erreur si on �tait au premier segment du texte.
	 */
	public void doPrevious() {
		goTo(p.player.getCurrentPhraseIndex() - 1);
	}

	/**
	 * Essaye d'arr�ter l'enregistrement en cours.
	 */
	public void doStop() {
		p.player.stop();
		activeThread.doStop();
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est d�j� d�marr�, reprend depuis
	 * le d�but.
	 */
	public void doPlay() {
		goTo(p.player.getCurrentPhraseIndex());
	}

	/**
	 * Cr�� un processus associ� � la lecture d'un seul segment dans le mode de
	 * lecture actuel.
	 */
	public ReadThread getReadThread(int n) {
		ReadThread t;
		switch (p.param.readMode) {
			case ANTICIPATED:
				t = new AnticipatedThread(controler, n);
				break;
			case GUIDED_READING:
				t = new GuidedThread(controler, n);
				break;
			case NORMAL:
				t = new SegmentedThread(controler, n);
				break;
			case HIGHLIGHT:
				t = new HighlightThread(controler, n);
				break;
			default:
				t = null;
				break;
		}
		return t;
	}
	
	public int getCurrentPhraseIndex() {
		return p.player.getCurrentPhraseIndex();
	}
	
}
