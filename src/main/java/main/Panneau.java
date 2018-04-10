package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	//panneau du texte
	public JTextPane editorPane;

	public Panneau() throws IOException {
		
		ControlerMouse controlerMouse = new ControlerMouse(this);
		
		this.setLayout(new GridLayout(1, 1));
		
		editorPane = new TextPane();
		editorPane.setText(getTextFromFile("ressources/textes/dameDeFoix.txt"));
		editorPane.setEditable(false);	
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane);
	}
	
	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
	 *
	 */ String getTextFromFile(String emplacement) throws IOException{
		File fichierTxt = new File(emplacement);
		InputStream ips = null;
		ips = new FileInputStream(fichierTxt);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String toReturn = "";
		String ligneCourante = br.readLine();
		while ( ligneCourante != null){
			toReturn += ligneCourante;
			ligneCourante = br.readLine();
		}
		br.close();
		return toReturn;
	}

}
