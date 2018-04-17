package main;

public enum ReadMode {
	
	/**
	 * Tous les segments passés sont surlignés en vert, l'utilisateur peut donc voir le début du segment qu'il doit répéter.
	 */
	HIGHLIGHT,
	
	/**
	 * La lecture se fait automatiquement en surlignant en vert le segment en train d'être lu.<br>
	 * Aucun clic n'est pris en compte dans ce mode.
	 */
	GUIDED_READING,
	
	/**
	 * Aucun surlignage n'indique au patient sur quel segment il se trouve, seules les erreurs sont surlignées.
	 */
	NORMAL;
	
}
