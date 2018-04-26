package main.controler;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import main.Constants;
import main.reading.ReadMode;
import main.view.FenetreParametre;
import main.view.Panneau;

public class ControleurParam implements ActionListener, ChangeListener {

	FenetreParametre.PanneauParam panneau;

	public ControleurParam(FenetreParametre.PanneauParam p) {
		this.panneau = p;
	}

	public void actionPerformed(ActionEvent arg0) {
		JComboBox<?> jcb = null;
		if (arg0.getSource() instanceof JComboBox) {
			jcb = (JComboBox<?>) arg0.getSource();
		}
		if (jcb == panneau.listeCouleurs || jcb == panneau.listeMauvaisesCouleurs || jcb == panneau.listeBonnesCouleurs
				|| panneau.listeCorrectionCouleurs == jcb) {
			String s = (String) jcb.getSelectedItem();
			Color color = null;
			if (s == "Jaune") {
				color = Constants.BG_COLOR;
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
			if (s == "Rouge") {
				color = Color.RED;
			}
			if (s == "Vert") {
				color = Color.GREEN;
			}
			((JComboBox<?>) jcb).setBackground(color);
			if (jcb == panneau.listeMauvaisesCouleurs) {
				Constants.WRONG_COLOR = color;
			}
			if (jcb == panneau.listeCorrectionCouleurs) {
				Constants.WRONG_PHRASE_COLOR = color;
			}
			if (jcb == panneau.listeBonnesCouleurs) {
				Constants.RIGHT_COLOR = color;
			}
			if (jcb == panneau.listeCouleurs) {
				if (FenetreParametre.editorPane != null) {
					FenetreParametre.editorPane.setBackground(color);
				}
				FenetreParametre.couleurFond = color;
			}
			panneau.grabFocus();
		}
		if (jcb == panneau.listeTailles) {
			int taille = Integer.valueOf((String) jcb.getSelectedItem());
			FenetreParametre.taillePolice = taille;
			FenetreParametre.police = FenetreParametre.police.deriveFont((float) taille);
			panneau.listeTailles.setFont(FenetreParametre.police);
			if (FenetreParametre.editorPane != null) {
				FenetreParametre.editorPane.setFont(FenetreParametre.police);
				((Panneau) FenetreParametre.editorPane.getParent()).rebuildPages();
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
		if (arg0.getSource() == panneau.modeSurlignage) {
			if (((JRadioButton) arg0.getSource()).isSelected()) {
				FenetreParametre.readMode = ReadMode.HIGHLIGHT;
			}
		}
		if (arg0.getSource() == panneau.modeKaraoke) {
			if (panneau.modeKaraoke.isSelected()) {
				FenetreParametre.readMode = ReadMode.GUIDED_READING;
			}
		}
		if (arg0.getSource() == panneau.modeNormal) {
			if (panneau.modeNormal.isSelected()) {
				FenetreParametre.readMode = ReadMode.NORMAL;
			}
		}
		if (arg0.getSource() == panneau.modeAnticipe) {
			if (panneau.modeAnticipe.isSelected()) {
				FenetreParametre.readMode = ReadMode.ANTICIPATED;
			}
		}
		if (arg0.getSource() == panneau.rejouerSon) {
			FenetreParametre.rejouerSon = panneau.rejouerSon.isSelected();
		}
		if (arg0.getSource() == panneau.valider) {
			if (verifierValiditeChamp()) {
				try {
				} catch (Exception e) {
				}
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
						FenetreParametre.premierSegment = Math.max(0,
								Integer.valueOf(panneau.segmentDeDepart.getText()));
					} catch (Exception e) {
						FenetreParametre.premierSegment = 0;
						panneau.segmentDeDepart.setText("0");
					}
					FenetreParametre.fen.lancerExercice();
					FenetreParametre.rejouerSon = panneau.rejouerSon.isSelected();
					panneau.fermer();
					// si on a deja lancé l'exercice
				} else {
					// on reactive l'exercice
					FenetreParametre.fen.fenetre.setEnabled(true);
					FenetreParametre.fen.fenetre.pan.controlFrame.setEnabled(true);
					panneau.fermer();
					FenetreParametre.nbFautesTolerees = Integer.valueOf(panneau.champNbFautesTolerees.getText());
					FenetreParametre.fen.fenetre.pan.nbEssaisParSegment = Integer
							.valueOf(panneau.champNbFautesTolerees.getText());
					Panneau.defautNBEssaisParSegment = Integer.valueOf(panneau.champNbFautesTolerees.getText());
					FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = panneau.sliderAttente.getValue();
					if (FenetreParametre.readMode == ReadMode.HIGHLIGHT) {
						try {
							FenetreParametre.editorPane.updateColors();
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Retourne le font correspondant à :
	 *  @param1 : la police 
	 *  @param2 : l'index du font dans la liste des polices de la FenetreParametre
	 *  @param3 : le style 
	 *  @param4 : la taille 
	 */
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

	
	/**
	 * Retourne vrai si :
	 * - Aucune couleur n'est sélectionnée en double
	 * - Les champs saisies sont cohérents
	 */
	public boolean verifierValiditeChamp() {
		boolean valide = true;

		if (!couleursUniques()) {
			JOptionPane.showMessageDialog(panneau, "Les couleurs doivent être différentes", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			valide = false;
		}

		// premier segment
		int premierSegment = -1;
		try {
			premierSegment = Integer.valueOf((String) panneau.segmentDeDepart.getText());
			if (premierSegment+2 > ((Panneau) FenetreParametre.fen.fenetre.getContentPane()).textHandler.getPhrasesCount()
					|| premierSegment < 1) {
				JOptionPane.showMessageDialog(panneau, "Entrez un segment inférieur à "+(((Panneau) FenetreParametre.fen.fenetre.getContentPane()).textHandler.getPhrasesCount()-1), "Erreur",
						JOptionPane.ERROR_MESSAGE);
				premierSegment = 1;
				panneau.segmentDeDepart.setText("1");
				valide = false;
			}
		} catch (Exception e) {
			panneau.segmentDeDepart.setText("1");
			valide = false;
		}
		FenetreParametre.premierSegment = premierSegment;

		// nb fautes tolérées
		int n = -1;
		try {
			n = Integer.valueOf((String) panneau.champNbFautesTolerees.getText());
			if (n < 0) {
				JOptionPane.showMessageDialog(panneau, "Le nombre de fautes tolérées doit être positif ou nul",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				n = 1;
				panneau.champNbFautesTolerees.setText("0");
				valide = false;
			}
		} catch (Exception e) {
			panneau.champNbFautesTolerees.setText("0");
			valide = false;
		}
		FenetreParametre.nbFautesTolerees = n;
		return valide;
	}

	/**
	 * Retourne vrai si toutes les couleurs des paramètres sont uniques
	 */
	private boolean couleursUniques() {
		boolean r = true;
		List<Color> couleursUtilisées = new ArrayList<Color>();
		couleursUtilisées.add(Constants.RIGHT_COLOR);
		couleursUtilisées.add(Constants.WRONG_COLOR);
		couleursUtilisées.add(Constants.WRONG_PHRASE_COLOR);
		couleursUtilisées.add(FenetreParametre.couleurFond);
		if (occurence(Constants.RIGHT_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_PHRASE_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(FenetreParametre.couleurFond, couleursUtilisées) != 1) {
			r = false;
		}
		return r;
	}

	/**
	 * Retourne le nombre d'apparition de la couleur dans la FenetreParametre
	 */
	private int occurence(Color c, List<Color> liste) {
		int r = 0;
		for (Color c2 : liste) {
			if (c.equals(c2)) {
				r++;
			}
		}
		return r;
	}

}
