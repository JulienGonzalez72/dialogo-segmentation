package main.view;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import main.Constants;
import main.Parametres;

public class TextFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public TextPanel pan;
	public boolean preferencesExiste = true;
	private Parametres param;

	public TextFrame(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("icone.jpg"));
		try {
			pan = new TextPanel(this, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setMinimumSize(new Dimension(Constants.MIN_FENETRE_WIDTH, Constants.MIN_FENETRE_HEIGHT));
	}
	
	public void init(final Parametres param) {
		setParameters(param);
		
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
				
				TextFrame.this.param.panWidth = TextFrame.this.getWidth();
				TextFrame.this.param.panHeight = TextFrame.this.getHeight();
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				TextFrame.this.param.panX = TextFrame.this.getX();
				TextFrame.this.param.panY = TextFrame.this.getY();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				param.startingPhrase = pan.pilot.getCurrentPhraseIndex() + 1;
				param.stockerPreference();
			}
		});
	}
	
	public void setParameters(Parametres param) {
		this.param = param;
		param.appliquerPreferenceTaillePosition(this);
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

}
