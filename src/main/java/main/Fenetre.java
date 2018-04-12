package main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	private Panneau pan;

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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pan.init();
			}
		});

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("Options");

		JMenuItem eMenuItem = new JMenuItem("Quitter");
		eMenuItem.setToolTipText("Quitter l'application");
		eMenuItem.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		JMenuItem eMenuItem2 = new JMenuItem("Relancer");
		eMenuItem2.setToolTipText("Relancer l'exercice");
		eMenuItem2.addActionListener((ActionEvent event) -> {
			FenetreParametre.editorPane = null;
			pan.fenetre.setVisible(false);
			FenetreParametre.fen.setVisible(true);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).champNbFautesTolerees
					.setEnabled(true);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).segmentDeDepart
			.setEnabled(true);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listeTailles
			.setEnabled(true);
		});
		JMenuItem eMenuItem3 = new JMenuItem("Parametres");
		eMenuItem3.setToolTipText("Parametres de l'exercice");
		eMenuItem3.addActionListener((ActionEvent event) -> {
			FenetreParametre.editorPane = pan.editorPane;
			FenetreParametre.fen.setVisible(true);
			int x = 4 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
			int y = 4 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
			FenetreParametre.fen.setLocation(x, y);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).champNbFautesTolerees
					.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).segmentDeDepart
			.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listeTailles
			.setEnabled(false);
		});

		file.add(eMenuItem3);
		file.add(eMenuItem2);
		file.add(eMenuItem);

		menubar.add(file);

		setJMenuBar(menubar);

		setVisible(true);
	}

}
