package main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.*;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	public Panneau pan;
	public boolean preferencesExiste = true;

	public Fenetre(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("ressources/images/lexidia.gif"));
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

	private void stockerPreference() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("preference.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println("w:"+getWidth());
		writer.println("h:"+getHeight());
		writer.println("x:"+getX());
		writer.println("y:"+getY());
		writer.close();
	}

	private void appliquerPreference() {
		String fichier = "preference.txt";
		// lecture du fichier texte
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			int w = -1, h = -1, x = -1, y = -1;
			int i = 0;
			while ((ligne = br.readLine()) != null) {
				switch (i) {
				case 0:
					w = Integer.valueOf(ligne.split(":")[1]);
					break;
				case 1:
					h = Integer.valueOf(ligne.split(":")[1]);
					break;
				case 2:
					x = Integer.valueOf(ligne.split(":")[1]);
					break;
				case 3:
					y = Integer.valueOf(ligne.split(":")[1]);
					break;
				default:
					break;
				}
				i++;
			}
			setBounds(x, y, w, h);
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
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
		eMenuItem3.setMnemonic(KeyEvent.VK_P);
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
		JMenuItem eMenuItem4 = new JMenuItem("Stocker Preferences");
		eMenuItem4.setMnemonic(KeyEvent.VK_S);
		eMenuItem4.setToolTipText("Enregistre les tailles et position de la fenêtre");
		eMenuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		eMenuItem4.addActionListener((ActionEvent event) -> {
			stockerPreference();
		});
		JMenuItem eMenuItem5 = new JMenuItem("Appliquer Preferences");
		eMenuItem5.setMnemonic(KeyEvent.VK_A);
		eMenuItem5.setToolTipText("Applique les preferences de taille et position de la fenêtre");
		eMenuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		eMenuItem5.addActionListener((ActionEvent event) -> {
			appliquerPreference();
		});
		file.add(eMenuItem3);
		file.add(eMenuItem2);
		file.add(eMenuItem4);
		file.add(eMenuItem5);
		file.add(eMenuItem);
		

		menubar.add(file);

		setJMenuBar(menubar);

		setVisible(true);
	}

}
