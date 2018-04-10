package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
<<<<<<< HEAD
		new Fenetre("Lexidia", 500, 500);
=======
		try {
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/OpenDyslexic-Bold.otf")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Fenetre("Roman et Julien", 500, 500);
>>>>>>> 5603b221d1bb0520aa95df5b149907bf3acf1ef3
	}
	
}
