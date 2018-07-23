package org.lexidia.dialogo.segmentation.main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;

import org.lexidia.dialogo.segmentation.controller.ControllerText;
import org.lexidia.dialogo.segmentation.reading.ReadThread;
import org.lexidia.dialogo.segmentation.reading.ReaderFactory;
import org.lexidia.dialogo.segmentation.view.SegmentedTextFrame;

public class TATTest {

	public static void main(String[] args) {
		/// initialisation du système de log local ///
		System.setProperty("org.apache.commons.logging.simplelog.logFile", "System.out");
		System.setProperty("org.apache.commons.logging.simplelog.levelInBrackets", "true");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		
		/// on cree la fenetre d'exercice ///
		final SegmentedTextFrame frame = new SegmentedTextFrame("Dialogo - Lecture segmentee"); // le titre

		/// on initalise la fenetre avec les parametres necessaires a sa creation ///
		frame.init(getTextFromFile("resources/textes/Amélie la sorcière.txt"), // le texte a afficher
				0, // le premier segment a afficher
				new Font(Font.MONOSPACED, Font.BOLD, 20), // les caractéristiques de la police (nom, style, taille)
				100, // la position x de la fenetre (en pixels)
				100, // la position y de la fenetre (en pixels)
				12, // la largeur de la fenetre (en cm)
				12); // la hauteur de la fenetre (en cm)
		
		/// on spécifie à la fenêtre d'elle termine le processus lorsqu'elle est fermée ///
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/// on affiche la fenêtre ///
		frame.start();

		/// on exécute les traitements seulement lorsque la fenêtre d'exercice s'est
		/// bien initilisée ///
		frame.setOnInit(new Runnable() {
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
		});
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
			while (getN() < getControler().getPhrasesCount() && isRunning()) {
				/// opération de mise a jour, indispensable au début de chaque segment ///
				getControler().updateCurrentPhrase();

				/// affichage de la page correspondant au segment actuel ///
				getControler().showPage(getControler().getPageOfPhrase(getN()));

				/// on enlève le surlignage existant ///
				getControler().removeAllHighlights();

				// on recupere le numero du trou courant//
				int h;
				if (getControler().getHolesCount(getN()) == 0) {
					h = -1;
				} else {
					h = getControler().getFirstHole(getN());
				}
				// on recupère le premier trou du segment //
				int firstHole = h;

				if (h != -1) {
					/// valide tous les trous de la page avant le trou actuel ///
					for (int i = 0; i < h; i++) {
						if (getControler().getPageOf(i) == getControler().getPageOf(h)) {
							getControler().fillHole(i);
						}
					}
				}

				// tant qu'il reste un trou dans le segment
				while (h > -1 && h < (getControler().getHolesCount(getN()) + firstHole)) {
					// on montre uniquement les trous Ã  partir du trou actuel et de cette page
					getControler().showHolesInPage(h);

					/*******************************
					 * EXEMPLE POUR FENETRE FIXE
					 ******************************/

					//tant que la saisie n'est pas juste
					
					/*while (!getControler().waitForFillFixedFrame(h)) {
						getControler().doError(h);
					}*/
					 

					/**********************************
					 * EXEMPLE POUR FENETRE NON FIXE
					 **********************************/

					// tant que la saisie n'est pas juste
					while (!getControler().waitForFill(h)) {
						getControler().doError(h);
					}

					// remplacer le trou par le mot correspondant et replacer tous les masques
					getControler().replaceMaskByWord(h);
					/// passage au trou suivant
					h++;
				}

				/// passage au prochain segment ///
				setN(getN() + 1);

				getControler().setHint(-1);
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

