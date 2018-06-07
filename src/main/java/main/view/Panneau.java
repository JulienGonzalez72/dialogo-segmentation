package main.view;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import main.Constants;
import main.controler.ControlerText;
import main.controler.Pilot;
import main.Parametres;
import main.controler.ControlerKey;
import main.controler.ControlerMouse;
import main.model.Player;
import main.model.TextHandler;
import main.reading.ReadMode;
import main.reading.ReadThread;

public class Panneau extends JPanel {

	private static final long serialVersionUID = 1L;
	public static int premierSegment;

	// panneau du texte
	public TextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbEssaisParSegment;
	public int nbEssaisRestantPourLeSegmentCourant;
	public int nbErreurs;
	public int nbErreursParSegment;
	public Fenetre fenetre;
	public ControlPanel controlPanel;
	public ControlerText controlerGlobal;
	public ControlerKey controlerKey;
	public ControlerMouse controlerMouse;
	public Pilot pilot;
	public ReadThread task;
	public Map<Integer, List<Integer>> segmentsEnFonctionDeLaPage = new HashMap<Integer, List<Integer>>();
	public Player player;
	public Parametres param;
	
	/**
	 *  Barre de progression
	 */
	public JProgressBar progressBar;

	public Panneau(Fenetre fenetre, Parametres param) throws IOException {
		this.fenetre = fenetre;
		this.controlerGlobal = new ControlerText(this);
		this.fenetre = fenetre;
		
		String textPath = "ressources/textes/" + Constants.TEXT_FILE_NAME;
		String texteCesures = null;
		try {
			texteCesures = getTextFromFile(textPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"<html><h3 align='center'>Erreur lors du chargement du texte !</h3></html>",
					"Erreur", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		/// enl�ve la consigne ///
		if (Constants.HAS_INSTRUCTIONS) {
			texteCesures = texteCesures.substring(texteCesures.indexOf("/") + 1, texteCesures.length());
		}
		textHandler = new TextHandler(texteCesures);
		System.out.println(textHandler.toString());
		try {
			Player.loadAll("ressources/sounds/" + Constants.AUDIO_FILE_NAME, Constants.AUDIO_FILE_NAME, 1, textHandler.getPhrasesCount());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"<html><h3 align='center'>Erreur lors du chargement de l'audio !</h3></html>",
					"Erreur", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		this.setLayout(new BorderLayout());
		
		player = new Player(textHandler, null);
		
		editorPane = new TextPane();
		editorPane.setEditable(false);
		add(editorPane, BorderLayout.CENTER);
		
		progressBar = new JProgressBar(0, (textHandler.getPhrasesCount()-1));
		progressBar.setStringPainted(true);
		//progressBar.setForeground(Constants.RIGHT_COLOR);
		add(progressBar, BorderLayout.SOUTH);
	}

	/**
	 * S'ex�cute lorsque le panneau s'est bien int�gr� � la fen�tre.
	 */
	public void init(Parametres param) {
		setParameters(param);
		
		progressBar.setValue(param.startingPhrase);
		progressBar.setString(param.startingPhrase+"/"+(textHandler.getPhrasesCount()-1));
		
		pageActuelle = 0;
		
		/// construit la mise en page virtuelle ///
		rebuildPages();
		/// initialise le lecteur ///
		player.load(param.startingPhrase - 1);
		
		this.pilot = new Pilot(this);
		
		controlerKey = new ControlerKey(pilot);
		editorPane.addKeyListener(controlerKey);
		controlerMouse = new ControlerMouse(this, textHandler);
		editorPane.addMouseListener(controlerMouse);
		editorPane.requestFocus();
	}
	
	public void setParameters(Parametres param) {
		this.param = editorPane.param = param;
		premierSegment = param.startingPhrase;
		
		editorPane.setBackground(param.bgColor);
		editorPane.setFont(param.police);
		nbEssaisRestantPourLeSegmentCourant = nbEssaisParSegment = param.nbFautesTolerees;
		player.setParameters(param);
	}
	
	public void setCursor(String fileName) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage("ressources/images/" + fileName);
		Cursor monCurseur = tk.createCustomCursor(img, new Point(16, 16), fileName);
		setCursor(monCurseur);
	}

	public String getCursorName() {
		return getCursor().getName();
	}

	/**
	 * retourne le contenu du fichier .txt situ� � l'emplacement du param�tre
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
		editorPane.d�surlignerTout();
		if ((param.readMode == ReadMode.GUIDEE || param.readMode == ReadMode.ANTICIPE)
				&& (controlerGlobal != null && player != null)) {
			controlerGlobal.highlightPhrase(param.rightColor, player.getCurrentPhraseIndex());
		}
	}

	/**
	 * Construit les pages et affiche la premi�re.
	 */
	public void rebuildPages() {
		buildPages(param.startingPhrase - 1);
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
			editorPane.d�surlignerTout();
		}
	}

	/**
	 * Construit les pages
	 */
	public void buildPages(int startPhrase) {
		segmentsEnFonctionDeLaPage.clear();
		editorPane.d�surlignerTout();
		String text = textHandler.getShowText();
		int lastOffset = 0;
		int page = 1;
		int lastPhrase = startPhrase - 1;
		while (lastPhrase < textHandler.getPhrasesCount()) {
			List<Integer> phrases = new ArrayList<>();
			editorPane.setText(text);
			int h = 0;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				h = editorPane.modelToView(0).height;
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
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
			/// derni�re page ///
			if (newText.equals(text)) {
				if (!segmentsEnFonctionDeLaPage.get(page - 1).contains(textHandler.getPhraseIndex(off))) {
					segmentsEnFonctionDeLaPage.get(page - 1).add(textHandler.getPhraseIndex(off));
				}
				break;
			} else {
				text = newText;
			}
		}
	}

	public void showPage(int page) {
		/// on ne fait rien si on est d�j� sur cette page ///
		if (pageActuelle == page) {
			return;
		}
		pageActuelle = page;
		//on met a jour le titre de la fenetre
		String temp = param.readMode+"";
		temp = temp.toLowerCase();
		char[] c = temp.toCharArray();
		c[0] = Character.toUpperCase(c[0]);
		temp = String.copyValueOf(c);
		fenetre.setTitle("Lexidia - "+temp+" - Page " + page);
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
		editorPane.surlignerPhrase(debut, fin, param.wrongColor);
	}

	public int getNumeroPremierSegmentAffich�() {
		return segmentsEnFonctionDeLaPage.get(pageActuelle).get(0);
	}

	public void afficherCompteRendu() {
		//met a jour la barre de progression
		progressBar.setValue(textHandler.getPhrasesCount()-1);
		progressBar.setString((textHandler.getPhrasesCount()-1)+"/"+(textHandler.getPhrasesCount()-1));
		
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			String message = null;
			switch (param.readMode) {
			case SEGMENTE:
			case SUIVI:
				message = "L'exercice est termin�." + "\n" + "Le patient a fait " + nbErreurs + " erreur"
						+ (nbErreurs > 1 ? "s" : "") + " de clic.\n" + "Le patient a fait " + nbErreursParSegment
						+ " erreur" + (nbErreursParSegment > 1 ? "s" : "") + " de segment.";
				break;
			case ANTICIPE:
			case GUIDEE:
				message = "L'exercice est termin�.";
			default:
				break;
			}
			JOptionPane.showMessageDialog(this, message, "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		fenetre.setResizable(true);
	}

	/**
	 * Colorie tout jusqu'au segment n en couleur c
	 */
	public void surlignerJusquaSegment(Color c, int n) {
		if (textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(getNumeroPremierSegmentAffich�(), n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(0, finRelativeSegment, param.rightColor);
		}
	}

	/**
	 * Retourne la longueur du segment n
	 */
	public int getPagesLength(int n) {
		int start = segmentsEnFonctionDeLaPage.get(n).get(0);
		int fin = segmentsEnFonctionDeLaPage.get(n).get(segmentsEnFonctionDeLaPage.get(n).size() - 1);
		return textHandler.getPhrasesLength(start, fin);
	}

}
