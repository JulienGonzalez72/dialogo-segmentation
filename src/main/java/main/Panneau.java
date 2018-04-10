package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	public Panneau() throws IOException {
		this.setLayout(new GridLayout(1, 1));
		TextPane editorPane = new TextPane();
		editorPane.setText(getTextFromFile("ressources/textes/dameDeFoix.txt"));
		editorPane.insert(editorPane.getText().indexOf(""), "/");
		editorPane.setEditable(false);
		
		this.add(editorPane);
	}
	
	private String getTextFromFile(String emplacement) throws IOException{
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
