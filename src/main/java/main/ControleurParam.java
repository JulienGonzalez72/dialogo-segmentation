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
		JComboBox<?> jcb = null;
		if (arg0.getSource() instanceof JComboBox) {
			jcb = (JComboBox<?>) arg0.getSource();
		}
		if (jcb == panneau.listeCouleurs) {
			String s = (String) jcb.getSelectedItem();
			if (s == "Jaune") {
				FenetreParametre.couleurFond = new Color(255, 255, 150);
				panneau.listeCouleurs.setBackground(new Color(255, 255, 150));
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
			panneau.listeTailles.setFont(new Font("OpenDyslexic", Font.PLAIN, taille));
		}
		if (jcb == panneau.listePolices) {
			String police = (String) jcb.getSelectedItem();
			FenetreParametre.police = "ressources/fonts/" + police + "-Regular.otf";
			// TODO mettre la police du selecteur de police sur la police selectionnée
		}
		if (arg0.getSource() == panneau.valider) {
			if (FenetreParametre.fenExercice == null) {
				FenetreParametre.lancerExercice();
				panneau.fermer();
			} else {
				panneau.fermer();
				FenetreParametre.fenExercice.setBackground(FenetreParametre.couleurFond);
				FenetreParametre.fenExercice
						.setFont(new Font("OpenDyslexic", Font.BOLD, FenetreParametre.taillePolice));
				//TODO mettre la bonne police
			}
		}
	}

}
