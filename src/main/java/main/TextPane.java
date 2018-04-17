package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.text.*;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;

	private List<Object> redHighlightTags = new ArrayList<>();
	private List<Object> blueHighlightTags = new ArrayList<>();
	private List<Object> greenHighlightTags = new ArrayList<>();
	private List<Object> blueHighlightTagsMemory = new ArrayList<>();

	public TextPane() {
		setFont(FenetreParametre.police);
		setBackground(FenetreParametre.couleurFond);
		setSelectionColor(new Color(0, 0, 0, 0));

		/// mets les marges sur les c�t�s ///
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, 1);
		StyleConstants.setSpaceAbove(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setLeftIndent(attrs, Constants.TEXTPANE_MARGING);
		StyleConstants.setRightIndent(attrs, Constants.TEXTPANE_MARGING);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}

	public int indiceDernierCaractereSurlign�;

	/**
	 * surligne tout de d�but � fin avec la couleur sp�cifi�e
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
		for (Object tag : redHighlightTags) {
			getHighlighter().removeHighlight(tag);
		}
		redHighlightTags.clear();
	}

	public void enleverSurlignageBleu() {
		for (Object tag : blueHighlightTags) {
			getHighlighter().removeHighlight(tag);
		}
		blueHighlightTags.clear();
	}

	public void enleverSurlignageVert() {
		for (Object tag : greenHighlightTags) {
			getHighlighter().removeHighlight(tag);
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
	public void d�surlignerTout() {
		getHighlighter().removeAllHighlights();
		indiceDernierCaractereSurlign� = 0;
		redHighlightTags.clear();
		greenHighlightTags.clear();
		blueHighlightTagsMemory.addAll(blueHighlightTags);
		blueHighlightTags.clear();
	}

	private static ArrayList<Object> antiDoublon(ArrayList<Object> al) {
		ArrayList<Object> al2 = new ArrayList<Object>();
		for (int i = 0; i < al.size(); i++) {
			Object o = al.get(i);
			if (!al2.contains(o))
				al2.add(o);
		}
		al = null;
		return al2;
	}

	public void retablirSurlignageBlue() {
		blueHighlightTags.addAll(blueHighlightTagsMemory);
		blueHighlightTags = TextPane.antiDoublon((ArrayList<Object>) blueHighlightTags);
		blueHighlightTagsMemory = TextPane.antiDoublon((ArrayList<Object>) blueHighlightTagsMemory);
		Highlighter highlighter = this.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Constants.WRONG_PHRASE_COLOR);
		for (Object o : blueHighlightTags) {
			Highlight temp = (Highlight) o;
			try {
				Panneau pan = (Panneau) getParent();
				TextHandler handler = pan.textHandler;
				highlighter.addHighlight(
						handler.getRelativeOffset(pan.getNumeroPremierSegmentAffich�(), temp.getStartOffset()),
						handler.getRelativeOffset(pan.getNumeroPremierSegmentAffich�(), temp.getEndOffset()), painter);
			} catch (BadLocationException e) {
				e.printStackTrace();
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
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10000; i++) {
			if (i % 2 == 0) g.setColor(Color.RED);
			else g.setColor(Color.BLUE);
			try {
				((Graphics2D) g).draw(modelToView(i));
			} catch (BadLocationException e) {
				break;
			}
		}
	}

}
