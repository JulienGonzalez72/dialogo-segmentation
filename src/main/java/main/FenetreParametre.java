package main;

import java.awt.*;
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
	
	public Fenetre fenetre;

	public FenetreParametre(String titre, int tailleX, int tailleY) {
		FenetreParametre.titre = titre;
		FenetreParametre.tailleX = tailleX;
		FenetreParametre.tailleY = tailleY;
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(true);
		PanneauParam pan = new PanneauParam();
		setContentPane(pan);
		setVisible(true);
		fenetre = new Fenetre(titre, tailleX, tailleY);
	}

	public class PanneauParam extends JPanel {

		private static final long serialVersionUID = 1L;

		JComboBox<Object> listePolices;
		JComboBox<Object> listeTailles;
		JComboBox<Object> listeCouleurs;
		JTextField segmentDeDepart;
		JTextField champNbFautesTolerees;
		JButton valider;

		public PanneauParam() {
			fen = FenetreParametre.this;
			setLayout(new GridLayout(13, 1));
			JLabel titre = fastLabel("Choississez vos parametres");
			titre.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
			add(titre);

			valider = fastButton("Valider les parametres", new Font("OpenDyslexic", Font.BOLD, 18), Color.green);
			JLabel police = fastLabel("Police : ");
			JLabel taillePolice = fastLabel("Taille de la police : ");
			JLabel couleurDeFond = fastLabel("Couleur de fond : ");
			JLabel segments = fastLabel("Segment de départ ");

			final Object[] polices = new Object[] { "OpenDyslexic", "Andika", "Lexia", "Arial", "Times New Roman" };
			Object[] tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };
			Object[] couleurs = new Object[] { "Jaune", "Blanc", "Orange", "Rose", "Bleu" };

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

			listeCouleurs = new JComboBox<Object>(couleurs);
			((JLabel) listeCouleurs.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeCouleurs.addActionListener(controleur);
			listeCouleurs.setBackground(new Color(255, 255, 150));
			listeCouleurs.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));

			segmentDeDepart = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "1");
			segmentDeDepart.addActionListener(controleur);

			add(police);
			fastCentering(listePolices, this);
			add(taillePolice);
			fastCentering(listeTailles, this);
			add(segments);
			fastCentering(segmentDeDepart, this);

			JLabel nbFautesTolerees = fastLabel("Nombre de fautes maximum");
			champNbFautesTolerees = fastTextField("", new Font("OpenDyslexic", Font.PLAIN, 15), "2");
			champNbFautesTolerees.addActionListener(controleur);

			add(nbFautesTolerees);
			fastCentering(champNbFautesTolerees, this);
			add(couleurDeFond);
			fastCentering(listeCouleurs, this);
			add(new JLabel());
			add(valider);

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
