package org.lexidia.dialogo.segmentation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import org.lexidia.dialogo.segmentation.controller.ControllerMask;
import org.lexidia.dialogo.segmentation.controller.ControllerMouse;
import org.lexidia.dialogo.segmentation.main.Constants;
import org.lexidia.dialogo.segmentation.model.Player;
import org.lexidia.dialogo.segmentation.model.ReadingParameters;
import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.model.ToolParameters;
import org.lexidia.dialogo.segmentation.reading.ReadThread;

public class SegmentedTextPanel extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	public static int premierSegment;

	// panneau du texte
	public SegmentedTextPane editorPane;
	public TextHandler textHandler;
	public int pageActuelle;
	public int nbPages;
	public int nbEssaisParSegment;
	public int nbEssaisRestantPourLeSegmentCourant;
	/**
	 * Nombre d'erreurs total.
	 */
	public int nbErreurs;
	/**
	 * Nombre de segments pour lesquels le patient a atteint le nombre limite
	 * d'essais possibles.
	 */
	public int nbErreursParSegment;
	/**
	 * Nombre d'erreurs pour le segment courant.
	 */
	public int nbErreursSegmentCourant;
	public SegmentedTextFrame fenetre;
	public ControlPanel controlPanel;
	public ControllerMouse controlerMouse;
	public ReadThread task;
	public Map<Integer, List<Integer>> phrasesInFonctionOfPages = new HashMap<Integer, List<Integer>>();
	public Player player;
	public ToolParameters param;
	public ReadingParameters rParam = new ReadingParameters();

	/**
	 * Barre de progression
	 */
	public JProgressBar progressBar;

	/*
	 * ///////////////////////////////////////////////////////////// Ci dessous les
	 * attributs utilis�s pour la gestion des trous
	 */////////////////////////////////////////////////////////////

	public ControllerMask controlerMask;
	public List<Mask> maskFrame = new ArrayList<>();
	public JPanel panelSud = new JPanel();
	public JDesktopPane panelFixedFrame = null;
	public Mask fixedFrame;

	public SegmentedTextPanel(SegmentedTextFrame fenetre) throws IOException {
		controlerMask = new ControllerMask();
		this.fenetre = fenetre;
		this.setLayout(new BorderLayout());

		editorPane = new SegmentedTextPane();
		editorPane.setEditable(false);
		add(editorPane, BorderLayout.CENTER);

		progressBar = new JProgressBar(0, 0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.GREEN);

		add(panelSud, BorderLayout.SOUTH);

	}

	/**
	 * S'ex�cute lorsque le panneau s'est bien int�gr� � la fen�tre.
	 */
	public void init(ToolParameters param) {
		setParameters(param);

		textHandler = new TextHandler(param.text);
		progressBar.setMaximum(textHandler.getPhrasesCount());

		progressBar.setValue(param.startingPhrase);
		progressBar.setString(param.startingPhrase + "/" + (textHandler.getPhrasesCount() - 1));
		pageActuelle = 0;

		/// construit la mise en page virtuelle ///
		rebuildPages();
		/// initialise le lecteur ///
		controlerMouse = new ControllerMouse(this, textHandler);
		editorPane.addMouseListener(controlerMouse);
		editorPane.requestFocus();

		if (fenetre.onInit != null) {
			fenetre.onInit.run();
		}

		if (rParam.fixedField) {
			panelSud.setLayout(new BorderLayout());
			panelFixedFrame = new JDesktopPane();
			panelFixedFrame.setPreferredSize(new Dimension(fenetre.getWidth(), param.font.getSize()));
			panelSud.add(panelFixedFrame);
			panelSud.add(progressBar, BorderLayout.SOUTH);
		} else {
			panelSud.setLayout(new GridLayout(1, 1));
			panelSud.add(progressBar);
		}
		panelSud.setVisible(true);
	}

	public void setParameters(ToolParameters param) {
		this.param = editorPane.param = param;
		premierSegment = param.startingPhrase;

		editorPane.setBackground(param.bgColor);
		editorPane.setFont(param.font);
		nbEssaisRestantPourLeSegmentCourant = nbEssaisParSegment = 0;
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
	 * Retourne le contenu du fichier .txt situ� � l'emplacement du param�tre.
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
		/*
		 * editorPane.d�surlignerTout(); if ((param.readMode == ReadMode.GUIDEE ||
		 * param.readMode == ReadMode.ANTICIPE) && (controlerGlobal != null && player !=
		 * null)) { controlerGlobal.highlightPhrase(param.rightColor,
		 * player.getCurrentPhraseIndex()); }
		 */
	}

	/**
	 * Construit les pages et affiche la premi�re.
	 */
	public void rebuildPages() {
		buildPages(param.startingPhrase);
		pageActuelle = 0;
		afficherPageSuivante();
		/// calcule le nombre de pages total ///
		nbPages = phrasesInFonctionOfPages.size();
	}

	public boolean hasNextPage() {
		return pageActuelle < nbPages;
	}

	public void afficherPagePrecedente() {
		if (pageActuelle > 0) {
			showPage(pageActuelle - 1);
			editorPane.removeAllHighlights();
		}
	}

	/**
	 * Construit la mise en page du texte.
	 */
	public void buildPages(int startPhrase) {
		phrasesInFonctionOfPages.clear();
		editorPane.removeAllHighlights();
		/// r�cup�re le texte entier � afficher ///
		String text = textHandler.getShowText();
		int lastOffset = 0;
		int page = 1;
		int lastPhrase = startPhrase - 1;
		while (lastPhrase < textHandler.getPhrasesCount()) {
			List<Integer> phrases = new ArrayList<>();
			/// affiche le texte virtuellement ///
			editorPane.setText(text);
			/// hauteur des lignes ///
			int h = 0;
			try {
				/// micro-attente (pour �viter certains bugs de synchronisation ///
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				/// on r�cup�re la hauteur des lignes en fonction de la police s�lectionn�e ///
				h = editorPane.modelToView(0).height;
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
			/// on cherche la position dans le texte du caract�re le plus proche du coin inf�rieur droit de la page ///
			int off = textHandler.getAbsoluteOffset(lastPhrase,
					editorPane.viewToModel(new Point((int) (editorPane.getWidth() - Constants.TEXTPANE_MARGING),
							(int) (editorPane.getHeight() - h))));
			/// on parcourt chaque caract�re jusqu'� cette position ///
			for (int i = lastOffset; i < off; i++) {
				int phraseIndex = textHandler.getPhraseIndex(i);
				if (phraseIndex == -1) {
					lastOffset = textHandler.getShowText().length();
				}
				/// on ajoute le segment s'il rentre en entier ///
				if (!phrases.contains(phraseIndex) && phraseIndex > lastPhrase
						&& phraseIndex != textHandler.getPhraseIndex(off)) {
					lastPhrase = phraseIndex;
					phrases.add(phraseIndex);
					lastOffset = i;
				}
			}
			/// enregistre tous les segments trouv�s dans une page pr�cise ///
			if (!phrases.isEmpty()) {
				phrasesInFonctionOfPages.put(page, phrases);
				page++;
			}
			String newText = textHandler.getShowText().substring(lastOffset);
			/// derni�re page ///
			if (newText.equals(text)) {
				if (!phrasesInFonctionOfPages.get(page - 1).contains(textHandler.getPhraseIndex(off))) {
					phrasesInFonctionOfPages.get(page - 1).add(textHandler.getPhraseIndex(off));
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
		String texteAfficher = "";
		// on recupere les segments a afficher dans la page
		List<String> liste = new ArrayList<String>();
		for (Integer i : phrasesInFonctionOfPages.get(pageActuelle)) {
			liste.add(textHandler.getPhrase(i));
		}
		for (String string : liste) {
			texteAfficher += string;
		}
		editorPane.setText(texteAfficher);

	}

	public int getFirstShownPhraseIndex() {
		return phrasesInFonctionOfPages.get(pageActuelle).get(0);
	}

	public void afficherCompteRendu(String message) {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			/*
			 * switch (param.readMode) { case SEGMENTE: case SUIVI: <<<<<<< HEAD =======
			 * 
			 * >>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560 message =
			 * "L'exercice est termin�." + "\n" + "Le patient a fait " + nbErreurs +
			 * " erreur" + (nbErreurs > 1 ? "s" : "") + " de clic.\n" + "Le patient a fait "
			 * + nbErreursParSegment + " erreur" + (nbErreursParSegment > 1 ? "s" : "") +
			 * " de segment."; break; case ANTICIPE: case GUIDEE: <<<<<<< HEAD message =
			 * "L'exercice est termin�."; =======
			 * 
			 * message = "L'exercice est termin�.";
			 * 
			 * >>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560 default: break; }
			 */
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
			int debutRelatifSegment = textHandler.getRelativeStartPhrasePosition(getFirstShownPhraseIndex(), n);
			int finRelativeSegment = debutRelatifSegment + textHandler.getPhrase(n).length();
			editorPane.surlignerPhrase(0, finRelativeSegment, editorPane.hParam.rightColor);
		}
	}

	/**
	 * Retourne la longueur du segment n
	 */
	public int getPagesLength(int n) {
		int start = phrasesInFonctionOfPages.get(n).get(0);
		int fin = phrasesInFonctionOfPages.get(n).get(phrasesInFonctionOfPages.get(n).size() - 1);
		return textHandler.getPhrasesLength(start, fin);
	}

	////////////////////////////////////////////
	///////////////////////////////////////////
	//////////////////////////////////////////
	/////////////////////////////////////////

	/**
	 * Affiche une fenetre correspondant au mot d�limit� par start et end, d'indice
	 * numeroCourant, et met le masque correspondant dans la liste des masques
	 */
	public void showFrame(int start, int end, int h) throws BadLocationException {

		Mask frame = new Mask(start, end, null);
		((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
		frame.setBorder(null);

		setLayout(null);

		frame.initField(param.font.deriveFont(param.font.getSize() / 1.5f), controlerMask);

		frame.setVisible(true);
		frame.hiddenWord = textHandler.getHiddendWord(h);
		frame.n = h;
		maskFrame.add(frame);

		Rectangle r = editorPane.modelToView(start).union(editorPane.modelToView(end));

		frame.setBounds(r.x, r.y, r.width, r.height / 2);
		add(frame);

		for (Component c : getComponents()) {
			if (c instanceof JInternalFrame) {
				((JInternalFrame) c).toFront();
			}
		}
	}

	public void replaceAllMask() {
		for (int i = 0; i < maskFrame.size(); i++) {
			if (maskFrame.get(i).isVisible()) {
				try {
					replacerMasque(maskFrame.get(i));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * replace une fenetre
	 */
	public void replacerMasque(Mask frame) throws BadLocationException {
		try {
			int start = frame.start;
			int end = frame.end;
			Rectangle r = editorPane.modelToView(start).union(editorPane.modelToView(end));
			frame.setBounds(r.x, r.y, r.width, r.height / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// donne le numero d'un masque
	public int getNumero(Mask m) {
		return maskFrame.indexOf(m);
	}

	public void removeAllMasks() {
		for (int i = 0; i < maskFrame.size(); i++) {
			Mask m = maskFrame.get(i);
			m.setVisible(false);
			remove(m);
		}
		maskFrame.clear();
	}

	public void blink(final Color c) {
		Timer blinkTimer = new Timer();
		final long interval = 250;
		blinkTimer.scheduleAtFixedRate(new TimerTask() {
			private long time;

			public void run() {
				time += interval;
				if (time >= interval * 4) {
					editorPane.setBackground(param.bgColor);
					cancel();
					return;
				}
				if (time % (interval * 2) != 0)
					editorPane.setBackground(c);
				else
					editorPane.setBackground(param.bgColor);
			}
		}, 0, interval);
	}

	public void updateText() {
		String texteAfficher = "";
		// on recupere les segments a afficher dans la page
		List<String> liste = new ArrayList<String>();

		for (int i = 0; i < phrasesInFonctionOfPages.get(pageActuelle).size(); i++) {
			int index = phrasesInFonctionOfPages.get(pageActuelle).get(i);
			liste.add(textHandler.getPhrase(index));
		}
		for (int i = 0; i < liste.size(); i++) {
			texteAfficher += liste.get(i);
		}

		editorPane.setText(texteAfficher);
	}

}
