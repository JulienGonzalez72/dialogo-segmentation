package main;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;
	public static int premierSegment;
	public static int defautNBEssaisParSegment;

	// panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbEssaisParSegment = defautNBEssaisParSegment;
	public int nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	public int segmentActuel;
	public int nbErreurs;
	public JFrame fenetre;

	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage;

	public Panneau(int w, int h, JFrame fenetre) throws IOException {
		this.fenetre = fenetre;
		segmentActuel = 0;
		pageActuelle = 0;
		String texteCesures = getTextFromFile("ressources/textes/20 000 lieux sous les mers");
		textHandler = new TextHandler(texteCesures);
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);
		this.setLayout(new BorderLayout());
		editorPane = new TextPane();
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		afficherPageSuivante();
		this.add(editorPane, BorderLayout.CENTER);
		editorPane.addKeyListener(controlerMouse);
		//TODO initialisation de texteSegmentEnFonctionNumero
		//TODO initialisation de segmentsEnFonctionDeLaPage

	}

	/**
	 * retourne le nombre de pages
	 *
	 */
	public int getNbPages() {
		int r = 0;
		for (Integer i : segmentsEnFonctionDeLaPage.keySet()) {
			if (i > r) {
				r = i;
			}
		}
		return r;
	}

	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
	 *
	 */
	public static String getTextFromFile(String emplacement) throws IOException {
		File fichierTxt = new File(emplacement);
		InputStream ips = null;
		ips = new FileInputStream(fichierTxt);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String toReturn = "";
		String ligneCourante = br.readLine();
		while (ligneCourante != null) {
			toReturn += ligneCourante + " ";
			ligneCourante = br.readLine();
		}
		br.close();
		return toReturn;
	}

	/**
	 * passe a la page suivante et l'affiche
	 *
	 */
	public void afficherPageSuivante() {
		if (pageActuelle == nbPages && pageActuelle > 0) {
			afficherCompteRendu();
		} else {
			pageActuelle++;
			fenetre.setTitle("Lexidia - Page " + pageActuelle);
			String texteAfficher = "";
<<<<<<< HEAD
			// on recuepre les segments a afficher dans la page
			/*
			 * espace total dans la fenetre = fw * fh
			 * espace total sans les marges = fw * fh - margin * fw
			 */
			String[] tab = textHandler.getPhrases((pageActuelle - 1) * nbSegmentsParPage,
					pageActuelle * nbSegmentsParPage - 1);
			for (String string : tab) {
=======
			// on recupere les segments a afficher dans la page
			List<String> liste = new ArrayList<String>();
			for (Integer i : segmentsEnFonctionDeLaPage.get(pageActuelle)) {
				liste.add(textHandler.getPhrase(i));
			}
			for (String string : liste) {
>>>>>>> a3b90a2ede6ac235c63e7b7396be550f5108eef6
				texteAfficher += string;
			}
			editorPane.setText(texteAfficher);
			editorPane.désurlignerTout();
		}
	}

	public boolean pageFinis() {
		// la page actuelle contient t-elle le segment suivant ? si non elle est finis
		return !segmentsEnFonctionDeLaPage.get(pageActuelle).contains(segmentActuel + 1);
	}

	public void indiquerErreur(int debut, int fin) {
		nbErreurs++;
		editorPane.enleverSurlignageRouge();
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_COLOR);
	}

	public void indiquerEtCorrigerErreur(int debut, int fin) {
		nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		nbErreurs++;
		editorPane.indiceDernierCaractereSurligné = fin;
		editorPane.enleverSurlignageRouge();
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_PHRASE_COLOR);
	}

	public int getNumeroPremierSegmentAffiché() {
		return -1;
	}

	public void afficherCompteRendu() {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			JOptionPane.showMessageDialog(this, "L'exercice est terminé." + "\n" + "Le patient a fait : " + nbErreurs
					+ " erreur" + (nbErreurs > 1 ? "s" : "") + ".", "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}

	}

}
