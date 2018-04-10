package main;

import java.awt.*;
import java.io.*;
import java.util.stream.Stream;

import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

	public Panneau() throws IOException {
		this.setLayout(new GridLayout(1, 1));
		JEditorPane editorPane = new JEditorPane();
		this.add(editorPane);

		File fichierTxt = new File("ressources/textes/dameDeFoix.txt");
		InputStream ips = null;
		ips = new FileInputStream(fichierTxt);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String chaine = "";
		Stream<String> lignes = br.lines();
		br.close();
		//commentaire de roman
	}

}
