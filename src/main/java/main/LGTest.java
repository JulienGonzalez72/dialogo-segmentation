package main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.controler.ControlerText;
import main.view.*;

public class LGTest {
	
	public static void main(String[] args) {
		/// on créé la fenêtre d'exercice ///
		final TextFrame frame = new TextFrame(
				"Dialogo - Lecture segmentée"); // le titre
		
		/// on initalise la fenêtre avec les paramètres nécessaires à sa création ///
		frame.init(getTextFromFile("ressources/textes/Amélie la sorcière.txt"), // le texte à afficher
				1, // le premier segment à afficher
				new Font(Font.MONOSPACED, Font.BOLD, 20), // les caractéristiques de la police (nom, style, taille)
				100, // la position x de la fenêtre
				100, // la position y de la fenêtre
				500, // la largeur de la fenêtre
				500); // la hauteur de la fenêtre
		
		/// on affiche la fenêtre ///
		frame.start();
		
		/// on exécute les traitement seulement lorsque la fenêtre d'exercice s'est bien initilisée ///
		frame.onInit = new Runnable() {
			public void run() {
				/// on récupère le contrôleur ///
				ControlerText controler = frame.getControler();
				
				controler.highlightPhrase(Color.GREEN, 4);
			}
		};
	}
	
	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
	 */
	public static String getTextFromFile(String emplacement) {
		try {
			File fichierTxt = new File(emplacement);
			InputStream ips = null;
			ips = new FileInputStream(fichierTxt);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String toReturn = "";
			String ligneCourante = br.readLine();
			while (ligneCourante != null) {
				toReturn += ligneCourante + " ";
				ligneCourante = br.readLine();
			}
			br.close();
			return toReturn;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
