package org.lexidia.dialogo.segmentation.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.lexidia.dialogo.segmentation.main.Constants;
import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.model.ToolParameters;

public class SegmentedTextFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * S'exécute lorsque la fenêtre et son conteneur se sont bien initialisés.
	 */
	private Runnable onInit;

	private SegmentedTextPanel pan;
	private boolean preferencesExiste = true;
	private ToolParameters param;

	public SegmentedTextFrame(String titre) {
		setIconImage(getToolkit().getImage("icone.jpg"));
		try {
			pan = new SegmentedTextPanel(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setTitle(titre);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setMinimumSize(new Dimension(Constants.MIN_FENETRE_WIDTH, Constants.MIN_FENETRE_HEIGHT));
	}

	/**
	 * Initialise la fenêtre en fonction de certains paramètres de base nécessaires à sa création.<br>
	 * Cette méthode est nécessaire pour appeler la méthode {@link #start} qui affiche la fenêtre.
	 * @param text le texte segmenté
	 * @param startingPhrase le numéro du premier segment à afficher (en partant de 0)
	 * @param font la police du texte
	 * @param x la position x de la fenêtre (en pixels)
	 * @param y la position y de la fenêtre (en pixels)
	 * @param width la largeur de la fenêtre (en cm)
	 * @param height la hauteur de la fenêtre (en cm)
	 */
	public void init(String text, int startingPhrase, Font font, int x, int y, float width, float height) {
		int w = cmToPx(width);
		int h = cmToPx(height);
		setParameters(new ToolParameters(font, w, h, x, y, startingPhrase, 0, text));
		
		addComponentListener(new ComponentAdapter() {
			private int lastWidth = getWidth(), lastHeight = getHeight();

			@Override
			public void componentResized(ComponentEvent e) {
				/// lors d'un redimensionnement, refait la mise en page ///
				if (isResizable() && pan.getEditorPane() != null && pan.getEditorPane().getWidth() > 0
						&& (lastWidth != getWidth() || lastHeight != getHeight())) {
					pan.rebuildPages();
					lastWidth = getWidth();
					lastHeight = getHeight();
				}
				
				SegmentedTextFrame.this.param.setPanWidth(SegmentedTextFrame.this.getWidth());
				SegmentedTextFrame.this.param.setPanHeight(SegmentedTextFrame.this.getHeight());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				SegmentedTextFrame.this.param.setPanX(SegmentedTextFrame.this.getX());
				SegmentedTextFrame.this.param.setPanY(SegmentedTextFrame.this.getY());
			}
		});
	}
	
	public void setParameters(ToolParameters param) {
		this.param = param;
		setBounds(param.getPanX(), param.getPanY(), param.getPanWidth(), param.getPanHeight());
		pan.setParameters(param);
	}

	public void start() {
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init(param);
			}
		});
	}
	
	/**
	 * Active ou désactive le traitement des textes à trou, c'est-à-dire que si cette option est activée (par défaut),
	 * toutes les expressions entre crochets seront remplacées par des underscore.<br>
	 * Cette méthode doit être appelée avant d'afficher la fenêtre.
	 * @param holeTreatment <code>true</code> si l'option de traitement de textes à trou doit être activé, <code>false</code> sinon
	 */
	public void setHoleTreatment(boolean holeTreatment) {
		TextHandler.holeTreatment = holeTreatment;
	}
	
	public SegmentedTextPanel getPanel() {
		return pan;
	}

	public Runnable getOnInit() {
		return onInit;
	}

	public void setOnInit(Runnable onInit) {
		this.onInit = onInit;
	}

	public boolean isPreferencesExiste() {
		return preferencesExiste;
	}

	public void setPreferencesExiste(boolean preferencesExiste) {
		this.preferencesExiste = preferencesExiste;
	}
	
	private static final float DOT_FACTOR = 1.25f;
	private static final float INCH_VALUE = 2.54f;
	
	/**
	 * Convertit une longueur des pixels aux centimètres.
	 * @param px la valeur en pixels
	 * @return la valeur en centimètres
	 */
	public static float pxToCm(int px) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		return px / dpi * INCH_VALUE / DOT_FACTOR;
	}
	
	/**
	 * Convertit une longueur des centimètres aux pixels.
	 * @param px la valeur en centimètres
	 * @return la valeur en pixels
	 */
	public static int cmToPx(float cm) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		return (int) (cm * dpi / INCH_VALUE * DOT_FACTOR);
	}
}
