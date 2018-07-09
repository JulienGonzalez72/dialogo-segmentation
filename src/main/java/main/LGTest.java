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
import main.reading.ReaderFactory;
import main.view.TextFrame;

public class LGTest {

	public static void main(String[] args) {
		/// on créé la fenêtre d'exercice ///
		final TextFrame frame = new TextFrame("Dialogo - Lecture segmentée"); // le titre

		/// on initalise la fenêtre avec les paramètres nécessaires à sa création ///
		frame.init(getTextFromFile("src/main/resources/textes/Am�lie la sorci�re.txt"), // le texte à afficher
				0, // le premier segment à afficher
				new Font(Font.MONOSPACED, Font.BOLD, 20), // les caractéristiques de la police (nom, style, taille)
				100, // la position x de la fenêtre
				100, // la position y de la fenêtre
				500, // la largeur de la fenêtre
				500); // la hauteur de la fenêtre

		/// on affiche la fenêtre ///
		frame.start();

		/// on exécute les traitements seulement lorsque la fenêtre d'exercice s'est
		/// bien initilisée ///
		frame.onInit = new Runnable() {
			public void run() {
				/// on récupère le contrôleur ///
				final ControlerText controler = new ControlerText(frame);

				/// initialisation des couleurs ///
				controler.setHighlightColors(Color.GREEN, Color.RED, Color.CYAN);

				/// initialisation du nombre d'essais par segment ///
				controler.setPhraseTrials(3);

				/// active les contrôles clavier ///
				controler.setKeyEnabled(true);

				/// on créé une usine de lecture qui va instancier notre thread personnalisé ///
				controler.setReaderFactory(new ReaderFactory() {
					public ReadThread createReadThread() {
						return new LGThread(controler);
					}
				});

				/// on démarre le thread au premier segment ///
				controler.goTo(0);
			}
		};
	}

	/**
	 * Ceci est notre algorithme de lecture personnalisé, il doit hériter de
	 * ReadThread et définir un constructeur identique.
	 */
	static class LGThread extends ReadThread {
		public LGThread(ControlerText controler) {
			super(controler);
		}

		public void run() {
			/// on répète l'opération jusqu'au dernier segment ou jusqu'à ce que le thread
			/// s'arrête ///
			while (N < controler.getPhrasesCount() && running) {
				/// opération de mise à jour, indispensable au début de chaque segment ///
				controler.updateCurrentPhrase();

				/// affichage de la page correspondant au segment actuel ///
				controler.showPage(controler.getPageOfPhrase(N));

				/// on enlève le surlignage existant ///
				controler.removeAllHighlights();

				/// on attend un clic du patient ///
				while (!controler.waitForClick(N) && running) {
					/// on comptabilise une erreur ///
					controler.countError();

					/// on surligne le mauvais mot ///
					controler.highlightWrongWord();

					/// lorsque le patient n'a plus d'essais restants ///
					if (!controler.hasMoreTrials()) {
						/// surlignage du segment actuel ///
						controler.highlightCorrectionPhrase(N);
					}
				}

				/// passage au prochain segment ///
				N++;
			}
		}
	}

	/**
	 * Retourne le contenu du fichier .txt situé à l'emplacement du paramètre.
	 */
	private static String getTextFromFile(String emplacement) {
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
