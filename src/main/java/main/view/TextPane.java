package main.view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import main.Constants;
import main.Parametres;
import main.model.ReadingParameters;
import main.model.ToolParameters;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;

	private List<Object> redHighlightTags = new ArrayList<>();
	private List<Object> blueHighlightTags = new ArrayList<>();
	private List<Object> greenHighlightTags = new ArrayList<>();
	
	public ToolParameters param;
	public ReadingParameters rParam
		= new ReadingParameters(); // TODO enleve
	
	public String textReel;

	public TextPane() {
		setSelectionColor(new Color(0, 0, 0, 0));

		/// mets les marges sur les cétés ///
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, 1);
		StyleConstants.setSpaceAbove(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setLeftIndent(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setRightIndent(attrs, Constants.TEXTPANE_MARGING);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}

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
			if (couleur.equals(rParam.wrongColor)) {
				redHighlightTags.add(tag);
			} else if (couleur.equals(rParam.correctionColor)) {
				blueHighlightTags.add(tag);
			} else if (couleur.equals(rParam.rightColor)) {
				greenHighlightTags.add(tag);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void enleverSurlignageRouge() {
		if (redHighlightTags != null) {
			for (int i = 0; i < redHighlightTags.size(); i++) {
				getHighlighter().removeHighlight(redHighlightTags.get(i));
			}
			redHighlightTags.clear();
		}
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
		redHighlightTags.clear();
		greenHighlightTags.clear();
		blueHighlightTags.clear();
	}

	/**
	 * Enléve tout le surlignage présent entre les bornes start et end.
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
					new DefaultHighlighter.DefaultHighlightPainter(rParam.correctionColor));
			newBlue.add(object);
		}
		for (Object object : redHighlightTags) {
			Highlight g = (Highlight) object;
			object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
					new DefaultHighlighter.DefaultHighlightPainter(rParam.wrongColor));
			newRed.add(object);
		}
		for (Object object : greenHighlightTags) {
			Highlight g = (Highlight) object;
			object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
					new DefaultHighlighter.DefaultHighlightPainter(rParam.rightColor));
			newGreen.add(object);
		}
		enleverSurlignageBleu();
		enleverSurlignageRouge();
		enleverSurlignageVert();
		blueHighlightTags = newBlue;
		redHighlightTags = newRed;
		greenHighlightTags = newGreen;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

}
