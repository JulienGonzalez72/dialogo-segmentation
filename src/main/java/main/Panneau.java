package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {
<<<<<<< HEAD

	public static final int defautNBSegmentsParPage = 5;
	
	private static final long serialVersionUID = 1L;

	//panneau du texte
=======
	
	private static final long serialVersionUID = 1L;

	
	public static final int defautNBSegmentsParPage = 4;
	public static final int defautNBEssaisParSegment = 1;

	// panneau du texte
>>>>>>> 5e3b18eb12d21f05b95175a349e8c30a877750f7
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
	
	
	public Panneau(int w, int h) throws IOException { 
>>>>>>> 5e3b18eb12d21f05b95175a349e8c30a877750f7
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
			toReturn += ligneCourante+" ";
			ligneCourante = br.readLine();
		}
		br.close();
		return toReturn;
	}
	
	/**
	 * passe a la page suivante et l'affiche
	 *
	 */
	public void afficherPageSuivante(){
		pageActuelle++;
		String texteAfficher = "";
		//on recuepre les segments a afficher dans la page
		String[] tab = textHandler.getPhrases((pageActuelle-1)*nbSegmentsParPage,pageActuelle*nbSegmentsParPage-1);
		for (String string : tab) {
			texteAfficher += string;
		}
		editorPane.setText(texteAfficher);
		editorPane.d�surlignerTout();
		//on restaure le nombre d'essais
		nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	}

	public boolean pageFinis() {
<<<<<<< HEAD
		return (segmentActuel) % nbSegmentsParPage == 0 && segmentActuel > 0;
=======
		return segmentActuel % nbSegmentsParPage == 0;
	}

	public void indiquerErreur() {
		
		
	}

	public void indiquerEtCorrigerErreur() {
		// TODO Auto-generated method stub
		
>>>>>>> 5e3b18eb12d21f05b95175a349e8c30a877750f7
	}

}
