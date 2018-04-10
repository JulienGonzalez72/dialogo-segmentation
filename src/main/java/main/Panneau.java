package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;


public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	//panneau du texte
	public JTextPane panelText;

	public Panneau() throws IOException, BadLocationException {
		
		ControlerMouse controlerMouse = new ControlerMouse(this);
		
		setLayout(new GridLayout(1, 1));
		
		//panel of text
		panelText = new JTextPane();
		panelText.setText(getTextFromFile("ressources/textes/dameDeFoix.txt"));
		panelText.setEditable(false);
		panelText.addMouseListener(controlerMouse);
=======
	public Panneau() throws IOException {
		this.setLayout(new GridLayout(1, 1));
		TextPane editorPane = new TextPane();
		editorPane.setText(getTextFromFile("ressources/textes/dameDeFoix.txt"));
		editorPane.insert(editorPane.getText().indexOf(""), "/");
		editorPane.setEditable(false);
>>>>>>> 5603b221d1bb0520aa95df5b149907bf3acf1ef3
		
		this.add(panelText);
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
