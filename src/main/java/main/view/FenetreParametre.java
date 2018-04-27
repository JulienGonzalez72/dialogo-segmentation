package main.view;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import main.Constants;
import main.controler.ControleurParam;
import main.reading.ReadMode;

public class FenetreParametre extends JFrame {

	private static final long serialVersionUID = 1L;
	public static Font police = ControleurParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
	public static int taillePolice = Constants.DEFAULT_FONT_SIZE;
	public static Color couleurFond = Constants.BG_COLOR;
	public static String titre;
	public static int tailleX;
	public static int tailleY;
	public static int premierSegment;
	public static FenetreParametre fen;
	public static TextPane editorPane;
	public static int nbFautesTolerees;
	public static int tempsPauseEnPourcentageDuTempsDeLecture;
	public static ReadMode readMode = ReadMode.NORMAL;
	public static boolean rejouerSon = true;
	public Fenetre fenetre;

	public FenetreParametre(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("icone.jpg"));
		FenetreParametre.police = ControleurParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
		FenetreParametre.taillePolice = Constants.DEFAULT_FONT_SIZE;
		FenetreParametre.couleurFond = Constants.BG_COLOR;
		FenetreParametre.editorPane = null;
		FenetreParametre.titre = titre;
		FenetreParametre.tailleX = tailleX;
		FenetreParametre.tailleY = tailleY;
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		PanneauParam pan = null;
		try {
			pan = new PanneauParam();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		setContentPane(pan);
		setVisible(true);
		fenetre = new Fenetre(titre, tailleX * 2, tailleY);
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
		public final Object[] couleurs;

		public PanneauParam() throws NumberFormatException, IOException {
			fen = FenetreParametre.this;
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
			JLabel segments = fastLabel("Segment de d�part ");
			JLabel attente = fastLabel("Temps d'attente en % du temps de lecture");

			polices = new Object[] { "OpenDyslexic", "Andika", "Lexia", "Arial", "Times New Roman" };
			tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };
			couleurs = new Object[] { "Jaune", "Blanc", "Orange", "Rose", "Bleu", "Rouge", "Vert" };

			ControleurParam controleur = new ControleurParam(this);
			valider.addActionListener(controleur);

			listePolices = new JComboBox<Object>(polices);
			listePolices.setRenderer(new ListCellRenderer<Object>() {
				private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					list.setFont(
							ControleurParam.getFont((String) value, index, Font.BOLD, Constants.DEFAULT_FONT_SIZE));
					renderer.setHorizontalAlignment(SwingConstants.CENTER);
					return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			listePolices.setFont(ControleurParam.getFont((String) listePolices.getSelectedItem(), 0, Font.BOLD,
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

			listeCouleurs = fastComboBox(controleur, couleurs);
			listeBonnesCouleurs = fastComboBox(controleur, couleurs);
			listeMauvaisesCouleurs = fastComboBox(controleur, couleurs);
			listeCorrectionCouleurs = fastComboBox(controleur, couleurs);

			segmentDeDepart = fastTextField(String.valueOf(FenetreParametre.premierSegment),
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
			
			LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
			JComboBox<Object> lfBox = fastComboBox(controleur, new Object[0]);
			for (int i = 0; i < lfs.length; i++) {
				lfBox.addItem(lfs[i].getName());
				if (UIManager.getLookAndFeel().getName().equals(lfs[i].getName())) {
					lfBox.setSelectedIndex(i);
				}
			}
			lfBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					try {
						UIManager.setLookAndFeel(lfs[lfBox.getSelectedIndex()].getClassName());
						SwingUtilities.updateComponentTreeUI(FenetreParametre.this);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace();
					}
				}
			});
			midPanel.add(fastLabel("Look And Feel"));
			midPanel.add(new JLabel());
			fastCentering(lfBox, midPanel, "   ");

			modeAnticipe = fastRadio("Anticip�", controleur);
			modeAnticipe.setToolTipText("Mode Anticip�");
			modeSurlignage = fastRadio("Suivi", controleur);
			modeSurlignage.setToolTipText("Mode de lecture segment�e : version surlignage");
			modeKaraoke = fastRadio("Guid�", controleur);
			modeKaraoke.setToolTipText("Mode guid�");
			modeNormal = fastRadio("Segment�", controleur);
			modeNormal.setToolTipText("Mode de lecture segment�e");
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
			String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + ".txt";
			InputStream ips;
			try {
				ips = new FileInputStream(fichier);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				int i = -1;
				while ((ligne = br.readLine()) != null) {
					i++;
					switch (i) {
					case 4:
						int t = Integer.valueOf(ligne.split(":")[1]);
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
						break;
					case 5:
						int index = -1;
						String p = ligne.split(":")[1];
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
						break;
					case 6:
						String temp = ligne.split(":")[1];
						Color color = new Color(Integer.valueOf(temp.split("/")[0]),
								Integer.valueOf(temp.split("/")[1]), Integer.valueOf(temp.split("/")[2]));

						Color rightColor = new Color(Integer.valueOf(temp.split("/")[3]),
								Integer.valueOf(temp.split("/")[4]), Integer.valueOf(temp.split("/")[5]));

						Color wrongColor = new Color(Integer.valueOf(temp.split("/")[6]),
								Integer.valueOf(temp.split("/")[7]), Integer.valueOf(temp.split("/")[8]));

						Color correctionColor = new Color(Integer.valueOf(temp.split("/")[9]),
								Integer.valueOf(temp.split("/")[10]), Integer.valueOf(temp.split("/")[11]));

						appliquerCouleur(color, listeCouleurs);
						appliquerCouleur(rightColor, listeBonnesCouleurs);
						appliquerCouleur(wrongColor, listeMauvaisesCouleurs);
						appliquerCouleur(correctionColor, listeCorrectionCouleurs);
						break;
					case 7:
						FenetreParametre.readMode = ReadMode.parse(ligne.split(":")[1]);
						switch (FenetreParametre.readMode) {
						case NORMAL:
							modeNormal.setSelected(true);
							break;
						case HIGHLIGHT:
							modeSurlignage.setSelected(true);
							break;
						case GUIDED_READING:
							modeKaraoke.setSelected(true);
							break;
						case ANTICIPATED:
							modeAnticipe.setSelected(true);
							break;
						default:
							break;
						}
						break;
					case 8:
						FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = Integer.valueOf(ligne.split(":")[1]);
						sliderAttente.setValue(Integer.valueOf(ligne.split(":")[1]));
						break;
					case 9:
						rejouerSon.setSelected(Boolean.valueOf(ligne.split(":")[1]));
						break;
					default:
						break;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {

			}
		}

		private void appliquerCouleur(Color color, JComboBox<Object> listeCouleurs) {
			if (color.equals(Constants.BG_COLOR)) {
				listeCouleurs.setSelectedItem(couleurs[0]);
			}
			if (color.equals(Color.WHITE)) {
				listeCouleurs.setSelectedItem(couleurs[1]);
			}
			if (color.equals(Color.ORANGE)) {
				listeCouleurs.setSelectedItem(couleurs[2]);
			}
			if (color.equals(Color.PINK)) {
				listeCouleurs.setSelectedItem(couleurs[3]);
			}
			if (color.equals(Color.CYAN)) {
				listeCouleurs.setSelectedItem(couleurs[4]);
			}
			if (color.equals(Color.RED)) {
				listeCouleurs.setSelectedItem(couleurs[5]);
			}
			if (color.equals(Color.GREEN)) {
				listeCouleurs.setSelectedItem(couleurs[6]);
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

		public JComboBox<Object> fastComboBox(ControleurParam controleur, Object[] elements) {
			JComboBox<Object> r = new JComboBox<Object>(elements);
			((JLabel) r.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			r.addActionListener(controleur);
			r.setBackground(Constants.BG_COLOR);
			r.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));
			return r;
		}

		public JRadioButton fastRadio(String nom, ControleurParam controleur) {
			JRadioButton r = new JRadioButton(nom);
			r.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			r.addActionListener(controleur);
			r.setVerticalTextPosition(JRadioButton.TOP);
			r.setHorizontalTextPosition(JRadioButton.CENTER);
			return r;
		}

		private JCheckBox fastCheckBox(String nom, ControleurParam controleur) {
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
		 * @param marge = taille de la marge
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
		Panneau.premierSegment = FenetreParametre.premierSegment;
		Panneau.defautNBEssaisParSegment = FenetreParametre.nbFautesTolerees;
		fenetre.start();
	}

}
