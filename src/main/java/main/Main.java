package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		try {
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/OpenDyslexic-Bold.otf")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
<<<<<<< HEAD
		
		new Fenetre("Lexidia", 500, 500);
=======

		new Fenetre("Lexidia", 1200, 600);

>>>>>>> 9dd8d22856a7fd5c1a9630037843d77792ad3bc1
	}

}
