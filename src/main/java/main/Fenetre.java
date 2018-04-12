package main;

import java.io.IOException;
import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Panneau pan;

	public Fenetre(String titre, int tailleX, int tailleY) {
		try {
			pan = new Panneau(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init();
			}
		});
	}

}
