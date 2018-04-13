package main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	public Panneau pan;

	public Fenetre(String titre, int tailleX, int tailleY) {
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
	}

	public void start() {
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init();
			}
		});

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("Options");

		JMenuItem eMenuItem = new JMenuItem("Quitter");
		eMenuItem.setToolTipText("Quitter l'application");
		eMenuItem.setMnemonic(KeyEvent.VK_Q);
		eMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		eMenuItem.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		JMenuItem eMenuItem2 = new JMenuItem("Relancer");
		eMenuItem2.setToolTipText("Relancer l'exercice");
		eMenuItem2.setMnemonic(KeyEvent.VK_R);
		eMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		eMenuItem2.addActionListener((ActionEvent event) -> {
			this.setVisible(false);
			new FenetreParametre("Dialogo", 500, 500);
		});
		JMenuItem eMenuItem3 = new JMenuItem("Parametres");
		eMenuItem2.setMnemonic(KeyEvent.VK_P);
		eMenuItem3.setToolTipText("Parametres de l'exercice");
		eMenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		eMenuItem3.addActionListener((ActionEvent event) -> {
			FenetreParametre.editorPane = pan.editorPane;
			FenetreParametre.fen.setVisible(true);
			int x = 4 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
			int y = 4 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
			FenetreParametre.fen.setLocation(x, y);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).champNbFautesTolerees
					.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).segmentDeDepart.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listeTailles.setEnabled(false);
		});

		file.add(eMenuItem3);
		file.add(eMenuItem2);
		file.add(eMenuItem);

		menubar.add(file);

		setJMenuBar(menubar);

		setVisible(true);
	}

}
