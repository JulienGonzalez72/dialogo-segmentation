package main.controler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import main.Constants;
import main.view.TextPanel;

public class ControlerText {

	private TextPanel p;

	/**
	 * Construit un contrôleur à partir du panneau correspondant.
	 */
	public ControlerText(TextPanel p) {
		this.p = p;
	}

	/**
	 * Construit les pages é partir du segment de numero spécifié.
	 */
	public void buildPages(int startPhrase) {
		p.buildPages(startPhrase);
	}

	/**
	 * Affiche la page indiquée.
	 */
	public void showPage(int page) {
		p.showPage(page);
	}

	/**
	 * Joue un fichier .wav correspondant é un segment de phrase. On sortira de
	 * cette fonction lorsque le fichier .wav aura été totalement joué. METHODE DE
	 * TEST
	 */
	public void play(int phrase) {
		p.setCursor(Constants.CURSOR_LISTEN);
		p.player.play(phrase);
		while (true) {
			if (p.player.isPhraseFinished()) {
				p.setCursor(Cursor.getDefaultCursor());
				break;
			}
			/// fixe toujours le curseur d'écoute pendant toute la durée de l'enregistrement
			/// ///
			if (!p.getCursorName().equals(Constants.CURSOR_LISTEN)) {
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
	 * Retourne la durée en millisecondes de l'enregistrement qui correspond au
	 * segment de phrase indiqué.
	 */
	public long getPhraseDuration(int phrase) {
		p.player.load(phrase);
		return p.player.getDuration();
	}

	/**
	 * Retourne la durée en millisecondes de l'enregistrement courant.
	 */
	public long getCurrentPhraseDuration() {
		return p.player.getDuration();
	}

	/**
	 * Retourne le temps d'attente en millisecondes correspondant é l'enregistrement
	 * courant.
	 */
	public long getCurrentWaitTime() {
		// TODO remettre le bon temps de pause (avec ReadingParameters)
		return (long) (getCurrentPhraseDuration() * /*p.param.tempsPauseEnPourcentageDuTempsDeLecture / 100.*/1);
	}

	/**
	 * Mets en pause le thread courant pendant un certain temps.
	 * 
	 * @param time
	 *            le temps de pause, en millisecondes
	 * @param cursorName
	 *            le type de curseur à définir pendant l'attente (peut étre
	 *            Constants.CURSOR_SPEAK ou Constants.CURSOR_LISTEN)
	 */
	public void doWait(long time, String cursorName) {
		try {
			p.setCursor(cursorName);
			Thread.sleep(time);
			p.setCursor(Cursor.getDefaultCursor());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Attente d'un clic de la souris sur le dernier mot du segment.
	 * <ul>
	 * <li>Paramétre déentréeé1: Numéro de segment</li>
	 * <li>Paramétre de sortieé: True ou False (réussite)</li>
	 * <li>On sort de cette fonction lorsquéun clic a été réalisé. Si le clic a été
	 * réalisé sur le bon mot on sort avec true, et si le clic a été réalisé sur une
	 * partie erronée, on surligne cette partie avec une couleur paramétrée. Et on
	 * sort avec False.
	 * </ul>
	 */
	public boolean waitForClick(int n) {
		p.controlerMouse.clicking = false;
		while (true) {
			Thread.yield();
			if (p.controlerMouse.clicking) {
				/// cherche la position exacte dans le texte ///
				int offset = p.textHandler.getAbsoluteOffset(p.getNumeroPremierSegmentAffiché(),
						p.editorPane.getCaretPosition());
				/// si le clic est juste ///
				if (p.textHandler.wordPause(offset)
						&& p.textHandler.getPhraseIndex(offset) == p.player.getCurrentPhraseIndex()) {
					return true;
				}
				/// si le clic est faux ///
				else {
					/// indique l'erreur en rouge ///
					p.indiquerErreur(
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
									p.textHandler.startWordPosition(offset) + 1),
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
									p.textHandler.endWordPosition(offset)));
					return false;
				}
			}
		}
	}

	/**
	 * Colorie le segment numero n en couleur c.
	 */
	public void highlightPhrase(Color c, int n) {
		n--;
		if (p.textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffiché(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
			p.editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, c);
		}
	}

	/**
	 * Supprime le surlignage qui se trouve sur le segment n. Ne fait rien si ce
	 * segment n'est pas surligné.
	 * 
	 */
	public void removeHighlightPhrase(int n) {
		int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffiché(), n);
		int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
		p.editorPane.removeHighlight(debutRelatifSegment, finRelativeSegment);
	}

	/**
	 * 
	 * Arréte l'enregistrement courant et enlève tout le surlignage.
	 */
	public void stopAll() {
		p.player.stop();
		p.editorPane.désurlignerTout();
	}

	/**
	 * Charge un segment de phrase dans le lecteur sans le démarrer.<br>
	 * Pas nécessaire si on démarre le lecteur directement avec la méthode
	 */
	public void loadSound(int phrase) {
		p.player.load(phrase);
	}

	/**
	 * Enlève tout le surlignage d'erreur.
	 */
	public void removeWrongHighlights() {
		p.editorPane.enleverSurlignageRouge();
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
	 * Surligne tout depuis le début de la page jusqu'au segment de phrase indiqué.
	 */
	public void highlightUntilPhrase(Color c, int n) {
		p.surlignerJusquaSegment(c, n);
	}

	public void incrementerErreurSegment() {
		p.nbErreursParSegment++;
	}

	/**
	 * Se place sur le segment de numero n et d�marre le lecteur.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		p.pilot.goTo(n);
	}

	/**
	 * Essaye de passer au segment suivant, passe à la page suivante si c'était le
	 * dernier segment de la page. Déclenche une erreur si on était au dernier
	 * segment du texte.
	 */
	public void doNext() {
		p.pilot.doNext();
	}

	/**
	 * Essaye de passer au segment précédent. Déclenche une erreur si on était au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		p.pilot.doPrevious();
	}

	/**
	 * Essaye d'arréter l'enregistrement en cours.
	 */
	public void doStop() {
		p.pilot.doStop();
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est déjà démarré, reprend depuis
	 * le début.
	 */
	public void doPlay() {
		p.pilot.doPlay();
	}

	/**
	 * Applique un Font à l'exercice
	 */
	public void setFont(Font f) {
		p.editorPane.setFont(f);
	}

}
