package main;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
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

	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage = new HashMap<Integer, List<Integer>>();

	public Panneau(JFrame fenetre) throws IOException {
		this.fenetre = fenetre;
		segmentActuel = FenetreParametre.premierSegment-1;
		pageActuelle = 0;
		String texteCesures = getTextFromFile("ressources/textes/Ah les crocodiles C");
		textHandler = new TextHandler(texteCesures);
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);
		this.setLayout(new BorderLayout());
		editorPane = new TextPane();
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane, BorderLayout.CENTER);
		editorPane.addKeyListener(controlerMouse);
		nbPages = getNbPages();
	}
	
	/**
	 * S'exécute lorsque le panneau s'est bien intégré à la fenêtre
	 */
	public void init() {
		/// construit la mise en page virtuelle ///
		buildPages();
		/// affiche la première page ///
		afficherPageSuivante();
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
			// on recupere les segments a afficher dans la page
			List<String> liste = new ArrayList<String>();
			for (Integer i : segmentsEnFonctionDeLaPage.get(pageActuelle)) {
				liste.add(textHandler.getPhrase(i));
			}
			for (String string : liste) {
				texteAfficher += string;
			}
			editorPane.setText(texteAfficher);
			editorPane.désurlignerTout();
		}
	}
	
	public void buildPages() {
		/*
		 * espace total dans la fenetre = fw * fh
		 * espace total sans les marges = (fw - 2 * m) * (fh - m)
		 * espace total sans les interlignes = rep / 2
		 */
		float maxArea = (getWidth() - 2 * Constants.TEXTPANE_MARGING) * (getHeight() - 2 * Constants.TEXTPANE_MARGING) / 2f;
		int segment = 0;
		int numPage = 1;
		while (segment < textHandler.getPhrasesCount()) {
			String page = "";
			List<Integer> segmentsNum = new ArrayList<Integer>();
			do {
				page += textHandler.getPhrase(segment);
				segmentsNum.add(segment);
				segment++;
			} while (editorPane.getTextBounds(page).getWidth()
					* editorPane.getTextBounds(page).getHeight() < maxArea);
			segmentsEnFonctionDeLaPage.put(numPage, segmentsNum);
			numPage++;
		}
	}

	public boolean pageFinis() {
		// la page actuelle contient t-elle le segment suivant ? si non elle est finis
		return !segmentsEnFonctionDeLaPage.get(pageActuelle).contains(segmentActuel);
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
		return segmentsEnFonctionDeLaPage.get(pageActuelle).get(0);
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
