package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class TextPane extends JTextPane {
	
	private static final long serialVersionUID = 1L;

	public TextPane() {
		setFont(new Font("OpenDyslexic", Font.BOLD, 20));
		setBackground(new Color(255, 255, 150));
	}
	
	public void insert(int offset, String str) {
		StringBuilder builder = new StringBuilder(getText());
		builder.insert(offset, str);
		setText(builder.toString());
	}
	
	int indiceDernierCaractereSurligne;
	
	/**
	 * surligne tout jusqu'à positionClic avec la couleur couleur
	 *
	 */
	public void surlignerPhrase(int positionClic, Color couleur) {
		if ( positionClic < indiceDernierCaractereSurligne){
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
					doc.insertString(i, ""+chaine.toCharArray()[i], doc.getStyle("surligner"));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			} else {
				try {
					doc.insertString(i,""+chaine.toCharArray()[i], doc.getStyle(""));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
}
