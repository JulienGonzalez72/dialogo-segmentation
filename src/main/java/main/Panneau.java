package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	//panneau du texte
	public TextPane editorPane;

	public Panneau() throws IOException {
=======
	// panneau du texte
	public TextPane editorPane;

	public Panneau(int w, int h) throws IOException {
>>>>>>> 9dd8d22856a7fd5c1a9630037843d77792ad3bc1

		String texte = getTextFromFile("ressources/textes/Ah les crocodiles");
		String texteCesures = getTextFromFile("ressources/textes/Ah les crocodiles C");

		ControlerMouse controlerMouse = new ControlerMouse(this, new TextHandler(texte, texteCesures));

		this.setLayout(new BorderLayout());

		editorPane = new TextPane();
<<<<<<< HEAD
		
		editorPane.setText(texte);
		editorPane.setEditable(false);	
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane);
=======

		editorPane.setText(texte);
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane, BorderLayout.CENTER);

		JPanel panelDesFleches = new JPanel();
		panelDesFleches.setLayout(new GridLayout(1, 2));
		JButton pagePrecedente = new JButton();
		JButton pageSuivante = new JButton();
		panelDesFleches.add(pagePrecedente);
		panelDesFleches.add(pageSuivante);
		this.add(panelDesFleches, BorderLayout.SOUTH);

		// redimention des images
		File f = new File("ressources/images/flechePrecedente.png");
		BufferedImage buff = null;
		try {
			buff = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image scaledImage = buff.getScaledInstance(w/10, h/10, Image.SCALE_SMOOTH);
		ImageIcon imageFinaleFlechePrecedente = new ImageIcon(scaledImage);

		f = new File("ressources/images/flechePrecedente.png");
		buff = null;
		try {
			buff = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledImage = buff.getScaledInstance(w/10, h/10, Image.SCALE_SMOOTH);
		ImageIcon imageFinaleFlecheSuivante = new ImageIcon(scaledImage);

		pagePrecedente.setIcon(imageFinaleFlechePrecedente);
		pageSuivante.setIcon(imageFinaleFlecheSuivante);
		
		pagePrecedente.setIcon(new ImageIcon("ressources/images/flechePrecedente.png"));
		pageSuivante.setIcon(new ImageIcon("ressources/images/flecheSuivante.png"));

>>>>>>> 9dd8d22856a7fd5c1a9630037843d77792ad3bc1
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
<<<<<<< HEAD
		while ( ligneCourante != null){
			toReturn += ligneCourante + " ";
=======
		while (ligneCourante != null) {
			toReturn += ligneCourante;
>>>>>>> 9dd8d22856a7fd5c1a9630037843d77792ad3bc1
			ligneCourante = br.readLine();
		}
		br.close();
		return toReturn;
	}

}
