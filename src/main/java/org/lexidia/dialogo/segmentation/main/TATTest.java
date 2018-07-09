package org.lexidia.dialogo.segmentation.main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lexidia.dialogo.segmentation.controller.ControllerText;
import org.lexidia.dialogo.segmentation.reading.ReadThread;
import org.lexidia.dialogo.segmentation.reading.ReaderFactory;
import org.lexidia.dialogo.segmentation.view.SegmentedTextFrame;

public class TATTest {

	public static void main(String[] args) {
		/// on cree la fenetre d'exercice ///
		final SegmentedTextFrame frame = new SegmentedTextFrame("Dialogo - Lecture segmentee"); // le titre

		/// on initalise la fenetre avec les parametres necessaires a sa creation ///
		frame.init(getTextFromFile("src/main/resources/textes/Amélie la sorcière.txt"), // le texte a afficher
				0, // le premier segment a afficher
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
				final ControllerText controler = new ControllerText(frame);

				/// initialisation des couleurs ///
				controler.setHighlightColors(Color.GREEN, Color.RED, Color.CYAN);

				/// initialisation du nombre d'essais par segment ///
				controler.setPhraseTrials(3);

				/// active les contrôles clavier ///
				controler.setKeyEnabled(true);
				
				/// désactive le mode fenêtre fixe ///
				controler.setModeFixedField(false);

				/// on créé une usine de lecture qui va instancier notre thread personnalisé ///
				controler.setReaderFactory(new ReaderFactory() {
					public ReadThread createReadThread() {
						return new TATThread(controler);
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
	static class TATThread extends ReadThread {
		public TATThread(ControllerText controler) {
			super(controler);
		}

		public void run() {

			/// on répète l'opération jusqu'au dernier segment ou jusqu'a ce que le thread
			/// s'arrête ///
			while (N < controler.getPhrasesCount() && running) {
				/// opération de mise a jour, indispensable au début de chaque segment ///
				controler.updateCurrentPhrase();

				/// affichage de la page correspondant au segment actuel ///
				controler.showPage(controler.getPageOfPhrase(N));

				/// on enlève le surlignage existant ///
				controler.removeAllHighlights();

				// on recupere le numero du trou courant//
				int h;
				if (controler.getHolesCount(N) == 0) {
					h = -1;
				} else {
					h = controler.getFirstHole(N);
				}
				// on recupère le premier trou du segment //
				int firstHole = h;

				if (h != -1) {
					/// valide tous les trous de la page avant le trou actuel ///
					for (int i = 0; i < h; i++) {
						if (controler.getPageOf(i) == controler.getPageOf(h)) {
							controler.fillHole(i);
						}
					}
				}

				// tant qu'il reste un trou dans le segment
				while (h > -1 && h < (controler.getHolesCount(N) + firstHole)) {
					// on montre uniquement les trous Ã  partir du trou actuel et de cette page
					controler.showHolesInPage(h);

					/*******************************
					 * EXEMPLE POUR FENETRE FIXE
					 ******************************/

					// tant que la saisie n'est pas juste
					
					  //while (!controler.waitForFillFixedFrame(h)) { controler.doError(h); }
					 

					/**********************************
					 * EXEMPLE POUR FENETRE NON FIXE
					 **********************************/

					// tant que la saisie n'est pas juste
					while (!controler.waitForFill(h)) {
						controler.doError(h);
					}

					// remplacer le trou par le mot correspondant et replacer tous les masques
					controler.replaceMaskByWord(h);
					/// passage au trou suivant
					h++;
				}

				/// passage au prochain segment ///
				N++;

				controler.setHint(-1);
			}
		}
	}

	/**
	 * Retourne le contenu du fichier .txt situé Ã  l'emplacement du paramètre.
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

