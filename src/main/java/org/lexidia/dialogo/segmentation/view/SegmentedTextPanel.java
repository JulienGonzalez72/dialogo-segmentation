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
import java.io.IOException;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexidia.dialogo.segmentation.controller.ControllerMask;
import org.lexidia.dialogo.segmentation.controller.ControllerMouse;
import org.lexidia.dialogo.segmentation.model.Player;
import org.lexidia.dialogo.segmentation.model.ReadingParameters;
import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.model.ToolParameters;
import org.lexidia.dialogo.segmentation.reading.ReadThread;

public class SegmentedTextPanel extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	public static int premierSegment;

	// panneau du texte
	private SegmentedTextPane editorPane;
	private TextHandler textHandler;
	private int pageActuelle;
	private int nbPages;
	private int nbEssaisParSegment;
	private int nbEssaisRestantPourLeSegmentCourant;
	/**
	 * Nombre d'erreurs total.
	 */
	private int nbErreurs;
	/**
	 * Nombre de segments pour lesquels le patient a atteint le nombre limite
	 * d'essais possibles.
	 */
	private int nbErreursParSegment;
	/**
	 * Nombre d'erreurs pour le segment courant.
	 */
	private int nbErreursSegmentCourant;
	private SegmentedTextFrame fenetre;
	private ControlPanel controlPanel;
	private ControllerMouse controlerMouse;
	private ReadThread task;
	private Map<Integer, List<Integer>> phrasesInFonctionOfPages = new HashMap<Integer, List<Integer>>();
	private Player player;
	private ToolParameters param;
	private ReadingParameters rParam = new ReadingParameters();

	/**
	 * Barre de progression
	 */
	private JProgressBar progressBar;

	/*
	 * ///////////////////////////////////////////////////////////// Ci dessous les
	 * attributs utilisés pour la gestion des trous
	 */////////////////////////////////////////////////////////////

	private ControllerMask controlerMask;
	private List<Mask> maskFrame = new ArrayList<>();
	private JPanel panelSud = new JPanel();
	private JDesktopPane panelFixedFrame = null;
	private Mask fixedFrame;
	
	//object used to debug

	private static final Log log = LogFactory.getLog(SegmentedTextPanel.class);

	public SegmentedTextPanel(SegmentedTextFrame fenetre) throws IOException {
		setControlerMask(new ControllerMask());
		this.setFenetre(fenetre);
		this.setLayout(new BorderLayout());
		
		setEditorPane(new SegmentedTextPane());
		getEditorPane().setEditable(false);
		add(getEditorPane(), BorderLayout.CENTER);
		
		setProgressBar(new JProgressBar(0, 0));
		getProgressBar().setStringPainted(true);
		getProgressBar().setForeground(Color.GREEN);
		
		add(getPanelSud(), BorderLayout.SOUTH);

	}

	/**
	 * S'exécute lorsque le panneau s'est bien intégré à la fenêtre.
	 */
	public void init(ToolParameters param){
		setParameters(param);
		
		setTextHandler(new TextHandler(param.getText()));
		getProgressBar().setMaximum(getTextHandler().getPhrasesCount());
		
		getProgressBar().setValue(param.getStartingPhrase());
		getProgressBar().setString(param.getStartingPhrase() + "/" + (getTextHandler().getPhrasesCount() - 1));
		setCurrentPage(0);
		
		/// construit la mise en page virtuelle ///
		rebuildPages();
		/// initialise le lecteur ///
		setControlerMouse(new ControllerMouse(this, getTextHandler()));
		getEditorPane().addMouseListener(getControlerMouse());
		getEditorPane().requestFocus();

		if (getFenetre().getOnInit() != null) {
			getFenetre().getOnInit().run();
		}

		if (getrParam().isFixedField()) {
			getPanelSud().setLayout(new BorderLayout());
			setPanelFixedFrame(new JDesktopPane());
			getPanelFixedFrame().setPreferredSize(new Dimension(getFenetre().getWidth(), param.getFont().getSize()));
			getPanelSud().add(getPanelFixedFrame());
			getPanelSud().add(getProgressBar(), BorderLayout.SOUTH);
		} else {
			getPanelSud().setLayout(new GridLayout(1, 1));
			getPanelSud().add(getProgressBar());
		}
		getPanelSud().setVisible(true);
	}
	
	public void setParameters(ToolParameters param) {
		this.setParam(getEditorPane().setParam(param));
		premierSegment = param.getStartingPhrase();

		getEditorPane().setBackground(param.getBgColor());
		getEditorPane().setFont(param.getFont());
		setNbEssaisRestantPourLeSegmentCourant(setNbEssaisParSegment(0));
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
	 * passe a la page suivante et l'affiche
	 *
	 */
	public void afficherPageSuivante() {
		showPage(getCurrentPage() + 1);
		/*
		 * editorPane.désurlignerTout(); if ((param.readMode == ReadMode.GUIDEE ||
		 * param.readMode == ReadMode.ANTICIPE) && (controlerGlobal != null && player !=
		 * null)) { controlerGlobal.highlightPhrase(param.rightColor,
		 * player.getCurrentPhraseIndex()); }
		 */
	}

	/**
	 * Construit les pages et affiche la première.
	 */
	public void rebuildPages() {
		buildPages(getParam().getStartingPhrase());
	}

	public boolean hasNextPage() {
		return getCurrentPage() < getNbPages();
	}

	public void afficherPagePrecedente() {
		if (getCurrentPage() > 0) {
			showPage(getCurrentPage() - 1);
			getEditorPane().removeAllHighlights();
		}
	}
	
	/**
	 * Construit la mise en page du texte.
	 */
	public void buildPages(final int startPhrase) {
		log.info("Start of buildPages");
		BuildPager pager = new BuildPager(getEditorPane(), getTextHandler());
		pager.setMaxPhrasesByPage(getParam().getMaxPhrases());
		Map<Integer, List<Integer>> pages = pager.getPages(startPhrase);
		if (pages != null) {
			setPhrasesInFonctionOfPages(pages);
		}
		/// redimensionne la fenêtre si même un seul segment ne rentre pas ///
		else {
			fenetre.setMinimumSize(pager.getMinimumSize());
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					buildPages(startPhrase);
				}
			});
			return;
		}
		log.info("End of buildPages");
		/// affiche la première page ///
		setCurrentPage(0);
		afficherPageSuivante();
		/// calcule le nombre de pages total ///
		setNbPages(getPhrasesInFonctionOfPages().size());
	}

	public void showPage(int page) {
		/// on ne fait rien si on est déjé sur cette page ///
		if (getCurrentPage() == page) {
			return;
		}
		setCurrentPage(page);
		String showText = "";
		// on recupere les segments a afficher dans la page
		List<String> liste = new ArrayList<String>();
		for (Integer i : getPhrasesInFonctionOfPages().get(getCurrentPage())) {
			liste.add(getTextHandler().getPhrase(i));
		}
		for (String string : liste) {
			showText += string;
		}
		getEditorPane().setText(showText);

	}

	public int getFirstShownPhraseIndex() {
		return getPhrasesInFonctionOfPages().get(getCurrentPage()).get(0);
	}

	public void showReport(String message) {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			JOptionPane.showMessageDialog(this, message, "Compte Rendu", JOptionPane.INFORMATION_MESSAGE);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		getFenetre().setResizable(true);
	}

	/**
	 * Colorie tout jusqu'au segment n en couleur c
	 */
	public void surlignerJusquaSegment(Color c, int n) {
		if (getTextHandler().getPhrase(n) != null) {
			int debutRelatifSegment = getTextHandler().getRelativeStartPhrasePosition(getFirstShownPhraseIndex(), n);
			int finRelativeSegment = debutRelatifSegment + getTextHandler().getPhrase(n).length();
			getEditorPane().highlightPhrase(0, finRelativeSegment, getEditorPane().gethParam().getRightColor());
		}
	}

	/**
	 * Retourne la longueur du segment n
	 */
	public int getPagesLength(int n) {
		int start = getPhrasesInFonctionOfPages().get(n).get(0);
		int fin = getPhrasesInFonctionOfPages().get(n).get(getPhrasesInFonctionOfPages().get(n).size() - 1);
		return getTextHandler().getPhrasesLength(start, fin);
	}

	////////////////////////////////////////////
	///////////////////////////////////////////
	//////////////////////////////////////////
	/////////////////////////////////////////

	/**
	 * Affiche une fenetre correspondant au mot dï¿½limitï¿½ par start et end, d'indice
	 * numeroCourant, et met le masque correspondant dans la liste des masques
	 */
	public void showFrame(int start, int end, int h) throws BadLocationException {

		Mask frame = new Mask(start, end, null);
		((javax.swing.plaf.basic.BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
		frame.setBorder(null);

		setLayout(null);

		frame.initField(getParam().getFont().deriveFont(getParam().getFont().getSize() / 1.5f), getControlerMask());

		frame.setVisible(true);
		frame.setHiddenWord(getTextHandler().getHiddendWord(h));
		frame.setN(h);
		getMaskFrame().add(frame);

		Rectangle r = getEditorPane().modelToView(start).union(getEditorPane().modelToView(end));

		frame.setBounds(r.x, r.y, r.width, r.height / 2);
		add(frame);

		for (Component c : getComponents()) {
			if (c instanceof JInternalFrame) {
				((JInternalFrame) c).toFront();
			}
		}
	}

	public void replaceAllMask() {
		for (int i = 0; i < getMaskFrame().size(); i++) {
			if (getMaskFrame().get(i).isVisible()) {
				try {
					replacerMasque(getMaskFrame().get(i));
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
			int start = frame.getStart();
			int end = frame.getEnd();
			Rectangle r = getEditorPane().modelToView(start).union(getEditorPane().modelToView(end));
			frame.setBounds(r.x, r.y, r.width, r.height / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// donne le numero d'un masque
	public int getNumero(Mask m) {
		return getMaskFrame().indexOf(m);
	}

	public void removeAllMasks() {
		for (int i = 0; i < getMaskFrame().size(); i++) {
			Mask m = getMaskFrame().get(i);
			m.setVisible(false);
			remove(m);
		}
		getMaskFrame().clear();
	}

	public void blink(final Color c) {
		Timer blinkTimer = new Timer();
		final long interval = 250;
		blinkTimer.scheduleAtFixedRate(new TimerTask() {
			private long time;

			public void run() {
				time += interval;
				if (time >= interval * 4) {
					getEditorPane().setBackground(getParam().getBgColor());
					cancel();
					return;
				}
				if (time % (interval * 2) != 0)
					getEditorPane().setBackground(c);
				else
					getEditorPane().setBackground(getParam().getBgColor());
			}
		}, 0, interval);
	}

	public void updateText() {
		String texteAfficher = "";
		// on recupere les segments a afficher dans la page
		List<String> liste = new ArrayList<String>();

		for (int i = 0; i < getPhrasesInFonctionOfPages().get(getCurrentPage()).size(); i++) {
			int index = getPhrasesInFonctionOfPages().get(getCurrentPage()).get(i);
			liste.add(getTextHandler().getPhrase(index));
		}
		for (int i = 0; i < liste.size(); i++) {
			texteAfficher += liste.get(i);
		}

		getEditorPane().setText(texteAfficher);
	}

	public SegmentedTextPane getEditorPane() {
		return editorPane;
	}

	public void setEditorPane(SegmentedTextPane editorPane) {
		this.editorPane = editorPane;
	}

	public TextHandler getTextHandler() {
		return textHandler;
	}

	public void setTextHandler(TextHandler textHandler) {
		this.textHandler = textHandler;
	}

	public int getCurrentPage() {
		return pageActuelle;
	}

	public void setCurrentPage(int pageActuelle) {
		this.pageActuelle = pageActuelle;
	}

	public int getNbPages() {
		return nbPages;
	}

	public void setNbPages(int nbPages) {
		this.nbPages = nbPages;
	}

	public int getNbEssaisParSegment() {
		return nbEssaisParSegment;
	}

	public int setNbEssaisParSegment(int nbEssaisParSegment) {
		this.nbEssaisParSegment = nbEssaisParSegment;
		return nbEssaisParSegment;
	}

	public int getNbEssaisRestantPourLeSegmentCourant() {
		return nbEssaisRestantPourLeSegmentCourant;
	}

	public void setNbEssaisRestantPourLeSegmentCourant(int nbEssaisRestantPourLeSegmentCourant) {
		this.nbEssaisRestantPourLeSegmentCourant = nbEssaisRestantPourLeSegmentCourant;
	}

	public int getNbErreurs() {
		return nbErreurs;
	}

	public void setNbErreurs(int nbErreurs) {
		this.nbErreurs = nbErreurs;
	}

	public int getNbErreursParSegment() {
		return nbErreursParSegment;
	}

	public void setNbErreursParSegment(int nbErreursParSegment) {
		this.nbErreursParSegment = nbErreursParSegment;
	}

	public int getNbErreursSegmentCourant() {
		return nbErreursSegmentCourant;
	}

	public void setNbErreursSegmentCourant(int nbErreursSegmentCourant) {
		this.nbErreursSegmentCourant = nbErreursSegmentCourant;
	}

	public SegmentedTextFrame getFenetre() {
		return fenetre;
	}

	public void setFenetre(SegmentedTextFrame fenetre) {
		this.fenetre = fenetre;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	public ControllerMouse getControlerMouse() {
		return controlerMouse;
	}

	public void setControlerMouse(ControllerMouse controlerMouse) {
		this.controlerMouse = controlerMouse;
	}

	public ReadThread getTask() {
		return task;
	}

	public void setTask(ReadThread task) {
		this.task = task;
	}

	public Map<Integer, List<Integer>> getPhrasesInFonctionOfPages() {
		return phrasesInFonctionOfPages;
	}

	public void setPhrasesInFonctionOfPages(Map<Integer, List<Integer>> phrasesInFonctionOfPages) {
		this.phrasesInFonctionOfPages = phrasesInFonctionOfPages;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ToolParameters getParam() {
		return param;
	}

	public void setParam(ToolParameters param) {
		this.param = param;
	}

	public ReadingParameters getrParam() {
		return rParam;
	}

	public void setrParam(ReadingParameters rParam) {
		this.rParam = rParam;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public ControllerMask getControlerMask() {
		return controlerMask;
	}

	public void setControlerMask(ControllerMask controlerMask) {
		this.controlerMask = controlerMask;
	}

	public List<Mask> getMaskFrame() {
		return maskFrame;
	}

	public void setMaskFrame(List<Mask> maskFrame) {
		this.maskFrame = maskFrame;
	}

	public JPanel getPanelSud() {
		return panelSud;
	}

	public void setPanelSud(JPanel panelSud) {
		this.panelSud = panelSud;
	}

	public JDesktopPane getPanelFixedFrame(){
		if(panelFixedFrame == null) {
			System.out.println("panelFixedFrame is null, check in rParam if the fixed frame mode is enabled");
		}
		return panelFixedFrame;
	}

	public void setPanelFixedFrame(JDesktopPane panelFixedFrame) {
		this.panelFixedFrame = panelFixedFrame;
	}

	public Mask getFixedFrame() {
		return fixedFrame;
	}

	public void setFixedFrame(Mask fixedFrame) {
		this.fixedFrame = fixedFrame;
	}

}
