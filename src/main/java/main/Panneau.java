package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int defautNBSegmentsParPage = 4;
	public static final int defautNBEssaisParSegment = 1;

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

	public Panneau(int w, int h) throws IOException {
		segmentActuel = 0;
		pageActuelle = 0;
		String texteCesures = getTextFromFile("ressources/textes/Ah les crocodiles C");
		textHandler = new TextHandler(texteCesures);
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);

		this.setLayout(new BorderLayout());
		editorPane = new TextPane();
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		afficherPageSuivante();
		this.add(editorPane, BorderLayout.CENTER);
	}

	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
	 *
	 */
	String getTextFromFile(String emplacement) throws IOException {
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
		pageActuelle++;
		String texteAfficher = "";
		// on recuepre les segments a afficher dans la page
		String[] tab = textHandler.getPhrases((pageActuelle - 1) * nbSegmentsParPage,
				pageActuelle * nbSegmentsParPage - 1);
		for (String string : tab) {
			texteAfficher += string;
		}
		editorPane.setText(texteAfficher);
		editorPane.désurlignerTout();
		// on restaure le nombre d'essais
		nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	}

	public boolean pageFinis() {
		return segmentActuel % nbSegmentsParPage == 0;
	}

	public void indiquerErreur() {
		nbErreurs++;
		int position = editorPane.getCaretPosition();
		editorPane.surlignerPhrase(position, Color.RED);
	}

	public void indiquerEtCorrigerErreur() {
		nbErreurs++;
	}
	
	public int getNumeroPremierSegmentAffiché() {
		return (pageActuelle-1)*nbSegmentsParPage;
	}

}
