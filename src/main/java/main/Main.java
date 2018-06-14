package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

import main.view.FenetreParametre;

public class Main {

	/*public static void main(String[] args) {

		try {
			WebLookAndFeel.install();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		File rep = new File("ressources/fonts");
		for (String s : rep.list()) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + s)));
			} catch (IOException | FontFormatException e) {
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(Constants.PARAMS_FRAME_X, Constants.PARAMS_FRAME_Y);
				new FenetreParametre(Constants.PARAMS_FRAME_TITLE, Constants.PARAMS_FRAME_WIDTH,
						Constants.PARAMS_FRAME_HEIGHT).setLocation(p);
			}
		});

	}*/

}