package main;

import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) {
<<<<<<< HEAD
=======
		
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
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
>>>>>>> f3a009b0c4e3079d7f26583c9f82077fc128a8f1
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FenetreParametre("Lexidia", 500, 500);
			}
		});
	}

}
