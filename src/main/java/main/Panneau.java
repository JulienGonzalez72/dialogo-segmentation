package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	//panneau du texte
	public TextPane editorPane;
=======
	//panneau du texte sans cesure
	public TextPane editorPane;

	//contient le texte avec cesure
	public String texteAvecCesure;
>>>>>>> a759dec80509cf7b562d839c99832cfd29bb5bdc

	public Panneau() throws IOException {

		String texte = getTextFromFile("ressources/textes/Ah les crocodiles");
		String texteCesures = getTextFromFile("ressources/textes/Ah les crocodiles C");
		
		ControlerMouse controlerMouse = new ControlerMouse(this, new TextHandler(texte, texteCesures));
		
		this.setLayout(new GridLayout(1, 1));
		
		editorPane = new TextPane();
<<<<<<< HEAD
		
		editorPane.setText(texte);
=======
		editorPane.setText(getTextFromFile("ressources/textes/Ah les crocodiles"));
>>>>>>> a759dec80509cf7b562d839c99832cfd29bb5bdc
		editorPane.setEditable(false);	
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane);

		texteAvecCesure = getTextFromFile("ressources/textes/Ah les crocodiles C");
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
