package main;

import java.awt.*;
import javax.swing.*;

public class FenetreParametre extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String police = "ressources/fonts/Arial-Regular.otf";
	public static int taillePolice = 12;
	public static Color couleurFond = new Color(255, 255, 150);
	public static String titre;
	public static int tailleX;
	public static int tailleY;

	public static void main(String[] args) {
		new FenetreParametre("Parametres", 500, 500);
	}

	public FenetreParametre(String titre, int tailleX, int tailleY) {
		FenetreParametre.titre = titre;
		FenetreParametre.tailleX = tailleX;
		FenetreParametre.tailleY = tailleY;
		setTitle(titre);
		setSize(tailleX, tailleY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		PanneauParam pan = new PanneauParam();
		setContentPane(pan);
		setVisible(true);
	}

	public class PanneauParam extends JPanel {

		private static final long serialVersionUID = 1L;

		JComboBox<Object> listePolices;
		JComboBox<Object> listeTailles;
		JComboBox<Object> listeCouleurs;
		JButton valider;

		public PanneauParam() {
			setLayout(new GridLayout(9, 1));
			JLabel titre = new JLabel("Choississez vos parametres");
			titre.setFont(new Font("TimeNewsRoman", Font.BOLD, 20));
			add(titre);
			titre.setHorizontalAlignment(JLabel.CENTER);
			titre.setBorder(BorderFactory.createLineBorder(Color.blue, 10));

			valider = new JButton("Valider les parametres");
			valider.setFont(new Font("TimeNewsRoman", Font.BOLD, 18));
			valider.setBackground(Color.green);

			JLabel police = new JLabel("Police : ");
			police.setFont(new Font("TimeNewsRoman", Font.ITALIC, 16));
			police.setHorizontalAlignment(JLabel.CENTER);
			JLabel taillePolice = new JLabel("Taille de la police : ");
			taillePolice.setHorizontalAlignment(JLabel.CENTER);
			taillePolice.setFont(new Font("TimeNewsRoman", Font.ITALIC, 16));
			JLabel couleurDeFond = new JLabel("Couleur de fond : ");
			couleurDeFond.setHorizontalAlignment(JLabel.CENTER);
			couleurDeFond.setFont(new Font("TimeNewsRoman", Font.ITALIC, 16));

			Object[] polices = new Object[] { "Arial", "Andika", "Lexia", "OpenDyslexic" };
			Object[] tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };
			Object[] couleurs = new Object[] { "Jaune", "Blanc", "Orange" };

			ControleurParam controleur = new ControleurParam(this);
			valider.addActionListener(controleur);

			JPanel panneauListe1 = new JPanel(new GridLayout(1, 3));
			JPanel panneauListe2 = new JPanel(new GridLayout(1, 3));
			JPanel panneauListe3 = new JPanel(new GridLayout(1, 3));

			listePolices = new JComboBox<Object>(polices);
			((JLabel) listePolices.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listePolices.addActionListener(controleur);
			listePolices.setFont(new Font("Arial", Font.PLAIN, 12));
			listeTailles = new JComboBox<Object>(tailles);
			((JLabel) listeTailles.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeTailles.addActionListener(controleur);
			listeTailles.setFont(new Font("Arial", Font.PLAIN, 12));
			listeCouleurs = new JComboBox<Object>(couleurs);
			((JLabel) listeCouleurs.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeCouleurs.addActionListener(controleur);
			listeCouleurs.setBackground(Color.YELLOW);
			listeCouleurs.setFont(new Font("Arial", Font.PLAIN, 12));

			panneauListe1.add(new JLabel());
			panneauListe1.add(listePolices);
			panneauListe1.add(new JLabel());
			panneauListe2.add(new JLabel());
			panneauListe2.add(listeTailles);
			panneauListe2.add(new JLabel());
			panneauListe3.add(new JLabel());
			panneauListe3.add(listeCouleurs);
			panneauListe3.add(new JLabel());

			add(police);
			add(panneauListe1);
			add(taillePolice);
			add(panneauListe2);
			add(couleurDeFond);
			add(panneauListe3);
			add(new JLabel());
			add(valider);

		}

	}

	public static void lancerExercice() {
		new Fenetre(titre, tailleX, tailleY);
	}

}
