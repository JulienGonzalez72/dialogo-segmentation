package main.controler;

import java.awt.Color;
import java.awt.Cursor;

import main.Constants;
import main.reading.*;
import main.view.FenetreParametre;
import main.view.Panneau;

//import javax.swing.SwingWorker;

public class ControlerGlobal {

	public Panneau p;
	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;

	/**
	 * Construit un contr�leur � partir du panneau correspondant.
	 */
	public ControlerGlobal(Panneau p) {
		this.p = p;
	}
	
	/**
	 * Se place sur le segment de numero n et d�marre le lecteur.
	 */
	 public void goTo(int n) throws IllegalArgumentException {	
		if (n < p.param.premierSegment - 1 || n >= p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Num�ro de segment invalide : " + n);
		}
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
	 * Construit les pages � partir du segment de numero sp�cifi�.
	 */
	public void buildPages(int startPhrase) {
		p.buildPages(startPhrase);
	}

	/**
	 * Affiche la page indiqu�e.
	 */
	public void showPage(int page) {
		p.showPage(page);
	}
	
	/**
	 * Joue un fichier .wav correspondant � un segment de phrase.
	 * On sortira de cette fonction lorsque le fichier .wav aura �t� totalement jou�.
	 */
	public void play(int phrase) {
		p.setCursor(Constants.CURSOR_LISTEN);
		p.player.play(phrase);
		while (true) {
			if (p.player.isPhraseFinished()) {
				p.setCursor(Cursor.getDefaultCursor());
				break;
			}
			/// fixe toujours le curseur d'�coute pendant toute la dur�e de l'enregistrement ///
			else if (!p.getCursorName().equals(Constants.CURSOR_SPEAK)) {
				p.setCursor(Constants.CURSOR_LISTEN);
			}
		}
	}

	/**
	 * Retourne le nombre de segments total du texte.
	 */
	public int getPhrasesCount() {
		return p.textHandler.getPhrasesCount();
	}

	/**
	 * Retourne la dur�e en millisecondes de l'enregistrement qui correspond au segment de phrase indiqu�.
	 */
	public long getPhraseDuration(int phrase) {
		p.player.load(phrase);
		return p.player.getDuration();
	}

	/**
	 * Retourne la dur�e en millisecondes de l'enregistrement courant.
	 */
	public long getCurrentPhraseDuration() {
		return p.player.getDuration();
	}

	/**
	 * Retourne le temps d'attente en millisecondes correspondant � l'enregistrement courant.
	 */
	public long getCurrentWaitTime() {
		return (long) (getCurrentPhraseDuration() * p.param.tempsPauseEnPourcentageDuTempsDeLecture / 100.);
	}

	/**
	 * Mets en pause le thread courant pendant un certain temps.
	 * 
	 * @param time
	 *            le temps de pause, en millisecondes
	 * @param cursorName
	 *            le type de curseur � d�finir pendant l'attente (peut �tre
	 *            Constants.CURSOR_SPEAK ou Constants.CURSOR_LISTEN)
	 */
	public void doWait(long time, String cursorName) {
		try {
			p.setCursor(cursorName);
			Thread.sleep(time);
			p.setCursor(Cursor.getDefaultCursor());
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Attente d�un clic de la souris sur le dernier mot du segment.
	 * <ul>
	 * <li>Param�tre d�entr�e�1: Num�ro de segment</li>
	 * <li>Param�tre de sortie�: True ou False (r�ussite)</li>
	 * <li>On sort de cette fonction lorsqu�un clic a �t� r�alis�.
	 * Si le clic a �t� r�alis� sur le bon mot on sort avec true, et si le clic a �t� r�alis� sur une partie erron�e,
	 * on surligne cette partie avec une couleur qui indique une erreur, Rouge ? En param�tre ?
	 * Et on sort avec False.
	 * </ul>
	 */
	public boolean waitForClick(int n) {
		p.controlerMouse.clicking = false;
		while (true) {
			Thread.yield();
			if (p.controlerMouse.clicking) {
				/// cherche la position exacte dans le texte ///
				int offset = p.textHandler.getAbsoluteOffset(p.getNumeroPremierSegmentAffich�(),p.editorPane.getCaretPosition());
				/// si le clic est juste ///
				if (p.textHandler.wordPause(offset) && p.textHandler.getPhraseIndex(offset) == p.player.getCurrentPhraseIndex()) {
					return true;
				}
				/// si le clic est faux ///
				else {
					/// indique l'erreur en rouge ///
					p.indiquerErreur(
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffich�(),
									p.textHandler.startWordPosition(offset) + 1),
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffich�(),
									p.textHandler.endWordPosition(offset)));
					return false;
				}
			}
		}
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void highlightPhrase(Color c, int n) {
		if (p.textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffich�(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
			p.editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, c);
		}
	}

	/**
	 * Supprime le surlignage qui se trouve sur le segment n. Ne fait rien si ce
	 * segment n'est pas surlign�.
	 */
	public void removeHighlightPhrase(int n) {
		int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffich�(), n);
		int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
		p.editorPane.removeHighlight(debutRelatifSegment, finRelativeSegment);
	}

	/**
	 * Arr�te l'enregistrement courant et enl�ve tout le surlignage.
	 */
	public void stopAll() {
		p.player.stop();
		p.editorPane.d�surlignerTout();
	}
	
	/**
	 * Charge un segment de phrase dans le lecteur sans le d�marrer.<br>
	 * Pas n�cessaire si on d�marre le lecteur directement avec la m�thode {@link #play}.
	 */
	public void loadSound(int phrase) {
		p.player.load(phrase);
	}

	/**
	 * Enl�ve tout le surlignage d'erreur.
	 */
	public void removeWrongHighlights() {
		p.editorPane.enleverSurlignageRouge();
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
	 * Retourne la page qui contient le segment, ou -1 si le segment n'existe pas.
	 */
	public int getPageOfPhrase(int n) {
		int numeroPage = -1;
		for (Integer i : p.segmentsEnFonctionDeLaPage.keySet()) {
			if (p.segmentsEnFonctionDeLaPage.get(i).contains(n)) {
				numeroPage = i;
				break;
			}
		}
		return numeroPage;
	}

	/**
	 * Surligne tout depuis le d�but de la page jusqu'au segment de phrase indiqu�.
	 */
	public void highlightUntilPhrase(Color c, int n) {
		p.surlignerJusquaSegment(c, n);
	}

	/**
	 * Cr�� un processus associ� � la lecture d'un seul segment dans le mode de
	 * lecture actuel.
	 */
	public ReadThread getReadThread(int n) {
		ReadThread t;
		switch (p.param.readMode) {
			case ANTICIPATED:
				t = new AnticipatedThread(this, n);
				break;
			case GUIDED_READING:
				t = new GuidedThread(this, n);
				break;
			case NORMAL:
				t = new SegmentedThread(this, n);
				break;
			case HIGHLIGHT:
				t = new HighlightThread(this, n);
				break;
			default:
				t = null;
				break;
		}
		return t;
	}

	public void incrementerErreurSegment() {
		p.nbErreursParSegment++;	
	}

}
