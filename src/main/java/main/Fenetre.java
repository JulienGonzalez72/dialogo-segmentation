package main;

import java.io.IOException;

import javax.swing.*;

public class Fenetre extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public Fenetre(String titre, int tailleX, int tailleY){
		this.setTitle(titre);
		this.setSize(tailleX, tailleY);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		Panneau pan = null;
		try {
			pan = new Panneau();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setContentPane(pan);		
		this.setVisible(true);
	}

}
