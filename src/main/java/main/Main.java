package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
<<<<<<< HEAD
import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
=======

import com.alee.laf.WebLookAndFeel;

>>>>>>> e3ade8438f5b3f0c5dcfa42e0ff32688d3a9f078
import main.view.FenetreParametre;

public class Main {
    
	public static void main(String[] args) {

		try {
<<<<<<< HEAD
			UIManager.setLookAndFeel(new KunststoffLookAndFeel());
		} catch (Exception e1) {}

=======
			WebLookAndFeel.install();
		} catch (Exception e1) {
                
		}
		
>>>>>>> e3ade8438f5b3f0c5dcfa42e0ff32688d3a9f078
		File rep = new File("ressources/fonts");
		for (String s : rep.list()) {
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + s)));
			} catch (IOException | FontFormatException e) {}
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(Constants.PARAMS_FRAME_X, Constants.PARAMS_FRAME_Y);
				new FenetreParametre(Constants.PARAMS_FRAME_TITLE, Constants.PARAMS_FRAME_WIDTH,
						Constants.PARAMS_FRAME_HEIGHT).setLocation(p);
			}
		});

	}

}