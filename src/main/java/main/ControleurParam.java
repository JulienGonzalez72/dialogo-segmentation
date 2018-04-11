package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ControleurParam implements ActionListener {

	FenetreParametre.PanneauParam panneau;

	public ControleurParam(FenetreParametre.PanneauParam p) {
		this.panneau = p;
	}

	public void actionPerformed(ActionEvent arg0) {
		JComboBox jcb = null;
		if (arg0.getSource() instanceof JComboBox) {
			jcb = (JComboBox) arg0.getSource();
		}
		if (jcb == panneau.listeCouleurs) {
			String s = (String) jcb.getSelectedItem();
			if (s == "Jaune") {
				FenetreParametre.couleurFond = Color.YELLOW;
				panneau.listeCouleurs.setBackground(Color.yellow);
			}
			if (s == "Orange") {
				FenetreParametre.couleurFond = Color.orange;
				panneau.listeCouleurs.setBackground(Color.orange);
			}
			if (s == "Blanc") {
				FenetreParametre.couleurFond = Color.white;
				panneau.listeCouleurs.setBackground(Color.white);
			}
		}
		if (jcb == panneau.listeTailles) {
			int taille = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.taillePolice = taille;
			panneau.listeTailles.setFont(new Font("Arial",Font.PLAIN,taille));
		}
		if (jcb == panneau.listePolices) {
			String police = (String) jcb.getSelectedItem();
			FenetreParametre.police = police;
			panneau.listeTailles.setFont(new Font(police,Font.PLAIN,12));
		}
		if (arg0.getSource() == panneau.valider) {
			System.out.println("Validation : ");
			System.out.println("Police : "+FenetreParametre.police);
			System.out.println("Taille : "+FenetreParametre.taillePolice);
			System.out.println("Couleur : "+FenetreParametre.couleurFond);
		}
	}

}
