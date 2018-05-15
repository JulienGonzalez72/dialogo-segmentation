package main.controler;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import main.Constants;
import main.Parametres;
import main.view.FenetreParametre;
import main.view.Panneau;

public class ControlerParam implements ActionListener, ChangeListener {

	FenetreParametre.PanneauParam panneau;
	FenetreParametre fen;
	private Parametres param;

	public ControlerParam(FenetreParametre fen, FenetreParametre.PanneauParam p) {
		this.panneau = p;
		this.fen = fen;
		//param = fen.param;
	}

	public void actionPerformed(ActionEvent arg0) {
		JComboBox<?> jcb = null;
		if (arg0.getSource() instanceof JComboBox) {
			jcb = (JComboBox<?>) arg0.getSource();
		}
		//Parametres param = fen.getCurrentParameters();
		if (jcb == panneau.bgColorComboBox || jcb == panneau.wrongColorComboBox || jcb == panneau.rightColorComboBox
				|| panneau.correctColorComboBox == jcb) {
			String s = (String) jcb.getSelectedItem();
			Color color = FenetreParametre.stringToColor(s);
			((JComboBox<?>) jcb).setBackground(color);
			/*if (jcb == panneau.wrongColorComboBox) {
				Constants.WRONG_COLOR = color;
			}
			if (jcb == panneau.correctColorComboBox) {
				Constants.WRONG_PHRASE_COLOR = color;
			}
			if (jcb == panneau.rightColorComboBox) {
				Constants.RIGHT_COLOR = color;
			}*/
			if (jcb == panneau.bgColorComboBox) {
				if (fen.editorPane != null) {
					fen.editorPane.setBackground(color);
				}
				//param.couleurFond = color;
			}
			panneau.grabFocus();
		}
		/*if (jcb == panneau.fontSizeComboBox) {
			int taille = (Integer) jcb.getSelectedItem();
			param.taillePolice = taille;
			param.police = param.police.deriveFont((float) taille);
			panneau.fontSizeComboBox.setFont(new Font(param.police.getFontName(), param.police.getStyle(),
					Math.min(20, param.police.getSize())));
			if (fen.editorPane != null) {
				fen.editorPane.setFont(param.police);
				fen.fenetre.pan.rebuildPages();
			}
		}
		if (jcb == panneau.fontFamilyComboBox) {
			String police = (String) jcb.getSelectedItem();
			param.police = getFont(police, jcb.getSelectedIndex(), Font.BOLD, param.taillePolice);
			panneau.fontFamilyComboBox.setFont(new Font(param.police.getFontName(), param.police.getStyle(),
					Math.min(20, param.police.getSize())));
			if (fen.editorPane != null) {
				fen.editorPane.setFont(param.police);
			}
		}*/
		/*if (arg0.getSource() == panneau.highlightModeRadio) {
			if (((JRadioButton) arg0.getSource()).isSelected()) {
				param.readMode = ReadMode.SUIVI;
				try {
					fen.pan.applyPreferences();
				} catch (NumberFormatException | IOException e) {}
			}
		}
		if (arg0.getSource() == panneau.guidedModeRadio) {
			if (panneau.guidedModeRadio.isSelected()) {
				param.readMode = ReadMode.GUIDEE;
				try {
					fen.pan.applyPreferences();
				} catch (NumberFormatException | IOException e) {}
			}
		}
		if (arg0.getSource() == panneau.segmentedModeRadio) {
			if (panneau.segmentedModeRadio.isSelected()) {
				param.readMode = ReadMode.SEGMENTE;
				try {
					fen.pan.applyPreferences();
				} catch (NumberFormatException | IOException e) {}
			}
		}
		if (arg0.getSource() == panneau.anticipatedModeRadio) {
			if (panneau.anticipatedModeRadio.isSelected()) {
				param.readMode = ReadMode.ANTICIPE;
				try {
					fen.pan.applyPreferences();
				} catch (NumberFormatException | IOException e) {}
			}
		}
		if (arg0.getSource() == panneau.replayCheckBox) {
			param.rejouerSon = panneau.replayCheckBox.isSelected();
		}*/
		if (arg0.getSource() == panneau.guidedModeRadio
				|| arg0.getSource() == panneau.highlightModeRadio
				|| arg0.getSource() == panneau.segmentedModeRadio
				|| arg0.getSource() == panneau.anticipatedModeRadio) {
			panneau.savePreferences(panneau.oldMode);
			panneau.oldMode = panneau.getReadMode();
			panneau.applyPreferences(panneau.getReadMode());
		}
		if (arg0.getSource() == panneau.validButton) {
			panneau.savePreferences(panneau.oldMode);
			param = fen.getCurrentParameters();
			fen.stopItem.setEnabled(true);
			//fen.fenetre.pan.progressBar.setForeground(Constants.RIGHT_COLOR);
			if (verifierValiditeChamp()) {

				/*try {
					param.nbFautesTolerees = Math.max(0, Integer.valueOf(panneau.toleratedErrorsField.getText()));
				} catch (Exception e) {
					param.nbFautesTolerees = 0;
					panneau.toleratedErrorsField.setText("0");
				}
				try {
					param.premierSegment = Math.max(0, Integer.valueOf(panneau.startingPhraseField.getText()));
				} catch (Exception e) {
					param.premierSegment = 0;
					panneau.startingPhraseField.setText("0");
				}*/
				fen.lancerExercice();
				//param.rejouerSon = panneau.replayCheckBox.isSelected();

			}
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (arg0.getSource() == panneau.waitSlider) {
			//param.tempsPauseEnPourcentageDuTempsDeLecture = panneau.waitSlider.getValue();
		}
	}

	/**
	 * Retourne vrai si : - Aucune couleur n'est sélectionnée en double - Les champs
	 * saisies sont cohérents
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
			premierSegment = Integer.valueOf((String) panneau.startingPhraseField.getText());
			if (premierSegment + 2 > ((Panneau) fen.fenetre.getContentPane()).textHandler.getPhrasesCount()
					|| premierSegment < 1) {
				JOptionPane.showMessageDialog(panneau,
						"Entrez un segment inférieur à "
								+ (((Panneau) fen.fenetre.getContentPane()).textHandler.getPhrasesCount() - 1),
						"Erreur", JOptionPane.ERROR_MESSAGE);
				premierSegment = 1;
				panneau.startingPhraseField.setText("1");
				valide = false;
			}
		} catch (Exception e) {e.printStackTrace();
			panneau.startingPhraseField.setText("1");
			valide = false;
		}
		//param.premierSegment = premierSegment;

		// nb fautes tolérées
		int n = -1;
		try {
			n = Integer.valueOf((String) panneau.toleratedErrorsField.getText());
			if (n < 0) {
				JOptionPane.showMessageDialog(panneau, "Le nombre de fautes tolérées doit être positif ou nul",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				n = 1;
				panneau.toleratedErrorsField.setText("0");
				valide = false;
			}
		} catch (Exception e) {e.printStackTrace();
			panneau.toleratedErrorsField.setText("0");
			valide = false;
		}
		//param.nbFautesTolerees = n;
		return valide;
	}

	/**
	 * Retourne vrai si toutes les couleurs des paramètres sont uniques
	 */
	private boolean couleursUniques() {
		boolean r = true;
		List<Color> couleursUtilisées = new ArrayList<Color>();
		couleursUtilisées.add(param.rightColor);
		couleursUtilisées.add(param.wrongColor);
		couleursUtilisées.add(param.correctionColor);
		couleursUtilisées.add(param.bgColor);
		if (occurence(param.rightColor, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(param.wrongColor, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(param.correctionColor, couleursUtilisées) != 1) {
			r = false;
		}
		if (occurence(param.bgColor, couleursUtilisées) != 1) {
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
