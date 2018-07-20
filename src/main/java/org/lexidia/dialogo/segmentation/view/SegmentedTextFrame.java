package org.lexidia.dialogo.segmentation.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

	public void init(String text, int startingPhrase, Font font, int x, int y, int width, int height) {
		setParameters(new ToolParameters(font, width, height, x, y, startingPhrase, text));

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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//param.startingPhrase = pan.controlerGlobal.getCurrentPhraseIndex() + 1;
				// param.stockerPreference();
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

}
