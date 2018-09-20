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
	 * S'ex�cute lorsque la fen�tre et son conteneur se sont bien initialis�s.
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
	 * Initialise la fen�tre en fonction de certains param�tres de base n�cessaires � sa cr�ation.<br>
	 * Cette m�thode est n�cessaire pour appeler la m�thode {@link #start} qui affiche la fen�tre.
	 * @param text le texte segment�
	 * @param startingPhrase le num�ro du premier segment � afficher (en partant de 0)
	 * @param font la police du texte
	 * @param x la position x de la fen�tre (en pixels)
	 * @param y la position y de la fen�tre (en pixels)
	 * @param width la largeur de la fen�tre (en cm)
	 * @param height la hauteur de la fen�tre (en cm)
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
	 * Active ou d�sactive le traitement des textes � trou, c'est-�-dire que si cette option est activ�e (par d�faut),
	 * toutes les expressions entre crochets seront remplac�es par des underscore.<br>
	 * Cette m�thode doit �tre appel�e avant d'afficher la fen�tre.
	 * @param holeTreatment <code>true</code> si l'option de traitement de textes � trou doit �tre activ�, <code>false</code> sinon
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
	 * Convertit une longueur des pixels aux centim�tres.
	 * @param px la valeur en pixels
	 * @return la valeur en centim�tres
	 */
	public static float pxToCm(int px) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		return px / dpi * INCH_VALUE / DOT_FACTOR;
	}
	
	/**
	 * Convertit une longueur des centim�tres aux pixels.
	 * @param px la valeur en centim�tres
	 * @return la valeur en pixels
	 */
	public static int cmToPx(float cm) {
		float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		return (int) (cm * dpi / INCH_VALUE * DOT_FACTOR);
	}
}
