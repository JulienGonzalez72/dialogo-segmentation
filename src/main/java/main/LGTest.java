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
import main.reading.ReadThread;
import main.view.*;

public class LGTest {
	
	public static void main(String[] args) {
		/// on créé la fenêtre d'exercice ///
		final TextFrame frame = new TextFrame(
				"Dialogo - Lecture segmentée"); // le titre
		
		/// on initalise la fenêtre avec les paramètres nécessaires à sa création ///
		frame.init(getTextFromFile("ressources/textes/Amélie la sorcière.txt"), // le texte à afficher
				3, // le premier segment à afficher
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
				ControlerText controler = new ControlerText(frame);
				
				/// initialisation des couleurs ///
				controler.setHighlightColors(Color.ORANGE, Color.PINK, Color.CYAN);
				
				/// on créé notre thread personnalisé ///
				LGThread thread = new LGThread(controler);
				
				/// on le charge dans le contrôleur pour avoir tous les contrôles dessus ///
				controler.loadReadThread(thread);
				
				/// on démarre le thread au segment 1 ///
				controler.goTo(20);
			}
		};
	}
	
	/**
	 * Ceci est notre algorithme de lecture personnalisé, il doit hériter de ReadThread et définir un constructeur identique.
	 */
	static class LGThread extends ReadThread {
		public LGThread(ControlerText controler) {
			super(controler);
		}
		public void run() {
			/// on répète l'opération jusqu'au dernier segment ou jusqu'à ce que le thread s'arrête ///
			while (N < controler.getPhrasesCount() && running) {
				/// opération de mise à jour, indispensable au début de chaque segment ///
				controler.updateCurrentPhrase();
				
				/// affichage de la page correspondant au segment actuel ///
				controler.showPage(controler.getPageOfPhrase(N));
				
				/// surlignage du segment actuel ///
				controler.highlightPhrase(N);
				
				/// on attend un clic du patient ///
				while (!controler.waitForClick(N)) {
					/// on surligne le mot en cas d'erreur ///
					controler.highlightWrongWord();
				}
				
				/// passage au prochain segment ///
				N++;
			}
		}
	}
	
	/**
	 * Retourne le contenu du fichier .txt situé à l'emplacement du paramètre.
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
