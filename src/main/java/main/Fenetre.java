package main;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
<<<<<<< HEAD
=======

>>>>>>> 9af62975f19182f41d47e5b4c7580574b81d4f6a
import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	public Panneau pan;
	public boolean preferencesExiste = true;

	public Fenetre(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("icone.jpg"));
		try {
			pan = new Panneau(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);

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
			}
		});
	}
<<<<<<< HEAD

=======
	
>>>>>>> 9af62975f19182f41d47e5b4c7580574b81d4f6a
	public void start() {
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init();
			}
		});
	}

}
