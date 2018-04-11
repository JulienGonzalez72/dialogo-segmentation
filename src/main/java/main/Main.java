package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

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
		
		new Fenetre("Lexidia", 1200, 600);
	}

}
