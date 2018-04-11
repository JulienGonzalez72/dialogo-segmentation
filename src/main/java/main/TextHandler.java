package main;

import java.util.*;

public class TextHandler {

	public static final String PAUSE = "/";

	/**
	 * Texte avec c�sures
	 */
	private String txt;
	/**
	 * Liste des segments associ�s � leurs num�ros
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

	public String[] getPhrases() {
		return getPhrases(0, getPhrasesCount());
	}

	/**
	 * Nombre de segments total
	 */
	public int getPhrasesCount() {
		return phrases.size();
	}

	/**
	 * Indique si la c�sure est plac�e au bon endroit.
	 */
	public boolean correctPause(int offset) {
		String b = getTextWithCutPauses(offset);
		return b.charAt(offset) == '/';
	}

	/**
	 * Retourne l'indice de la pause � la position indiqu�e.
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
	 * Retourne l'indice du segment � la position indiqu�e.
	 */
	public int getPhraseIndex(int offset) {
		int index = 0;
		for (int i = 0; i < offset; i++) {
			if (txt.charAt(i) == '/') {
				index++;
				offset += PAUSE.length();
			}
		}
		return index;
	}

	/**
	 * Enl�ve les c�sures du texte avec c�sures jusqu'� la position indiqu�e.
	 */
	private String getTextWithCutPauses(int endOffset) {
		StringBuilder b = new StringBuilder(txt);
		for (int i = 0; i < b.length(); i++) {
			if (i >= endOffset) {
				break;
			}
			/// supprime le slash ///
			if (b.charAt(i) == '/') {
				b.deleteCharAt(i);
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

	public int startWordPosition(int offset) {
		for (int i = offset; i >= 0; i--) {
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
	 * Indique si le mot sur lequel a cliqu� l'utilisateur correspond bien � une
	 * c�sure.
	 */
	public boolean wordPause(int offset) {
		int err = 0;
		for (int i = offset; i < getShowText().length(); i++) {
			System.out.print(getShowText().charAt(i));
			if (correctPause(i)) {
				return true;
			}
			if (Character.isWhitespace(getShowText().charAt(i)) || isPunctuation(getShowText().charAt(i))) {
				err++;
				if (err >= 2) {
					return false;
				}
			}
			if (Character.isAlphabetic(getShowText().charAt(i)) && err == 1) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Retourne la position du caract�re dans le texte en entier en indiquant la
	 * position d'un caract�re cliqu� � partir d'un segment indiqu�.
	 */
	public int getAbsoluteOffset(int startPhrase, int offset) {
		return getPhrasesLength(0, startPhrase - 1) + offset;
	}

	/**
	 * Ceci est l'op�ration inverse, elle permet d'obtenir la position par rapport
	 * au premier segment affich� avec la position du caract�re dans tout le texte.
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
