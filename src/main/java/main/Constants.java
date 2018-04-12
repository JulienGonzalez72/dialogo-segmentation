package main;

import java.awt.Color;

public final class Constants {

	public static final String[] FONTS_NAMES = {
			"OpenDyslexic-Bold.otf", "AndBasR.ttf", "LEXIA___.otf"
	};
	
	public static final Color WRONG_COLOR = Color.RED;
	public static final Color RIGHT_COLOR = Color.GREEN;
	public static final Color WRONG_PHRASE_COLOR = Color.CYAN;
	
	public static final int DEFAULT_FONT_SIZE = 12;
	
	/**
	 * Temps d'attente entre chaque page
	 */
	public static final long PAGE_WAIT_TIME = 1000;
	
	/**
	 * Caractère qui correspond à une césure
	 */
	public static final String PAUSE = "/";
	
	public static final float TEXTPANE_MARGING = 20f;
	
}
