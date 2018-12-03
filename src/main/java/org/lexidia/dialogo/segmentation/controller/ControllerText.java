package org.lexidia.dialogo.segmentation.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.lexidia.dialogo.segmentation.main.Constants;
import org.lexidia.dialogo.segmentation.reading.ReadThread;
import org.lexidia.dialogo.segmentation.reading.ReaderFactory;
import org.lexidia.dialogo.segmentation.view.Mask;
import org.lexidia.dialogo.segmentation.view.SegmentedTextFrame;
import org.lexidia.dialogo.segmentation.view.SegmentedTextPanel;

public class ControllerText {
	
	private SegmentedTextPanel p;
	private Pilot pilot;
	
	/**
	 * Construit un contr�leur � partir de la fen�tre d'exercice correspondante.
	 */
	public ControllerText(SegmentedTextFrame frame) {
		this.p = frame.getPanel();
		this.pilot = new Pilot(p);
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
	 * Limite le nombre de segments de page. Si 0, la mise en page se fait � la limite de la fen�tre.
	 */
	public void setMaxPhrasesByPage(int maxPhrases) {
		p.getParam().setMaxPhrases(maxPhrases);
		p.rebuildPages();
	}
	
	/**
	 * Aligne le texte verticallement en ajustant la marge en haut.
	 */
	public void centerTextVertically() {
		p.getEditorPane().centerText();
	}
	
	/**
	 * Joue un fichier .wav correspondant � un segment de phrase. On sortira de
	 * cette fonction lorsque le fichier .wav aura �t� totalement jou�. METHODE DE
	 * TEST
	 */
	@Deprecated
	public void play(int phrase) {
		p.setCursor(Constants.CURSOR_LISTEN);
		p.getPlayer().play(phrase);
		while (true) {
			if (p.getPlayer().isPhraseFinished()) {
				p.setCursor(Cursor.getDefaultCursor());
				break;
			}
			/// fixe toujours le curseur d'�coute pendant toute la dur�e de l'enregistrement
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
		return p.getTextHandler().getPhrasesCount();
	}
	
	/**
	 * Retourne le contenu textuel du segment de num�ro indiqu�.
	 */
	public String getPhraseContent(int phrase) {
		return p.getTextHandler().getPhrase(phrase);
	}
	
	/**
	 * Retourne la dur�e en millisecondes de l'enregistrement qui correspond au
	 * segment de phrase indiqu�.
	 */
	@Deprecated
	public long getPhraseDuration(int phrase) {
		p.getPlayer().load(phrase);
		return p.getPlayer().getDuration();
	}
	
	/**
	 * Retourne la dur�e en millisecondes de l'enregistrement courant.
	 */
	@Deprecated
	public long getCurrentPhraseDuration() {
		return p.getPlayer().getDuration();
	}
	
	/**
	 * Retourne le temps d'attente en millisecondes correspondant � l'enregistrement
	 * courant.
	 */
	@Deprecated
	public long getCurrentWaitTime() {
		return (long) (getCurrentPhraseDuration() * /* p.param.tempsPauseEnPourcentageDuTempsDeLecture / 100. */1);
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
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Attends un clic sur un mot du texte.<br/>
	 * Retourne <code>true</code> si le mot correspond au dernier mot du segment n,
	 * <code>false</code> si c'est le mauvais mot.
	 * 
	 * @highlightWrongWord si le mot est surlign� en cas d'erreur.
	 */
	public boolean waitForClick(int n) {
		p.getControlerMouse().waitForClick();

		/// cherche la position exacte dans le texte ///
		int offset = p.getTextHandler().getAbsoluteOffset(p.getFirstShownPhraseIndex(),
				p.getEditorPane().getCaretPosition());
		/// si le clic est juste ///
		if (p.getTextHandler().wordPause(offset)
				&& p.getTextHandler().getPhraseIndex(offset) == pilot.getCurrentPhraseIndex()) {
			return true;
		}
		/// si le clic est faux ///
		else {
			return false;
		}

	}
	
	/**
	 * Colorie le segment numero n avec la couleur de r�ussite.
	 */
	public void highlightPhrase(int n) {
		highlightPhrase(p.getEditorPane().gethParam().getRightColor(), n);
	}
	
	/**
	 * Colorie le segment numero n avec la couleur de correction.
	 */
	public void highlightCorrectionPhrase(int n) {
		highlightPhrase(p.getEditorPane().gethParam().getCorrectionColor(), n);
	}
	
	private void highlightPhrase(Color c, int n) {
		if (p.getTextHandler().getPhrase(n) != null) {
			int debutRelatifSegment = p.getTextHandler().getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.getTextHandler().getPhrase(n).length();
			p.getEditorPane().highlightPhrase(debutRelatifSegment, finRelativeSegment, c);
		}
	}
	
	/**
	 * Supprime tout le surlignage qui se trouve sur le segment <i>n</i>.<br/>
	 */
	public void removeHighlightPhrase(int n) {
		int debutRelatifSegment = p.getTextHandler().getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(), n);
		int finRelativeSegment = debutRelatifSegment + p.getTextHandler().getPhrase(n).length();
		p.getEditorPane().removeHighlight(debutRelatifSegment, finRelativeSegment);
	}
	
	/**
	 * 
	 * Arr�te l'enregistrement courant et enl�ve tout le surlignage.
	 */
	@Deprecated
	public void stopAll() {
		p.getPlayer().stop();
		p.getEditorPane().removeAllHighlights();
	}
	
	/**
	 * Charge un segment de phrase dans le lecteur sans le d�marrer.<br>
	 * Pas n�cessaire si on d�marre le lecteur directement avec la m�thode
	 */
	@Deprecated
	public void loadSound(int phrase) {
		p.getPlayer().load(phrase);
	}
	
	/**
	 * Enl�ve tout le surlignage d'erreur.
	 */
	public void removeWrongHighlights() {
		p.getEditorPane().removeRedHighlight();
	}
	
	/**
	 * Enl�ve tout le surlignage pr�sent.
	 */
	public void removeAllHighlights() {
		p.getEditorPane().removeAllHighlights();
	}
	
	/**
	 * Retourne la page qui contient le segment, ou -1 si le segment n'existe pas.
	 */
	public int getPageOfPhrase(int n) {
		int numeroPage = -1;
		for (Integer i : p.getPhrasesInFonctionOfPages().keySet()) {
			if (p.getPhrasesInFonctionOfPages().get(i).contains(n)) {
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
	
	@Deprecated
	public void incrementerErreurSegment() {
		p.setNbErreursParSegment(p.getNbErreursParSegment() + 1);
	}
	
	/**
	 * Surligne le dernier mot sur lequel le patient a cliqu� apr�s avoir enlev� le
	 * surlignage d'erreur existant.
	 */
	public void highlightWrongWord() {
		removeWrongHighlights();
		int clickOffset = p.getControlerMouse().getLastTextOffset();
		int startOffset = p.getTextHandler().getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.getTextHandler().startWordPosition(clickOffset) + 1);
		int endOffset = p.getTextHandler().getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.getTextHandler().endWordPosition(clickOffset));
		highlight(p.getEditorPane().gethParam().getWrongColor(), startOffset, endOffset);
	}
	
	private void highlight(Color c, int start, int end) {
		p.getEditorPane().highlightPhrase(start, end, c);
	}
	
	/**
	 * Comptabilise une erreur.
	 */
	public void countError() {
		p.setNbErreursSegmentCourant(p.getNbErreursSegmentCourant() + 1);
		p.setNbErreurs(p.getNbErreurs() + 1);
	}
	
	/**
	 * Comptabilise une erreur de segment (lorsque le patient atteint le nombre
	 * maximal d'erreurs tol�r�s par segment).
	 */
	public void countPhraseError() {
		p.setNbErreursParSegment(p.getNbErreursParSegment() + 1);
	}
	
	/**
	 * Retourne <code>true</code> si il reste au moins un essai au patient pour le
	 * segment courant.
	 */
	public boolean hasMoreTrials() {
		return p.getNbErreursSegmentCourant() < p.getrParam().getToleratedErrors() + 1;
	}
	
	/**
	 * Modifie le nombre d'essais tol�r�s par segment.
	 */
	public void setPhraseTrials(int trials) {
		p.getrParam().setToleratedErrors(trials);
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
		String message = "<html>L'exercice est termin�.";
		if (showErrors) {
			message += "<br/>Le patient a fait " + p.getNbErreurs() + (p.getNbErreurs() > 1 ? "s" : "") + ".";
		}
		if (showPhraseErrors) {
			message += "<br/>Le patient a fait " + p.getNbErreurs() + (p.getNbErreurs() > 1 ? "s" : "")
					+ " de segment.";
		}
		message += "</html>";
		p.showReport(message);
	}
	
	/**
	 * Se place sur le segment de numero <i>n</i> et d�marre l'algorithme de
	 * lecture.
	 * 
	 * @throws IllegalArgumentException
	 *             si <i>n</i> est inf�rieur au num�ro du premier segment ou
	 *             sup�rieur au nombre de segments dans le texte, ou si le thread de
	 *             lecture n'a pas �t� charg� avec
	 *             {@link #loadReadThread(ReadThread)}.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		pilot.goTo(n);
	}
	
	/**
	 * Essaye de passer au segment suivant, passe � la page suivante si c'�tait le
	 * dernier segment de la page. D�clenche une erreur si on �tait au dernier
	 * segment du texte.
	 */
	public void doNext() {
		pilot.doNext();
	}
	
	/**
	 * Essaye de passer au segment pr�c�dent. D�clenche une erreur si on �tait au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		pilot.doPrevious();
	}
	
	/**
	 * Essaye d'arr�ter l'enregistrement en cours.
	 */
	public void doStop() {
		pilot.doStop();
	}
	
	/**
	 * Essaye de reprendre l'enregistrement. Si il est d�j� d�marr�, reprend depuis
	 * le d�but.
	 */
	public void doPlay() {
		pilot.doPlay();
	}
	
	/**
	 * Applique un Font � l'exercice.
	 * 
	 * @throws IllegalArgumentException
	 *             si l'exercice a d�j� commenc�.
	 */
	public void setFont(Font f) throws IllegalArgumentException {
		if (pilot.hasStarted()) {
			throw new IllegalArgumentException("Police inchangeable apr�s le d�marrage de l'exercice !");
		}
		p.getEditorPane().setBaseFont(f);
		p.rebuildPages();
	}
	
	/**
	 * Retourne la police non modifi�e par la mise en page automatique.
	 */
	public Font getFont() {
		return p.getEditorPane().getFont();
	}
	
	/**
	 * Change la taille de la police.
	 * 
	 * @throws IllegalArgumentException
	 *             si l'exercice a d�j� commenc�.
	 */
	public void setFontSize(float fontSize) throws IllegalArgumentException {
		setFont(p.getEditorPane().getFont().deriveFont(fontSize));
	}
	
	/**
	 * R�gle l'�cartement vertical entre les lignes.<br/>
	 * 1 correspond � la hauteur d'une ligne selon la police utilis�e (par d�faut 0.8).<br/>
	 * <b>Attention !</b> Si la taille de police est grande, il ne faut pas utiliser un espacement
	 * trop grand afin de permettre la mise en page.
	 */
	public void setLineSpacing(float lineSpacing) {
		p.getEditorPane().setLineSpacing(lineSpacing);
		p.rebuildPages();
	}

	/**
	 * Termine l'exercice. Retourne au premier segment de la page. Permet de pouvoir
	 * faire � nouveau les r�glages de base de l'exercice.
	 */
	public void end() {
		pilot.end();
		p.rebuildPages();
	}

	/**
	 * Modifie les couleurs de surlignage pour l'exercice.<br/>
	 * Certaines couleurs peuvent �tre initilias�es � <code>null</code> si elles ne
	 * sont pas utilis�es dans l'exercice. Ne peut pas fonctionner si du texte a
	 * d�j� �t� surlign�.
	 * 
	 * @param rightColor
	 *            couleur de surlignage pour les segments pass�s avec succ�s
	 * @param wrongColor
	 *            couleur de surlignage pour les erreurs
	 * @param correctionColor
	 *            couleur de surlignage pour les segments corrig�s
	 */
	public void setHighlightColors(Color rightColor, Color wrongColor, Color correctionColor) {
		p.getEditorPane().gethParam().setRightColor(rightColor);
		p.getEditorPane().gethParam().setWrongColor(wrongColor);
		p.getEditorPane().gethParam().setCorrectionColor(correctionColor);
		p.getEditorPane().updateColors();
	}

	/**
	 * Change la couleur de fond de l'exercice.
	 */
	public void setBackgroundColor(Color color) {
		p.getParam().setBgColor(color);
		p.getEditorPane().setBackground(color);
	}

	/**
	 * Retourne le num�ro du segment courant (en partant de 0).
	 */
	public int getCurrentPhraseIndex() {
		return pilot.getCurrentPhraseIndex();
	}

	/**
	 * Charge une usine de lecture (n�cessaire pour appeler la m�thode
	 * {@link #goTo(int)}).
	 */
	public void setReaderFactory(ReaderFactory readerFactory) {
		pilot.setReaderFactory(readerFactory);
	}

	/**
	 * Mets � jour la barre de progression et le num�ro du segment en cours.<br/>
	 * A effectuer au d�but de chaque segment trait�, mais pas plus d'une fois par
	 * segment.
	 */
	public void updateCurrentPhrase() {
		pilot.updateCurrentPhrase();
		p.setNbErreursSegmentCourant(0);
	}
	
	/**
	 * Retourne le num�ro du premier segment affich� de la page actuelle.
	 */
	public int getFirstShownPhraseIndex() {
		return p.getFirstShownPhraseIndex();
	}
	
	/**
	 * Retourne le nombre de segments affich� actuellement.
	 */
	public int getShownPhrasesCount() {
		return p.getPhrasesInFonctionOfPages().get(p.getCurrentPage()).size();
	}
	
	/**
	 * Active ou d�sactive les contr�les clavier (touche gauche pour r�p�ter le
	 * segment, touche espace pour arr�ter/recommencer).
	 */
	public void setKeyEnabled(boolean keyEnabled) {
		/// ajoute un contr�le clavier ///
		if (keyEnabled && p.getKeyListeners().length == 0) {
			ControllerKey controlerKey = new ControllerKey(pilot);
			p.getEditorPane().addKeyListener(controlerKey);
		}
		/// retire les contr�les clavier ///
		else if (!keyEnabled && p.getKeyListeners().length >= 1) {
			for (KeyListener listener : p.getKeyListeners()) {
				p.removeKeyListener(listener);
			}
		}
	}
	
	/**
	 * Retourne le rectangle du panneau de l'exercice
	 */
	public Rectangle getBounds() {
		return p.getEditorPane().getBounds();
	}
	
	/**
	 * Retourne la largeur de la fen�tre, en centim�tres.
	 */
	public float getFrameWidth() {
		return SegmentedTextFrame.pxToCm(p.getFenetre().getWidth());
	}
	
	/**
	 * Retourne la hauteur de la fen�tre, en centim�tres.
	 */
	public float getFrameHeight() {
		return SegmentedTextFrame.pxToCm(p.getFenetre().getHeight());
	}
	
	/**
	 * Cache ou fait appara�tre les sliders qui permettent d'ajuster les marges sur les c�t�s du texte.<br>
	 * Ceux-ci sont automatiquement d�sactiv�s lorsque l'exercice d�marre.<br>
	 * Par d�fault, ils sont visibles lors du d�marrage de la fen�tre.
	 */
	public void setMarginSlidersVisible(boolean visible) {
		p.setSlidersVisible(visible);
	}
	
	/**
	 * Retourne la marge � gauche du texte.
	 */
	public float getLeftMargin() {
		return p.getEditorPane().getLeftMargin();
	}
	
	/**
	 * Retourne la marge � droite du texte.
	 */
	public float getRightMargin() {
		return p.getEditorPane().getRightMargin();
	}
	
	/**
	 * Retourne la marge en haut du texte.
	 */
	public float getTopMargin() {
		return p.getEditorPane().getTopMargin();
	}
	
	/**
	 * Retourne la marge en bas du texte.
	 */
	public float getBottomMargin() {
		return p.getEditorPane().getBottomMargin();
	}
	
	/**
	 * Applique les marges sur les c�t�s du texte.<br>
	 * Par d�faut, si param�tres ne sont pas modifi�s,
	 * des marges horizontales sont plac�es automatiquement.
	 */
	public void setMargin(final float leftMargin, final float rightMargin,
			final float topMargin, final float bottomMargin) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				p.getEditorPane().setLeftMargin(leftMargin);
				p.getEditorPane().setRightMargin(rightMargin);
				p.getEditorPane().setTopMargin(topMargin);
				p.getEditorPane().setBottomMargin(bottomMargin);
				p.updateSliders();
				p.rebuildPages();
			}
		});
	}
	
	/**
	 * G�le la fenetre si b est <code>true</code>, la d�g�le si b est
	 * <code>false</code>
	 */
	public void freeze(boolean b) {
		p.getFenetre().setResizable(!b);
	}
	
	/**
	 * Definis le nombre maximum de segments � afficher par page
	 */
	public void setMaxSegmentByPage(int i) {
		p.getrParam().setMaxSegmentByPage(i);
	}

	/*
	 * M�thodes du texte � trou
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
		return !p.getTextHandler().hasPreviousHoleInPhrase(h);
	}
	
	/**
	 * Retourne false si le trou est le dernier de son segment
	 */
	public boolean isLastInPhrase(int h) {
		return !p.getTextHandler().hasNextHoleInPhrase(h);
	}
	
	/**
	 * 
	 * Affiche tous les trous correspondant � la page et � partir du trou indiqu�e.
	 * D�saffiche au pr�alable tous les trous.
	 */
	public void showHolesInPage(int h) {
		showHolesInPage(h, getPageOf(h));
	}
	
	/**
	 * 
	 * Affiche tous les trous correspondant � la page indiqu� et � partir du trou
	 * indiqu�. D�saffiche au pr�alable tous les trous.
	 */
	public void showHolesInPage(int h, int page) {
		// r�initialisation des trous
		removeAllMasks();
		// pour tous les trous
		for (int i = 0; i < p.getTextHandler().getHolesCount(); i++) {
			// si ce trou est dans la meme page que h
			if (getPageOf(i) == page) {
				// si ce trou est apr�s le trou h ou est le trou h
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

		/// on cache le trou avant de montrer la fen�tre ///
		hideHole(h);

		int startPhrase = p.getPhrasesInFonctionOfPages().get(getPageOf(h)).get(0);

		int start = p.getTextHandler().getRelativeOffset(startPhrase, p.getTextHandler().getHoleStartOffset(h));
		int end = p.getTextHandler().getRelativeOffset(startPhrase, p.getTextHandler().getHoleEndOffset(h));

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
		return p.getTextHandler().getPhraseOf(h);
	}
	
	/**
	 * Retourne le nombre de trous associ�es au segment n.
	 */
	public int getHolesCount(int n) {
		return p.getTextHandler().getHolesCount(n);
	}
	
	/**
	 * Retourne le nombre de trous total dans le texte.
	 */
	public int getHolesCount() {
		return p.getTextHandler().getHolesCount();
	}
	
	/**
	 * D�finit le temps d'aper�u du mot en fonction du nombre de caract�res. Mettre
	 * � 0 pour un aper�u inexistant. Mettre � -1 pour un aper�u jusqu'� saisie.
	 */
	public void setHint(int value) {
		if (value == -1) {
			value = 99999;
		}
		p.getrParam().setHintDuration(value);
	}
	
	/**
	 * Active la fen�tre du trou actuel. Montre l'aper�u du mot. Renvoie
	 * <code>true</code> si le mot est bon, <code>false</code> sinon.
	 */
	public boolean waitForFill(int h) {
		Mask m = getMask(h);
		if (m == null)
			return true;
		m.activate();
		hint(m);

		p.getControlerMask().waitForFill();

		return m != null && m.getN() == h ? m.correctWord() : true;

	}
	
	/**
	 * Active la fen�tre fixe. Colorier le masque actuel en cyan ( ou jaune si le
	 * fond est cyan ). Montre l'aper�u du mot dans la fen�tre fixe. Attend une
	 * saisie. Si juste : d�sactive la fen�tre fixe et renvoie <code>true</code>. Si
	 * faux : renvoie <code>false</code>;
	 */
	public boolean waitForFillFixedFrame(int h) {
		colorFixedFrame(h, getColorBackground() != Color.cyan ? Color.cyan : Color.YELLOW);
		activateInputFixedFrame(h);

		Mask m = getFixedFrame();
		m.activate();
		hint(m);

		if (getFixedFrame() != m) {
			return true;
		}

		p.getControlerMask().waitForFill();

		if (getFixedFrame() != m) {
			return true;
		}

		if (m.getJtf().getText().equals(m.getHiddenWord())) {
			desactivateFixedFrame();
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * Affiche l'aper�u du mot sur le masque m
	 */
	private void hint(Mask m) {
		/*
		 * if (p.param.timeToShowWord == -1) { m.setHint(); } else {
		 * m.setHint(p.param.timeToShowWord * m.getNbCarac()); }
		 */
		m.setHint(p.getrParam().getHintDuration() * m.getNbCarac());
	}
	
	/**
	 * Enl�ve tous les masques
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
		getMask(h).getJtf().setBackground(c);
	}
	
	/**
	 * Retourne la couleur de fond de l'exercice
	 */
	public Color getColorBackground() {
		return p.getEditorPane().getBackground();
	}
	
	/**
	 * Retourne le masque du trou h
	 */
	private Mask getMask(int h) {
		for (int i = 0; i < p.getMaskFrame().size(); i++) {
			if (p.getMaskFrame().get(i).getN() == h) {
				return p.getMaskFrame().get(i);
			}
		}
		return null;
	}
	
	/**
	 * Cr�e une fen�tre de saisie fixe qui attends le resultat du trou h
	 */
	public void activateInputFixedFrame(int h) {

		Mask frame = new Mask();
		((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
		frame.setBorder(null);
		frame.setBounds(0, 0, p.getPanelFixedFrame().getWidth(), p.getPanelFixedFrame().getHeight());

		p.getPanelFixedFrame().add(frame);

		Font f = new Font(p.getEditorPane().getFont().getFontName(), p.getEditorPane().getFont().getStyle(),
				p.getEditorPane().getFont().getSize() * 7 / 10);

		frame.initField(f, p.getControlerMask());
		frame.setN(h);
		frame.setHiddenWord(p.getTextHandler().getHiddendWord(h));
		frame.toFront();
		frame.setVisible(true);
		frame.activate();

		p.setFixedFrame(frame);

	}
	
	/**
	 * Met le mode fen�tre fixe si <code>true</code>, met le mode fen�tre non fixe
	 * si <code>false</code>
	 */
	public void setModeFixedField(boolean b) {
		p.getrParam().setFixedField(b);
	}
	
	/**
	 * Cette m�thode propose la gestion d'erreur suivante : Clignote avec la couleur
	 * d'alerte. Incremente le nombre d'erreur. Reaffiche le hint. Si vous voulez un
	 * traitement d'erreur personnalis� ne pas utiliser cette m�thode.
	 */
	public void doError(int h) {
		blink(Constants.ALERT_COLOR);
		countError();
		// on reaffiche le hint
		if (p.getrParam().isFixedField()) {
			getFixedFrame().getJtf().setText("");
			hint(getFixedFrame());
		} else {
			getMask(h).getJtf().setText("");
			hint(getMask(h));
		}
		/*
		 * if (p.rParam.replayPhrase) { play(p.pilot.getCurrentPhraseIndex());
		 * doWait(getCurrentWaitTime(), Constants.CURSOR_LISTEN); }
		 */

	}
	
	/**
	 * D�sactive la f�netre fixe
	 * 
	 * @throws lance
	 *             une erreur si elle est null.
	 */
	public void desactivateFixedFrame() {
		if (getFixedFrame() == null) {
			throw new RuntimeException("La fen�tre fixe est null");
		}
		getFixedFrame().dispose();
	}
	
	/**
	 * Retourne la fen�tre fixe
	 */
	private Mask getFixedFrame() {
		return p.getFixedFrame();
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
		if (p.getTextHandler().isHidden(h)) {
			p.getTextHandler().fillHole(h);
			p.updateText();
		}
	}
	
	/**
	 * Cache le trou h.
	 */
	public void hideHole(int h) {
		if (!p.getTextHandler().isHidden(h)) {
			p.getTextHandler().hideHole(h);
			p.updateText();
			p.replaceAllMask();
		}
	}
	
	/**
	 * Retourne <code>true</code> si le segment n contient au moins un trou.
	 */
	public boolean hasHole(int n) {
		return p.getTextHandler().hasHole(n);
	}
	
	/**
	 * Retourne le num�ro du premier trou � partir du segment indiqu�.<br>
	 * Retourne -1 s'il n'y a aucun trou apr�s le segment.
	 */
	public int getFirstHole(int n) {
		return p.getTextHandler().getFirstHole(n);
	}
	
	/**
	 * Desaffiche tous les trous puis montre uniquement le trou h
	 */
	public void showJustHole(int h) {
		removeAllMasks();
		showHole(h);
	}

}
