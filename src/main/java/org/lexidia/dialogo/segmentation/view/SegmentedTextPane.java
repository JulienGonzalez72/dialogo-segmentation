package org.lexidia.dialogo.segmentation.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.Highlight;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexidia.dialogo.segmentation.main.Constants;
import org.lexidia.dialogo.segmentation.model.HighlightParameters;
import org.lexidia.dialogo.segmentation.model.ToolParameters;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;

public class SegmentedTextPane extends JTextPane {
	
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(SegmentedTextPane.class);
	
	private List<Object> redHighlightTags = new ArrayList<>();
	private List<Object> blueHighlightTags = new ArrayList<>();
	private List<Object> greenHighlightTags = new ArrayList<>();
	
	private ToolParameters param;
	private HighlightParameters hParam = new HighlightParameters();
	private float lineSpacing;
	private float leftMargin, rightMargin, topMargin, bottomMargin;
	private boolean center;
	private Font baseFont;
	
	private String textReel;
	
	public SegmentedTextPane() {
		setSelectionColor(new Color(0, 0, 0, 0));
		
		/// mets les marges sur les côtés ///
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, lineSpacing = Constants.DEFAULT_LINE_SPACING);
		StyleConstants.setSpaceAbove(attrs, topMargin = Constants.TEXTPANE_MARGIN);
		StyleConstants.setLeftIndent(attrs, leftMargin = Constants.TEXTPANE_MARGIN);
		StyleConstants.setRightIndent(attrs, rightMargin = Constants.TEXTPANE_MARGIN);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
		
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				/// centre le texte verticallement ///
				if (center) {
					centerText();
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}
	
	/**
	 * Compte le nombre de lignes affichées.<br>
	 * Source : <a href="http://www.camick.com/java/source/RXTextUtilities.java">http://www.camick.com/java/source/RXTextUtilities.java</a>
	 */
	private int getWrappedLines() {
		int lines = 0;
		View view = getUI().getRootView(this).getView(0);
		int paragraphs = view.getViewCount();
		for (int i = 0; i < paragraphs; i++)
		{
			lines += view.getView(i).getViewCount();
		}
		return lines;
	}
	
	/**
	 * surligne tout de début à fin avec la couleur spécifiée
	 *
	 */
	public void highlightPhrase(int debut, int fin, Color couleur) {
		if (fin <= debut)
			return;
		try {
			Object tag = getHighlighter().addHighlight(debut, fin,
					new DefaultHighlighter.DefaultHighlightPainter(couleur));
			if (couleur.equals(gethParam().getWrongColor())) {
				redHighlightTags.add(tag);
			} else if (couleur.equals(gethParam().getCorrectionColor())) {
				blueHighlightTags.add(tag);
			} else if (couleur.equals(gethParam().getRightColor())) {
				greenHighlightTags.add(tag);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void removeRedHighlight() {
		if (redHighlightTags != null) {
			for (int i = 0; i < redHighlightTags.size(); i++) {
				getHighlighter().removeHighlight(redHighlightTags.get(i));
			}
			redHighlightTags.clear();
		}
	}
	
	public void removeBlueHighlight() {
		for (int i = 0; i < blueHighlightTags.size(); i++) {
			getHighlighter().removeHighlight(blueHighlightTags.get(i));
		}
		blueHighlightTags.clear();
	}
	
	public void removeGreenHighlight() {
		for (int i = 0; i < greenHighlightTags.size(); i++) {
			getHighlighter().removeHighlight(greenHighlightTags.get(i));
		}
		greenHighlightTags.clear();
	}
	
	/**
	 * desurligne tout
	 */
	public void removeAllHighlights() {
		getHighlighter().removeAllHighlights();
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
		return getFont().createGlyphVector(getFontMetrics(getFont()).getFontRenderContext(), str)
				.getPixelBounds(null, 0, 0);
	}
	
	public float getSpacingFactor() {
		FontMetrics fm = getFontMetrics(getFont());
		return (float) (1f + fm.getHeight() / getTextBounds("|").getHeight());
	}
	
	public void updateColors() {
		try {
			List<Object> newBlue = new ArrayList<>();
			List<Object> newGreen = new ArrayList<>();
			List<Object> newRed = new ArrayList<>();
			for (Object object : blueHighlightTags) {
				Highlight g = (Highlight) object;
				object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
						new DefaultHighlighter.DefaultHighlightPainter(gethParam().getCorrectionColor()));
				newBlue.add(object);
			}
			for (Object object : redHighlightTags) {
				Highlight g = (Highlight) object;
				object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
						new DefaultHighlighter.DefaultHighlightPainter(gethParam().getWrongColor()));
				newRed.add(object);
			}
			for (Object object : greenHighlightTags) {
				Highlight g = (Highlight) object;
				object = getHighlighter().addHighlight(g.getStartOffset(), g.getEndOffset(),
						new DefaultHighlighter.DefaultHighlightPainter(gethParam().getRightColor()));
				newGreen.add(object);
			}
			removeBlueHighlight();
			removeRedHighlight();
			removeGreenHighlight();
			blueHighlightTags = newBlue;
			redHighlightTags = newRed;
			greenHighlightTags = newGreen;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public ToolParameters getParam() {
		return param;
	}
	
	public ToolParameters setParam(ToolParameters param) {
		this.param = param;
		return param;
	}
	
	public HighlightParameters gethParam() {
		return hParam;
	}
	
	public void sethParam(HighlightParameters hParam) {
		this.hParam = hParam;
	}
	
	public String getTextReel() {
		return textReel;
	}
	
	public void setTextReel(String textReel) {
		this.textReel = textReel;
	}
	
	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, lineSpacing);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}
	
	public float getLineSpacing() {
		return lineSpacing;
	}
	
	public void centerText() {		
		int lines = getWrappedLines();
		float lineHeight = getFontMetrics(getFont()).getHeight() * (lineSpacing + 1f);
		setTopMargin(getHeight() / 2f - lines * lineHeight / 2f);
	}
	
	public void setTopMargin(float margin) {
		log.info("Top margin : " + margin);
		Insets bounds = getMargin();
		bounds.top = (int) (bottomMargin = margin);
		setMargin(bounds);
	}
	
	public void setHorizontalMargin(float margin) {
		setLeftMargin(margin);
		setRightMargin(margin);
	}
	
	public void setLeftMargin(float margin) {
		log.info("Left margin : " + margin);
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLeftIndent(attrs, leftMargin = margin);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}
	
	public void setRightMargin(float margin) {
		log.info("Right margin : " + margin);
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setRightIndent(attrs, rightMargin = margin);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}
	
	public void setBottomMargin(float margin) {
		log.info("Bottom margin : " + margin);
		Insets bounds = getMargin();
		bounds.bottom = (int) (bottomMargin = margin);
		setMargin(bounds);
	}
	
	public float getLeftMargin() {
		return leftMargin;
	}
	
	public float getRightMargin() {
		return rightMargin;
	}
	
	public float getTopMargin() {
		return topMargin;
	}
	
	public float getBottomMargin() {
		return bottomMargin;
	}
	
	/**
	 * Diminue la taille de la police courante.
	 */
	public void decreaseFontSize(float decrease) {
		setFont(getFont().deriveFont(
				getFont().getSize() - decrease));
	}
	
	/**
	 * Augmente la taille de la police courante.
	 */
	public void increaseFontSize(float increase) {
		setFont(getFont().deriveFont(
				getFont().getSize() + increase));
	}
	
	/**
	 * Si on peut augmenter la taille de la police d'une valeur <i>increase</i>
	 * sans qu'elle ne dépasse la taille de la police de base.
	 */
	public boolean canIncreaseFontSize(float increase) {
		return getFont().getSize() + increase <= baseFont.getSize();
	}
	
	/**
	 * Applique la police de base.
	 */
	public void resetFont() {
		setFont(baseFont);
	}
	
	/**
	 * Modifie la police de base de l'editor pane.
	 */
	public void setBaseFont(Font font) {
		setFont(font);
		this.baseFont = font;
	}
	
	public Font getBaseFont() {
		return baseFont;
	}
	
	public void hideText() {
		setForeground(new Color(0, 0, 0, 0));
	}
	
	public void showText() {
		setForeground(Color.BLACK);
	}
	
	public void setLine(Line2D line) {
		this.line = line;
	}
	
	protected Rectangle debugRect;
	protected List<Rectangle> debugRects = new ArrayList<>();
	private Line2D line;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		java.awt.Graphics2D g2 = (Graphics2D) g;
		if (line != null) {
			g2.setColor(new Color(0, 0, 0, 100));
			g2.draw(line);
		}
		if (debugRect != null) {
			g2.setColor(Color.BLUE);
			g2.draw(debugRect);
		}
		for (Rectangle rect : debugRects) {
			g2.setColor(Color.RED);
			g2.draw(rect);
		}
	}
	
}
