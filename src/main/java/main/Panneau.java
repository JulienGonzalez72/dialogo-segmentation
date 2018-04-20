package main;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.text.BadLocationException;

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
	public ControlFrame controlFrame;
	public ControlerGlobal controlerGlobal;
	public ControlerKey controlerKey;
	public ControlerMouse controlerMouse;

	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage = new HashMap<Integer, List<Integer>>();

	/// lecteur des phrases ///
	public Player player;

	public Panneau(JFrame fenetre) throws IOException {
		this.controlerGlobal = new ControlerGlobal(this);
		this.fenetre = fenetre;
		String texteCesures = getTextFromFile("ressources/textes/" + Constants.TEXT_FILE_NAME);
		/// enlève la consigne ///
		if (Constants.HAS_INSTRUCTIONS) {
			texteCesures = texteCesures.substring(texteCesures.indexOf("/") + 1, texteCesures.length());
		}
		textHandler = new TextHandler(texteCesures);

		this.setLayout(new BorderLayout());

		editorPane = new TextPane();
		editorPane.setEditable(false);
		add(editorPane, BorderLayout.CENTER);
	}

	/**
	 * S'exécute lorsque le panneau s'est bien intégré à la fenêtre
	 */
	public void init() {
		editorPane.setBackground(FenetreParametre.couleurFond);
		editorPane.setFont(FenetreParametre.police);
		pageActuelle = 0;
		nbEssaisRestantPourLeSegmentCourant = nbEssaisParSegment = FenetreParametre.nbFautesTolerees;

		/// construit la mise en page virtuelle ///
		//rebuildPages();

		/// initialise le lecteur et le démarre ///
		player = new Player(textHandler);
		/*if (FenetreParametre.readMode == ReadMode.ANTICIPATED) {
			player.waitAfter = false;
		}
		player.onPreviousPhrase.add(() -> {

		});
		player.onBlockEnd.add(() -> {
			controlFrame.enableAll();
			
			if (FenetreParametre.readMode != ReadMode.GUIDED_READING
					&& FenetreParametre.readMode != ReadMode.ANTICIPATED) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			/// en mode lecture guidée, passe directement au segment suivant ///
			else if (FenetreParametre.readMode == ReadMode.GUIDED_READING) {
				controlerGlobal.doNext();
			}
			/// en mode lecture anticipée, joue l'enregistrement après le temps de pause ///
			else if (FenetreParametre.readMode == ReadMode.ANTICIPATED) {
				player.play();
			}
		});
		player.onPhraseEnd.add(() -> {
			/// en mode lecture anticipée, passe au segment suivant ///
			if (FenetreParametre.readMode == ReadMode.ANTICIPATED) {
				controlerGlobal.doNext();
			}
		});
		player.onPlay.add(() -> {
			/// empêche le redimensionnement lors de la première lecture ///
			fenetre.setResizable(false);

			/// change le curseur pour indiquer que l'utilisateur doit écouter la phrase ///
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = tk.getImage("ecouter.png");
			Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "ecouter.png");
			setCursor(monCurseur);

			if (FenetreParametre.readMode == ReadMode.GUIDED_READING
					|| FenetreParametre.readMode == ReadMode.ANTICIPATED) {
				editorPane.désurlignerTout();
				controlerGlobal.highlightPhrase(Constants.RIGHT_COLOR, player.getCurrentPhraseIndex());
			}

			controlerGlobal.sauvegarder();

			controlFrame.goToField.setText(String.valueOf(player.getCurrentPhraseIndex() + 1));
		});
		player.onWait.add(() -> {
			/// change le curseur pour indiquer que l'utilisateur doit répéter ///
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = tk.getImage("parler.png");
			Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "parler.png");
			setCursor(monCurseur);
			
			controlFrame.disableAll();
		});
		player.goTo(FenetreParametre.premierSegment - 1);*/
		Thread t = new SegmentedThread(controlerGlobal);
		t.start();
		
		controlFrame = new ControlFrame(this);
		controlerKey = new ControlerKey(player);
		editorPane.addKeyListener(controlerKey);
		controlerMouse = new ControlerMouse(this, textHandler);
		editorPane.addMouseListener(controlerMouse);
		editorPane.requestFocus();
	}
	
	public void setCursor(String fileName) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(fileName);
		Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), fileName);
		setCursor(monCurseur);
	}

	/**
	 * retourne le contenu du fichier .txt situé à l'emplacement du paramètre
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
		showPage(pageActuelle + 1);
		editorPane.désurlignerTout();
		if ((FenetreParametre.readMode == ReadMode.GUIDED_READING || FenetreParametre.readMode == ReadMode.ANTICIPATED)
				&& (controlerGlobal != null && player != null)) {
			controlerGlobal.highlightPhrase(Constants.RIGHT_COLOR, player.getCurrentPhraseIndex());
		}
	}

	/**
	 * Construit les pages et affiche la première.
	 */
	public void rebuildPages() {
		buildPages(FenetreParametre.premierSegment - 1);
		pageActuelle = 0;
		afficherPageSuivante();
		/// calcule le nombre de pages total ///
		nbPages = segmentsEnFonctionDeLaPage.size();
	}

	public boolean hasNextPage() {
		return pageActuelle < nbPages;
	}

	public void afficherPagePrecedente() {
		if (pageActuelle > 0) {
			showPage(pageActuelle - 1);
			editorPane.désurlignerTout();
		}
	}

	public void buildPages(int startPhrase) {
		segmentsEnFonctionDeLaPage.clear();
		String text = textHandler.getShowText();
		int lastOffset = 0;
		int page = 1;
		int lastPhrase = startPhrase - 1;
		while (lastPhrase < textHandler.getPhrasesCount()) {
			List<Integer> phrases = new ArrayList<>();
			editorPane.setText(text);
			int h = 0;
			try {
				h = editorPane.modelToView(0).height;
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			int off = textHandler.getAbsoluteOffset(lastPhrase,
					editorPane.viewToModel(new Point((int) (editorPane.getWidth() - Constants.TEXTPANE_MARGING),
							(int) (editorPane.getHeight() - h))));
			for (int i = lastOffset; i < off; i++) {
				int phraseIndex = textHandler.getPhraseIndex(i);
				if (phraseIndex == -1) {
					lastOffset = textHandler.getShowText().length();
				}
				if (!phrases.contains(phraseIndex) && phraseIndex > lastPhrase
						&& phraseIndex != textHandler.getPhraseIndex(off)) {
					lastPhrase = phraseIndex;
					phrases.add(phraseIndex);
					lastOffset = i;
				}
			}
			if (!phrases.isEmpty()) {
				segmentsEnFonctionDeLaPage.put(page, phrases);
				page++;
			}
			String newText = textHandler.getShowText().substring(lastOffset);
			/// dernière page ///
			if (newText.equals(text)) {
				if ( !segmentsEnFonctionDeLaPage.get(page - 1).contains(textHandler.getPhraseIndex(off))) {
					segmentsEnFonctionDeLaPage.get(page - 1).add(textHandler.getPhraseIndex(off));
				}
				break;
			} else {
				text = newText;
			}
		}
	}

	public void showPage(int page) {
		/// on ne fait rien si on est déjà sur cette page ///
		if (pageActuelle == page) {
			return;
		}
		pageActuelle = page;
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
		// nbErreurs++;
		editorPane.indiceDernierCaractereSurligné = fin;
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_PHRASE_COLOR);
		player.repeat();
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
			String message = null;
			switch (FenetreParametre.readMode) {
			case NORMAL:
			case HIGHLIGHT:
				message = "L'exercice est terminé." + "\n" + "Le patient a fait : " + nbErreurs + " erreur"
						+ (nbErreurs > 1 ? "s" : "") + ".";
				break;
			case ANTICIPATED:
			case GUIDED_READING:
				message = "L'exercice est terminé.";
			default:
				break;
			}
			JOptionPane.showMessageDialog(this, message, "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		fenetre.setVisible(false);
		Point p = new Point(50,50);
		new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam, Constants.hauteurFenetreParam).setLocation(p);;
	}

	/**
	 * Colorie tout jusqu'au segment n en couleur c
	 */
	public void surlignerJusquaSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(getNumeroPremierSegmentAffiché(), n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(0, finRelativeSegment, Constants.RIGHT_COLOR);
		}
	}

	public int getPagesLength(int n) {
		int start = segmentsEnFonctionDeLaPage.get(n).get(0);
		int fin = segmentsEnFonctionDeLaPage.get(n).get(segmentsEnFonctionDeLaPage.get(n).size() - 1);
		return textHandler.getPhrasesLength(start, fin);
	}

}
