package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
	
	public static final Font[] FONTS = new Font[Constants.FONTS_NAMES.length];
	
	public static void main(String[] args) {
		
		try {
			for (int i = 0; i < Constants.FONTS_NAMES.length; i++) {
				GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FONTS[i] = Font
						.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + Constants.FONTS_NAMES[i])));
			}
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/// attends que les polices soient chargées ///
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FenetreParametre("Lexidia", 500, 500);
			}
		});
	}

}
