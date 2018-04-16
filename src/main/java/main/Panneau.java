package main;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;
	public static int premierSegment;
	public static int defautNBEssaisParSegment;

	// panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbEssaisParSegment = defautNBEssaisParSegment;
	public int nbEssaisRestantPourLeSegmentCourant = defautNBEssaisParSegment;
	public int nbErreurs;
	public JFrame fenetre;
	public JFrame controlFrame;
	public ControlerGlobal controlerGlobal;

	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage = new HashMap<Integer, List<Integer>>();

	/// lecteur des phrases ///
	public Player player;

	public Panneau(JFrame fenetre) throws IOException {
		this.controlerGlobal = new ControlerGlobal(this);
		this.fenetre = fenetre;
<<<<<<< HEAD
		String texteCesures = getTextFromFile("ressources/textes/" + Constants.AUDIO_FILE_NAME);
=======
		String texteCesures = getTextFromFile("ressources/textes/20 000 lieux sous les mers");
>>>>>>> 8660b5130a98fc14169630e939057d8807694a5a
		textHandler = new TextHandler(texteCesures);

		this.setLayout(new BorderLayout());
	}

	/**
	 * S'exécute lorsque le panneau s'est bien intégré à la fenêtre
	 */
	public void init() {
		pageActuelle = 0;
		// segmentActuel = FenetreParametre.premierSegment - 1;
		nbEssaisRestantPourLeSegmentCourant = nbEssaisParSegment = FenetreParametre.nbFautesTolerees;
		ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);
		editorPane = new TextPane();
		editorPane.setEditable(false);
		editorPane.addMouseListener(controlerMouse);
		this.add(editorPane, BorderLayout.CENTER);

		/// construit la mise en page virtuelle ///
		buildPages(FenetreParametre.premierSegment - 1);
		/// affiche la première page ///
		afficherPageSuivante();
		/// calcule le nombre de pages total ///
		nbPages = segmentsEnFonctionDeLaPage.size();

		/// initialise le lecteur et le démarre ///
<<<<<<< HEAD
		try {
			player = new Player(AudioSystem.getAudioInputStream(new File("ressources/sounds/")));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
=======
		player = new Player(textHandler);
		player.onPhraseEnd.add(new Runnable() {		
			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));		
			}
		});
		player.onPlay.add(new Runnable() {		
			@Override
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));		
			}
		});
>>>>>>> 8660b5130a98fc14169630e939057d8807694a5a
		player.goTo(FenetreParametre.premierSegment - 1);
		// player.play();

		controlFrame = new ControlFrame(player);
	}
	
	public AudioInputStream readSound(int )

	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
	 *
	 */
	public static String getTextFromFile(String emplacement) throws IOException {
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
	}

	/**
	 * passe a la page suivante et l'affiche
	 *
	 */
	public void afficherPageSuivante() {
		if (pageActuelle == nbPages && pageActuelle > 0) {
			afficherCompteRendu();
		} else {
			pageActuelle++;
			showPage(pageActuelle);
			editorPane.désurlignerTout();
		}
	}

	public void buildPages(int startPhrase) {
		segmentsEnFonctionDeLaPage.clear();
		float maxArea = ((getWidth() - 4 * Constants.TEXTPANE_MARGING) * (getHeight() - 4 * Constants.TEXTPANE_MARGING))
				/ editorPane.getSpacingFactor();
		int segment = startPhrase;
		int numPage = 1;
		while (segment < textHandler.getPhrasesCount()) {
			String page = "";
			List<Integer> segmentsNum = new ArrayList<Integer>();
			while (true) {
				String str = textHandler.getPhrase(segment);
				/// le dernier segment a été atteint ///
				if (segment >= textHandler.getPhrasesCount())
					break;
				/// le segment dépasse la limite ///
				if (editorPane.getTextBounds(page + str).getWidth()
						* editorPane.getTextBounds(page + str).getHeight() >= maxArea)
					break;
				/// le segment rentre dans la page, il est alors ajouté à la page ///
				else {
					page += str;
					segmentsNum.add(segment);
					segment++;
				}
			}
			segmentsEnFonctionDeLaPage.put(numPage, segmentsNum);
			numPage++;
		}
	}

	public void showPage(int page) {
		fenetre.setTitle("Lexidia - Page " + page);
		String texteAfficher = "";
		// on recupere les segments a afficher dans la page
		List<String> liste = new ArrayList<String>();
		for (Integer i : segmentsEnFonctionDeLaPage.get(pageActuelle)) {
			liste.add(textHandler.getPhrase(i));
		}
		for (String string : liste) {
			texteAfficher += string;
		}
		editorPane.setText(texteAfficher);
	}

	public boolean pageFinis() {
		// la page actuelle contient t-elle le segment suivant ? si non elle est finis
		return (!segmentsEnFonctionDeLaPage.get(pageActuelle).contains(player.getCurrentPhraseIndex() + 1))
				|| player.getCurrentPhraseIndex() + 2 == textHandler.getPhrasesCount();
	}

	public void indiquerErreur(int debut, int fin) {
		nbErreurs++;
		editorPane.enleverSurlignageRouge();
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_COLOR);
	}

	public void indiquerEtCorrigerErreur(int debut, int fin) {
		// nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		nbErreurs++;
		editorPane.indiceDernierCaractereSurligné = fin;
		// editorPane.enleverSurlignageRouge();
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_PHRASE_COLOR);
	}

	public int getNumeroPremierSegmentAffiché() {
		return segmentsEnFonctionDeLaPage.get(pageActuelle).get(0);
	}

	public void afficherCompteRendu() {
		// desactivation du controleur
		editorPane.indiceDernierCaractereSurligné = Integer.MAX_VALUE;
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			JOptionPane.showMessageDialog(this, "L'exercice est terminé." + "\n" + "Le patient a fait : " + nbErreurs
					+ " erreur" + (nbErreurs > 1 ? "s" : "") + ".", "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		fenetre.setVisible(false);
		new FenetreParametre("Dialogo", 500, 500);
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void surlignerSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(FenetreParametre.premierSegment - 1,n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, Constants.RIGHT_COLOR);
		}
	}
	
	/**
	 * Colorie tout jusqu'au segment n en couleur c
	 */
	public void surlignerJusquaSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(FenetreParametre.premierSegment - 1,n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(0, finRelativeSegment, Constants.RIGHT_COLOR);
		}
	}

}
