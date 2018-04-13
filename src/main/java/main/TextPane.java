package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;
	
	private Object redHighlightTag;
	private Object blueHighlightTag;
	
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
				enleverSurlignageRouge();
				redHighlightTag = tag;
			}
			else if (couleur.equals(Constants.WRONG_PHRASE_COLOR)) {
				enleverSurlignageBleu();
				blueHighlightTag = tag;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void enleverSurlignageRouge() {
		if (redHighlightTag != null) {
			getHighlighter().removeHighlight(redHighlightTag);
			redHighlightTag = null;
		}
	}

	public void enleverSurlignageBleu() {
		if (blueHighlightTag != null) {
			getHighlighter().removeHighlight(blueHighlightTag);
			blueHighlightTag = null;
		}
	}
	
	public boolean containsBlueHighlight() {
		return blueHighlightTag != null;
	}
	
	/**
	 * desurligne tout
	 *
	 */
	public void désurlignerTout() {
		getHighlighter().removeAllHighlights();
		indiceDernierCaractereSurligné = 0;
	}
	
	public Rectangle getTextBounds(String str) {
		return getFont().createGlyphVector(getFontMetrics(getFont()).getFontRenderContext(), str).getPixelBounds(null, 0, 0);
	}
	
	public float getSpacingFactor() {
		FontMetrics fm = getFontMetrics(getFont());
		return (float) (1f + fm.getHeight() / getTextBounds("|").getHeight());
	}

}
