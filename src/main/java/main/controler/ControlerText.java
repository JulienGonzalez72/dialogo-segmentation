package main.controler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyListener;

import main.Constants;
import main.reading.ReadThread;
import main.reading.ReaderFactory;
import main.view.TextPanel;
import main.view.TextFrame;

public class ControlerText {

	private TextPanel p;
	private Pilot pilot;

	/**
	 * Construit un contrôleur à partir de la fenêtre d'exercice correspondante.
	 */
	public ControlerText(TextFrame frame) {
		this.p = frame.getPanel();
		this.pilot = new Pilot(p);
	}

	/**
	 * Construit les pages à partir du segment de numero spécifié.
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
	@Deprecated
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
	@Deprecated
	public long getPhraseDuration(int phrase) {
		p.player.load(phrase);
		return p.player.getDuration();
	}

	/**
	 * Retourne la durée en millisecondes de l'enregistrement courant.
	 */
	@Deprecated
	public long getCurrentPhraseDuration() {
		return p.player.getDuration();
	}

	/**
	 * Retourne le temps d'attente en millisecondes correspondant é l'enregistrement
	 * courant.
	 */
	@Deprecated
	public long getCurrentWaitTime() {
		// TODO remettre le bon temps de pause (avec ReadingParameters)
		return (long) (getCurrentPhraseDuration() * /* p.param.tempsPauseEnPourcentageDuTempsDeLecture / 100. */1);
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
	 * Attends un clic sur un mot du texte.<br/>
	 * Retourne <code>true</code> si le mot correspond au dernier mot du segment n, <code>false</code> si c'est le mauvais mot.
	 * @highlightWrongWord si le mot est surligné en cas d'erreur.
	 */
	public boolean waitForClick(int n) {
		p.controlerMouse.clicking = false;
		while (true) {
			Thread.yield();
			if (p.controlerMouse.clicking) {
				/// cherche la position exacte dans le texte ///
				int offset = p.textHandler.getAbsoluteOffset(p.getFirstShownPhraseIndex(),
						p.editorPane.getCaretPosition());
				/// si le clic est juste ///
				if (p.textHandler.wordPause(offset)
						&& p.textHandler.getPhraseIndex(offset) == pilot.getCurrentPhraseIndex()) {
					return true;
				}
				/// si le clic est faux ///
				else {
					return false;
				}
			}
		}
	}

	/**
	 * Colorie le segment numero n avec la couleur de réussite.
	 */
	public void highlightPhrase(int n) {
		highlightPhrase(p.editorPane.hParam.rightColor, n);
	}
	
	/**
	 * Colorie le segment numero n avec la couleur de correction.
	 */
	public void highlightCorrectionPhrase(int n) {
		highlightPhrase(p.editorPane.hParam.correctionColor, n);
	}
	
	private void highlightPhrase(Color c, int n) {
		if (p.textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
			p.editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, c);
		}
	}

	/**
	 * Supprime tout le surlignage qui se trouve sur le segment <i>n</i>.<br/>
	 */
	public void removeHighlightPhrase(int n) {
		int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(), n);
		int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
		p.editorPane.removeHighlight(debutRelatifSegment, finRelativeSegment);
	}

	/**
	 * 
	 * Arréte l'enregistrement courant et enlève tout le surlignage.
	 */
	@Deprecated
	public void stopAll() {
		p.player.stop();
		p.editorPane.removeAllHighlights();
	}

	/**
	 * Charge un segment de phrase dans le lecteur sans le démarrer.<br>
	 * Pas nécessaire si on démarre le lecteur directement avec la méthode
	 */
	@Deprecated
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
	 * Enlève tout le surlignage présent.
	 */
	public void removeAllHighlights() {
		p.editorPane.removeAllHighlights();
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

	@Deprecated
	public void incrementerErreurSegment() {
		p.nbErreursParSegment++;
	}
	
	/**
	 * Surligne le dernier mot sur lequel le patient a cliqué après avoir enlevé le surlignage d'erreur existant.
	 */
	public void highlightWrongWord() {
		removeWrongHighlights();
		int clickOffset = p.controlerMouse.lastTextOffset;
		int startOffset = p.textHandler.getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.textHandler.startWordPosition(clickOffset) + 1);
		int endOffset = p.textHandler.getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.textHandler.endWordPosition(clickOffset));
		highlight(p.editorPane.hParam.wrongColor, startOffset, endOffset);
	}
	
	private void highlight(Color c, int start, int end) {
		p.editorPane.surlignerPhrase(start, end, c);
	}
	
	/**
	 * Comptabilise une erreur.
	 */
	public void countError() {
		p.nbErreursSegmentCourant++;
		p.nbErreurs++;
	}
	
	/**
	 * Comptabilise une erreur de segment (lorsque le patient atteint le nombre maximal d'erreurs tolérés par segment).
	 */
	public void countPhraseError() {
		p.nbErreursParSegment++;
	}
	
	/**
	 * Retourne <code>true</code> si il reste au moins un essai au patient pour le segment courant.
	 */
	public boolean hasMoreTrials() {
		return p.nbErreursSegmentCourant < p.rParam.toleratedErrors + 1;
	}
	
	/**
	 * Modifie le nombre d'essais tolérés par segment.
	 */
	public void setPhraseTrials(int trials) {
		p.rParam.toleratedErrors = trials;
	}
	
	/**
	 * Affiche le compte rendu de l'exercice.
	 * @param showErrors si le compte rendu affiche le nombre d'erreurs total
	 * @param showPhraseErrors si le compte rendu affiche le nombre d'erreurs de segment
	 */
	public void showReport(boolean showErrors, boolean showPhraseErrors) {
		String message = "<html>L'exercice est terminé.";
		if (showErrors) {
			message += "<br/>Le patient a fait " + p.nbErreurs + (p.nbErreurs > 1 ? "s" : "") + ".";
		}
		if (showPhraseErrors) {
			message += "<br/>Le patient a fait " + p.nbErreurs + (p.nbErreurs > 1 ? "s" : "") + " de segment.";
		}
		message += "</html>";
		p.afficherCompteRendu(message);
	}

	/**
	 * Se place sur le segment de numero <i>n</i> et démarre l'algorithme de lecture.
	 * @throws IllegalArgumentException si <i>n</i> est inférieur au numéro du premier segment
	 * ou supérieur au nombre de segments dans le texte,
	 * ou si le thread de lecture n'a pas été chargé avec {@link #loadReadThread(ReadThread)}.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		pilot.goTo(n);
	}

	/**
	 * Essaye de passer au segment suivant, passe à la page suivante si c'était le
	 * dernier segment de la page. Déclenche une erreur si on était au dernier
	 * segment du texte.
	 */
	public void doNext() {
		pilot.doNext();
	}

	/**
	 * Essaye de passer au segment précédent. Déclenche une erreur si on était au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		pilot.doPrevious();
	}

	/**
	 * Essaye d'arréter l'enregistrement en cours.
	 */
	public void doStop() {
		pilot.doStop();
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est déjà démarré, reprend depuis
	 * le début.
	 */
	public void doPlay() {
		pilot.doPlay();
	}

	/**
	 * Applique un Font à l'exercice.
	 */
	public void setFont(Font f) {
		p.editorPane.setFont(f);
	}
	
	/**
	 * Change la taille de la police.
	 */
	public void setFontSize(float fontSize) {
		setFont(p.editorPane.getFont().deriveFont(fontSize));
	}
	
	/**
	 * Modifie les couleurs de surlignage pour l'exercice.<br/>
	 * Certaines couleurs peuvent être initiliasées à <code>null</code> si elles ne sont pas utilisées dans l'exercice.
	 * Ne peut pas fonctionner si du texte a déjà été surligné.
	 * @param rightColor couleur de surlignage pour les segments passés avec succès
	 * @param wrongColor couleur de surlignage pour les erreurs
	 * @param correctionColor couleur de surlignage pour les segments corrigés
	 */
	public void setHighlightColors(Color rightColor, Color wrongColor, Color correctionColor) {
		p.editorPane.hParam.rightColor = rightColor;
		p.editorPane.hParam.wrongColor = wrongColor;
		p.editorPane.hParam.correctionColor = correctionColor;
	}

	/**
	 * Retourne le numéro du segment courant (en partant de 0).
	 */
	public int getCurrentPhraseIndex() {
		return pilot.getCurrentPhraseIndex();
	}
	
	/**
	 * Charge une usine de lecture (nécessaire pour appeler la méthode {@link #goTo(int)}).
	 */
	public void setReaderFactory(ReaderFactory readerFactory) {
		pilot.setReaderFactory(readerFactory);
	}
	
	/**
	 * Mets à jour la barre de progression et le numéro du segment en cours.<br/>
	 * A effectuer au début de chaque segment traité, mais pas plus d'une fois par segment.
	 */
	public void updateCurrentPhrase() {
		pilot.updateCurrentPhrase();
		p.nbErreursSegmentCourant = 0;
	}
	
	/**
	 * Active ou désactive les contrôles clavier
	 * (touche gauche pour répéter le segment, touche espace pour arrêter/recommencer).
	 */
	public void setKeyEnabled(boolean keyEnabled) {
		/// ajoute un contrôle clavier ///
		if (keyEnabled && p.getKeyListeners().length == 0) {
			ControlerKey controlerKey = new ControlerKey(pilot);
			p.editorPane.addKeyListener(controlerKey);
		}
		/// retire les contrôles clavier ///
		else if (!keyEnabled && p.getKeyListeners().length >= 1) {
			for (KeyListener listener : p.getKeyListeners()) {
				p.removeKeyListener(listener);
			}
		}
	}

}
