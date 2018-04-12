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
			if (s == "Rose") {
				FenetreParametre.couleurFond = Color.PINK;
				panneau.listeCouleurs.setBackground(Color.pink);
			}
			if (s == "Bleu") {
				FenetreParametre.couleurFond = Color.cyan;
				panneau.listeCouleurs.setBackground(Color.cyan);
			}
		}
		if (jcb == panneau.listeTailles) {
			int taille = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.taillePolice = taille;
			panneau.listeTailles.setFont(new Font("OpenDyslexic", Font.PLAIN, taille));
		}
		if (jcb == panneau.listePolices) {
			String police = (String) jcb.getSelectedItem();
			FenetreParametre.police = getFontName(police, jcb.getSelectedIndex());
			panneau.listePolices.setFont(new Font(getFontName(police, jcb.getSelectedIndex()), Font.BOLD, Constants.DEFAULT_FONT_SIZE));
		}
		if (jcb == panneau.listeSegments) {
			int nbSegments = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.nbSegments = nbSegments;
		}
		if (arg0.getSource() == panneau.valider) {
			if (FenetreParametre.editorPane == null) {
				try {
					FenetreParametre.nbFautesTolerees = Math.max(0,
							Integer.valueOf(panneau.champNbFautesTolerees.getText()));
				} catch (Exception e) {
					FenetreParametre.nbFautesTolerees = 0;
				}
				FenetreParametre.lancerExercice();
				panneau.fermer();
			} else {
				panneau.fermer();
				Panneau.defautNBSegmentsParPage = FenetreParametre.nbSegments;
				FenetreParametre.editorPane.setBackground(FenetreParametre.couleurFond);
				FenetreParametre.editorPane
						.setFont(new Font(FenetreParametre.police, Font.PLAIN, FenetreParametre.taillePolice));
			}
		}
	}

	public static String getFontName(String police, int selectedIndex) {
		return selectedIndex < Main.FONTS.length && selectedIndex >= 0 ? Main.FONTS[selectedIndex].getFontName()
				: police;
	}

}
