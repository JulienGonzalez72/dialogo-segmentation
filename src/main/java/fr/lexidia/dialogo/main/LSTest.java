package fr.lexidia.dialogo.main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.alee.laf.WebLookAndFeel;

import fr.lexidia.dialogo.controller.ControllerText;
import fr.lexidia.dialogo.reading.ReadThread;
import fr.lexidia.dialogo.reading.ReaderFactory;
import fr.lexidia.dialogo.view.SegmentedTextFrame;

public class LSTest {

	///
	/// Constantes de test
	///

	public static final boolean START_EXERCICE = false;
	public static final boolean WEBLAF = false;
	public static final boolean TEST_FRAME = true;
	public static final boolean WRAPPED_TEXT = false;
	public static final int MAX_PHRASES_BY_PAGE = 0;

	private static TestFrame tf;

	public static void main(final String[] args) {
		/// initialisation du système de log local ///
		System.setProperty("org.apache.commons.logging.simplelog.logFile", "System.out");
		System.setProperty("org.apache.commons.logging.simplelog.levelInBrackets", "true");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");

		/// installe le look & feel WebLaF ///
		if (WEBLAF) {
			WebLookAndFeel.install();
			UIManager.put("TextPaneUI", javax.swing.plaf.basic.BasicTextPaneUI.class.getCanonicalName());
		}
		/// ou le look & feel de Windows ///
		else {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		/// on créé la fenetre d'exercice ///
		final SegmentedTextFrame frame = new SegmentedTextFrame("Dialogo - Lecture segmentée"); // le titre

		String file = "resources/textes/Amélie la sorcière" + (WRAPPED_TEXT ? "" : "_oneline") + ".txt";
		//String file = "resources/textes/20 000 lieux sous les mers";	
		/// on initalise la fenetre avec les parametres necessaires a sa creation ///
		frame.init(getTextFromFile(file), // le texte a afficher
				0, // le premier segment à afficher
				new Font(Font.DIALOG, Font.PLAIN, args.length > 0 ? Integer.parseInt(args[0]) : 20), // les
																										// caracteristiques
																										// de la police
																										// (nom, style,
																										// taille)
				100, // la position x de la fenetre (en pixels)
				100, // la position y de la fenetre (en pixels)
				20.25f, // la largeur de la fenetre (en cm)
				9f); // la hauteur de la fenetre (en cm)

		/// on spécifie à la fenêtre qu'elle termine le processus lorsqu'elle est fermée
		/// ///
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/// met la fenêtre en plein écran ///
		frame.setFullScreen();

		/// on affiche la fenetre ///
		frame.start();

		/// on execute les traitements seulement lorsque la fenetre d'exercice s'est
		/// bien initilisee ///
		frame.setOnInit(new Runnable() {
			public void run() {
				/// on recupere le contrôleur ///
				final ControllerText controler = new ControllerText(frame);

				if (TEST_FRAME) {
					tf = new TestFrame(controler);
				}

				// controler.setMargin(500, 500, 200, 200);

				/// initialisation des couleurs ///
				controler.setHighlightColors(Color.GREEN, Color.RED, Color.CYAN);

				/// règle l'espacement entre les lignes ///
				controler.setLineSpacing(args.length > 1 ? Float.parseFloat(args[1]) : 0.2f);

				/// initialisation du nombre d'essais par segment ///
				controler.setPhraseTrials(3);

				/// nombre maximal de segments par page ///
				controler.setMaxPhrasesByPage(MAX_PHRASES_BY_PAGE);

				// controler.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 20));

				/// active les contrôles clavier ///
				controler.setKeyEnabled(true);

				/// on cree une usine de lecture qui va instancier notre thread personnalise ///
				controler.setReaderFactory(new ReaderFactory() {
					public ReadThread createReadThread() {
						return new LSThread(controler);
					}
				});

				/// on demarre le thread au premier segment ///
				if (START_EXERCICE) {
					controler.goTo(0);
				}
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

				/// on surligne tous les segments passes (si cette option est activee) ///
				if (tf == null || tf.highlightFromStart()) {
					getControler().highlightUntilPhrase(Color.GREEN, getN() - 1);
				}

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
