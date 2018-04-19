package main;

import java.awt.Color;

public final class Constants {

	public static final String[] FONTS_NAMES = { "OpenDyslexic-Bold.otf", "AndBasR.ttf", "LEXIA___.otf" };

	public static Color WRONG_COLOR = new Color(255, 40, 40);
	public static Color RIGHT_COLOR = Color.GREEN;
	public static Color WRONG_PHRASE_COLOR = Color.CYAN;

	/*
	 * Valeurs par défaut des paramètres
	 */
	public static final int DEFAULT_FONT_SIZE = 12;
	public static final int MIN_WAIT_TIME_PERCENT = 0;
	public static final int MAX_WAIT_TIME_PERCENT = 300;
	public static final int DEFAULT_WAIT_TIME_PERCENT = 0;
	public static final int hauteurFenetreParam = 900;
	public static final int largeurFenetreParam = 550;
	public static final String titreFenetreParam = "Dialogo by roman and julien";
	public static final boolean LOAD_FIRST_PHRASE = false;

	/**
	 * Temps d'attente entre chaque page
	 */
	public static final long PAGE_WAIT_TIME = 1000;

	/**
	 * Caractère qui correspond à une césure
	 */
	public static final String PAUSE = "/";

	public static final float TEXTPANE_MARGING = 20f;

	/**
	 * Vitesse de lecture (en fréquence)
	 */
	public static final long PLAYER_INTERVAL = 20;

	public static final String TEXT_FILE_NAME = "Amélie la sorcière.txt";
	public static final String AUDIO_FILE_NAME = "T Amélie la sorcière";
	public static final boolean HAS_INSTRUCTIONS = true;

	public static final long LEFT_DELAY = 500;

}
