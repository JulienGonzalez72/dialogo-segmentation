package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
<<<<<<< HEAD
import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
=======

//import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
>>>>>>> 8f6d96f2003207e2305b569a6d46555d7dd0d017
import main.view.FenetreParametre;


public class Main {

	public static void main(String[] args) {
<<<<<<< HEAD
		
=======
<<<<<<< HEAD
=======

>>>>>>> 88460d2e13f766abb1e373d521ad448bb6dc2575
>>>>>>> 8f6d96f2003207e2305b569a6d46555d7dd0d017
		try {
			//UIManager.setLookAndFeel(new KunststoffLookAndFeel());
		} catch (Exception e1) {
<<<<<<< HEAD

		}
		
=======
		}

>>>>>>> 88460d2e13f766abb1e373d521ad448bb6dc2575
		File rep = new File("ressources/fonts");
		for (String s : rep.list()) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + s)));
			} catch (IOException | FontFormatException e) {
				// e.printStackTrace();
			}
		}
<<<<<<< HEAD
		
=======

>>>>>>> 88460d2e13f766abb1e373d521ad448bb6dc2575
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(50, 0);
				new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam,
						Constants.hauteurFenetreParam).setLocation(p);				
			}
		});

	}

}