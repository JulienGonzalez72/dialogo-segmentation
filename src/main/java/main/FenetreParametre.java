package main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.*;

public class FenetreParametre extends JFrame {

	private static final long serialVersionUID = 1L;
	public static Font police = ControleurParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
	public static int taillePolice = Constants.DEFAULT_FONT_SIZE;
	public static Color couleurFond = new Color(255, 255, 150);
	public static String titre;
	public static int tailleX;
	public static int tailleY;
	public static int premierSegment;
	public static FenetreParametre fen;
	public static TextPane editorPane;
	public static int nbFautesTolerees;
	//public static boolean modeSurlignage;
	public static int tempsPauseEnPourcentageDuTempsDeLecture;
	//public static boolean modeLectureGuidee = false;
	public static ReadMode readMode = ReadMode.HIGHLIGHT;
	public Fenetre fenetre;

	public FenetreParametre(String titre, int tailleX, int tailleY) {
		setIconImage(getToolkit().getImage("icone.jpg"));
		FenetreParametre.police = ControleurParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
		FenetreParametre.taillePolice = Constants.DEFAULT_FONT_SIZE;
		FenetreParametre.couleurFond = new Color(255, 255, 150);
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

		JPanel panelModes;
		JComboBox<Object> listePolices;
		JComboBox<Object> listeTailles;
		JComboBox<Object> listeCouleurs;
		JTextField segmentDeDepart;
		JTextField champNbFautesTolerees;
		JButton valider;
		JRadioButton modeSurlignage, modeKaraoke, modePasDispo;
		JSlider sliderAttente;
		final Object[] polices;
		final Object[] tailles;
		final Object[] couleurs;

		public PanneauParam() throws NumberFormatException, IOException {
			fen = FenetreParametre.this;
			setLayout(new GridLayout(18, 1));
			JLabel titre = fastLabel("Choisissez vos parametres");
			titre.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
			add(titre);

			valider = fastButton("Valider les parametres", new Font("OpenDyslexic", Font.BOLD, 18), Color.green);
			JLabel police = fastLabel("Police : ");
			JLabel taillePolice = fastLabel("Taille de la police : ");
			JLabel couleurDeFond = fastLabel("Couleur de fond : ");
			JLabel segments = fastLabel("Segment de départ ");
			JLabel attente = fastLabel("Temps d'attente en % du temps de lecture");

			polices = new Object[] { "OpenDyslexic", "Andika", "Lexia", "Arial", "Times New Roman" };
			tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };
			couleurs = new Object[] { "Jaune", "Blanc", "Orange", "Rose", "Bleu" };

			ControleurParam controleur = new ControleurParam(this);
			addFocusListener(controleur);
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

			listeCouleurs = new JComboBox<Object>(couleurs);
			((JLabel) listeCouleurs.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeCouleurs.addActionListener(controleur);
			listeCouleurs.setBackground(new Color(255, 255, 150));
			listeCouleurs.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));

			segmentDeDepart = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "1");
			segmentDeDepart.addActionListener(controleur);
			segmentDeDepart.addFocusListener(controleur);

			add(police);
			fastCentering(listePolices, this);
			add(taillePolice);
			fastCentering(listeTailles, this);
			add(segments);
			fastCentering(segmentDeDepart, this);

			JLabel nbFautesTolerees = fastLabel("Nombre de fautes maximum");
			champNbFautesTolerees = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "2");
			champNbFautesTolerees.addActionListener(controleur);
			champNbFautesTolerees.addFocusListener(controleur);
			
			modeSurlignage = new JRadioButton("Mode surlignage");
			modeSurlignage.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			modeSurlignage.addActionListener(controleur);
			
			modeKaraoke = new JRadioButton("Mode Lecture guidée");
			modeKaraoke.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			modeKaraoke.addActionListener(controleur);
			
			modePasDispo = new JRadioButton("Mode Normal");
			modePasDispo.setFont(new Font("OpenDyslexic", Font.ITALIC, 15));
			modePasDispo.addActionListener(controleur);

			add(nbFautesTolerees);
			fastCentering(champNbFautesTolerees, this);
			add(couleurDeFond);
			fastCentering(listeCouleurs, this);
			add(new JLabel());
			
			panelModes = new JPanel(new GridLayout(1, 3));
			panelModes.add(modeSurlignage);
			panelModes.add(modeKaraoke);
			panelModes.add(modePasDispo);
			add(panelModes);
			add(new JLabel());
			add(attente);

			sliderAttente = new JSlider();
			sliderAttente.setMaximum(Constants.MAX_WAIT_TIME_PERCENT);
			sliderAttente.setMinimum(Constants.MIN_WAIT_TIME_PERCENT);
			sliderAttente.setValue(Constants.DEFAULT_FONT_SIZE);
			sliderAttente.setPaintTicks(true);
			sliderAttente.setPaintLabels(true);
			sliderAttente.setMinorTickSpacing(10);
			sliderAttente.setMajorTickSpacing(50);
			sliderAttente.addChangeListener(controleur);

			// fastCentering(sliderAttente,this);
			add(sliderAttente);

			add(new JLabel());
			add(valider);

			String fichier = "preference.txt";
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
						if (p.equals("Andika")) {
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
						if (color == new Color(255, 255, 150)) {
							listeCouleurs.setSelectedItem(couleurs[0]);
						}
						// "Jaune", "Blanc", "Orange", "Rose", "Bleu"
						if (color == Color.WHITE) {
							listeCouleurs.setSelectedItem(couleurs[2]);
						}
						if (color == Color.ORANGE) {
							listeCouleurs.setSelectedItem(couleurs[3]);
						}
						if (color == Color.PINK) {
							listeCouleurs.setSelectedItem(couleurs[4]);
						}
						if (color == Color.BLUE) {
							listeCouleurs.setSelectedItem(couleurs[5]);
						}
						break;
					case 7:
						modeSurlignage.setSelected(Boolean.valueOf(ligne.split(":")[1]));
						break;
					case 8:
						FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture = Integer.valueOf(ligne.split(":")[1]);
						sliderAttente.setValue(Integer.valueOf(ligne.split(":")[1]));
						break;
					default:
						break;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {

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
		 */
		public void fastCentering(Component c, JPanel p) {
			JPanel temp = new JPanel(new GridLayout(1, 3));
			temp.add(new JLabel());
			temp.add(c);
			temp.add(new JLabel());
			p.add(temp);
		}

	}

	public void lancerExercice() {
		Panneau.premierSegment = FenetreParametre.premierSegment;
		Panneau.defautNBEssaisParSegment = FenetreParametre.nbFautesTolerees;
		fenetre.start();
	}

}
