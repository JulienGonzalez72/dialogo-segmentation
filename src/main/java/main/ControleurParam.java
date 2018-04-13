package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
			Color color = null;
			if (s == "Jaune") {
				color = new Color(255, 255, 150);
			}
			if (s == "Orange") {
				color = Color.ORANGE;
			}
			if (s == "Blanc") {
				color = Color.WHITE;
			}
			if (s == "Rose") {
				color = Color.PINK;
			}
			if (s == "Bleu") {
				color = Color.CYAN;
			}
			panneau.listeCouleurs.setBackground(FenetreParametre.couleurFond = color);
		}
		if (jcb == panneau.listeTailles) {
			int taille = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.taillePolice = taille;
			FenetreParametre.police = FenetreParametre.police.deriveFont((float) taille);
			panneau.listeTailles.setFont(FenetreParametre.police);
			if (FenetreParametre.editorPane != null) {
				FenetreParametre.editorPane.setFont(FenetreParametre.police);
			}
		}
		if (jcb == panneau.listePolices) {
			String police = (String) jcb.getSelectedItem();
			FenetreParametre.police = getFont(police, Font.BOLD, FenetreParametre.taillePolice);
			panneau.listePolices.setFont(FenetreParametre.police);
			if (FenetreParametre.editorPane != null) {
				FenetreParametre.editorPane.setFont(FenetreParametre.police);
			}
		}
		if (arg0.getSource() == panneau.segmentDeDepart) {
			int premierSegment = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.premierSegment = premierSegment;
		}
		if (arg0.getSource() == panneau.modeSurlignage) {
			FenetreParametre.modeSurlignage = ((JCheckBox) arg0.getSource()).isSelected();
		}
		if (arg0.getSource() == panneau.valider) {
			if (FenetreParametre.editorPane == null) {
				try {
					FenetreParametre.nbFautesTolerees = Math.max(0,
							Integer.valueOf(panneau.champNbFautesTolerees.getText()));
				} catch (Exception e) {
					FenetreParametre.nbFautesTolerees = 0;
					panneau.champNbFautesTolerees.setText("0");
				}
				try {
					FenetreParametre.premierSegment = Math.max(0, Integer.valueOf(panneau.segmentDeDepart.getText()));
				} catch (Exception e) {
					FenetreParametre.premierSegment = 0;
					panneau.segmentDeDepart.setText("0");
				}
				FenetreParametre.fen.lancerExercice();
				panneau.fermer();
			} else {
				panneau.fermer();
				Panneau.premierSegment = FenetreParametre.premierSegment;
				FenetreParametre.editorPane.setBackground(FenetreParametre.couleurFond);
			}
		}
	}

	public static Font getFont(String police, int style, int size) {
		try {
			Font font;
			if (police == "OpenDyslexic" || police == "Andika" || police == "Lexia") {
				font = Font.createFont(Font.TRUETYPE_FONT, new File("ressources/fonts/" + police)).deriveFont(style)
						.deriveFont((float) size);
			} else {
				font = new Font(police, style, size);
			}
			return font;
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
