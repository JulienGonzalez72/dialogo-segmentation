package main;

import java.awt.Color;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public final class Constants {

	public static final String[] FONTS_NAMES = {
			"OpenDyslexic-Bold.otf", "AndBasR.ttf", "LEXIA___.otf"
	};
	
	public static final Color WRONG_COLOR = new Color(255, 40, 40);
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
	
	/**
	 * Vitesse de lecture (en fréquence)
	 */
	public static final long PLAYER_INTERVAL = 20;
	
	public static final String TEXT_FILE_NAME = "Amélie la sorcière.txt";
	public static final String AUDIO_FILE_NAME = "T Amélie la sorcière";
	
}
