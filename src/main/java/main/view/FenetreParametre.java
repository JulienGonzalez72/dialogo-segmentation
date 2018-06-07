package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import main.Constants;
import main.Parametres;
import main.controler.ControlerParam;
import main.reading.ReadMode;

// TODO classe non fonctionnelle
public class FenetreParametre extends JFrame {

	public TextFrame fenetre;
	/**
	 * Liste des paramètres par mode de lecture.
	 */
	private Map<ReadMode, Parametres> params = new HashMap<>();
	public ControlPanel controlPanel;
	public JMenuItem stopItem;
	public PanneauParam pan;

	public FenetreParametre(String titre, int tailleX, int tailleY) {
		params = Parametres.loadAll();
		setIconImage(getToolkit().getImage("icone.jpg"));
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		fenetre = new TextFrame(titre, tailleX * 2, tailleY);

		pan = null;
		try {
			pan = new PanneauParam(this);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		controlPanel = new ControlPanel(fenetre.pan, this);

		JTabbedPane generalTab = new JTabbedPane();
		generalTab.addTab("Paramètres", pan);
		generalTab.addTab("Contrôle", controlPanel);
		setContentPane(generalTab);
		addMenu();
		setVisible(true);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				if (pan.waitSlider != null) {
					pan.waitSlider.updateUI();
				}
			}
		});
	}

	public class PanneauParam extends JPanel {

		private static final long serialVersionUID = 1L;

		public JPanel panelModes;
		public JComboBox<String> fontFamilyComboBox;
		public JComboBox<Integer> fontSizeComboBox;
		public ColorComboBox bgColorComboBox;
		public ColorComboBox rightColorComboBox;
		public ColorComboBox wrongColorComboBox;
		public ColorComboBox correctColorComboBox;
		public JTextField startingPhraseField;
		public JTextField toleratedErrorsField;
		private JLabel couleurJuste;
		private JLabel couleurFausse;
		private JLabel couleurCorrection;
		public JButton validButton;
		public JCheckBox replayCheckBox;
		public JRadioButton highlightModeRadio, guidedModeRadio, segmentedModeRadio, anticipatedModeRadio;
		public ButtonGroup modes;
		public JSlider waitSlider;
		public FenetreParametre fen;
		public ReadMode oldMode = ReadMode.SEGMENTE;
		private ControlerParam controleur;

		public PanneauParam(FenetreParametre fen) throws NumberFormatException, IOException {
			this.fen = fen;
			setLayout(new BorderLayout());
			JLabel titre = fastLabel("Choisissez vos parametres");
			titre.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
			titre.setPreferredSize(new Dimension(0, 30));
			add(titre, BorderLayout.NORTH);
			
			validButton = fastButton("Démarrer l'exercice", new Font("OpenDyslexic", Font.BOLD, 18), Color.green);
			JLabel police = fastLabel("Police : ");
			JLabel taillePolice = fastLabel("Taille de la police : ");
			JLabel couleurDeFond = fastLabel("Couleur de fond : ");
			couleurJuste = fastLabel("Couleur pour \"juste\" : ");
			couleurFausse = fastLabel("Couleur pour \"faux\" : ");
			couleurCorrection = fastLabel("Couleur de correction : ");
			JLabel segments = fastLabel("Segment de départ ");
			JLabel attente = fastLabel("Temps d'attente en % du temps de lecture");

			controleur = new ControlerParam(fen, this);
			validButton.addActionListener(controleur);

			fontFamilyComboBox = new JComboBox<String>(Constants.FONT_FAMILIES);
			fontFamilyComboBox.setRenderer(new ListCellRenderer<Object>() {
				private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					list.setFont(
							FenetreParametre.getFont((String) value, index, Font.BOLD, Constants.DEFAULT_FONT_SIZE));
					renderer.setHorizontalAlignment(SwingConstants.CENTER);
					return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			fontFamilyComboBox.setFont(FenetreParametre.getFont((String) fontFamilyComboBox.getSelectedItem(), 0,
					Font.BOLD, Constants.DEFAULT_FONT_SIZE));
			fontFamilyComboBox.addActionListener(controleur);

			fontSizeComboBox = new JComboBox<Integer>(Constants.FONT_SIZES);
			fontSizeComboBox.setRenderer(new ListCellRenderer<Object>() {
				private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					list.setFont(new Font(Font.DIALOG, Font.BOLD, (Integer) value));
					renderer.setHorizontalAlignment(SwingConstants.CENTER);
					return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			fontSizeComboBox.addActionListener(controleur);
			fontSizeComboBox.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));

			bgColorComboBox = new ColorComboBox(Constants.COLORS, true);
			bgColorComboBox.colorListener = new ColorComboBox.ColorChangeListener() {
				@Override
				public void colorChanged(Color newColor) {
					if (fenetre != null && fenetre.pan.editorPane != null) {
						fenetre.pan.editorPane.setBackground(newColor);
					}
					grabFocus();
				}
			};
			rightColorComboBox = new ColorComboBox(Constants.COLORS, true);
			wrongColorComboBox = new ColorComboBox(Constants.COLORS, true);
			correctColorComboBox = new ColorComboBox(Constants.COLORS, true);

			/// fléches haut et bas pour incrémenter/décrémenter un nombre ///
			KeyListener numberKey = new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN)
						return;

					JTextField jtf = (JTextField) e.getSource();
					try {
						int n = Integer.parseInt(jtf.getText());

						if (e.getKeyCode() == KeyEvent.VK_UP)
							n++;
						else if (e.getKeyCode() == KeyEvent.VK_DOWN)
							n--;
						else
							return;

						if ((controleur.isValidPhrase(n) && jtf == startingPhraseField)
								|| (n >= 0 && jtf == toleratedErrorsField))
							jtf.setText(String.valueOf(n));
					} catch (NumberFormatException ex) {
					}
				}
			};

			startingPhraseField = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "1");
			startingPhraseField.addActionListener(controleur);
			startingPhraseField.addKeyListener(numberKey);

			JLabel nbFautesTolerees = fastLabel("Nombre de fautes maximum");
			toleratedErrorsField = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "2");
			toleratedErrorsField.addActionListener(controleur);
			toleratedErrorsField.addKeyListener(numberKey);

			JPanel midPanel = new JPanel(new GridLayout(9, 2));

			midPanel.add(police);
			midPanel.add(taillePolice);
			fastCentering(fontFamilyComboBox, midPanel, "   ");
			fastCentering(fontSizeComboBox, midPanel, "   ");

			midPanel.add(segments);
			midPanel.add(nbFautesTolerees);
			fastCentering(startingPhraseField, midPanel, "   ");
			fastCentering(toleratedErrorsField, midPanel, "   ");

			midPanel.add(couleurDeFond);
			midPanel.add(couleurJuste);
			fastCentering(bgColorComboBox, midPanel, "   ");
			fastCentering(rightColorComboBox, midPanel, "   ");

			midPanel.add(couleurFausse);
			midPanel.add(couleurCorrection);
			fastCentering(wrongColorComboBox, midPanel, "   ");
			fastCentering(correctColorComboBox, midPanel, "   ");

			anticipatedModeRadio = fastRadio("Anticipé", controleur);
			anticipatedModeRadio.setToolTipText("Mode Anticipé");
			highlightModeRadio = fastRadio("Suivi", controleur);
			highlightModeRadio.setToolTipText("Mode de lecture segmentée : version surlignage");
			guidedModeRadio = fastRadio("Guidé", controleur);
			guidedModeRadio.setToolTipText("Mode guidé");
			segmentedModeRadio = fastRadio("Segmenté", controleur);
			segmentedModeRadio.setToolTipText("Mode de lecture segmentée");
			segmentedModeRadio.setSelected(true);

			modes = new ButtonGroup();
			modes.add(highlightModeRadio);
			modes.add(guidedModeRadio);
			modes.add(segmentedModeRadio);
			modes.add(anticipatedModeRadio);

			waitSlider = new JSlider();
			waitSlider.setMaximum(Constants.MAX_WAIT_TIME_PERCENT);
			waitSlider.setMinimum(Constants.MIN_WAIT_TIME_PERCENT);
			waitSlider.setValue(Constants.DEFAULT_WAIT_TIME_PERCENT);
			waitSlider.setPaintTicks(true);
			waitSlider.setPaintLabels(true);
			waitSlider.setMinorTickSpacing(10);
			waitSlider.setMajorTickSpacing(50);

			JPanel panelSud = new JPanel(new GridLayout(8, 1));
			replayCheckBox = fastCheckBox("Rejouer les phrases si erreur", controleur);
			replayCheckBox.setSelected(true);
			JPanel temp = new JPanel();
			temp.add(replayCheckBox);
			panelSud.add(temp);

			panelModes = new JPanel(new GridLayout(1, 4));
			panelModes.add(guidedModeRadio);
			panelModes.add(highlightModeRadio);
			panelModes.add(segmentedModeRadio);
			panelModes.add(anticipatedModeRadio);
			panelSud.add(new JLabel());
			panelSud.add(panelModes);
			panelSud.add(new JLabel());
			panelSud.add(attente);
			panelSud.add(waitSlider);
			panelSud.add(new JLabel());
			panelSud.add(validButton);

			add(midPanel, BorderLayout.CENTER);
			add(panelSud, BorderLayout.SOUTH);

			applyPreferences(getReadMode());
			updateMode();
		}

		/**
<<<<<<< HEAD
		 * Applique les préférences chargées aux pré-sélections de la fenétre de paramétres
		 * et é la fenétre principale si elle existe.
=======
		 * Applique les pr�f�rences charg�es aux pr�-s�lections de la fen�tre de
		 * param�tres et � la fen�tre principale si elle existe.
>>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560
		 */
		public void applyPreferences(ReadMode readMode) {
			Parametres param = params.get(readMode);

			fontSizeComboBox.setSelectedItem(param.taillePolice);
			fontFamilyComboBox.setSelectedItem(getCorrectFontName(param.police.getFontName()));

			startingPhraseField.setText(String.valueOf(param.startingPhrase));
			toleratedErrorsField.setText(String.valueOf(param.nbFautesTolerees));

			appliquerCouleur(param.bgColor, bgColorComboBox);
			appliquerCouleur(param.rightColor, rightColorComboBox);
			appliquerCouleur(param.wrongColor, wrongColorComboBox);
			appliquerCouleur(param.correctionColor, correctColorComboBox);

			waitSlider.setValue(param.tempsPauseEnPourcentageDuTempsDeLecture);

			replayCheckBox.setSelected(param.rejouerSon);
		}

		/**
<<<<<<< HEAD
		 * Enregistre les préférences en fonction de la sélection de l'utilisateur.
=======
		 * Enregistre les pr�f�rences en fonction de la s�lection de l'utilisateur.
>>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560
		 */
		public void savePreferences(ReadMode readMode) {
			Parametres param = params.get(readMode);
			param.bgColor = bgColorComboBox.getBackground();
			param.rightColor = rightColorComboBox.getBackground();
			param.wrongColor = wrongColorComboBox.getBackground();
			param.correctionColor = correctColorComboBox.getBackground();
			param.taillePolice = (Integer) fontSizeComboBox.getSelectedItem();
			param.police = FenetreParametre.getFont((String) fontFamilyComboBox.getSelectedItem(),
					fontFamilyComboBox.getSelectedIndex(), Font.BOLD, param.taillePolice);
			try {
				param.startingPhrase = Integer.parseInt(startingPhraseField.getText());

				param.nbFautesTolerees = Integer.parseInt(toleratedErrorsField.getText());
			} catch (

			NumberFormatException e) {
			}
			param.rejouerSon = replayCheckBox.isSelected();
			param.tempsPauseEnPourcentageDuTempsDeLecture = waitSlider.getValue();
		}

		private void appliquerCouleur(Color color, ColorComboBox listeCouleurs) {
			listeCouleurs.selectColor(color);
		}

		public void fermer() {
			fen.setVisible(false);
		}

		/**
<<<<<<< HEAD
		 * Mets é jour les composants de la fenétre en fonction du mode sélectionné.
=======
		 * Mets � jour les composants de la fen�tre en fonction du mode s�lectionn�.
>>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560
		 */
		public void updateMode() {
			switch (getReadMode()) {
			case GUIDEE:
			case ANTICIPE:
				setRightColorParameterVisible(true);
				setWrongColorParameterVisible(false);
				setCorrectColorParameterVisible(false);
				break;
			case SEGMENTE:
				setRightColorParameterVisible(false);
				setWrongColorParameterVisible(true);
				setCorrectColorParameterVisible(true);
				break;
			case SUIVI:
				setRightColorParameterVisible(true);
				setWrongColorParameterVisible(true);
				setCorrectColorParameterVisible(true);
				break;
			}
		}

		private void setRightColorParameterVisible(boolean visible) {
			couleurJuste.setVisible(visible);
			rightColorComboBox.setVisible(visible);
		}

		private void setWrongColorParameterVisible(boolean visible) {
			couleurFausse.setVisible(visible);
			wrongColorComboBox.setVisible(visible);
		}

		private void setCorrectColorParameterVisible(boolean visible) {
			couleurCorrection.setVisible(visible);
			correctColorComboBox.setVisible(visible);
		}

		final Font defaultFont = new Font("OpenDyslexic", Font.ITALIC, 16);

		public JLabel fastLabel(String nom, Font font) {
			JLabel r = new JLabel(nom);
			r.setFont(font);
			r.setHorizontalAlignment(JLabel.CENTER);
			return r;
		}

		public JLabel fastLabel(String nom) {
			JLabel r = new JLabel(nom);
			r.setFont(defaultFont);
			r.setHorizontalAlignment(JLabel.CENTER);
			return r;
		}

		public JComboBox<Object> fastComboBox(ControlerParam controleur, Object[] elements) {
			JComboBox<Object> r = new JComboBox<Object>(elements);
			((JLabel) r.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			r.addActionListener(controleur);
			r.setBackground(Constants.BG_COLOR);
			r.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));
			return r;
		}

		public JRadioButton fastRadio(String nom, ControlerParam controleur) {
			JRadioButton r = new JRadioButton(nom);
			r.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			r.addActionListener(controleur);
			r.setVerticalTextPosition(JRadioButton.TOP);
			r.setHorizontalTextPosition(JRadioButton.CENTER);
			return r;
		}

		private JCheckBox fastCheckBox(String nom, ControlerParam controleur) {
			JCheckBox r = new JCheckBox(nom);
			r.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			r.addActionListener(controleur);
			r.setVerticalTextPosition(JRadioButton.TOP);
			r.setHorizontalTextPosition(JRadioButton.CENTER);
			return r;
		}

		public JTextField fastTextField(String nom, Font font, String texteParDefaut) {
			JTextField r = new JTextField(nom);
			r.setHorizontalAlignment(JButton.CENTER);
			r.setFont(font);
			r.setText(texteParDefaut);
			return r;
		}

		public JButton fastButton(String nom, Font font, Color color) {
			JButton r = new JButton(nom);
			r.setHorizontalAlignment(JButton.CENTER);
			r.setBackground(color);
			r.setFont(font);
			return r;
		}

		/**
		 * Centre le composant c dans le panneau p
		 * 
		 * @param marge
		 *            = taille de la marge
		 */
		private void fastCentering(Component c, JPanel p, String marge) {
			JPanel temp = new JPanel(new BorderLayout());
			temp.add(new JLabel(marge), BorderLayout.WEST);
			temp.add(c, BorderLayout.CENTER);
			temp.add(new JLabel(marge), BorderLayout.EAST);
			p.add(temp);
		}

		/**
<<<<<<< HEAD
		 * Retourne le mode de lecture sélectionné par l'utilisateur.
=======
		 * Retourne le mode de lecture s�lectionn� par l'utilisateur.
>>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560
		 */
		public ReadMode getReadMode() {
			if (guidedModeRadio.isSelected())
				return ReadMode.GUIDEE;
			if (highlightModeRadio.isSelected())
				return ReadMode.SUIVI;
			if (segmentedModeRadio.isSelected())
				return ReadMode.SEGMENTE;
			if (anticipatedModeRadio.isSelected())
				return ReadMode.ANTICIPE;
			return null;
		}

	}

	public void lancerExercice() {
		pan.validButton.setText("Appliquer les paramètres");
		setLaunchParametersEnabled(false);
		//fenetre.init(getCurrentParameters());
		fenetre.start();
		controlPanel.init();
	}

	public void stopExercice() {
		pan.validButton.setText("Démarrer l'exercice");
		setLaunchParametersEnabled(true);
		stopItem.setEnabled(false);
		fenetre.setVisible(false);
		controlPanel.disableAll();
		fenetre.pan.pilot.doStop();
		setStartParametersEnabled(true);
	}

	/**
	 * Active ou désactive les paramétres non modifiables à partir de la première écoute.
	 */
	public void setStartParametersEnabled(boolean enabled) {
		///désacive la taille et la police et le segment de départ ///
		pan.fontFamilyComboBox.setEnabled(enabled);
		pan.fontSizeComboBox.setEnabled(enabled);
		pan.startingPhraseField.setEnabled(enabled);

		pan.rightColorComboBox.setEnabled(enabled);
		pan.wrongColorComboBox.setEnabled(enabled);
		pan.correctColorComboBox.setEnabled(enabled);
	}

	/**
	 * Active ou désactive les paramétres non modifiables lors du lancement de l'exercice.
	 */
	public void setLaunchParametersEnabled(boolean enabled) {
		pan.anticipatedModeRadio.setEnabled(enabled);
		pan.guidedModeRadio.setEnabled(enabled);
		pan.highlightModeRadio.setEnabled(enabled);
		pan.segmentedModeRadio.setEnabled(enabled);

		pan.startingPhraseField.setEnabled(enabled);
	}

	/**
	 * Retourne les paramétres du mode sélectionné.
	 */
	public Parametres getCurrentParameters() {
		if (pan.getReadMode() == null)
			return null;
		return params.get(pan.getReadMode());
	}

	private void addMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("Options");

		JMenuItem quitItem = new JMenuItem("Quitter");
		quitItem.setToolTipText("Quitter l'application");
		quitItem.setMnemonic(KeyEvent.VK_Q);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		stopItem = new JMenuItem("Arréter l'exercice");
		stopItem.setToolTipText("Relancer l'exercice");
		stopItem.setMnemonic(KeyEvent.VK_R);
		stopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		stopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stopExercice();
			}
		});
		file.add(stopItem);
		stopItem.setEnabled(false);
		file.add(quitItem);
		menubar.add(file);
		setJMenuBar(menubar);
	}

	public static String getCorrectFontName(String font) {
		String[] deletions = { " Bold", " Basic", " Gras", " Italic" };
		for (int i = 0; i < deletions.length; i++) {
			font = font.replaceAll(deletions[i], "");
		}
		return font;
	}

	/**
<<<<<<< HEAD
	 * Retourne le font correspondant à :
=======
	 * Retourne le font correspondant � :
>>>>>>> 80a9c7c8cb1ee3c12c78c000bb50e157e0aed560
	 * 
	 * @param1 : la police
	 * @param2 : l'index du font dans la liste des polices de la FenetreParametre
	 * @param3 : le style
	 * @param4 : la taille
	 */
	public static Font getFont(String police, int selectedIndex, int style, int size) {
		try {
			Font font;
			if (selectedIndex < Constants.FONTS_NAMES.length && selectedIndex >= 0) {
				font = Font
						.createFont(Font.TRUETYPE_FONT,
								new File("ressources/fonts/" + Constants.FONTS_NAMES[selectedIndex]))
						.deriveFont(style).deriveFont((float) size);
			} else {
				font = new Font(police, style, size);
			}
			return font;
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}