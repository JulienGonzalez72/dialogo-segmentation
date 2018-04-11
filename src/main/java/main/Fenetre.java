package main;

import java.io.IOException;
import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	public Fenetre(String titre, int tailleX, int tailleY) {
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		Panneau pan = null;
		try {
			pan = new Panneau(tailleX, tailleY, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setVisible(true);
	}

}
