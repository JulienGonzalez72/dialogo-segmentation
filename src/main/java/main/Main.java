package main;

import java.awt.Point;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point p = new Point(50,50);
				new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam, Constants.hauteurFenetreParam).setLocation(p);
			}
		});
	}

}