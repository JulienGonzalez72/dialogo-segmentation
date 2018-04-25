package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import java.util.*;
import javax.swing.JTextPane;
import javax.swing.text.*;
import javax.swing.text.Highlighter.*;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;

	private List<Object> redHighlightTags = new ArrayList<>();
	private List<Object> blueHighlightTags = new ArrayList<>();
	private List<Object> greenHighlightTags = new ArrayList<>();

	public TextPane() {
		setFont(FenetreParametre.police);
		setBackground(FenetreParametre.couleurFond);
		setSelectionColor(new Color(0, 0, 0, 0));

		/// mets les marges sur les côtés ///
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, 1);
		StyleConstants.setSpaceAbove(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setLeftIndent(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setRightIndent(attrs, Constants.TEXTPANE_MARGING);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}

	public int indiceDernierCaractereSurligné;

	/**
	 * surligne tout de début à fin avec la couleur spécifiée
	 *
	 */
	public void surlignerPhrase(int debut, int fin, Color couleur) {
		if (fin <= debut)
			return;
		try {
			Object tag = getHighlighter().addHighlight(debut, fin,
					new DefaultHighlighter.DefaultHighlightPainter(couleur));
			if (couleur.equals(Constants.WRONG_COLOR)) {
				redHighlightTags.add(tag);
			} else if (couleur.equals(Constants.WRONG_PHRASE_COLOR)) {
				blueHighlightTags.add(tag);
			} else if (couleur.equals(Constants.RIGHT_COLOR)) {
				greenHighlightTags.add(tag);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void enleverSurlignageRouge() {
		for (int i = 0; i < redHighlightTags.size(); i++) {
			getHighlighter().removeHighlight(redHighlightTags.get(i));
		}
		redHighlightTags.clear();
	}

	public void enleverSurlignageBleu() {
		for (int i = 0; i < blueHighlightTags.size(); i++) {
			getHighlighter().removeHighlight(blueHighlightTags.get(i));
		}
		blueHighlightTags.clear();
	}

	public void enleverSurlignageVert() {
		for (int i = 0; i < greenHighlightTags.size(); i++) {
			getHighlighter().removeHighlight(greenHighlightTags.get(i));
		}
		greenHighlightTags.clear();
	}

	public boolean containsBlueHighlight() {
		return !blueHighlightTags.isEmpty();
	}

	/**
	 * desurligne tout
	 *
	 */
	public void désurlignerTout() {
		getHighlighter().removeAllHighlights();
		indiceDernierCaractereSurligné = 0;
		redHighlightTags.clear();
		greenHighlightTags.clear();
		blueHighlightTags.clear();
	}

	/**
	 * Enlève tout le surlignage présent entre les bornes start et end.
	 */
	public void removeHighlight(int start, int end) {
		Highlight[] hl = getHighlighter().getHighlights();
		for (int i = 0; i < hl.length; i++) {
			if ((hl[i].getStartOffset() >= start && hl[i].getEndOffset() <= end)
					|| (hl[i].getEndOffset() >= start && hl[i].getEndOffset() <= end)) {
				getHighlighter().removeHighlight(hl[i]);
			}
		}
	}

	public Rectangle getTextBounds(String str) {
		return getFont().createGlyphVector(getFontMetrics(getFont()).getFontRenderContext(), str).getPixelBounds(null,
				0, 0);
	}

	public float getSpacingFactor() {
		FontMetrics fm = getFontMetrics(getFont());
		return (float) (1f + fm.getHeight() / getTextBounds("|").getHeight());
	}

	public void updateColors() throws BadLocationException {
		List<Object> newBlue = new ArrayList<>();
		List<Object> newGreen = new ArrayList<>();
		List<Object> newRed = new ArrayList<>();
		for (Object object : blueHighlightTags) {
			Highlight g = (Highlight) object;
			object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
					new DefaultHighlighter.DefaultHighlightPainter(Constants.WRONG_PHRASE_COLOR));
			newBlue.add(object);
		}
		for (Object object : redHighlightTags) {
			Highlight g = (Highlight) object;
			object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
					new DefaultHighlighter.DefaultHighlightPainter(Constants.WRONG_COLOR));
			newRed.add(object);
		}
		for (Object object : greenHighlightTags) {
			Highlight g = (Highlight) object;
			object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
					new DefaultHighlighter.DefaultHighlightPainter(Constants.RIGHT_COLOR));
			newGreen.add(object);
		}
		enleverSurlignageBleu();
		enleverSurlignageRouge();
		enleverSurlignageVert();
		blueHighlightTags = newBlue;
		redHighlightTags = newRed;
		greenHighlightTags = newGreen;
	}

}
