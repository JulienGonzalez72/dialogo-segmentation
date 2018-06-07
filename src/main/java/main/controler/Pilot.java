package main.controler;

import main.reading.AnticipatedThread;
import main.reading.GuidedThread;
import main.reading.HighlightThread;
import main.reading.ReadThread;
import main.reading.SegmentedThread;
import main.view.TextPanel;

// TODO classe non fonctionnelle
public class Pilot {

	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;
	private TextPanel p;
	private ControlerText controler;
	/**
	 * Segment actuel
	 */
	private int phrase;
	
	public Pilot(TextPanel p) {
		this.p = p;
		controler = p.controlerGlobal;
	}
	
	/**
	 * Se place sur le segment de numero n et d�marre le lecteur.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.param.startingPhrase - 1 || n >= p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Numéro de segment invalide : " + n);
		}
		phrase = n;
		//vire le surlignagerouge
		p.editorPane.enleverSurlignageRouge();
		
		/// empêche le redimensionnement de la fenêtre lors de la première lecture ///
		p.fenetre.setResizable(false);
		
		//met a jour la barre de progression
		updateBar();
		
		if (activeThread != null) {
			activeThread.doStop();
		}
		activeThread = getReadThread(n);
		activeThread.onPhraseEnd.add(new Runnable() {
			public void run() {
				phrase = activeThread.N;
				/// fin du dernier segment du texte ///
				if (phrase == p.textHandler.getPhrasesCount() - 1) {
					p.afficherCompteRendu();
				}
				/// met � jour la barre de progression ///
				else {
					updateBar();
				}
			}
		});
		activeThread.start();
	}
	
	private void updateBar() {
		p.progressBar.setValue(getCurrentPhraseIndex());
		p.progressBar.setString((getCurrentPhraseIndex() + 1) + "/" + (p.textHandler.getPhrasesCount() - 1));
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
		if (activeThread != null) {
			activeThread.doStop();
		}
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
		ReadThread t = null;
		/*switch (p.param.readMode) {
			case ANTICIPE:
				t = new AnticipatedThread(controler, n);
				break;
			case GUIDEE:
				t = new GuidedThread(controler, n);
				break;
			case SEGMENTE:
				t = new SegmentedThread(controler, n);
				break;
			case SUIVI:
				t = new HighlightThread(controler, n);
				break;
			default:
				t = null;
				break;
		}
		t.param = p.param;*/
		return t;
	}
	
	public int getCurrentPhraseIndex() {
		return phrase;
	}
	
	public boolean isPlaying() {
		return p.player.isPlaying();
	}
	
	public boolean hasPreviousPhrase() {
		return p.player.hasPreviousPhrase();
	}
	
}
