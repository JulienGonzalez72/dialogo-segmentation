package main.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.swing.*;

import main.Constants;
import main.Parametres;
import main.controler.ControlerParam;
import main.reading.ReadMode;

public class FenetreParametre extends JFrame {

	public Fenetre fenetre;
	public Parametres param;
	public TextPane editorPane;
	public ControlPanel controlPanel;
<<<<<<< HEAD
	
	public JMenuItem stopItem;
=======
	public JMenuItem stopItem;
	public PanneauParam pan;
>>>>>>> 76f25113fe9e3bf5d965fb505126673ed92d8018
	
	public FenetreParametre(String titre, int tailleX, int tailleY) {
		param = new Parametres();
		setIconImage(getToolkit().getImage("icone.jpg"));
		param.police = ControlerParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
		param.taillePolice = Constants.DEFAULT_FONT_SIZE;
		param.couleurFond = Constants.BG_COLOR;
		editorPane = null;
		param.titre = titre;
		param.tailleX = tailleX;
		param.tailleY = tailleY;
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		pan = null;
		try {
			pan = new PanneauParam(this);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

		fenetre = new Fenetre(param.titre, param.tailleX * 2, param.tailleY, this, param);
		controlPanel = new ControlPanel(fenetre.pan, this, param);

		JTabbedPane generalTab = new JTabbedPane();
		generalTab.addTab("Paramètres", pan);
		generalTab.addTab("Contrôle", controlPanel);
		setContentPane(generalTab);
		addMenu();
		setVisible(true);

	}

	public class PanneauParam extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		public JPanel panelModes;
		public JComboBox<Object> listePolices;
		public JComboBox<Object> listeTailles;
		public JComboBox<Object> listeCouleurs;
		public JComboBox<Object> listeBonnesCouleurs;
		public JComboBox<Object> listeMauvaisesCouleurs;
		public JComboBox<Object> listeCorrectionCouleurs;
		public JTextField segmentDeDepart;
		public JTextField champNbFautesTolerees;
		public JButton valider;
		public JCheckBox rejouerSon;
		public JRadioButton modeSurlignage, modeKaraoke, modeNormal, modeAnticipe;
		public ButtonGroup modes;
		public JSlider sliderAttente;
		public final Object[] polices;
		public final Object[] tailles;
		public FenetreParametre fen;
		
		public PanneauParam(FenetreParametre fen) throws NumberFormatException, IOException {
			this.fen = fen;
			setLayout(new BorderLayout());
			JLabel titre = fastLabel("Choisissez vos parametres");
			titre.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
			add(titre, BorderLayout.NORTH);

			valider = fastButton("Valider les parametres", new Font("OpenDyslexic", Font.BOLD, 18), Color.green);
			JLabel police = fastLabel("Police : ");
			JLabel taillePolice = fastLabel("Taille de la police : ");
			JLabel couleurDeFond = fastLabel("Couleur de fond : ");
			JLabel couleurJuste = fastLabel("Couleur pour \"juste\" : ");
			JLabel couleurFausse = fastLabel("Couleur pour \"faux\" : ");
			JLabel couleurCorrection = fastLabel("Couleur de correction : ");
			JLabel segments = fastLabel("Segment de départ ");
			JLabel attente = fastLabel("Temps d'attente en % du temps de lecture");

			polices = new Object[] { "OpenDyslexic", "Andika", "Lexia", "Arial", "Times New Roman" };
			tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };

			ControlerParam controleur = new ControlerParam(fen, this);
			valider.addActionListener(controleur);

			listePolices = new JComboBox<Object>(polices);
			listePolices.setRenderer(new ListCellRenderer<Object>() {
				private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					list.setFont(
							ControlerParam.getFont((String) value, index, Font.BOLD, Constants.DEFAULT_FONT_SIZE));
					renderer.setHorizontalAlignment(SwingConstants.CENTER);
					return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			listePolices.setFont(ControlerParam.getFont((String) listePolices.getSelectedItem(), 0, Font.BOLD,
					Constants.DEFAULT_FONT_SIZE));
			listePolices.addActionListener(controleur);

			listeTailles = new JComboBox<Object>(tailles);
			listeTailles.setRenderer(new ListCellRenderer<Object>() {
				private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					list.setFont(new Font(Font.DIALOG, Font.BOLD, Integer.parseInt((String) value)));
					renderer.setHorizontalAlignment(SwingConstants.CENTER);
					return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			listeTailles.addActionListener(controleur);
			listeTailles.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));

			listeCouleurs = fastComboBox(controleur, getColorNames());
			listeCouleurs.setRenderer(new ColorCellRenderer());
			listeBonnesCouleurs = fastComboBox(controleur, getColorNames());
			listeBonnesCouleurs.setRenderer(new ColorCellRenderer());
			listeMauvaisesCouleurs = fastComboBox(controleur, getColorNames());
			listeMauvaisesCouleurs.setRenderer(new ColorCellRenderer());
			listeCorrectionCouleurs = fastComboBox(controleur, getColorNames());
			listeCorrectionCouleurs.setRenderer(new ColorCellRenderer());

			segmentDeDepart = fastTextField(String.valueOf(param.premierSegment),
					new Font("OpenDyslexic", Font.PLAIN, 15), "1");
			segmentDeDepart.addActionListener(controleur);

			JLabel nbFautesTolerees = fastLabel("Nombre de fautes maximum");
			champNbFautesTolerees = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "2");
			champNbFautesTolerees.addActionListener(controleur);

			JPanel midPanel = new JPanel(new GridLayout(10, 2));

			midPanel.add(police);
			midPanel.add(taillePolice);
			fastCentering(listePolices, midPanel, "   ");
			fastCentering(listeTailles, midPanel, "   ");

			midPanel.add(segments);
			midPanel.add(nbFautesTolerees);
			fastCentering(segmentDeDepart, midPanel, "   ");
			fastCentering(champNbFautesTolerees, midPanel, "   ");

			midPanel.add(couleurDeFond);
			midPanel.add(couleurJuste);
			fastCentering(listeCouleurs, midPanel, "   ");
			fastCentering(listeBonnesCouleurs, midPanel, "   ");

			midPanel.add(couleurFausse);
			midPanel.add(couleurCorrection);
			fastCentering(listeMauvaisesCouleurs, midPanel, "   ");
			fastCentering(listeCorrectionCouleurs, midPanel, "   ");

			modeAnticipe = fastRadio("Anticipé", controleur);
			modeAnticipe.setToolTipText("Mode Anticipé");
			modeSurlignage = fastRadio("Suivi", controleur);
			modeSurlignage.setToolTipText("Mode de lecture segmentée : version surlignage");
			modeKaraoke = fastRadio("Guidé", controleur);
			modeKaraoke.setToolTipText("Mode guidé");
			modeNormal = fastRadio("Segmenté", controleur);
			modeNormal.setToolTipText("Mode de lecture segmentée");
			modeNormal.setSelected(true);

			modes = new ButtonGroup();
			modes.add(modeSurlignage);
			modes.add(modeKaraoke);
			modes.add(modeNormal);
			modes.add(modeAnticipe);

			sliderAttente = new JSlider();
			sliderAttente.setMaximum(Constants.MAX_WAIT_TIME_PERCENT);
			sliderAttente.setMinimum(Constants.MIN_WAIT_TIME_PERCENT);
			sliderAttente.setValue(Constants.DEFAULT_WAIT_TIME_PERCENT);
			sliderAttente.setPaintTicks(true);
			sliderAttente.setPaintLabels(true);
			sliderAttente.setMinorTickSpacing(10);
			sliderAttente.setMajorTickSpacing(50);
			sliderAttente.addChangeListener(controleur);

			JPanel panelSud = new JPanel(new GridLayout(9, 1));
			panelSud.add(new JLabel());
			rejouerSon = fastCheckBox("Rejouer les phrases si erreur", controleur);
			rejouerSon.setSelected(true);
			JPanel temp = new JPanel();
			temp.add(rejouerSon);
			panelSud.add(temp);

			panelModes = new JPanel(new GridLayout(1, 4));
			panelModes.add(modeKaraoke);
			panelModes.add(modeSurlignage);
			panelModes.add(modeNormal);
			panelModes.add(modeAnticipe);
			panelSud.add(new JLabel());
			panelSud.add(panelModes);
			panelSud.add(new JLabel());
			panelSud.add(add(attente));
			panelSud.add(sliderAttente);
			panelSud.add(new JLabel());
			panelSud.add(valider);

			add(midPanel, BorderLayout.CENTER);
			add(panelSud, BorderLayout.SOUTH);
			
			chargerPreferences();

		}

		public void chargerPreferences() throws NumberFormatException, IOException {
			String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE +"_"+param.readMode+ ".txt";
			InputStream ips = null;
			try {
				ips = new FileInputStream(fichier);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Properties pro = new Properties();
			try {
				pro.load(ips);
			} catch (IOException e) {
				e.printStackTrace();
			}

			int t = Integer.valueOf(pro.getProperty("taillePolice"));
			if (t == 12) {
				listeTailles.setSelectedItem(tailles[0]);
			}
			if (t == 16) {
				listeTailles.setSelectedItem(tailles[1]);
			}
			if (t == 18) {
				listeTailles.setSelectedItem(tailles[2]);
			}
			if (t == 20) {
				listeTailles.setSelectedItem(tailles[3]);
			}
			if (t == 22) {
				listeTailles.setSelectedItem(tailles[4]);
			}
			if (t == 24) {
				listeTailles.setSelectedItem(tailles[5]);
			}
			if (t == 30) {
				listeTailles.setSelectedItem(tailles[6]);
			}
			if (t == 36) {
				listeTailles.setSelectedItem(tailles[7]);
			}
			if (t == 42) {
				listeTailles.setSelectedItem(tailles[8]);
			}

			int index = -1;
			String p = pro.getProperty("typePolice");
			if (p.equals("OpenDyslexic") || p.equals("OpenDyslexic Bold")) {
				index = 0;
			}
			if (p.equals("Andika") || p.equals("Andika Basic")) {
				index = 1;
			}
			if (p.equals("Lexia")) {
				index = 2;
			}
			if (p.equals("Arial") || p.equals("Arial Gras")) {
				index = 3;
			}
			if (p.equals("Times New Roman") || p.equals("Times New Roman Gras")) {
				index = 4;
			}
			listePolices.setSelectedItem(polices[index]);

			appliquerCouleur(param.fromStringToColor(pro.getProperty("couleurFond")), listeCouleurs);
			appliquerCouleur(param.fromStringToColor(pro.getProperty("couleurBonne")), listeBonnesCouleurs);
			appliquerCouleur(param.fromStringToColor(pro.getProperty("couleurFausse")), listeMauvaisesCouleurs);
			appliquerCouleur(param.fromStringToColor(pro.getProperty("couleurCorrection")), listeCorrectionCouleurs);

			param.readMode = ReadMode.parse((pro.getProperty("mode")));
			switch (param.readMode) {
			case SEGMENTE:
				modeNormal.setSelected(true);
				break;
			case SUIVI:
				modeSurlignage.setSelected(true);
				break;
			case GUIDEE:
				modeKaraoke.setSelected(true);
				break;
			case ANTICIPE:
				modeAnticipe.setSelected(true);
				break;
			}

			int temp = Integer.valueOf(pro.getProperty("tempsAttente"));
			param.tempsPauseEnPourcentageDuTempsDeLecture = temp;
			sliderAttente.setValue(temp);

			rejouerSon.setSelected(Boolean.valueOf(pro.getProperty("rejouerSon")));
		}

		private void appliquerCouleur(Color color, JComboBox<Object> listeCouleurs) {
			listeCouleurs.setSelectedItem(colorToString(color));
		}
		
		private class ColorCellRenderer implements ListCellRenderer<Object> {
			private DefaultListCellRenderer renderer = new DefaultListCellRenderer();
			private static final float NORMAL_FONT_SIZE = 15;
			private static final float SELECTED_FONT_SIZE = 25;
			public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				list.setBackground(stringToColor((String) value));
				list.setSelectionBackground(stringToColor((String) value));
				list.setFont(list.getFont().deriveFont(isSelected ? SELECTED_FONT_SIZE : NORMAL_FONT_SIZE));
				renderer.setHorizontalAlignment(SwingConstants.CENTER);
				renderer.setPreferredSize(new Dimension(0, Constants.COMBOBOX_CELL_HEIGHT));
				return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		}

		public void fermer() {
			fen.setVisible(false);
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

	}

	public void lancerExercice() {
		Panneau.premierSegment = param.premierSegment;
		Panneau.defautNBEssaisParSegment = param.nbFautesTolerees;
		fenetre.start();
	}

	private void addMenu() {
		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("Options");

		JMenuItem quitItem = new JMenuItem("Quitter");
		quitItem.setToolTipText("Quitter l'application");
		quitItem.setMnemonic(KeyEvent.VK_Q);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
<<<<<<< HEAD
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
=======
		quitItem.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
<<<<<<< HEAD
>>>>>>> 76f25113fe9e3bf5d965fb505126673ed92d8018
		stopItem = new JMenuItem("Arrêter l'exercice");
		stopItem.setToolTipText("Relancer l'exercice");
		stopItem.setMnemonic(KeyEvent.VK_R);
		stopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
<<<<<<< HEAD
		stopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stopExercice();
			}
=======
		stopItem.addActionListener((ActionEvent event) -> {
=======
		eMenuItem2 = new JMenuItem("Arrêter l'exercice");
		eMenuItem2.setMnemonic(KeyEvent.VK_R);
		eMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		eMenuItem2.addActionListener((ActionEvent event) -> {
			/// réactive la taille et la police et le segment de départ
			pan.listePolices.setEnabled(true);
			pan.listeTailles.setEnabled(true);
			pan.segmentDeDepart.setEnabled(true);
			pan.fen.fenetre.setResizable(true);
>>>>>>> cc4c6d3440627edfa82d7458b46008b5118a1e6f
			stopExercice();
>>>>>>> 76f25113fe9e3bf5d965fb505126673ed92d8018
		});
		file.add(stopItem);
		stopItem.setEnabled(false);
		file.add(quitItem);
		menubar.add(file);
		setJMenuBar(menubar);
	}

	public void stopExercice() {
		param.appliquerPreference(this, fenetre.pan);
		stopItem.setEnabled(false);
		fenetre.setVisible(false);
		controlPanel.disableAll();
		fenetre.pan.pilot.doStop();
	}
	
	public static Color stringToColor(String name) {
		return Constants.COLORS.get(name);
	}
	
	public static String colorToString(Color color) {
		Set<String> keys = Constants.COLORS.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (stringToColor(key).equals(color)) {
				return key;
			}
		}
		return null;
	}
	
	public static String[] getColorNames() {
		return Constants.COLORS.keySet().toArray(new String[0]);
	}

}
