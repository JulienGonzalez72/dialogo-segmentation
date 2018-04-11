package main;

import java.util.*;

public class TextHandler {

	private String txt;
	private Map<Integer, String> phrases;
	/**
	 * Numéro de la césure courante
	 */
	private int currentPause;

	public TextHandler(String texteOriginal) {
		this.txt = texteOriginal;
		this.phrases = new HashMap<Integer, String>();
		for (String phrase : texteOriginal.split("/")) {
			phrases.put(phrases.size(), phrase);
		}
	}

	/**
	 * Retourne le texte sans slash
	 */
	public String getShowText() {
		return txt.replaceAll("\\", "");
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
	
	public int getPauseIndex(int offset) {
		if (!correctPause(offset))
			return -1;
		return 0;
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
		System.out.println(b);
		return b.toString();
	}

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
	 * Indique si le mot sur lequel a cliqué l'utilisateur correspond bien à une
	 * césure.
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
		// return correctPause(endWordPosition(offset) + 1);
	}

}
