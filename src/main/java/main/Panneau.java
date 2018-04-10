package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	// panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	
	public Panneau(int w, int h) throws IOException {

		pageActuelle = 1;	
		String texteCesures = getTextFromFile("ressources/textes/Ah les crocodiles C");	
		TextHandler textHandler = new TextHandler(texteCesures);
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);

		String texteAfficher = "";
		//on recuepre les segments a afficher dans la apge
		String[] tab = textHandler.getPhrases((pageActuelle-1)*textHandler.nbSegmentParPage,pageActuelle*textHandler.nbSegmentParPage);
		for (String string : tab) {
			texteAfficher += tab;
		}
		
		
		this.setLayout(new BorderLayout());
		editorPane = new TextPane();
		editorPane.setText(texteAfficher);
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
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

}
