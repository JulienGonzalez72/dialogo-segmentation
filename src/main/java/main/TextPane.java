package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class TextPane extends JTextPane {

	private static final long serialVersionUID = 1L;
	
	public static final float MARGING = 20f;
	
	private Object redHightlightTag;
	
	public TextPane() {
		setFont(new Font("OpenDyslexic", Font.BOLD, 20));
		setBackground(new Color(255, 255, 150));
		
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attrs, 1);
		StyleConstants.setSpaceAbove(attrs, MARGING);
		StyleConstants.setLeftIndent(attrs, MARGING);
		StyleConstants.setRightIndent(attrs, MARGING);
		getStyledDocument().setParagraphAttributes(0, 0, attrs, false);
	}

	public void insert(int offset, String str) {
		StringBuilder builder = new StringBuilder(getText());
		builder.insert(offset, str);
		setText(builder.toString());
	}

	public int indiceDernierCaractereSurligne;

	/**
	 * surligne tout jusqu'à positionClic avec la couleur spécifiée
	 *
	 */
	public void surlignerPhrase(int positionClic, Color couleur) {
		if (positionClic < indiceDernierCaractereSurligne) {
			positionClic = indiceDernierCaractereSurligne;
		}
		indiceDernierCaractereSurligne = positionClic;
		StyledDocument doc = this.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style regular = doc.addStyle("regular", def);
		Style s = doc.addStyle("surligner", regular);
		StyleConstants.setBackground(s, couleur);
		String chaine = this.getText();
		this.setText("");
		for (int i = 0; i < chaine.length(); i++) {
			if (i < positionClic) {
				try {
					doc.insertString(i, "" + chaine.toCharArray()[i], doc.getStyle("surligner"));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			} else {
				try {
					doc.insertString(i, "" + chaine.toCharArray()[i], doc.getStyle(""));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * surligne tout de début à fin avec la couleur spécifiée
	 *
	 */
	public void surlignerPhrase(int debut, int fin , Color couleur) {
		if (fin <= debut)
			return;
		try {
			Object tag = getHighlighter().addHighlight(debut, fin, new DefaultHighlighter.DefaultHighlightPainter(couleur));
			if (couleur.equals(Panneau.WRONG_COLOR))
				redHightlightTag = tag;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void enleverSurlignageRouge() {
		if (redHightlightTag != null)
			getHighlighter().removeHighlight(redHightlightTag);
	}

	/**
	 * desurligne tout
	 *
	 */
	public void désurlignerTout() {
		getHighlighter().removeAllHighlights();
		indiceDernierCaractereSurligne = 0;
	}

}
