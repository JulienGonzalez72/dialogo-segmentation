package main;

public enum ReadMode {
	
	/**
	 * Tous les segments pass�s sont surlign�s en vert, l'utilisateur peut donc voir le d�but du segment qu'il doit r�p�ter.
	 */
	HIGHLIGHT,
	
	/**
	 * La lecture se fait automatiquement en surlignant en vert le segment en train d'�tre lu.<br>
	 * Aucun clic n'est pris en compte dans ce mode.
	 */
	GUIDED_READING,
	
	/**
	 * Aucun surlignage n'indique au patient sur quel segment il se trouve, seules les erreurs sont surlign�es.
	 */
	NORMAL;
	
	public static ReadMode parse(String s) {
		ReadMode mode = null;
		switch(s) {
		case "GUIDED_READING":
			mode = ReadMode.GUIDED_READING;
			break;
		case "HIGHLIGHT":
			mode = ReadMode.HIGHLIGHT;
			break;
		case "NORMAL":
			mode = ReadMode.NORMAL;
			break;
		}
		return mode;
	}
	
}
