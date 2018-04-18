package main;

import java.io.IOException;

import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	public Panneau pan;
	public boolean preferencesExiste = true;

	public Fenetre(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("icone.jpg"));
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
	}
	
	public void start() {
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init();
			}
		});
	}

}
