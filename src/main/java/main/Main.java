package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import main.view.FenetreParametre;


public class Main {

	public static void main(String[] args) {
<<<<<<< HEAD
		try {
			//UIManager.setLookAndFeel(new KunststoffLookAndFeel());
		} catch (Exception e1) {

		}
		
=======

		try {
			UIManager.setLookAndFeel(new KunststoffLookAndFeel());
		} catch (Exception e1) {}

>>>>>>> f8dd1f67379fcd2d2fd7808522a48f1ecdf88a23
		File rep = new File("ressources/fonts");
		for (String s : rep.list()) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + s)));
			} catch (IOException | FontFormatException e) {}
		}
<<<<<<< HEAD
		
=======

>>>>>>> f8dd1f67379fcd2d2fd7808522a48f1ecdf88a23
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(50, 0);
				new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam,
						Constants.hauteurFenetreParam).setLocation(p);				
			}
		});

	}

}