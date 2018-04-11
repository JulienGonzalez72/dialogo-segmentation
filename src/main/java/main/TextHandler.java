package main;

import java.util.*;

public class TextHandler {

	public static final String PAUSE = " /";
	
	/**
	 * Texte avec césures
	 */
	private String txt;
	/**
	 * Liste des segments associés à leurs numéros
	 */
	private Map<Integer, String> phrases;

	public TextHandler(String texteOriginal) {
		this.txt = texteOriginal;
		this.phrases = new HashMap<Integer, String>();
		for (String phrase : texteOriginal.split(PAUSE)) {
			phrases.put(phrases.size(), phrase);
		}
	}
	
	/**
	 * Retourne le texte sans slash
	 */
	public String getShowText() {
		return txt.replace(PAUSE, "");
	}
	
	public String[] getPhrases(int start, int end) {
		List<String> list = new ArrayList<String>();
		Iterator<Integer> keys = phrases.keySet().iterator();
		while (keys.hasNext()) {
			int key = keys.next();
			if (key >= start && key <= end) {
				list.add(phrases.get(key));
			}
		}
		return list.toArray(new String[0]);
	}
	
	/**
	 * Indique si la césure est placée au bon endroit.
	 */
	public boolean correctPause(int offset) {
		String b = getTextWithCutPauses(offset);
		return b.charAt(offset + 1) == '/';
	}
	
	/**
	 * Retourne l'indice de la pause à la position indiquée.
	 */
	public int getPauseIndex(int offset) {
		if (!correctPause(offset))
			return -1;
		int index = 0;
		for (int i = 0; i < offset; i++) {
			if (txt.charAt(i) == '/') {
				index++;
			}
		}
		return index;
	}

	/**
	 * Enlève les césures du texte avec césures jusqu'à la position indiquée.
	 */
	private String getTextWithCutPauses(int endOffset) {
		StringBuilder b = new StringBuilder(txt);
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
		return b.toString();
	}

	public int endWordPosition(int offset) {
		for (int i = offset; i < getShowText().length(); i++) {
			if (Character.isWhitespace(getShowText().charAt(i)) || isPunctuation(getShowText().charAt(i))) {
				return i;
			}
		}
		return -1;
	}

	private static boolean isPunctuation(char c) {
		return c == ',' || c == '.' || c == ';' || c == ':' || c == '!' || c == '?';
	}

	/**
	 * Indique si le mot sur lequel a cliqué l'utilisateur correspond bien à une
	 * césure.
	 */
	public boolean wordPause(int offset) {
		int err = 0;
		for (int i = offset; i < getShowText().length(); i++) {
			if (correctPause(i)) {
				return true;
			}
			if (Character.isWhitespace(getShowText().charAt(i)) || isPunctuation(getShowText().charAt(i))) {
				err++;
				if (err >= 2) {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Retourne la position du caractère dans le texte en entier en indiquant la position d'un caractère cliqué à partir d'un segment indiqué.
	 */
	public int getAbsoluteOffset(int startPhrase, int offset) {
		return getPhrasesLength(0, startPhrase - 1) + offset;
	}
	
	/**
	 * Ceci est l'opération inverse, elle permet d'obtenir la position par rapport au premier segment affiché avec la position du caractère dans tout le texte.
	 */
	public int getRelativeOffset(int startPhrase, int offset) {
		return offset - getPhrasesLength(0, startPhrase - 1);
	}
	
	private int getPhrasesLength(int startPhrase, int endPhrase) {
		int length = 0;
		for (String phrase : getPhrases(startPhrase, endPhrase)) {
			length += phrase.length();
		}
		return length;
	}
	
}
