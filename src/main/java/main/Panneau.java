package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	public static final int defautNBSegmentsParPage = 5;
	
	private static final long serialVersionUID = 1L;

	//panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbSegmentsParPage = defautNBSegmentsParPage;

	public Panneau(int w, int h) throws IOException {
		pageActuelle = 1;
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
		String[] tab = textHandler.getPhrases((pageActuelle-1)*nbSegmentsParPage,pageActuelle*nbSegmentsParPage);
		for (String string : tab) {
			texteAfficher += string;
		}
		editorPane.setText(texteAfficher);
	}

}
