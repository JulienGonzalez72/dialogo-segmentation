package fr.lexidia.dialogo.controller;

import static fr.lexidia.dialogo.main.Assert.assertContains;
import static fr.lexidia.dialogo.main.Assert.assertFont;
import static fr.lexidia.dialogo.main.Assert.assertGreater;
import static fr.lexidia.dialogo.main.Assert.assertGreaterOrEquals;
import static fr.lexidia.dialogo.main.Assert.assertNotNull;
import static fr.lexidia.dialogo.main.Assert.assertNotStarted;
import static fr.lexidia.dialogo.main.Assert.assertPositive;
import static fr.lexidia.dialogo.main.Assert.assertPositiveOrNull;
import static fr.lexidia.dialogo.main.Assert.assertSwingThread;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import fr.lexidia.dialogo.dispatcher.Event;
import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.Constants;
import fr.lexidia.dialogo.reading.ReadThread;
import fr.lexidia.dialogo.reading.ReaderFactory;
import fr.lexidia.dialogo.view.Mask;
import fr.lexidia.dialogo.view.SegmentedTextFrame;
import fr.lexidia.dialogo.view.SegmentedTextPanel;

public class ControllerText {
	
	private EventDispatcher ed;
	private SegmentedTextPanel p;
	private Pilot pilot;
	
	/**
	 * Construit un contrôleur à partir de la fenêtre d'exercice correspondante.
	 */
	public ControllerText(SegmentedTextFrame frame, boolean isPatient) {
		this.p = frame.getPanel();
		this.pilot = new Pilot(p);
		if(isPatient) {
			try {
				this.ed = new EventDispatcher();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void dispatch(Event e, List<Object> params) {
		if (ed != null) {
			ed.dispatch(e, params);
		}
	}
	
	/**
	 * Construit les pages à partir du segment de numéro spécifié.
	 * @throws IllegalArgumentException su startPhrase est négatif.
	 */
	public void buildPages(final int startPhrase) throws IllegalArgumentException {
		assertPositiveOrNull(startPhrase, "startPhrase");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.buildPages(startPhrase);
			}
		});
		List<Object> params = new ArrayList<>();
		params.add(startPhrase);
		dispatch(Event.buildPages,params);
	}
	
	/**
	 * Affiche la page indiquée.
	 * @throws IllegalArgumentException si page est négatif.
	 */
	public void showPage(final int page) throws IllegalArgumentException {
		assertPositiveOrNull(page, "page");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.showPage(page);
			}
		});
	}
	
	/**
	 * Limite le nombre de segments de page. Si 0, la mise en page se fait à la limite de la fenêtre.
	 * @throws IllegalArgumentException si maxPhrases < 0.
	 */
	public void setMaxPhrasesByPage(int maxPhrases) throws IllegalArgumentException {
		assertPositiveOrNull(maxPhrases, "maxPhrases");
		p.getParam().setMaxPhrases(maxPhrases);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.rebuildPages();
			}
		});
	}
	
	/**
	 * Aligne le texte verticallement en ajustant la marge en haut.
	 */
	public void centerTextVertically() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().centerText();
			}
		});
	}
	
	/**
	 * Joue un fichier .wav correspondant à un segment de phrase. On sortira de
	 * cette fonction lorsque le fichier .wav aura été totalement joué. METHODE DE
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
		return p.getTextHandler().getPhrasesCount();
	}
	
	/**
	 * Retourne le contenu textuel du segment de numéro indiqué.
	 */
	public String getPhraseContent(int n) {
		assertPositiveOrNull(n, "n");
		return p.getTextHandler().getPhrase(n);
	}
	
	/**
	 * Retourne la durée en millisecondes de l'enregistrement qui correspond au
	 * segment de phrase indiqué.
	 */
	@Deprecated
	public long getPhraseDuration(int phrase) {
		p.getPlayer().load(phrase);
		return p.getPlayer().getDuration();
	}
	
	/**
	 * Retourne la durée en millisecondes de l'enregistrement courant.
	 */
	@Deprecated
	public long getCurrentPhraseDuration() {
		return p.getPlayer().getDuration();
	}
	
	/**
	 * Retourne le temps d'attente en millisecondes correspondant à l'enregistrement
	 * courant.
	 */
	@Deprecated
	public long getCurrentWaitTime() {
		return (long) (getCurrentPhraseDuration() * /* p.param.tempsPauseEnPourcentageDuTempsDeLecture / 100. */1);
	}
	
	/**
	 * Mets en pause le thread courant pendant un certain temps.
	 * @param time le temps de pause, en millisecondes
	 * @param cursorName le type de curseur à définir pendant l'attente
	 * (peut être Constants.CURSOR_SPEAK ou Constants.CURSOR_LISTEN)
	 * @throws IllegalArgumentException si time est négatif ou si cursor n'est pas valide.
	 */
	public void doWait(long time, String cursorName) throws IllegalArgumentException {
		assertPositiveOrNull(time, "time");
		assertContains(cursorName, "cursorName", Constants.CURSOR_SPEAK, Constants.CURSOR_LISTEN);
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
	 * @throws IllegalArgumentException si n est négatif.
	 */
	public boolean waitForClick(int n) throws IllegalArgumentException {
		assertPositiveOrNull(n, "n");
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
	 * Colorie le segment numéro n avec la couleur de réussite.
	 * @throws IllegalArgumentException si n est négatif.
	 */
	public void highlightPhrase(int n) throws IllegalArgumentException {
		highlightPhrase(p.getEditorPane().gethParam().getRightColor(), n);
	}
	
	/**
	 * Colorie le segment numéro n avec la couleur de correction.
	 * @throws IllegalArgumentException si n est négatif.
	 */
	public void highlightCorrectionPhrase(int n) throws IllegalArgumentException {
		highlightPhrase(p.getEditorPane().gethParam().getCorrectionColor(), n);
	}
	
	private void highlightPhrase(final Color c, int n) throws IllegalArgumentException {
		assertNotNull(c, "color");
		assertPositiveOrNull(n, "n");
		if (p.getTextHandler().getPhrase(n) != null) {
			final int debutRelatifSegment = p.getTextHandler().getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(),
					n);
			final int finRelativeSegment = debutRelatifSegment + p.getTextHandler().getPhrase(n).length();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					p.getEditorPane().highlightPhrase(debutRelatifSegment, finRelativeSegment, c);
				}
			});
		}
	}
	
	/**
	 * Supprime tout le surlignage qui se trouve sur le segment <i>n</i>.<br/>
	 * @throws IllegalArgumentException si n est négatif.
	 */
	public void removeHighlightPhrase(int n) throws IllegalArgumentException {
		assertPositiveOrNull(n, "n");
		final int debutRelatifSegment = p.getTextHandler().getRelativeStartPhrasePosition(p.getFirstShownPhraseIndex(), n);
		final int finRelativeSegment = debutRelatifSegment + p.getTextHandler().getPhrase(n).length();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().removeHighlight(debutRelatifSegment, finRelativeSegment);
			}
		});
	}
	
	/**
	 * Arrête l'enregistrement courant et enlève tout le surlignage.
	 */
	@Deprecated
	public void stopAll() {
		p.getPlayer().stop();
		p.getEditorPane().removeAllHighlights();
	}
	
	/**
	 * Charge un segment de phrase dans le lecteur sans le démarrer.<br>
	 * Pas nécessaire si on démarre le lecteur directement avec la méthode
	 */
	@Deprecated
	public void loadSound(int phrase) {
		p.getPlayer().load(phrase);
	}
	
	/**
	 * Enlève tout le surlignage d'erreur.
	 */
	public void removeWrongHighlights() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().removeRedHighlight();
			}
		});
	}
	
	/**
	 * Enlève tout le surlignage présent.
	 */
	public void removeAllHighlights() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().removeAllHighlights();
			}
		});
	}
	
	/**
	 * Retourne la page qui contient le segment n, ou -1 si le segment n'existe pas.
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
	 * Surligne tout depuis le début de la page jusqu'au segment de phrase indiqué.<br/>
	 * n peut être négatif (rien ne sera surligné mais aucune exception ne sera déclenchée).
	 * @throws IllegalArgumentException si color est null
	 */
	public void highlightUntilPhrase(final Color color, final int n) throws IllegalArgumentException {
		assertNotNull(color, "color");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.highlightUntilPhrase(color, n);
			}
		});
	}
	
	/**
	 * Surligne le dernier mot sur lequel le patient a cliqué après avoir enlevé le
	 * surlignage d'erreur existant.
	 */
	public void highlightWrongWord() {
		removeWrongHighlights();
		int clickOffset = p.getControlerMouse().getLastTextOffset();
		int startOffset = p.getTextHandler().getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.getTextHandler().startWordPosition(clickOffset) + 1);
		int endOffset = p.getTextHandler().getRelativeOffset(p.getFirstShownPhraseIndex(),
				p.getTextHandler().endWordPosition(clickOffset));
		if (endOffset > startOffset) {
			highlight(p.getEditorPane().gethParam().getWrongColor(), startOffset, endOffset);
		}
	}
	
	private void highlight(final Color c, final int start, final int end) throws IllegalArgumentException {
		assertNotNull(c, "color");
		assertPositiveOrNull(start, "start");
		assertGreater(end, start, "end");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().highlightPhrase(start, end, c);
			}
		});
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
	 * maximal d'erreurs tolérés par segment).
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
	 * Modifie le nombre d'essais tolérés par segment.
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
		String message = "<html>L'exercice est terminé.";
		if (showErrors) {
			message += "<br/>Le patient a fait " + p.getNbErreurs() + (p.getNbErreurs() > 1 ? "s" : "") + ".";
		}
		if (showPhraseErrors) {
			message += "<br/>Le patient a fait " + p.getNbErreurs() + (p.getNbErreurs() > 1 ? "s" : "")
					+ " de segment.";
		}
		message += "</html>";
		final String finalMessage = message;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.showReport(finalMessage);
			}
		});
	}
	
	/**
	 * Se place sur le segment de numéro <i>n</i> et démarre l'algorithme de
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
	 * Event : CHANGE_POLICE
	 * @throws IllegalStateException si l'exercice a déjà commencé.
	 * @throws IllegalArgumentException si f est null ou invalide.
	 */
	public void setFont(final Font f) throws IllegalStateException, IllegalArgumentException {
		assertNotStarted(pilot, "change font");
		assertFont(f);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().setBaseFont(f);
				p.rebuildPages();
			}
		});
		List<Object> params = new ArrayList<>();
		params.add(f);
		dispatch(Event.setFont,params);
	}
	
	/**
	 * Retourne la police non modifiée par la mise en page automatique.
	 */
	public Font getFont() {
		return p.getEditorPane().getFont();
	}
	
	/**
	 * Change la taille de la police.
	 * @throws IllegalStateException si l'exercice a déjà commencé.
	 * @throws IllegalArgumentException si fontSize est nul ou négatif.
	 */
	public void setFontSize(float fontSize) throws IllegalStateException, IllegalArgumentException {
		assertPositive(fontSize, "fontSize");
		setFont(p.getEditorPane().getFont().deriveFont(fontSize));
	}
	
	/**
	 * Régle l'écartement vertical entre les lignes.<br/>
	 * 1 correspond à la hauteur d'une ligne selon la police utilisée (par défaut 0.8).<br/>
	 * <b>Attention !</b> Si la taille de police est grande, il ne faut pas utiliser un espacement
	 * trop grand afin de permettre la mise en page.
	 * @throws IllegalStateException si l'exercice a déjà commencé.
	 * @throws IllegalArgumentException si lineSpacing < 0.
	 */
	public void setLineSpacing(final float lineSpacing) throws IllegalArgumentException {
		assertNotStarted(pilot, "change line spacing");
		assertPositiveOrNull(lineSpacing, "lineSpacing");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().setLineSpacing(lineSpacing);
				p.rebuildPages();
			}
		});
	}
	
	/**
	 * Retourne l'écartement vertical relatif actuel entre les lignes.
	 */
	public float getLineSpacing() {
		return p.getEditorPane().getLineSpacing();
	}

	/**
	 * Termine l'exercice. Retourne au premier segment de la page. Permet de pouvoir
	 * faire à nouveau les réglages de base de l'exercice.
	 */
	public void end() {
		pilot.end();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.rebuildPages();
			}
		});
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
		p.getEditorPane().gethParam().setRightColor(rightColor);
		p.getEditorPane().gethParam().setWrongColor(wrongColor);
		p.getEditorPane().gethParam().setCorrectionColor(correctionColor);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().updateColors();
			}
		});
	}

	/**
	 * Change la couleur de fond de l'exercice.
	 */
	public void setBackgroundColor(final Color color) {
		p.getParam().setBgColor(color);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.getEditorPane().setBackground(color);
			}
		});
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pilot.updateCurrentPhrase();
				p.setNbErreursSegmentCourant(0);
			}
		});
	}
	
	/**
	 * Retourne le numéro du premier segment affiché de la page actuelle.
	 */
	public int getFirstShownPhraseIndex() {
		return p.getFirstShownPhraseIndex();
	}
	
	/**
	 * Retourne le nombre de segments affiché actuellement.
	 */
	public int getShownPhrasesCount() {
		return p.getPhrasesInFonctionOfPages().get(p.getCurrentPage()).size();
	}
	
	/**
	 * Active ou désactive les contrôles clavier (touche gauche pour répéter le
	 * segment, touche espace pour arrêter/recommencer).
	 */
	public void setKeyEnabled(final boolean keyEnabled) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				/// ajoute un contrôle clavier ///
				if (keyEnabled && p.getKeyListeners().length == 0) {
					ControllerKey controlerKey = new ControllerKey(pilot);
					p.getEditorPane().addKeyListener(controlerKey);
				}
				/// retire les contrôles clavier ///
				else if (!keyEnabled && p.getKeyListeners().length >= 1) {
					for (KeyListener listener : p.getKeyListeners()) {
						p.removeKeyListener(listener);
					}
				}
			}
		});
	}
	
	/**
	 * Retourne le rectangle du panneau de l'exercice
	 */
	public Rectangle getBounds() {
		return p.getEditorPane().getBounds();
	}
	
	/**
	 * Retourne la largeur de la fenêtre, en centimètres.
	 */
	public float getFrameWidth() {
		return SegmentedTextFrame.pxToCm(p.getFenetre().getWidth());
	}
	
	/**
	 * Retourne la hauteur de la fenêtre, en centimètres.
	 */
	public float getFrameHeight() {
		return SegmentedTextFrame.pxToCm(p.getFenetre().getHeight());
	}
	
	/**
	 * Cache ou fait apparaître les sliders qui permettent d'ajuster les marges sur les côtés du texte.<br>
	 * Ceux-ci sont automatiquement désactivés lorsque l'exercice démarre.<br>
	 * Par défault, ils sont visibles lors du démarrage de la fenêtre.
	 */
	public void setMarginSlidersVisible(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.setSlidersVisible(visible);
			}
		});
	}
	
	/**
	 * Retourne la marge à gauche du texte.
	 */
	public float getLeftMargin() {
		return p.getEditorPane().getLeftMargin();
	}
	
	/**
	 * Retourne la marge à droite du texte.
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
	 * Applique les marges sur les côtés du texte.<br>
	 * Par défaut, si paramètres ne sont pas modifiés,
	 * des marges horizontales sont placées automatiquement.
	 * @throws IllegalStateException si l'exercice a déjà commencé.
	 * @throws IllegalArgumentException si leftMargin < 0, rightMargin < 0,
	 * topMargin < 0 ou bottomMargin < 0.
	 */
	public void setMargin(final float leftMargin, final float rightMargin,
			final float topMargin, final float bottomMargin) throws IllegalArgumentException, IllegalStateException {
		assertNotStarted(pilot, "change margin");
		assertPositiveOrNull(leftMargin, "leftMargin");
		assertPositiveOrNull(rightMargin, "rightMargin");
		assertPositiveOrNull(topMargin, "topMargin");
		assertPositiveOrNull(bottomMargin, "bottomMargin");
		setMargin(new Runnable() {
			public void run() {
				p.getEditorPane().setLeftMargin(leftMargin > 0
						? leftMargin : p.getDefaultLeftMargin());
				p.getEditorPane().setRightMargin(rightMargin > 0
						? rightMargin : p.getDefaultRightMargin());
				p.getEditorPane().setTopMargin(topMargin > 0
						? topMargin : p.getDefaultTopMargin());
				p.getEditorPane().setBottomMargin(bottomMargin > 0
						? bottomMargin : p.getDefaultBottomMargin());
			}
		});
	}
	
	/**
	 * Applique une marge à gauche du texte.
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 * @throws IllegalStateException si leftMargin < 0.
	 */
	public void setLeftMargin(final float leftMargin) throws IllegalArgumentException, IllegalStateException {
		assertNotStarted(pilot, "change margin");
		assertPositiveOrNull(leftMargin, "leftMargin");
		setMargin(new Runnable() {
			public void run() {
				p.getEditorPane().setLeftMargin(leftMargin > 0
						? leftMargin : p.getDefaultLeftMargin());
			}
		});
	}
	
	/**
	 * Applique une marge à droite du texte.
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 * @throws IllegalStateException si rightMargin < 0.
	 */
	public void setRightMargin(final float rightMargin) throws IllegalArgumentException, IllegalStateException {
		assertNotStarted(pilot, "change margin");
		assertPositiveOrNull(rightMargin, "rightMargin");
		setMargin(new Runnable() {
			public void run() {
				p.getEditorPane().setRightMargin(rightMargin > 0
						? rightMargin : p.getDefaultRightMargin());
			}
		});
	}
	
	/**
	 * Applique une marge en haut du texte.
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 * @throws IllegalStateException si topMargin < 0.
	 */
	public void setTopMargin(final float topMargin) throws IllegalArgumentException, IllegalStateException {
		assertNotStarted(pilot, "change margin");
		assertPositiveOrNull(topMargin, "topMargin");
		setMargin(new Runnable() {
			public void run() {
				p.getEditorPane().setTopMargin(topMargin > 0
						? topMargin : p.getDefaultTopMargin());
			}
		});
	}
	
	/**
	 * Applique une marge en bas du texte.
	 * @throws IllegalArgumentException si l'exercice a déjà commencé.
	 * @throws IllegalStateException si bottomMargin < 0.
	 */
	public void setBottomMargin(final float bottomMargin) throws IllegalArgumentException, IllegalStateException {
		assertNotStarted(pilot, "change margin");
		assertPositiveOrNull(bottomMargin, "bottomMargin");
		setMargin(new Runnable() {
			public void run() {
				p.getEditorPane().setBottomMargin(bottomMargin > 0
						? bottomMargin : p.getDefaultBottomMargin());				
			}
		});
	}
	
	private void setMargin(final Runnable action) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				action.run();
				p.updateSliders();
				p.rebuildPages();
			}
		});
	}
	
	public void addCustomSliderListener(final PropertyChangeListener sliderListener) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.addCustomSliderListener(sliderListener);
			}
		});
	}
	
	/**
	 * Gèle la fenetre si b est <code>true</code>, la dégèle si b est
	 * <code>false</code>
	 */
	public void freeze(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				p.getFenetre().setResizable(!b); 
			}
		});
	}
	
	/**
	 * Definis le nombre maximum de segments à afficher par page
	 */
	public void setMaxSegmentByPage(int i) {
		p.getrParam().setMaxSegmentByPage(i);
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
	public void showHolesInPage(final int h, final int page) {
		assertPositiveOrNull(h, "h");
		assertPositiveOrNull(page, "page");
		
		// réinitialisation des trous
		removeAllMasks();
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				// pour tous les trous
				for (int i = 0; i < p.getTextHandler().getHolesCount(); i++) {
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
		});
	}
	
	/**
	 * Montre le trou h
	 */
	private void showHole(final int h) {
		assertPositiveOrNull(h, "h");
		
		/// on cache le trou avant de montrer la fenêtre ///
		hideHole(h);

		int startPhrase = p.getPhrasesInFonctionOfPages().get(getPageOf(h)).get(0);

		final int start = p.getTextHandler().getRelativeOffset(startPhrase, p.getTextHandler().getHoleStartOffset(h));
		final int end = p.getTextHandler().getRelativeOffset(startPhrase, p.getTextHandler().getHoleEndOffset(h));

		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				try {
					p.showFrame(start, end, h);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Retourne la page correspondant au trou h
	 */
	public int getPageOf(int h) {
		assertPositiveOrNull(h, "h");
		return getPageOfPhrase(getPhraseOf(h));
	}
	
	/**
	 * Retourne le segment correspondant au trou h
	 */
	public int getPhraseOf(int h) {
		return p.getTextHandler().getPhraseOf(h);
	}
	
	/**
	 * Retourne le nombre de trous associées au segment n.
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
	 * Définit le temps d'aperçu du mot en fonction du nombre de caractères. Mettre
	 * à 0 pour un aperçu inexistant. Mettre à -1 pour un aperçu jusqu'à saisie.
	 */
	public void setHint(int value) {
		assertGreaterOrEquals(value, -1, "value");
		
		if (value == -1) {
			value = 99999;
		}
		p.getrParam().setHintDuration(value);
	}
	
	/**
	 * Active la fenêtre du trou actuel. Montre l'aperçu du mot. Renvoie
	 * <code>true</code> si le mot est bon, <code>false</code> sinon.
	 */
	public boolean waitForFill(int h) throws IllegalThreadStateException {
		assertSwingThread("call waitForFill");
		assertPositiveOrNull(h, "h");
		
		Mask m = getMask(h);
		if (m == null)
			return true;
		m.activate();
		hint(m);
		
		p.getControlerMask().waitForFill();
		
		return m != null && m.getN() == h ? m.correctWord() : true;

	}
	
	/**
	 * Active la fenêtre fixe. Colorier le masque actuel en cyan ( ou jaune si le
	 * fond est cyan ). Montre l'aperçu du mot dans la fenêtre fixe. Attend une
	 * saisie. Si juste : désactive la fenêtre fixe et renvoie <code>true</code>. Si
	 * faux : renvoie <code>false</code>;
	 */
	public boolean waitForFillFixedFrame(int h) {
		assertSwingThread("call waitForFillFixedFrame");
		assertPositiveOrNull(h, "h");
		
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
	 * Affiche l'aperçu du mot sur le masque m
	 */
	private void hint(final Mask mask) {
		/*
		 * if (p.param.timeToShowWord == -1) { m.setHint(); } else {
		 * m.setHint(p.param.timeToShowWord * m.getNbCarac()); }
		 */
		assertNotNull(mask, "mask");
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				mask.setHint(p.getrParam().getHintDuration() * mask.getNbCarac());
			}
		});
	}
	
	/**
	 * Enlève tous les masques
	 */
	public void removeAllMasks() {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				p.removeAllMasks();
			}
		});
	}
	
	/**
	 * Fait clignoter l'exercice avec la couleur c
	 */
	public void blink(final Color c) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				p.blink(c);
			}
		});
		List<Object> params = new ArrayList<>();
		params.add(c);
		dispatch(Event.blink,params);
	}
	
	/**
	 * Colore le trou h en couleur c
	 */
	public void colorFixedFrame(final int h, final Color c) {
		assertPositiveOrNull(h, "h");
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				getMask(h).getJtf().setBackground(c);
			}
		});
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
	 * Crée une fenêtre de saisie fixe qui attends le resultat du trou h
	 */
	public void activateInputFixedFrame(final int h) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
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
		});
		List<Object> params = new ArrayList<>();
		params.add(h);
		dispatch(Event.activateInputFixedFrame,params);
	}
	
	/**
	 * Met le mode fenêtre fixe si <code>true</code>, met le mode fenêtre non fixe
	 * si <code>false</code>
	 */
	public void setModeFixedField(boolean b) {
		p.getrParam().setFixedField(b);
	}
	
	/**
	 * Cette méthode propose la gestion d'erreur suivante : Clignote avec la couleur
	 * d'alerte. Incremente le nombre d'erreur. Reaffiche le hint. Si vous voulez un
	 * traitement d'erreur personnalisé ne pas utiliser cette méthode.
	 */
	public void doError(final int h) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
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
			}
		});
		/*
		 * if (p.rParam.replayPhrase) { play(p.pilot.getCurrentPhraseIndex());
		 * doWait(getCurrentWaitTime(), Constants.CURSOR_LISTEN); }
		 */

	}
	
	/**
	 * Désactive la fênetre fixe
	 * 
	 * @throws RuntimeException si elle est null.
	 */
	public void desactivateFixedFrame() {
		if (getFixedFrame() == null) {
			throw new RuntimeException("La fenêtre fixe est null");
		}
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				getFixedFrame().dispose();
			}
		});
	}
	
	/**
	 * Retourne la fenêtre fixe
	 */
	private Mask getFixedFrame() {
		return p.getFixedFrame();
	}
	
	/**
	 * Cache le trou h et le remplace par le bon mot
	 */
	public void replaceMaskByWord(final int h) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				Mask m = getMask(h);
				if (m != null) {
					m.setVisible(false);
				}
				fillHole(h);
			}
		});
	}
	
	/**
	 * Remplace le trou h par le bon mot.
	 */
	public void fillHole(final int h) {
		if (p.getTextHandler().isHidden(h)) {
			SwingUtilities.invokeLater(new Runnable() {	
				@Override
				public void run() {
					p.getTextHandler().fillHole(h);
					p.updateText();
				}
			});
		}
	}
	
	/**
	 * Cache le trou h.
	 */
	public void hideHole(final int h) {
		if (!p.getTextHandler().isHidden(h)) {
			SwingUtilities.invokeLater(new Runnable() {	
				@Override
				public void run() {
					p.getTextHandler().hideHole(h);
					p.updateText();
					p.replaceAllMask();
				}
			});
		}
	}
	
	/**
	 * Retourne <code>true</code> si le segment n contient au moins un trou.
	 */
	public boolean hasHole(int n) {
		return p.getTextHandler().hasHole(n);
	}
	
	/**
	 * Retourne le numéro du premier trou à partir du segment indiqué.<br>
	 * Retourne -1 s'il n'y a aucun trou après le segment.
	 */
	public int getFirstHole(int n) {
		return p.getTextHandler().getFirstHole(n);
	}
	
	/**
	 * Desaffiche tous les trous puis montre uniquement le trou h
	 */
	public void showJustHole(final int h) {
		assertPositiveOrNull(h, "h");
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				removeAllMasks();
				showHole(h);
			}
		});
	}

	public EventDispatcher getEventDispatcher() {
		return ed;
	}

}
