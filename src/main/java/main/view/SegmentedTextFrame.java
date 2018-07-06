package main.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.Constants;
import main.model.ToolParameters;

public class SegmentedTextFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * S'exécute lorsque la fenêtre et son conteneur se sont bien initialisés.
	 */
	public Runnable onInit;

	private SegmentedTextPanel pan;
	public boolean preferencesExiste = true;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				if (isResizable() && pan.editorPane != null && pan.editorPane.getWidth() > 0
						&& (lastWidth != getWidth() || lastHeight != getHeight())) {
					pan.rebuildPages();
					lastWidth = getWidth();
					lastHeight = getHeight();
				}

				SegmentedTextFrame.this.param.panWidth = SegmentedTextFrame.this.getWidth();
				SegmentedTextFrame.this.param.panHeight = SegmentedTextFrame.this.getHeight();

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				SegmentedTextFrame.this.param.panX = SegmentedTextFrame.this.getX();
				SegmentedTextFrame.this.param.panY = SegmentedTextFrame.this.getY();
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
		setBounds(param.panX, param.panY, param.panWidth, param.panHeight);
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
	
	public SegmentedTextPanel getPanel() {
		return pan;
	}

}
