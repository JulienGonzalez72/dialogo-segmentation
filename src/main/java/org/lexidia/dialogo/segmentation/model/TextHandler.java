package org.lexidia.dialogo.segmentation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.lexidia.dialogo.segmentation.main.Constants;

public class TextHandler {

	/**
	 * Texte d'origine
	 */
	private String originText;

	/**
	 * Texte formate
	 */
	private String txt;
	
	/**
	 * Texte � afficher
	 */
	private String showText;

	/**
	 * Liste des segments associes a leurs num�ros
	 */
	private Map<Integer, String> phrases;

	/**
	 * Liste des mots associes a leurs num�ros de trous.
	 */
	private Map<Integer, Hole> holes;

	/**
	 * Liste des mots pour chaque segment.
	 */
	private Map<Integer, List<Hole>> motsParSegment;

	/**
	 * Pour chaque mot, true si il est en texte plein ou false si il est cach�.
	 */
	private Map<Integer, Boolean> filledWords;
	
	/**
	 * Si le texte est trait� pour des exercices de textes � trou.
	 */
	public static boolean holeTreatment = true;
	
	public TextHandler(String texteOriginal) {
		this.originText = format(texteOriginal);
		
		init();
	}

	private static String format(String str) {
		String[] phrases = str.split(Constants.PAUSE);
		for (int i = 0; i < phrases.length; i++) {
			if (!isValidPhrase(phrases[i])) {
				phrases[i] = "";
			}
		}
		str = StringUtils.join(phrases, Constants.PAUSE);
		return str.replace(" /", "/");
	}
	
	private static boolean isValidPhrase(String phrase) {
		return phrase.length() > 1
				&& phrase.matches("(.|\\v)*[[a-z][A-Z][0-9][,;:!?.-]]+(.|\\v)*");
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

	public String getPhrase(int index) {
		return phrases.get(index);
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
			if (getTxt().charAt(i) == '/') {
				index++;
			}
		}
		return index;
	}
	
	/**
	 * Retourne la position absolue de la fin du segment pass� en param�tre.
	 */
	public int getPauseOffset(int phrase) {
		return getPhrasesLength(0, phrase);
	}
	
	/**
	 * Retourne la position absolue du d�but du segment pass� en param�tre.
	 */
	public int getPhraseOffset(int phrase) {
		return getPhrasesLength(0, phrase - 1);
	}
	
	/**
	 * Retourne l'indice du segment � la position indiqu�e, ou -1 si la position est en dehors des limites du texte.
	 */
	public int getPhraseIndex(int offset) {
		if (offset < 0 || offset >= getShowText().length())
			return -1;
		int index = 0;
		for (int i = 0; i < offset; i++) {
			if (getTxt().charAt(i) == '/') {
				index++;
				offset += Constants.PAUSE.length();
			}
		}
		return index;
	}
	
	/**
	 * Retourne <code>true</code> si la position indiqu�e correspond au dernier caract�re d'un segment.
	 */
	public boolean isPhraseLastOffset(int offset) {
		return offset == getShowText().length() - 1
				|| getPhraseIndex(offset + 1) > getPhraseIndex(offset);
	}

	/**
	 * Enl�ve les c�sures du texte avec c�sures jusqu'� la position indiqu�e.
	 */
	private String getTextWithCutPauses(int endOffset) {
		StringBuilder b = new StringBuilder(getTxt());
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
		for (int i = Math.min(getShowText().length() - 1, offset); i >= 0; i--) {
			if (Character.isWhitespace(getShowText().charAt(i)) || isPunctuation(getShowText().charAt(i))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Retourne la position du d�but du segment d'indice <i>phrase</i>, relative au
	 * premier segment <i>startPhrase</i>.
	 */
	public int getRelativeStartPhrasePosition(int startPhrase, int phrase) {
		return getRelativeOffset(startPhrase, getPhraseOffset(phrase));
	}
	
	private static boolean isPunctuation(char c) {
		return c == ',' || c == '.' || c == ';' || c == ':' || c == '!'
				|| c == '?' || c == '\u2026' || c == '�';
	}
	
	/**
	 * Indique si le mot sur lequel a cliqu� l'utilisateur correspond bien � une c�sure.
	 */
	public boolean wordPause(int offset) {
		int err = 0;
		String txt = getShowText();
		for (int i = offset; i < txt.length(); i++) {
			if (correctPause(i)) {
				return true;
			}
			/// �vite le probl�me de la ponctuation avec des espaces avant ///
			if (i < txt.length() - 1 && isPunctuation(txt.charAt(i + 1))) {
				err--;
			}
			if (Character.isWhitespace(txt.charAt(i)) || isPunctuation(txt.charAt(i))) {
				err++;
			}
			if (Character.isAlphabetic(txt.charAt(i)) && err >= 1) {
				return false;
			}
		}
		return correctPause(txt.length());
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

	public int getPhrasesLength(int startPhrase, int endPhrase) {
		int length = 0;
		for (String phrase : getPhrases(startPhrase, endPhrase)) {
			length += phrase.length();
		}
		return length;
	}

	@Override
	public String toString() {
		return phrases.toString();
	}

	//////////////////////////////////////////////////
	/////////////////////////////////////////////////
	////////////////////////////////////////////////

	public boolean oneHoleEqualOneWord() {
		boolean r = true;
		for (Hole h : holes.values()) {
			if (h.getHidedWord().contains(" ")) {
				r = false;
			}
		}
		return r;
	}

	private void remplirMots(String s) {
		int numeroSegmentCourant = 0;
		String temp = s.replaceAll(" /", "/");
		char[] tab = temp.toCharArray();
		boolean dansCrochet = false;
		int numero = 0;
		int offset = 0;
		String motCourant = "";
		List<Hole> listStrings = new ArrayList<>();
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == '/') {
				getMotsParSegment().put(numeroSegmentCourant, listStrings);
				listStrings = new ArrayList<>();
				numeroSegmentCourant++;
				offset = 0;
			} else if (tab[i] == '[') {
				dansCrochet = true;
				i++;
			} else if (tab[i] == ']') {
				dansCrochet = false;
			} else {
				offset++;
			}
			if (dansCrochet) {
				motCourant += tab[i];
			} else if (motCourant != "") {
				Hole h = new Hole(motCourant);
				h.setStartOffset(offset - motCourant.length() + 1);
				offset += h.getShift() + 1;
				holes.put(numero, h);
				filledWords.put(numero, false);
				listStrings.add(h);
				motCourant = "";
				numero++;
			}
		}
	}

	private void updateText() {
		String r = "";
		String temp = originText.replace(" /", "/");
		char[] tab = temp.toCharArray();
		boolean dansCrochet = false;
		int numeroTrou = 0;

		for (int i = 0; i < tab.length; i++) {
			if (holeTreatment) {
				if (tab[i] == '[') {
					dansCrochet = true;
					i++;
				} else if (tab[i] == ']') {
					r += holes.get(numeroTrou);
					dansCrochet = false;
					numeroTrou++;
					i++;
				}
				if (!dansCrochet) {
					r += tab[i];
				} 
			}
			else {
				r += tab[i];
			}
		}
		
		setTxt(r);
		phrases.clear();
		String[] ps = getTxt().split(Constants.PAUSE);
		for (int i = 0; i < ps.length; i++) {
			String phrase = ps[i];
			/// enl�ve les retours chariot au d�but et les met � la fin du segment pr�c�dent ///
			while (phrase.startsWith("\n")) {
				phrase = phrase.substring(1);
				if (i > 0) {
					phrases.put(i - 1, phrases.get(i - 1) + "\n");
				}
			}
			/// ajoute le segment s'il est bien format� ///
			//if (isValidPhrase(phrase)) {
				phrases.put(i, phrase);
			//}
		}
		updateShowText();
	}
	
	private void updateShowText() {
		String r = "";
		String temp = getTxt().replace(Constants.PAUSE, "");
		char[] tab = temp.toCharArray();
		for (int i = 0; i < tab.length; i++) {
			if (holeTreatment) {
				if (tab[i] == '[' || tab[i] == ']') {
					i++;
				}
				r += tab[i];
			}
			else {
				r += tab[i];
			}
		}
		showText = r;
	}

	public String getShowText() {
		return showText;
	}
	
	/**
	 * Retourne tout le texte � partir du segment indiqu�.
	 */
	public String getTextFrom(int startPhrase) {
		if (startPhrase == 0) {
			return getShowText();
		}
		String text = "";
		for (int i = startPhrase; i < getPhrasesCount(); i++) {
			text += getPhrase(i);
		}
		return text;
	}

	/**
	 * Retourne une liste des mots � trouver par segment.
	 */
	public List<String> getHidedWords(int phrase) {
		return getMotsParSegment().containsKey(phrase) ? Hole.getHidedWords(getMotsParSegment().get(phrase))
				: new ArrayList<String>();
	}

	/**
	 * Retourne le nombre de trous que contient le segment <code>phrase</code>.
	 */
	public int getHolesCount(int phrase) {
		return getMotsParSegment().containsKey(phrase) ? getMotsParSegment().get(phrase).size() : 0;
	}

	public int getHolesCount(int startPhrase, int endPhrase) {
		int count = 0;
		for (int i = startPhrase; i <= endPhrase; i++) {
			count += getHolesCount(i);
		}
		return count;
	}

	/**
	 * Retourne le nombre de trous total du texte.
	 */
	public int getHolesCount() {
		return holes.size();
	}

	/**
	 * Retourne <code>true</code> si il y a au moins un autre trou apr�s le trou
	 * indiqu� dans le m�me segment.
	 */
	public boolean hasNextHoleInPhrase(int hole) {
		int p = getPhraseOf(hole);
		List<String> words = getHidedWords(p);
		int holeInPhrase = hole - getHolesCount(0, p - 1);
		return holeInPhrase < words.size() - 1;
	}

	/**
	 * Retourne <code>true</code> si il y a au moins un autre trou avant le trou
	 * indiqu� dans le m�me segment.
	 */
	public boolean hasPreviousHoleInPhrase(int hole) {
		int p = getPhraseOf(hole);
		List<String> words = getHidedWords(p);
		int holeInPhrase = hole - getHolesCount(0, p - 1);
		return holeInPhrase > 0 && words.size() > 1;
	}

	/**
	 * Retourne le num�ro de segment correspondant au trou indiqu�.
	 */
	public int getPhraseOf(int hole) {
		int n = 0;
		for (int i = 0; i < getPhrasesCount(); i++) {
			n += getHolesCount(i);
			if (n > hole) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Retourne la position de d�part du trou indiqu�.
	 */
	public int getHoleStartOffset(int hole) {
		return getPhrasesLength(0, getPhraseOf(hole) - 1) + holes.get(hole).getStartOffset();
	}

	public int getHoleEndOffset(int hole) {
		return getHoleStartOffset(hole) + holes.get(hole).length();
	}

	/**
	 * Retourne le num�ro du dernier trou du segment indiqu�.
	 */
	public int getLastHole(int phrase) {
		int r = -1;
		for (int i = 0; i < getHolesCount(); i++) {
			if (phrase == getPhraseOf(i)) {
				r = i + 1;
			}
		}
		return r;
	}

	/**
	 * Retourne le num�ro du premier trou � partir du segment indiqu�.<br>
	 * Retourne -1 s'il n'y a plus de trous apr�s.
	 */
	public int getFirstHole(int phrase) {
		for (int i = 0; i < getHolesCount(); i++) {
			if (getPhraseOf(i) >= phrase) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Retourne <code>true</code> si le segment indiqu� contient au moins un trou.
	 */
	public boolean hasHole(int phrase) {
		return getHolesCount(phrase) > 0;
	}

	/**
	 * Retourne le mot associ� au trou indiqu�.
	 */
	public String getHiddendWord(int hole) {
		return holes.get(hole).getHidedWord();
	}

	public int getHidedWordLength(int h) {
		return holes.get(h).length();
	}

	/**
	 * Remplace le trou h par le mot qui lui correspond.
	 */
	public void fillHole(int hole) {
		Hole h = holes.get(hole);

		if (h.isHidden()) {
			h.fill();

			/// d�cale les trous du m�me segment ///
			for (int i = hole + 1; i < getHolesCount() && getPhraseOf(i) == getPhraseOf(hole); i++) {
				holes.get(i).setStartOffset(holes.get(i).getStartOffset() - h.getShift());
			}

			updateText();
		}
	}

	public void hideHole(int hole) {
		Hole h = holes.get(hole);

		if (!h.isHidden()) {
			h.hide();

			/// d�cale les trous du m�me segment ///
			for (int i = hole + 1; i < getHolesCount() && getPhraseOf(i) == getPhraseOf(hole); i++) {
				holes.get(i).setStartOffset(holes.get(i).getStartOffset() + h.getShift());
			}

			updateText();
		}
	}

	public boolean isHidden(int hole) {
		return holes.get(hole).isHidden();
	}

	public void init() {
		this.holes = new HashMap<Integer, Hole>();
		this.setMotsParSegment(new HashMap<Integer,List<Hole>>());
		this.filledWords = new HashMap<>();
		this.phrases = new HashMap<Integer, String>();
		if (holeTreatment) {
			remplirMots(originText);
		}
		updateText();
	}

	private String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	private Map<Integer, List<Hole>> getMotsParSegment() {
		return motsParSegment;
	}

	private void setMotsParSegment(Map<Integer, List<Hole>> motsParSegment) {
		this.motsParSegment = motsParSegment;
	}

}
