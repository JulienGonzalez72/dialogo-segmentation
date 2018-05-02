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
import main.Parametres;
import main.reading.ReadMode;
import main.view.FenetreParametre;
import main.view.Panneau;

public class ControleurParam implements ActionListener, ChangeListener {

	FenetreParametre.PanneauParam panneau;
	FenetreParametre fen;
	Parametres param;

	public ControleurParam(FenetreParametre fen, FenetreParametre.PanneauParam p) {
		this.panneau = p;
		this.fen = fen;
		param = fen.param;
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
				if (fen.editorPane != null) {
					fen.editorPane.setBackground(color);
				}
				param.couleurFond = color;
			}
			panneau.grabFocus();
		}
		if (jcb == panneau.listeTailles) {
			int taille = Integer.valueOf((String) jcb.getSelectedItem());
			param.taillePolice = taille;
			param.police = param.police.deriveFont((float) taille);
			panneau.listeTailles.setFont(new Font(param.police.getFontName(),param.police.getStyle(),Math.min(20, param.police.getSize())));
			if (fen.editorPane != null) {
				fen.editorPane.setFont(param.police);
				((Panneau) fen.editorPane.getParent()).rebuildPages();
			}
		}
		if (jcb == panneau.listePolices) {
			String police = (String) jcb.getSelectedItem();
			param.police = getFont(police, jcb.getSelectedIndex(), Font.BOLD,param.taillePolice);
			panneau.listePolices.setFont(new Font(param.police.getFontName(),param.police.getStyle(),Math.min(20, param.police.getSize())));
			if (fen.editorPane != null) {
				fen.editorPane.setFont(param.police);
			}
		}
		if (arg0.getSource() == panneau.modeSurlignage) {
			if (((JRadioButton) arg0.getSource()).isSelected()) {
				param.readMode = ReadMode.SUIVI;
			}
		}
		if (arg0.getSource() == panneau.modeKaraoke) {
			if (panneau.modeKaraoke.isSelected()) {
				param.readMode = ReadMode.GUIDEE;
			}
		}
		if (arg0.getSource() == panneau.modeNormal) {
			if (panneau.modeNormal.isSelected()) {
				param.readMode = ReadMode.SEGMENTE;
			}
		}
		if (arg0.getSource() == panneau.modeAnticipe) {
			if (panneau.modeAnticipe.isSelected()) {
				param.readMode = ReadMode.ANTICIPE;
			}
		}
		if (arg0.getSource() == panneau.rejouerSon) {
			param.rejouerSon = panneau.rejouerSon.isSelected();
		}
		if (arg0.getSource() == panneau.valider) {
			fen.eMenuItem2.setEnabled(true);
			//mise a jour de la couleur de la barre de progression
			fen.fenetre.pan.progressBar.setForeground(Constants.RIGHT_COLOR);
			if (verifierValiditeChamp()) {
				try {
				} catch (Exception e) {
				}
				// si on a pas encore lancé l'exercice
				if (fen.editorPane == null) {
					try {
						param.nbFautesTolerees = Math.max(0,
								Integer.valueOf(panneau.champNbFautesTolerees.getText()));
					} catch (Exception e) {
						param.nbFautesTolerees = 0;
						panneau.champNbFautesTolerees.setText("0");
					}
					try {
						param.premierSegment = Math.max(0,
								Integer.valueOf(panneau.segmentDeDepart.getText()));
					} catch (Exception e) {
						param.premierSegment = 0;
						panneau.segmentDeDepart.setText("0");
					}
					fen.lancerExercice();
					param.rejouerSon = panneau.rejouerSon.isSelected();
					//panneau.fermer();
					// si on a deja lancé l'exercice
				} else {
					// on reactive l'exercice
					fen.fenetre.setEnabled(true);
					fen.fenetre.pan.controlPanel.setEnabled(true);
					//panneau.fermer();
					param.nbFautesTolerees = Integer.valueOf(panneau.champNbFautesTolerees.getText());
					fen.fenetre.pan.nbEssaisParSegment = Integer
							.valueOf(panneau.champNbFautesTolerees.getText());
					Panneau.defautNBEssaisParSegment = Integer.valueOf(panneau.champNbFautesTolerees.getText());
					param.tempsPauseEnPourcentageDuTempsDeLecture = panneau.sliderAttente.getValue();
					if (param.readMode == ReadMode.SUIVI) {
						try {
							fen.editorPane.updateColors();
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
			param.tempsPauseEnPourcentageDuTempsDeLecture = panneau.sliderAttente.getValue();
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
			if (premierSegment+2 > ((Panneau) fen.fenetre.getContentPane()).textHandler.getPhrasesCount()
					|| premierSegment < 1) {
				JOptionPane.showMessageDialog(panneau, "Entrez un segment inférieur à "+(((Panneau) fen.fenetre.getContentPane()).textHandler.getPhrasesCount()-1), "Erreur",
						JOptionPane.ERROR_MESSAGE);
				premierSegment = 1;
				panneau.segmentDeDepart.setText("1");
				valide = false;
			}
		} catch (Exception e) {
			panneau.segmentDeDepart.setText("1");
			valide = false;
		}
		param.premierSegment = premierSegment;

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
		param.nbFautesTolerees = n;
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
		couleursUtilisées.add(param.couleurFond);
		if (occurence(Constants.RIGHT_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_PHRASE_COLOR, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(param.couleurFond, couleursUtilisées) != 1) {
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
