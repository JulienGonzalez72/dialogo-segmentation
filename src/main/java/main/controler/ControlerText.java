package main.controler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyListener;

import javax.swing.text.BadLocationException;

import main.Constants;
import main.reading.ReadThread;
import main.reading.ReaderFactory;
import main.view.Mask;
import main.view.TextFrame;
import main.view.TextPanel;

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
	 * Retourne <code>true</code> si le mot correspond au dernier mot du segment n,
	 * <code>false</code> si c'est le mauvais mot.
	 * 
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
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(), n);
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
		for (Integer i : p.phrasesInFonctionOfPages.keySet()) {
			if (p.phrasesInFonctionOfPages.get(i).contains(n)) {
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
	 * Surligne le dernier mot sur lequel le patient a cliqué après avoir enlevé le
	 * surlignage d'erreur existant.
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
	 * Comptabilise une erreur de segment (lorsque le patient atteint le nombre
	 * maximal d'erreurs tolérés par segment).
	 */
	public void countPhraseError() {
		p.nbErreursParSegment++;
	}

	/**
	 * Retourne <code>true</code> si il reste au moins un essai au patient pour le
	 * segment courant.
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
	 * 
	 * @param showErrors
	 *            si le compte rendu affiche le nombre d'erreurs total
	 * @param showPhraseErrors
	 *            si le compte rendu affiche le nombre d'erreurs de segment
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
	 * Se place sur le segment de numero <i>n</i> et démarre l'algorithme de
	 * lecture.
	 * 
	 * @throws IllegalArgumentException
	 *             si <i>n</i> est inférieur au numéro du premier segment ou
	 *             supérieur au nombre de segments dans le texte, ou si le thread de
	 *             lecture n'a pas été chargé avec
	 *             {@link #loadReadThread(ReadThread)}.
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
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 */
	public void setFont(Font f) throws IllegalArgumentException {
		if (pilot.hasStarted()) {
			throw new IllegalArgumentException("Police inchangeable après le démarrage de l'exercice !");
		}
		p.editorPane.setFont(f);
	}

	/**
	 * Change la taille de la police.
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 */
	public void setFontSize(float fontSize) throws IllegalArgumentException {
		setFont(p.editorPane.getFont().deriveFont(fontSize));
	}

	/**
	 * Termine l'exercice. Retourne au premier segment de la page.
	 * Permet de pouvoir faire à nouveau les réglages de base de l'exercice.
	 */
	public void end() {
		pilot.end();
		p.rebuildPages();
	}
	
	/**
	 * Modifie les couleurs de surlignage pour l'exercice.<br/>
	 * Certaines couleurs peuvent être initiliasées à <code>null</code> si elles ne
	 * sont pas utilisées dans l'exercice. Ne peut pas fonctionner si du texte a
	 * déjà été surligné.
	 * 
	 * @param rightColor
	 *            couleur de surlignage pour les segments passés avec succès
	 * @param wrongColor
	 *            couleur de surlignage pour les erreurs
	 * @param correctionColor
	 *            couleur de surlignage pour les segments corrigés
	 */
	public void setHighlightColors(Color rightColor, Color wrongColor, Color correctionColor) {
		p.editorPane.hParam.rightColor = rightColor;
		p.editorPane.hParam.wrongColor = wrongColor;
		p.editorPane.hParam.correctionColor = correctionColor;
		p.editorPane.updateColors();
	}
	
	/**
	 * Change la couleur de fond de l'exercice.
	 */
	public void setBackgroundColor(Color color) {
		p.param.bgColor = color;
		p.editorPane.setBackground(color);
	}

	/**
	 * Retourne le numéro du segment courant (en partant de 0).
	 */
	public int getCurrentPhraseIndex() {
		return pilot.getCurrentPhraseIndex();
	}

	/**
	 * Charge une usine de lecture (nécessaire pour appeler la méthode
	 * {@link #goTo(int)}).
	 */
	public void setReaderFactory(ReaderFactory readerFactory) {
		pilot.setReaderFactory(readerFactory);
	}

	/**
	 * Mets à jour la barre de progression et le numéro du segment en cours.<br/>
	 * A effectuer au début de chaque segment traité, mais pas plus d'une fois par
	 * segment.
	 */
	public void updateCurrentPhrase() {
		pilot.updateCurrentPhrase();
		p.nbErreursSegmentCourant = 0;
	}

	/**
	 * Active ou désactive les contrôles clavier (touche gauche pour répéter le
	 * segment, touche espace pour arrêter/recommencer).
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
	
	/*
	 * Méthodes du texte à trou
	 */

	/////////////////////////////////////////////
	////////////////////////////////////////////
	///////////////////////////////////////////
	//////////////////////////////////////////
	/////////////////////////////////////////

	/**
	 * Retourne true si le trou est le premier de son segment.
	 */
	public boolean isFirstInPhrase(int h) {
		return !p.textHandler.hasPreviousHoleInPhrase(h);
	}

	/**
	 * Retourne false si le trou est le dernier de son segment
	 */
	public boolean isLastInPhrase(int h) {
		return !p.textHandler.hasNextHoleInPhrase(h);
	}

	/**
	 * 
	 * Affiche tous les trous correspondant à la page et à partir du trou indiquée.
	 * Désaffiche au préalable tous les trous.
	 */
	public void showHolesInPage(int h) {
		showHolesInPage(h, getPageOf(h));
	}

	/**
	 * 
	 * Affiche tous les trous correspondant à la page indiqué et à partir du trou
	 * indiqué. Désaffiche au préalable tous les trous.
	 */
	public void showHolesInPage(int h, int page) {
		// réinitialisation des trous
		removeAllMasks();
		// pour tous les trous
		for (int i = 0; i < p.textHandler.getHolesCount(); i++) {
			// si ce trou est dans la meme page que h
			if (getPageOf(i) == page) {
				// si ce trou est après le trou h ou est le trou h
				if (i >= h) {
					// on affiche ce trou
					showHole(i);
				}
			}
		}
	}

	/**
	 * Montre le trou h
	 */
	private void showHole(int h) {

		/// on cache le trou avant de montrer la fenêtre ///
		hideHole(h);

		int startPhrase = p.phrasesInFonctionOfPages.get(getPageOf(h)).get(0);

		int start = p.textHandler.getRelativeOffset(startPhrase, p.textHandler.getHoleStartOffset(h));
		int end = p.textHandler.getRelativeOffset(startPhrase, p.textHandler.getHoleEndOffset(h));

		try {
			p.showFrame(start, end, h);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retourne la page correspondant au trou h
	 */
	public int getPageOf(int h) {
		return getPageOfPhrase(getPhraseOf(h));
	}

	/**
	 * Retourne le segment correspondant au trou h
	 */
	public int getPhraseOf(int h) {
		return p.textHandler.getPhraseOf(h);
	}

	/**
	 * Retourne le nombre de trous associ�s au segment n.
	 */
	public int getHolesCount(int n) {
		return p.textHandler.getHolesCount(n);
	}

	/**
	 * Retourne le nombre de trous total dans le texte.
	 */
	public int getHolesCount() {
		return p.textHandler.getHolesCount();
	}

	/**
	 * Définit le temps d'aperçu du mot en fonction du nombre de caractères. Mettre
	 * à 0 pour un aperçu inexistant. Mettre à -1 pour un aperçu jusqu'à saisie.
	 */
	public void setHint(int value) {
		if (value == -1) {
			value = 99999;
		}
		p.rParam.hintDuration = value;
	}

	/**
	 * Active la fenêtre du trou actuel. Montre l'aperçu du mot. Renvoie
	 * <code>true</code> si le mot est bon, <code>false</code> sinon.
	 */
	public boolean waitForFill(int h) {
		Mask m = getMask(h);
		if (m == null)
			return true;
		m.activate();
		hint(m);
		p.controlerMask.enter = false;
		while (true) {
			Thread.yield();
			if (p.controlerMask.enter) {
				return m != null && m.n == h ? m.correctWord() : true;
			}
		}
	}

	/**
	 * Active la fenêtre fixe. Colorier le masque actuel en cyan ( ou jaune si le
	 * fond est cyan ). Montre l'aperçu du mot dans la fenêtre fixe. Attend une
	 * saisie. Si juste : désactive la fenêtre fixe et renvoie <code>true</code>. Si
	 * faux : renvoie <code>false</code>;
	 */
	public boolean waitForFillFixedFrame(int h) {
		colorFixedFrame(h, getColorBackground() != Color.cyan ? Color.cyan : Color.YELLOW);
		activateInputFixedFrame(h);

		Mask m = getFixedFrame();
		m.activate();
		hint(m);
		while (true) {

			if (getFixedFrame() != m) {
				return true;
			}

			Thread.yield();
			if (p.controlerMask.enter) {
				p.controlerMask.enter = false;

				if (m.jtf.getText().equals(m.hiddenWord)) {
					desactivateFixedFrame();
					return true;
				} else {
					return false;
				}
			}
		}

	}

	/**
	 * Affiche l'aperçu du mot sur le masque m
	 */
	private void hint(Mask m) {
		/*
		 * if (p.param.timeToShowWord == -1) { m.setHint(); } else {
		 * m.setHint(p.param.timeToShowWord * m.getNbCarac()); }
		 */
		m.setHint(p.rParam.hintDuration * m.getNbCarac());
	}

	/**
	 * Enlève tous les masques
	 */
	public void removeAllMasks() {
		p.removeAllMasks();
	}

	/**
	 * Fait clignoter l'exercice avec la couleur c
	 */
	public void blink(Color c) {
		p.blink(c);
	}

	/**
	 * Colore le trou h en couleur c
	 */
	public void colorFixedFrame(int h, Color c) {
		getMask(h).jtf.setBackground(c);
	}

	/**
	 * Retourne la couelur de fond de l'exercice
	 */
	public Color getColorBackground() {
		return p.editorPane.getBackground();
	}

	/**
	 * Retourne le masque du trou h
	 */
	private Mask getMask(int h) {
		for (int i = 0; i < p.maskFrame.size(); i++) {
			if (p.maskFrame.get(i).n == h) {
				return p.maskFrame.get(i);
			}
		}
		return null;
	}

	/**
	 * Crée une fenêtre de saisie fixe qui attends le resultat du trou h
	 */
	public void activateInputFixedFrame(int h) {

		Mask frame = new Mask();
		((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
		frame.setBorder(null);
		frame.setBounds(0, 0, p.panelFixedFrame.getWidth(), p.panelFixedFrame.getHeight());

		p.panelFixedFrame.add(frame);

		Font f = new Font(p.editorPane.getFont().getFontName(), p.editorPane.getFont().getStyle(),
				p.editorPane.getFont().getSize() * 7 / 10);

		frame.initField(f, p.controlerMask);
		frame.n = h;
		frame.hiddenWord = p.textHandler.getHiddendWord(h);
		frame.toFront();
		frame.setVisible(true);
		frame.activate();

		p.fixedFrame = frame;

	}

	/**
	 * Met le mode fenêtre fixe si <code>true</code>, met le mode fenêtre non fixe
	 * si <code>false</code>
	 */
	public void setModeFixedField(boolean b) {
		p.rParam.fixedField = b;
	}

	/**
	 * Cette méthode propose la gestion d'erreur suivante : Clignote avec la couleur
	 * d'alerte. Incremente le nombre d'erreur. Reaffiche le hint. Si vous voulez un
	 * traitement d'erreur personnalisé ne pas utiliser cette méthode.
	 */
	public void doError(int h) {
		blink(Constants.ALERT_COLOR);
		countError();
		// on reaffiche le hint
		if (p.rParam.fixedField) {
			getFixedFrame().jtf.setText("");
			hint(getFixedFrame());
		} else {
			getMask(h).jtf.setText("");
			hint(getMask(h));
		}
		/*
		 * if (p.rParam.replayPhrase) { play(p.pilot.getCurrentPhraseIndex());
		 * doWait(getCurrentWaitTime(), Constants.CURSOR_LISTEN); }
		 */

	}

	/**
	 * Désactive la fênetre fixe
	 * 
	 * @throws lance
	 *             une erreur si elle est null.
	 */
	public void desactivateFixedFrame() {
		if (getFixedFrame() == null) {
			throw new RuntimeException("La fenêtre fixe est null");
		}
		getFixedFrame().dispose();
	}

	/**
	 * Retourne la fenêtre fixe
	 */
	private Mask getFixedFrame() {
		return p.fixedFrame;
	}

	/**
	 * Cache le trou h et le remplace par le bon mot
	 */
	public void replaceMaskByWord(int h) {
		Mask m = getMask(h);
		if (m != null) {
			m.setVisible(false);
		}
		fillHole(h);
	}

	/**
	 * Remplace le trou h par le bon mot.
	 */
	public void fillHole(int h) {
		if (p.textHandler.isHidden(h)) {
			p.textHandler.fillHole(h);
			p.updateText();
		}
	}

	/**
	 * Cache le trou h.
	 */
	public void hideHole(int h) {
		if (!p.textHandler.isHidden(h)) {
			p.textHandler.hideHole(h);
			p.updateText();
			p.replaceAllMask();
		}
	}

	/**
	 * Retourne <code>true</code> si le segment n contient au moins un trou.
	 */
	public boolean hasHole(int n) {
		return p.textHandler.hasHole(n);
	}

	/**
	 * Retourne le numéro du premier trou à partir du segment indiqué.<br>
	 * Retourne -1 s'il n'y a aucun trou après le segment.
	 */
	public int getFirstHole(int n) {
		return p.textHandler.getFirstHole(n);
	}

	/**
	 * Desaffiche tous les trous puis montre uniquement le trou h
	 */
	public void showJustHole(int h) {
		removeAllMasks();
		showHole(h);
	}

}
