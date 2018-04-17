package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControleurParam implements ActionListener, ChangeListener, FocusListener {

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
			panneau.grabFocus();
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
			FenetreParametre.police = getFont(police, jcb.getSelectedIndex(), Font.BOLD, FenetreParametre.taillePolice);
			panneau.listePolices.setFont(FenetreParametre.police);
			if (FenetreParametre.editorPane != null) {
				FenetreParametre.editorPane.setFont(FenetreParametre.police);
			}
		}
		/*if (arg0.getSource() == panneau.segmentDeDepart) {
			int premierSegment = -1;
			try {
				premierSegment = Integer.valueOf((String) panneau.segmentDeDepart.getText());
				if (premierSegment > ((Panneau) FenetreParametre.fen.fenetre.getContentPane()).textHandler
						.getPhrasesCount() || premierSegment < 1) {
					JOptionPane.showMessageDialog(FenetreParametre.fen, "Le segment spécifié n'existe pas.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					premierSegment = 1;
					panneau.segmentDeDepart.setText("1");
				}
			} catch (Exception e) {
				panneau.segmentDeDepart.setText("1");
			}
			FenetreParametre.premierSegment = premierSegment;
		}*/
		if (arg0.getSource() == panneau.modeSurlignage) {
			FenetreParametre.modeSurlignage = ((JCheckBox) arg0.getSource()).isSelected();
			if (FenetreParametre.modeSurlignage) {
				if (FenetreParametre.fen.fenetre.pan.player != null) {
					FenetreParametre.fen.fenetre.pan.editorPane.retablirSurlignageBlue();
					FenetreParametre.fen.fenetre.pan.surlignerJusquaSegment(Constants.RIGHT_COLOR,
							FenetreParametre.fen.fenetre.pan.player.getCurrentPhraseIndex() - 1);
				}
			} else {
				if (FenetreParametre.editorPane != null) {
					FenetreParametre.editorPane.désurlignerTout();
				}
			}
		}
		if (arg0.getSource() == panneau.modeKaraoke) {
			FenetreParametre.modeLectureGuidee = panneau.modeKaraoke.isSelected();
		}
		if (arg0.getSource() == panneau.modePasDispo) {
			if (panneau.modePasDispo.isSelected()) {
				FenetreParametre.modeSurlignage = false;
			}
		}
		if (arg0.getSource() instanceof JCheckBox) {
			JCheckBox temp = (JCheckBox) arg0.getSource();
			for (Component c : ((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).panelModes
					.getComponents()) {
				if (c instanceof JCheckBox) {
					if (temp.isSelected()) {
						if ((JCheckBox) c != temp) {
							((JCheckBox) c).setSelected(false);
						}
					}
				}
			}
		}
		if (arg0.getSource() == panneau.valider) {
			// si on a pas encore lancé l'exercice
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
				// si on a deja lancé l'exercice
			} else {
				// on reactive l'exercice
				FenetreParametre.fen.fenetre.setEnabled(true);
				FenetreParametre.fen.fenetre.pan.controlFrame.setEnabled(true);
				panneau.fermer();
				Panneau.premierSegment = FenetreParametre.premierSegment;
				FenetreParametre.editorPane.setBackground(FenetreParametre.couleurFond);
			}
		}
	}

	public static Font getFont(String police, int selectedIndex, int style, int size) {
		try {
			Font font;
			if (selectedIndex < Constants.FONTS_NAMES.length && selectedIndex >= 0) {
				font = Font
						.createFont(Font.TRUETYPE_FONT,
								new File("ressources/fonts/" + Constants.FONTS_NAMES[selectedIndex]))
						.deriveFont(style).deriveFont((float) size);
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

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() == panneau.sliderAttente) {
			FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = panneau.sliderAttente.getValue();
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		int premierSegment = -1;
		try {
			premierSegment = Integer.valueOf((String) panneau.segmentDeDepart.getText());
			if (premierSegment > ((Panneau) FenetreParametre.fen.fenetre.getContentPane()).textHandler
					.getPhrasesCount() || premierSegment < 1) {
				JOptionPane.showMessageDialog(FenetreParametre.fen, "Le segment spécifié n'existe pas.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				premierSegment = 1;
				panneau.segmentDeDepart.setText("1");
			}
		} catch (Exception e) {
			panneau.segmentDeDepart.setText("1");
		}
		FenetreParametre.premierSegment = premierSegment;		
	}

}
