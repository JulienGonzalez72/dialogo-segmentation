package main;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public ControlFrame controlFrame;
	public ControlerGlobal controlerGlobal;

	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage = new HashMap<Integer, List<Integer>>();

	/// lecteur des phrases ///
	public Player player;

	public Panneau(JFrame fenetre) throws IOException {
		this.controlerGlobal = new ControlerGlobal(this);
		this.fenetre = fenetre;
		String texteCesures = getTextFromFile("ressources/textes/" + Constants.TEXT_FILE_NAME);
		/// enl�ve la consigne ///
		if (Constants.HAS_INSTRUCTIONS) {
			texteCesures = texteCesures.substring(texteCesures.indexOf("/") + 1, texteCesures.length());
		}
		textHandler = new TextHandler(texteCesures);

		this.setLayout(new BorderLayout());

		editorPane = new TextPane();
		editorPane.setEditable(false);
		add(editorPane, BorderLayout.CENTER);
		if (!FenetreParametre.modeLectureGuidee) {
			ControlerMouse controlerMouse = new ControlerMouse(this, textHandler);
			editorPane.addMouseListener(controlerMouse);
		}
	}

	/**
	 * S'ex�cute lorsque le panneau s'est bien int�gr� � la fen�tre
	 */
	public void init() {
		editorPane.setFont(FenetreParametre.police);
		pageActuelle = 0;
		// segmentActuel = FenetreParametre.premierSegment - 1;
		nbEssaisRestantPourLeSegmentCourant = nbEssaisParSegment = FenetreParametre.nbFautesTolerees;

		/// construit la mise en page virtuelle ///
		buildPagesByRoman(FenetreParametre.premierSegment - 1);
		/// affiche la premi�re page ///
		afficherPageSuivante();
		/// calcule le nombre de pages total ///
		nbPages = segmentsEnFonctionDeLaPage.size();

		/// initialise le lecteur et le d�marre ///
		player = new Player(textHandler);
		player.onBlockEnd.add(new Runnable() {
			@Override
			public void run() {
				if (!FenetreParametre.modeLectureGuidee) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					editorPane.d�surlignerTout();
				}
			}
		});
		player.onPhraseEnd.add(new Runnable() {
			@Override
			public void run() {
				/// change le curseur pour indiquer que l'utilisateur doit r�p�ter ///
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image img = tk.getImage("parler.png");
				Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "parler.png");
				setCursor(monCurseur);
			}
		});
		player.onPlay.add(new Runnable() {
			@Override
			public void run() {
				/// change le curseur pour indiquer que l'utilisateur doit �couter la phrase ///
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image img = tk.getImage("ecouter.png");
				Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), "ecouter.png");
				setCursor(monCurseur);
				if (FenetreParametre.modeLectureGuidee) {
					surlignerSegment(Constants.RIGHT_COLOR, player.getCurrentPhraseIndex());
				}
			}
		});
		player.goTo(FenetreParametre.premierSegment - 1);
		controlFrame = new ControlFrame(player, controlerGlobal);
		ControlerKey controlerKey = new ControlerKey(player);
		editorPane.addKeyListener(controlerKey);
		editorPane.requestFocus();

	}

	/**
	 * retourne le contenu du fichier .txt situ� � l'emplacement du param�tre
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
		pageActuelle++;
		showPage(pageActuelle);
		editorPane.d�surlignerTout();
	}

	public boolean hasNextPage() {
		return pageActuelle < nbPages;
	}

	public void afficherPagePrecedente() {
		if (pageActuelle > 0) {
			pageActuelle--;
			showPage(pageActuelle);
			editorPane.d�surlignerTout();
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
				/// le dernier segment a �t� atteint ///
				if (segment >= textHandler.getPhrasesCount())
					break;
				/// le segment d�passe la limite ///
				if (editorPane.getTextBounds(page + str).getWidth()
						* editorPane.getTextBounds(page + str).getHeight() >= maxArea)
					break;
				/// le segment rentre dans la page, il est alors ajout� � la page ///
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

	public void buildPagesByRoman(int startPhrase) {
		segmentsEnFonctionDeLaPage.clear();
		float m = Constants.TEXTPANE_MARGING;
		// numero de la page courante
		int page = 1;
		// numero du segment courant
		int segment = startPhrase;
		// numero de la lettre courante du segment courant
		int lettre = 0;
		List<Integer> segments = new ArrayList<>();
		String chaineSegmentCourant = "";
		String chaineLigneCourante = "";
		boolean finis = false;
		// tant qu'on a pas finis
		while (!finis) {
			// on fait ce for tant que il reste de la place pour une ligne de plus dans la
			// page
			for (int i = 1; !finis && (getHauteur("|") * 3 * (i)) < (getHeight() - m); i++) {
				// on fait ce for tant qu'il reste de la palce pour une lettre dans la ligne
				while (!finis && getLargeur(chaineLigneCourante) < (getWidth() - (m * 2))) {
					// si on a finis un segment
					if (!finis && chaineSegmentCourant.length() == textHandler.getPhrase(segment).length()) {
						// on reinitialise la chaine qui contient le segment en construction
						chaineSegmentCourant = "";
						// on ajoute le segment � la liste des segments de la page
						segments.add(segment);
						// si on a plac� tous les segments
						if (segment + 1 == textHandler.getPhrasesCount()) {
							// on a finis l'algo
							finis = true;
						}
						// on passe au segment suivant
						segment++;
						// on revient a la lettre 0
						lettre = 0;
					}
					// si on a pas finis l'algo
					if (!finis) {
						// on rajoute a la chaine du segment courant et a lla chaine de la lignecourante
						// la lettre courante du segment courant
						chaineSegmentCourant += textHandler.getPhrase(segment).charAt(lettre);
						chaineLigneCourante += textHandler.getPhrase(segment).charAt(lettre);
						// on passe � la ligne suivante
						lettre++;
					}
				}
				// on reinitialise la chaine de la ligne courante
				chaineLigneCourante = "";
				/*
				 * System.out.println(); System.out.println("Page numero : "+page);
				 * System.out.println("Ligne numero : "+(i));
				 * System.out.println("Taille totale disponible : "+(getHeight() - m));
				 * System.out.println("Taille apr�s ajout d'une nouvelle ligne : "+(getHauteur(
				 * "|") *3* (i+1))); System.out.println();
				 */
			}
			// on ajoute les segments de la page dans la page
			segmentsEnFonctionDeLaPage.put(page, segments);
			// la liste des segments courant est reinitialis�e
			segments = new ArrayList<>();
			// on passe � la page suivante
			page++;
		}
	}

	private double getLargeur(String s) {
		return editorPane.getTextBounds(s).getWidth();
	}

	private double getHauteur(String s) {
		return editorPane.getTextBounds(s).getHeight();
	}

	@SuppressWarnings("unused")
	private double getHauteurMax(String s) {
		double r = 0.0;
		for (char c : s.toCharArray()) {
			if (getHauteur(String.valueOf(c)) > r) {
				r = getHauteur(String.valueOf(c));
			}
		}
		return r;
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
		editorPane.indiceDernierCaractereSurlign� = fin;
		editorPane.surlignerPhrase(debut, fin, Constants.WRONG_PHRASE_COLOR);
	}

	public int getNumeroPremierSegmentAffich�() {
		return segmentsEnFonctionDeLaPage.get(pageActuelle).get(0);
	}

	public void afficherCompteRendu() {
		// desactivation du controleur
		editorPane.indiceDernierCaractereSurlign� = Integer.MAX_VALUE;
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			JOptionPane.showMessageDialog(this, "L'exercice est termin�." + "\n" + "Le patient a fait : " + nbErreurs
					+ " erreur" + (nbErreurs > 1 ? "s" : "") + ".", "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		fenetre.setVisible(false);
		new FenetreParametre("Dialogo", 500, 700);
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void surlignerSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(FenetreParametre.premierSegment - 1,
					n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, Constants.RIGHT_COLOR);
		}
	}

	/**
	 * Colorie tout jusqu'au segment n en couleur c
	 */
	public void surlignerJusquaSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(FenetreParametre.premierSegment - 1,
					n);
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
