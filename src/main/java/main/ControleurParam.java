package main;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
			if (jcb == panneau.listeBonnesCouleurs) {
				Constants.WRONG_PHRASE_COLOR = color;
			}
			if (jcb == panneau.listeCorrectionCouleurs) {
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
				if (FenetreParametre.fen.fenetre.pan.player != null) {
					FenetreParametre.fen.fenetre.pan.editorPane.retablirSurlignageBlue();
					FenetreParametre.fen.fenetre.pan.surlignerJusquaSegment(Constants.RIGHT_COLOR,
							FenetreParametre.fen.fenetre.pan.player.getCurrentPhraseIndex() - 1);
				}
			} else {
				if (FenetreParametre.editorPane != null) {
					FenetreParametre.editorPane.d�surlignerTout();
				}
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
				FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture *= 2;
			} else {
				FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture /= 2;
			}
		}
		if (arg0.getSource() == panneau.valider) {
			if (verifierValiditeChamp()) {
				// si on a pas encore lanc� l'exercice
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
					panneau.fermer();
					// si on a deja lanc� l'exercice
				} else {
					// on reactive l'exercice
					FenetreParametre.fen.fenetre.setEnabled(true);
					FenetreParametre.fen.fenetre.pan.controlFrame.setEnabled(true);
					panneau.fermer();
					Panneau.premierSegment = FenetreParametre.premierSegment;
					FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = panneau.sliderAttente.getValue();
					if (FenetreParametre.readMode == ReadMode.ANTICIPATED) {
						FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture *= 2;
					}
				}
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

	public boolean verifierValiditeChamp() {
		boolean valide = true;

		if (!couleursUniques()) {
			JOptionPane.showMessageDialog(panneau, "Les couleurs doivent �tre diff�rentes", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			valide = false;
		}

		// premier segment
		int premierSegment = -1;
		try {
			premierSegment = Integer.valueOf((String) panneau.segmentDeDepart.getText());
			if (premierSegment > ((Panneau) FenetreParametre.fen.fenetre.getContentPane()).textHandler.getPhrasesCount()
					|| premierSegment < 1) {
				JOptionPane.showMessageDialog(panneau, "Le segment sp�cifi� n'existe pas.", "Erreur",
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

		// nb fautes tol�r�es
		int n = -1;
		try {
			n = Integer.valueOf((String) panneau.champNbFautesTolerees.getText());
			if (n < 0) {
				JOptionPane.showMessageDialog(panneau, "Le nombre de fautes tol�r�es doit �tre positif ou nul",
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

	private boolean couleursUniques() {
		boolean r = true;
		List<Color> couleursUtilis�es = new ArrayList<Color>();
		couleursUtilis�es.add(Constants.RIGHT_COLOR);
		couleursUtilis�es.add(Constants.WRONG_COLOR);
		couleursUtilis�es.add(Constants.WRONG_PHRASE_COLOR);
		couleursUtilis�es.add(FenetreParametre.couleurFond);
		if (occurence(Constants.RIGHT_COLOR, couleursUtilis�es) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_COLOR, couleursUtilis�es) != 1) {
			r = false;
		}
		if (occurence(Constants.WRONG_PHRASE_COLOR, couleursUtilis�es) != 1) {
			r = false;
		}
		if (occurence(FenetreParametre.couleurFond, couleursUtilis�es) != 1) {
			r = false;
		}
		return r;
	}

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
