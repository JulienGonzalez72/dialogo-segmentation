package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;
<<<<<<< HEAD


	public static int defautNBSegmentsParPage;
	public static int defautNBEssaisParSegment;


=======
	
	public static int defautNBSegmentsParPage = 4;
	public static int defautNBEssaisParSegment = 2;

>>>>>>> 0bcfbeaea59ca069bcd10e7ade7cb8b640cb3750
	// panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbSegmentsParPage = defautNBSegmentsParPage;
	public int nbEssaisParSegment = defautNBEssaisParSegment;
	public int nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	public int segmentActuel;
	public int nbErreurs;
	public JFrame fenetre;

	public Panneau(int w, int h, JFrame fenetre) throws IOException {
		this.fenetre = fenetre;
		segmentActuel = 0;
		pageActuelle = 0;
		String texteCesures = getTextFromFile("ressources/textes/20 000 lieux sous les mers");
		textHandler = new TextHandler(texteCesures);
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);
		nbPages = textHandler.getPhrasesCount() / nbSegmentsParPage + 1;

		this.setLayout(new BorderLayout());
		editorPane = new TextPane();
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		afficherPageSuivante();
		this.add(editorPane, BorderLayout.CENTER);
		editorPane.addKeyListener(controlerMouse);
	}

	/**
	 * retourne le contenu du fichier .txt situ� � l'emplacement du param�tre
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
			// on recuepre les segments a afficher dans la page
			String[] tab = textHandler.getPhrases((pageActuelle - 1) * nbSegmentsParPage,
					pageActuelle * nbSegmentsParPage - 1);
			for (String string : tab) {
				texteAfficher += string;
			}
			editorPane.setText(texteAfficher);
			editorPane.d�surlignerTout();
		}
	}

	public boolean pageFinis() {
		return segmentActuel % nbSegmentsParPage == 0;
	}

	public void indiquerErreur(int debut, int fin) {
		nbErreurs++;
		editorPane.enleverSurlignageRouge();
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_COLOR);
	}

	public void indiquerEtCorrigerErreur(int debut, int fin) {
		nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		nbErreurs++;
		editorPane.indiceDernierCaractereSurlign� = fin;
		editorPane.enleverSurlignageRouge();
<<<<<<< HEAD
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_PHRASE_COLOR);
=======
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_COLOR);
>>>>>>> 0bcfbeaea59ca069bcd10e7ade7cb8b640cb3750
	}

	public int getNumeroPremierSegmentAffich�() {
		return (pageActuelle - 1) * nbSegmentsParPage;
	}

	public void afficherCompteRendu() {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			JOptionPane.showMessageDialog(this, "L'exercice est termin�." + "\n" + "Le patient a fait : " + nbErreurs
					+ " erreur" + (nbErreurs > 1 ? "s" : "") + ".", "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}

	}

}
