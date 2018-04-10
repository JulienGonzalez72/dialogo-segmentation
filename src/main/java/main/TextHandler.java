package main;

import java.time.chrono.IsoChronology;

public class TextHandler {
	
	private String txt;
	/**
	 * Numéro de la césure courante
	 */
	private int currentPause;
	
	public TextHandler(String texteOriginal) {
		this.txt = texteOriginal;
	}
	
	/**
	 * Retourne le texte sans slash
	 */
	public String getShowText() {
		return txt.replaceAll("\\", "");
	}
	
	/**
	 * Indique si la césure est placée au bon endroit.
	 */
	public boolean correctPause(int offset) {
		String b = getTextWithCutPauses(offset);
		return b.charAt(offset + 1) == '/';
	}
	
	/**
	 * Enlève les césures du texte avec césures jusqu'à la position indiquée.
	 */
	private String getTextWithCutPauses(int endOffset) {
		StringBuilder b = new StringBuilder(ctxt);
		for (int i = 0; i < b.length(); i++) {
			if (i >= endOffset) {
				break;
			}
			/// supprime le slash et l'espace avant ///
			if (b.charAt(i) == '/') {
				b.deleteCharAt(i);
				b.deleteCharAt(i - 1);
			}
		}
		System.out.println(b);
		return b.toString();
	}
	
	/**
	 * Retourne la position du caractère après le premier espace de l'endroit cliqué.
	 */
	public int endWordPosition(int offset) {
		for (int i = offset; i < txt.length(); i++) {
			if (Character.isWhitespace(txt.charAt(i)) || isPunctuation(txt.charAt(i))) {
				return i;
			}
		}
		return -1;
	}
	
	private static boolean isPunctuation(char c) {
		return c == ',' || c == '.' || c == ';' || c == ':' || c == '!' || c == '?';
	}
	
	/**
	 * Indique si le mot sur lequel a cliqué l'utilisateur correspond bien à une césure. 
	 */
	public boolean wordPause(int offset) {
		int err = 0;
		for (int i = offset; i < txt.length(); i++) {
			if (correctPause(i)) {
				return true;
			}
			if (Character.isWhitespace(txt.charAt(i)) || isPunctuation(txt.charAt(i))) {
				err++;
				if (err >= 2) {
					return false;
				}
			}
		}
		return false;
		//return correctPause(endWordPosition(offset) + 1);
	}
	
}
