package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	public Panneau() throws IOException {
		this.setLayout(new GridLayout(1, 1));
		JEditorPane editorPane = new JEditorPane();
		editorPane.setText(getTextFromFile("ressources/textes/dameDeFoix.txt"));
		editorPane.setEditable(false);
		Font font = new Font("TimesRoman", Font.BOLD, 18);
		editorPane.setFont(font);
		
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
