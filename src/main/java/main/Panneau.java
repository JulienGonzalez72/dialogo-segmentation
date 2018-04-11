package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {
<<<<<<< HEAD
	
	private static final long serialVersionUID = 1L;

	
=======

	private static final long serialVersionUID = 1L;

>>>>>>> d9c00f713c3d321be3bd3859b94593d0d47e63ce
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
<<<<<<< HEAD
	
	
	public Panneau(int w, int h) throws IOException { 
=======
	public int nbErreurs;

	public Panneau(int w, int h) throws IOException {
>>>>>>> d9c00f713c3d321be3bd3859b94593d0d47e63ce
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
	 * retourne le contenu du fichier .txt situ� � l'emplacement du param�tre
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
		editorPane.d�surlignerTout();
		// on restaure le nombre d'essais
		nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	}

	public boolean pageFinis() {
		return segmentActuel % nbSegmentsParPage == 0;
	}

	public void indiquerErreur() {
		nbErreurs++;
	}

	public void indiquerEtCorrigerErreur() {
<<<<<<< HEAD
		// TODO Auto-generated method stub
		
=======
		nbErreurs++;
>>>>>>> d9c00f713c3d321be3bd3859b94593d0d47e63ce
	}

}
