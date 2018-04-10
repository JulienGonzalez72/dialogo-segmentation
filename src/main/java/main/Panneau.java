package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;


public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

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
		
		this.add(panelText);
	}
	
	/**
	 * retourne le contenu du fichier .txt situ� � l'emplacement du param�tre
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
