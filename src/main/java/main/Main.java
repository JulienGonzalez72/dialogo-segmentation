package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import main.view.FenetreParametre;

public class Main {

	public static void main(String[] args) {
		File rep = new File("ressources/fonts");
		for (String s : rep.list()) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + s)));

			} catch (IOException | FontFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(50, 0);
				new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam,
						Constants.hauteurFenetreParam).setLocation(p);
			}
		});

	}

}