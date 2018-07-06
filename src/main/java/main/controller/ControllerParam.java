package main.controller;

// TODO classe non fonctionnelle
public class ControllerParam {

	/*
	 * FenetreParametre.PanneauParam panneau; FenetreParametre fen; private
	 * Parametres param;
	 * 
	 * public ControlerParam(FenetreParametre fen, FenetreParametre.PanneauParam p)
	 * { this.panneau = p; this.fen = fen; }
	 * 
	 * public void actionPerformed(ActionEvent arg0) { JComboBox<?> jcb = null; if
	 * (arg0.getSource() instanceof JComboBox) { jcb = (JComboBox<?>)
	 * arg0.getSource(); } if (jcb == panneau.fontSizeComboBox) { int taille =
	 * (Integer) jcb.getSelectedItem(); Font font = new
	 * Font(panneau.fontFamilyComboBox.getFont().getFontName(),
	 * Constants.DEFAULT_FONT_STYLE, taille); if (fen.fenetre.isVisible() &&
	 * fen.fenetre.pan.editorPane != null) {
	 * fen.fenetre.pan.editorPane.setFont(font); fen.fenetre.pan.rebuildPages(); } }
	 * if (jcb == panneau.fontFamilyComboBox) { String police = (String)
	 * jcb.getSelectedItem(); Font font = new Font(police,
	 * Constants.DEFAULT_FONT_STYLE, (Integer)
	 * panneau.fontSizeComboBox.getSelectedItem());
	 * jcb.setFont(font.deriveFont((float) jcb.getFont().getSize())); if
	 * (fen.fenetre.isVisible() && fen.fenetre.pan.editorPane != null) {
	 * fen.fenetre.pan.editorPane.setFont(font); fen.fenetre.pan.rebuildPages(); } }
	 * if (arg0.getSource() == panneau.guidedModeRadio || arg0.getSource() ==
	 * panneau.highlightModeRadio || arg0.getSource() == panneau.segmentedModeRadio
	 * || arg0.getSource() == panneau.anticipatedModeRadio) {
	 * panneau.savePreferences(panneau.oldMode); panneau.oldMode =
	 * panneau.getReadMode(); panneau.applyPreferences(panneau.getReadMode());
	 * panneau.updateMode(); } if (arg0.getSource() == panneau.validButton) {
	 * panneau.savePreferences(panneau.oldMode); param = fen.getCurrentParameters();
	 * fen.stopItem.setEnabled(true); if (verifierValiditeChamp()) { /// lance
	 * l'exercice /// if (!fen.fenetre.isVisible()) { fen.lancerExercice(); } ///
	 * modifie les paramètres de l'exercice en cours /// else {
	 * //fen.fenetre.setParameters(param); } } } }
	 * 
	 *//**
		 * Retourne vrai si : - Aucune couleur n'est sélectionnée en double - Les champs
		 * saisies sont coh�rents
		 */
	/*
	 * public boolean verifierValiditeChamp() { boolean valide = true;
	 * 
	 * if (!couleursUniques()) { JOptionPane.showMessageDialog(panneau,
	 * "Certaines couleurs sont identiques et risquent de se confondre !",
	 * "Attention", JOptionPane.WARNING_MESSAGE); valide = true; }
	 * 
	 * // premier segment int premierSegment = -1; try { premierSegment =
	 * Integer.valueOf((String) panneau.startingPhraseField.getText()); if
	 * (!isValidPhrase(premierSegment)) { JOptionPane.showMessageDialog(panneau,
	 * "Entrez un segment inférieur à "
	 * 
	 * + (((TextPanel) fen.fenetre.getContentPane()).textHandler.getPhrasesCount() -
	 * 1), "Erreur", JOptionPane.ERROR_MESSAGE); premierSegment = 1;
	 * panneau.startingPhraseField.setText("1"); valide = false; } } catch
	 * (NumberFormatException e) { panneau.startingPhraseField.setText("1"); valide
	 * = false; }
	 * 
	 * // nb fautes tol�r�es int n = -1; try { n = Integer.valueOf((String)
	 * panneau.toleratedErrorsField.getText()); if (n < 0) {
	 * JOptionPane.showMessageDialog(panneau,
	 * "Le nombre de fautes tol�r�es doit �tre positif ou nul", "Erreur",
	 * JOptionPane.ERROR_MESSAGE); n = 1; panneau.toleratedErrorsField.setText("0");
	 * valide = false; } } catch (Exception e) { e.printStackTrace();
	 * panneau.toleratedErrorsField.setText("0"); valide = false; } return valide; }
	 * 
	 * public boolean isValidPhrase(int phrase) {
	 * 
	 * return phrase + 2 <= ((TextPanel)
	 * fen.fenetre.getContentPane()).textHandler.getPhrasesCount() && phrase >= 1; }
	 * 
	 *//**
		 * Retourne vrai si toutes les couleurs des paramétres sont uniques
		 */
	/*
	 * private boolean couleursUniques() { boolean r = true;
	 * 
	 * List<Color> usedColors = new ArrayList<Color>();
	 * usedColors.add(param.rightColor); usedColors.add(param.wrongColor);
	 * usedColors.add(param.correctionColor); usedColors.add(param.bgColor); if
	 * (occurence(param.rightColor, usedColors) != 1) { r = false; } if
	 * (occurence(param.wrongColor, usedColors) != 1) { r = false; } if
	 * (occurence(param.correctionColor, usedColors) != 1) { r = false; } if
	 * (occurence(param.bgColor, usedColors) != 1) { r = false; } return r; }
	 * 
	 *//**
		 * Retourne le nombre d'apparition de la couleur dans la FenetreParametre
		 *//*
			 * private int occurence(Color c, List<Color> liste) { int r = 0; for (Color c2
			 * : liste) { if (c.equals(c2)) { r++; } } return r; }
			 */

}