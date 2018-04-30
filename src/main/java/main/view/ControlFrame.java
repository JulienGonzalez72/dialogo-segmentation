package main.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import main.Constants;
import main.Parametres;
import main.model.Player;

public class ControlFrame extends JFrame {

	private static int imageSize = Constants.tailleImageFrame;
	private static Image previousIcon, playIcon, pauseIcon, nextIcon, repeatIcon;

	private Parametres param;
	private FenetreParametre fen;
	private JPanel panel = new JPanel();
	private JButton previousButton = new JButton();
	private JButton playButton = new JButton();
	private JButton nextButton = new JButton();
	private JButton repeatButton = new JButton();
	public JTextField goToField = new JTextField();
	private Player player;
	private Panneau pan;

	private boolean usable = true;

	static {
		loadImages();
	}

	public ControlFrame(Panneau pan, FenetreParametre fen, Parametres param) {
		this.param = param;
		this.fen = fen;
		this.pan = pan;
		player = pan.player;
		setIconImage(getToolkit().getImage("icone.jpg"));
		setTitle("Contrôle");
		setContentPane(panel);
		setSize(325, 140);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);

		panel.add(previousButton);
		previousButton.setIcon(new ImageIcon(previousIcon));
		previousButton.setEnabled(player.hasPreviousPhrase());
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.pilot.doPrevious();
				updateButtons();
			}
		});

		panel.add(playButton);
		playButton.setIcon(new ImageIcon(playIcon));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (player.isPlaying()) {
<<<<<<< HEAD
					pan.pilot.doStop();
				}
				else {
					pan.pilot.doPlay();
=======
					pan.controlerGlobal.doStop();
				} else {
					pan.controlerGlobal.doPlay();
>>>>>>> 3d10d562a026221f85d982a46ae1e21be2fcaf2a
				}
				updateButtons();
			}
		});

		panel.add(nextButton);
		nextButton.setIcon(new ImageIcon(nextIcon));
		nextButton.setEnabled(player.hasNextPhrase());
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
				pan.pilot.doNext();
				updateButtons();
=======
					pan.controlerGlobal.doNext();
					updateButtons();
>>>>>>> 3d10d562a026221f85d982a46ae1e21be2fcaf2a
			}
		});

		panel.add(repeatButton);
		repeatButton.setIcon(new ImageIcon(repeatIcon));
		repeatButton.setEnabled(false);
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.pilot.doPlay();
				updateButtons();
			}
		});

		Runnable update = () -> {
			updateButtons();
		};
		player.onPhraseEnd.add(update);
		player.onBlockEnd.add(update);
		player.onPlay.add(update);

		JLabel goToLabel = new JLabel("Passer au segment :");
		goToLabel.setFont(goToLabel.getFont().deriveFont(Font.ITALIC));
		panel.add(goToLabel);
		panel.add(goToField);
		goToField.setPreferredSize(new Dimension(40, 20));
		goToField.addActionListener((ActionEvent e) -> {
			int n;
			try {
				n = Integer.parseInt(goToField.getText()) - 1;
				pan.pilot.goTo(n);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, "Numéro de segment incorrect : " + goToField.getText());
			}
			updateButtons();
		});

		addMenu();
		updateButtons();
	}

	/**
	 * Actualise l'état de tous les composants de la fenêtre de contrôle.
	 */
	public void updateButtons() {
		if (usable) {
			previousButton.setEnabled(player.hasPreviousPhrase());
			playButton.setEnabled(true);
			playButton.setIcon(new ImageIcon(player.isPlaying() ? pauseIcon : playIcon));
			nextButton.setEnabled(player.hasNextPhrase());
			repeatButton.setEnabled(player.isPlaying());
			goToField.setEnabled(true);
		} else {
			previousButton.setEnabled(false);
			playButton.setEnabled(false);
			nextButton.setEnabled(false);
			repeatButton.setEnabled(false);
			goToField.setEnabled(false);
		}
<<<<<<< HEAD
=======
		//goToField.setText(String.valueOf(player.getCurrentPhraseIndex() + 1));
>>>>>>> 3d10d562a026221f85d982a46ae1e21be2fcaf2a
	}

	public void disableAll() {
		usable = false;
		updateButtons();
	}
	
	/**
	 * Désactive tous les boutons de la fenêtre de contrôle puis les ré-active après le temps duration.
	 */
	public void disableAll(long duration) {
		disableAll();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				enableAll();
			}
		});
	}

	public void enableAll() {
		usable = true;
		updateButtons();
	}

	private static void loadImages() {
		previousIcon = getIcon("previous_icon.png");
		playIcon = getIcon("play_icon.png");
		pauseIcon = getIcon("pause_icon.png");
		nextIcon = getIcon("next_icon.png");
		repeatIcon = getIcon("repeat_icon.png");
	}

	private static Image getIcon(String imageName) {
		try {
			return ImageIO.read(new File("ressources/images/" + imageName)).getScaledInstance(imageSize, imageSize,
					Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	

	private void addMenu() {
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
			this.pan.controlFrame.setVisible(false);
			this.pan.fenetre.setVisible(false);
			Point p = new Point(50, 50);
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				e.printStackTrace();
			}
			FenetreParametre temp = new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam,
					Constants.hauteurFenetreParam);
			temp.setLocation(p);
			SwingUtilities.updateComponentTreeUI(temp);
		});
		JMenuItem eMenuItem3 = new JMenuItem("Parametres");
		eMenuItem3.setMnemonic(KeyEvent.VK_P);
		eMenuItem3.setToolTipText("Parametres de l'exercice");
		eMenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		eMenuItem3.addActionListener((ActionEvent event) -> {
			fen.editorPane = pan.editorPane;
			fen.setVisible(true);
			int x = 4 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
			int y = 2 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
			fen.setLocation(x, y);
			fen.fenetre.setEnabled(false);
			fen.fenetre.pan.controlFrame.setEnabled(false);
			((FenetreParametre.PanneauParam) fen.getContentPane()).segmentDeDepart.setEnabled(false);
			((FenetreParametre.PanneauParam) fen.getContentPane()).modeKaraoke.setEnabled(false);
			((FenetreParametre.PanneauParam) fen.getContentPane()).modeSurlignage.setEnabled(false);
			((FenetreParametre.PanneauParam) fen.getContentPane()).modeNormal.setEnabled(false);
			((FenetreParametre.PanneauParam) fen.getContentPane()).modeAnticipe.setEnabled(false);
			// grisage de la taille et du style de la police si on est plus au premier
			// segment
			if ((player.getCurrentPhraseIndex() + 1) - param.premierSegment != 0) {
				((FenetreParametre.PanneauParam) fen.getContentPane()).listePolices.setEnabled(false);
				((FenetreParametre.PanneauParam) fen.getContentPane()).listeTailles.setEnabled(false);
			}
		});
		JMenuItem eMenuItem4 = new JMenuItem("Stocker Preferences");
		eMenuItem4.setMnemonic(KeyEvent.VK_S);
		eMenuItem4.setToolTipText("Enregistre les tailles et position de la fenêtre");
		eMenuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		eMenuItem4.addActionListener((ActionEvent event) -> {
			param.stockerPreference();
		});
		JMenuItem eMenuItem5 = new JMenuItem("Appliquer Preferences");
		eMenuItem5.setMnemonic(KeyEvent.VK_A);
		eMenuItem5.setToolTipText("Applique les preferences de taille et position de la fenêtre");
		eMenuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		eMenuItem5.addActionListener((ActionEvent event) -> {
			param.appliquerPreference(fen,pan);
		});
		file.add(eMenuItem3);
		file.add(eMenuItem2);
		file.add(eMenuItem4);
		file.add(eMenuItem5);
		file.add(eMenuItem);
		menubar.add(file);
		setJMenuBar(menubar);
	}

}
