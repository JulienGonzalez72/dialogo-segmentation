package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	public static final int defautNBSegmentsParPage = 2;
	
	private static final long serialVersionUID = 1L;

	//panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbSegmentsParPage = defautNBSegmentsParPage;
	public int segmentActuel;

	public Panneau(int w, int h) throws IOException {
<<<<<<< HEAD
		pageActuelle = 1;
=======

		segmentActuel = 0;
		pageActuelle = 0;	
>>>>>>> 8844eaee15dc161098fcaf4a8da8cd2a911ea4ea
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
	}

	public boolean pageFinis() {
		return (segmentActuel) % nbSegmentsParPage == 0;
	}

}
