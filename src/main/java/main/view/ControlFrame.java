package main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.*;

import main.Constants;
import main.controler.ControleurParam;
import main.model.Player;
import main.reading.ReadMode;

public class ControlFrame extends JFrame {

	private static int imageSize = 40;
	private static Image previousIcon, playIcon, pauseIcon, nextIcon, repeatIcon;

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

	public ControlFrame(Panneau pan) {
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
				pan.controlerGlobal.doPrevious();
				updateButtons();
			}
		});

		panel.add(playButton);
		playButton.setIcon(new ImageIcon(playIcon));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if (player.isPlaying()) {
					player.pause();
				} else if (player.isPhraseFinished()) {
					player.repeat();
				} else {
					/// on attend d'abord en mode lecture anticipée ///
					if (FenetreParametre.readMode == ReadMode.ANTICIPATED) {
						player.doWait();
						pan.controlerGlobal.highlightPhrase(Constants.RIGHT_COLOR, player.getCurrentPhraseIndex());
					} else {
						player.play();
					}
				}*/
				if (player.isPlaying()) {
					pan.controlerGlobal.doStop();
				}
				else {
					pan.controlerGlobal.doPlay();
				}
				updateButtons();
			}
		});

		panel.add(nextButton);
		nextButton.setIcon(new ImageIcon(nextIcon));
		nextButton.setEnabled(player.hasNextPhrase());
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.controlerGlobal.doNext();
				updateButtons();
			}
		});

		panel.add(repeatButton);
		repeatButton.setIcon(new ImageIcon(repeatIcon));
		repeatButton.setEnabled(false);
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.controlerGlobal.doPlay();
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
				pan.controlerGlobal.goTo(n);
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
		} else {
			previousButton.setEnabled(false);
			playButton.setEnabled(false);
			nextButton.setEnabled(false);
			repeatButton.setEnabled(false);
		}
		goToField.setText(String.valueOf(player.getCurrentPhraseIndex() + 1));
	}

	public void disableAll() {
		usable = false;
		updateButtons();
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

	private void stockerPreference() {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		int yes = -1;
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			yes = JOptionPane.showConfirmDialog(this, "Sauvegarder les preferences actuelles ?", "Confirmation",
					JOptionPane.YES_NO_OPTION);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		if (yes == 0) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("./ressources/preferences/preference_" + Constants.NOM_ELEVE + ".txt",
						"UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			writer.println("w:" + pan.fenetre.getWidth());
			writer.println("h:" + pan.fenetre.getHeight());
			writer.println("x:" + pan.fenetre.getX());
			writer.println("y:" + pan.fenetre.getY());
			writer.println("taillePolice:" + FenetreParametre.taillePolice);
			writer.println("typePolice:" + FenetreParametre.police.getFontName());
			writer.println("couleur:" + FenetreParametre.couleurFond.getRed() + "/"
					+ FenetreParametre.couleurFond.getGreen() + "/" + FenetreParametre.couleurFond.getBlue() + "/"

					+ Constants.RIGHT_COLOR.getRed() + "/" + Constants.RIGHT_COLOR.getGreen() + "/"
					+ Constants.RIGHT_COLOR.getBlue() + "/"

					+ Constants.WRONG_COLOR.getRed() + "/" + Constants.WRONG_COLOR.getGreen() + "/"
					+ Constants.WRONG_COLOR.getBlue() + "/"

					+ Constants.WRONG_PHRASE_COLOR.getRed() + "/" + Constants.WRONG_PHRASE_COLOR.getGreen() + "/"
					+ Constants.WRONG_PHRASE_COLOR.getBlue());

			writer.println("mode:" + FenetreParametre.readMode);
			writer.println("tempsAttente:" + FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture);
			writer.println("rejouerSon:" + FenetreParametre.rejouerSon);
			writer.close();
		}
	}

	private void appliquerPreference() {
		Object optionPaneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		int yes = -1;
		try {
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("Panel.background", Color.WHITE);
			yes = JOptionPane.showConfirmDialog(this, "Chargez les preferences ?", "Confirmation",
					JOptionPane.YES_NO_OPTION);
		} finally {
			UIManager.put("OptionPane.background", optionPaneBG);
			UIManager.put("Panel.background", panelBG);
		}
		String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + ".txt";
		if (yes == 0) {
			// lecture du fichier texte
			try {
				InputStream ips = new FileInputStream(fichier);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				int w = -1, h = -1, x = -1, y = -1, t = -1, tempsPause = -1;
				Color color = null, rightColor = null, wrongColor = null, correctionColor = null;
				String p = null;
				ReadMode mode = null;
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
					case 4:
						t = Integer.valueOf(ligne.split(":")[1]);
						break;
					case 5:
						p = ligne.split(":")[1];
						break;
					case 6:
						String temp = ligne.split(":")[1];
						color = new Color(Integer.valueOf(temp.split("/")[0]), Integer.valueOf(temp.split("/")[1]),
								Integer.valueOf(temp.split("/")[2]));
						rightColor = new Color(Integer.valueOf(temp.split("/")[3]), Integer.valueOf(temp.split("/")[4]),
								Integer.valueOf(temp.split("/")[5]));
						wrongColor = new Color(Integer.valueOf(temp.split("/")[6]), Integer.valueOf(temp.split("/")[7]),
								Integer.valueOf(temp.split("/")[8]));
						correctionColor = new Color(Integer.valueOf(temp.split("/")[9]),
								Integer.valueOf(temp.split("/")[10]), Integer.valueOf(temp.split("/")[11]));

						break;
					case 7:
						String s = String.valueOf(ligne.split(":")[1]);
						switch (s) {
						case "GUIDED_READING":
							mode = ReadMode.GUIDED_READING;
							break;
						case "HIGHLIGHT":
							mode = ReadMode.HIGHLIGHT;
							break;
						case "NORMAL":
							mode = ReadMode.NORMAL;
							break;
						case "ANTICIPATED":
							mode = ReadMode.ANTICIPATED;
							break;
						}
						FenetreParametre.readMode = mode;
						break;
					case 8:
						tempsPause = Integer.valueOf(ligne.split(":")[1]);
						break;
					case 9:
						FenetreParametre.rejouerSon = Boolean.valueOf(ligne.split(":")[1]);
						break;
					default:
						break;
					}
					i++;
				}
				pan.fenetre.setBounds(x, y, w, h);
				FenetreParametre.taillePolice = t;
				int index = 999;
				if (p.equals("OpenDyslexic") || p.equals("OpenDyslexic Bold")) {
					index = 0;
				}
				if (p.equals("Andika")) {
					index = 1;
				}
				if (p.equals("Lexia")) {
					index = 2;
				}
				FenetreParametre.police = ControleurParam.getFont(p, index, Font.BOLD, t);
				((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listeCouleurs
						.setBackground(FenetreParametre.couleurFond = color);
				pan.editorPane.setFont(FenetreParametre.police);
				pan.editorPane.setBackground(color);
				Constants.RIGHT_COLOR = rightColor;
				Constants.WRONG_COLOR = wrongColor;
				Constants.WRONG_PHRASE_COLOR = correctionColor;
				FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = tempsPause;
				((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).sliderAttente
						.setValue(tempsPause);
				br.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			pan.rebuildPages();
		}
	}

	public void addMenu() {
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
			new FenetreParametre(Constants.titreFenetreParam, Constants.largeurFenetreParam,
					Constants.hauteurFenetreParam).setLocation(p);
		});
		JMenuItem eMenuItem3 = new JMenuItem("Parametres");
		eMenuItem3.setMnemonic(KeyEvent.VK_P);
		eMenuItem3.setToolTipText("Parametres de l'exercice");
		eMenuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		eMenuItem3.addActionListener((ActionEvent event) -> {
			FenetreParametre.editorPane = pan.editorPane;
			FenetreParametre.fen.setVisible(true);
			int x = 4 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
			int y = 2 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
			FenetreParametre.fen.setLocation(x, y);
			FenetreParametre.fen.fenetre.setEnabled(false);
			FenetreParametre.fen.fenetre.pan.controlFrame.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).segmentDeDepart.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).modeKaraoke.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).modeSurlignage.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).modeNormal.setEnabled(false);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).modeAnticipe.setEnabled(false);
			// grisage de la taille et du style de la police si on est plus au premier
			// segment
			if ((player.getCurrentPhraseIndex() + 1) - FenetreParametre.premierSegment != 0) {
				((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listePolices.setEnabled(false);
				((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).listeTailles.setEnabled(false);
			}
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
	}

}
