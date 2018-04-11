package main;

import java.awt.*;
import javax.swing.*;

public class FenetreParametre extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String police = "ressources/fonts/OpenDyslexic-Regular.otf";
	public static int taillePolice = 12;
	public static Color couleurFond = new Color(255, 255, 150);
	public static String titre;
	public static int tailleX;
	public static int tailleY;
	public static int nbSegments = 4;
	public static FenetreParametre fen;
	public static TextPane fenExercice;
	public static int nbFautesTolerees = 999;

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
		PanneauParam pan = new PanneauParam(this);
		setContentPane(pan);
		setVisible(true);
	}

	public class PanneauParam extends JPanel {

		private static final long serialVersionUID = 1L;

		JComboBox<Object> listePolices;
		JComboBox<Object> listeTailles;
		JComboBox<Object> listeCouleurs;
		JComboBox<Object> listeSegments;
		JTextField champNbFautesTolerees;
		JButton valider;
		FenetreParametre fen;

		public PanneauParam(FenetreParametre fen) {
			FenetreParametre.fen = fen;
			this.fen = fen;
			setLayout(new GridLayout(13, 1));
			JLabel titre = new JLabel("Choississez vos parametres");
			titre.setFont(new Font("OpenDyslexic", Font.BOLD, 20));
			add(titre);
			titre.setHorizontalAlignment(JLabel.CENTER);
			titre.setBorder(BorderFactory.createLineBorder(Color.blue, 2));

			valider = new JButton("Valider les parametres");
			valider.setFont(new Font("OpenDyslexic", Font.BOLD, 18));
			valider.setBackground(Color.green);

			JLabel police = new JLabel("Police : ");
			police.setFont(new Font("OpenDyslexic", Font.ITALIC, 16));
			police.setHorizontalAlignment(JLabel.CENTER);
			JLabel taillePolice = new JLabel("Taille de la police : ");
			taillePolice.setHorizontalAlignment(JLabel.CENTER);
			taillePolice.setFont(new Font("OpenDyslexic", Font.ITALIC, 16));
			JLabel couleurDeFond = new JLabel("Couleur de fond : ");
			couleurDeFond.setHorizontalAlignment(JLabel.CENTER);
			couleurDeFond.setFont(new Font("OpenDyslexic", Font.ITALIC, 16));
			JLabel segments = new JLabel("Nombre de segments par page : ");
			segments.setHorizontalAlignment(JLabel.CENTER);
			segments.setFont(new Font("OpenDyslexic", Font.ITALIC, 16));

			Object[] polices = new Object[] { "OpenDyslexic", "Andika", "Lexia", "Arial" };
			Object[] tailles = new Object[] { "12", "16", "18", "20", "22", "24", "30", "36", "42" };
			Object[] couleurs = new Object[] { "Jaune", "Blanc", "Orange" };
			Object[] nbSegments = new Object[] { "4", "5", "6", "8", "10", "15", "20", "25", "30", "40", "50" };

			ControleurParam controleur = new ControleurParam(this);
			valider.addActionListener(controleur);

			JPanel panneauListe1 = new JPanel(new GridLayout(1, 3));
			JPanel panneauListe2 = new JPanel(new GridLayout(1, 3));
			JPanel panneauListe3 = new JPanel(new GridLayout(1, 3));
			JPanel panneauListe4 = new JPanel(new GridLayout(1, 3));

			listePolices = new JComboBox<Object>(polices);
			((JLabel) listePolices.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listePolices.addActionListener(controleur);
			listePolices.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));
			listeTailles = new JComboBox<Object>(tailles);
			((JLabel) listeTailles.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeTailles.addActionListener(controleur);
			listeTailles.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));
			listeCouleurs = new JComboBox<Object>(couleurs);
			((JLabel) listeCouleurs.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeCouleurs.addActionListener(controleur);
			listeCouleurs.setBackground(new Color(255, 255, 150));
			listeCouleurs.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));
			listeSegments = new JComboBox<Object>(nbSegments);
			((JLabel) listeSegments.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			listeSegments.addActionListener(controleur);
			listeSegments.setFont(new Font("OpenDyslexic", Font.PLAIN, 15));

			panneauListe1.add(new JLabel());
			panneauListe1.add(listePolices);
			panneauListe1.add(new JLabel());
			panneauListe2.add(new JLabel());
			panneauListe2.add(listeTailles);
			panneauListe2.add(new JLabel());
			panneauListe4.add(new JLabel());
			panneauListe4.add(listeSegments);
			panneauListe4.add(new JLabel());
			panneauListe3.add(new JLabel());
			panneauListe3.add(listeCouleurs);
			panneauListe3.add(new JLabel());

			add(police);
			add(panneauListe1);
			add(taillePolice);
			add(panneauListe2);
			add(segments);
			add(panneauListe4);
			add(couleurDeFond);
			add(panneauListe3);

			JLabel nbFautesTolerees = new JLabel("Nombre de fautes tolérées : ");
			nbFautesTolerees.setHorizontalAlignment(SwingConstants.CENTER);
			nbFautesTolerees.setFont(new Font("OpenDyslexic", Font.ITALIC, 16));
			
			JPanel panneauChamp1 = new JPanel(new GridLayout(1, 3));
			panneauChamp1.add(new JLabel());
			champNbFautesTolerees = new JTextField();
			champNbFautesTolerees.addActionListener(controleur);
			champNbFautesTolerees.setHorizontalAlignment(JLabel.CENTER);
		
			panneauChamp1.add(champNbFautesTolerees);
			panneauChamp1.add(new JLabel());
			add(nbFautesTolerees);
			add(panneauChamp1);
			add(new JLabel());
			add(valider);

		}

		public void fermer() {
			fen.setVisible(false);
		}

	}

	public static void lancerExercice() {
		Panneau.defautNBSegmentsParPage = FenetreParametre.nbSegments;
		new Fenetre(titre, tailleX, tailleY);
	}

}
