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

public class LSTest {

	public static void main(String[] args) {
		/// initialisation du système de log local ///
		System.setProperty("org.apache.commons.logging.simplelog.logFile", "System.out");
		System.setProperty("org.apache.commons.logging.simplelog.levelInBrackets", "true");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		
		/// on créé la fenetre d'exercice ///
		final SegmentedTextFrame frame = new SegmentedTextFrame("Dialogo - Lecture segmentée"); // le titre
		
		/// on initalise la fenetre avec les parametres necessaires a sa creation ///
		frame.init(getTextFromFile("resources/textes/Amélie la sorcière.txt"), // le texte a  afficher
				0, // le premier segment a  afficher
				new Font(Font.MONOSPACED, Font.BOLD, 22), // les caracteristiques de la police (nom, style, taille)
				100, // la position x de la fenetre (en pixels)
				100, // la position y de la fenetre (en pixels)
				12, // la largeur de la fenetre (en cm)
				12); // la hauteur de la fenetre (en cm)
		
		/// on spécifie à la fenêtre d'elle termine le processus lorsqu'elle est fermée ///
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/// on affiche la fenetre ///
		frame.start();

		/// on execute les traitements seulement lorsque la fenetre d'exercice s'est
		/// bien initilisee ///
		frame.setOnInit(new Runnable() {
			public void run() {
				/// on recupere le contreleur ///
				final ControllerText controler = new ControllerText(frame);

				/// initialisation des couleurs ///
				controler.setHighlightColors(Color.GREEN, Color.RED, Color.CYAN);
				
				/// régle l'espacement entre les lignes ///
				controler.setLineSpacing(0.5f);
				
				/// initialisation du nombre d'essais par segment ///
				controler.setPhraseTrials(3);

				/// active les contrôles clavier ///
				controler.setKeyEnabled(true);

				/// on cree une usine de lecture qui va instancier notre thread personnalise ///
				controler.setReaderFactory(new ReaderFactory() {
					public ReadThread createReadThread() {
						return new LSThread(controler);
					}
				});

				/// on demarre le thread au premier segment ///
				controler.goTo(0);
			}
		});
	}

	/**
	 * Ceci est notre algorithme de lecture personnalise, il doit heriter de
	 * ReadThread et definir un constructeur identique.
	 */
	static class LSThread extends ReadThread {
		public LSThread(ControllerText controler) {
			super(controler);
		}

		public void run() {
			/// on repete l'operation jusqu'au dernier segment ou jusqu'a  ce que le thread
			/// s'arrete ///
			while (getN() < getControler().getPhrasesCount() && isRunning()) {
				/// operation de mise a jour, indispensable au debut de chaque segment ///
				getControler().updateCurrentPhrase();

				/// affichage de la page correspondant au segment actuel ///
				getControler().showPage(getControler().getPageOfPhrase(getN()));

				/// on enleve le surlignage existant ///
				getControler().removeAllHighlights();

				/// on attend un clic du patient ///
				while (!getControler().waitForClick(getN()) && isRunning()) {
					/// on comptabilise une erreur ///
					getControler().countError();

					/// on surligne le mauvais mot ///
					getControler().highlightWrongWord();

					/// lorsque le patient n'a plus d'essais restants ///
					if (!getControler().hasMoreTrials()) {
						/// surlignage du segment actuel ///
						getControler().highlightCorrectionPhrase(getN());
					}
				}

				/// passage au prochain segment ///
				setN(getN() + 1);
			}
		}
	}

	/**
	 * Retourne le contenu du fichier .txt situe a  l'emplacement du parametre.
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
				toReturn += ligneCourante + "\n";
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
