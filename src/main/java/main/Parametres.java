package main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import main.controler.ControleurParam;
import main.reading.ReadMode;
import main.view.FenetreParametre;
import main.view.Panneau;

public class Parametres {

	public Font police = ControleurParam.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
	public int taillePolice = Constants.DEFAULT_FONT_SIZE;
	public Color couleurFond = Constants.BG_COLOR;
	public String titre;
	public int tailleX;
	public int tailleY;
	public int premierSegment;
	public int nbFautesTolerees;
	public int tempsPauseEnPourcentageDuTempsDeLecture;
	public ReadMode readMode = ReadMode.SEGMENTE;
	public boolean rejouerSon = true;
	public int panWidth, panHeight, panX, panY;

	public Parametres() {

	}

	public void stockerPreference() {
		String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + ".txt";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fichier);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.println("w:" + panWidth);
		writer.println("h:" + panHeight);
		writer.println("x:" + panX);
		writer.println("y:" + panY);
		writer.println("taillePolice:" + taillePolice);
		writer.println("typePolice:" + police.getFontName());
		writer.println(
				"couleur:" + couleurFond.getRed() + "/" + couleurFond.getGreen() + "/" + couleurFond.getBlue() + "/"

						+ Constants.RIGHT_COLOR.getRed() + "/" + Constants.RIGHT_COLOR.getGreen() + "/"
						+ Constants.RIGHT_COLOR.getBlue() + "/"

						+ Constants.WRONG_COLOR.getRed() + "/" + Constants.WRONG_COLOR.getGreen() + "/"
						+ Constants.WRONG_COLOR.getBlue() + "/"

						+ Constants.WRONG_PHRASE_COLOR.getRed() + "/" + Constants.WRONG_PHRASE_COLOR.getGreen() + "/"
						+ Constants.WRONG_PHRASE_COLOR.getBlue());
		writer.println("mode:" + readMode);
		writer.println("tempsAttente:" + tempsPauseEnPourcentageDuTempsDeLecture);
		writer.println("rejouerSon:" + rejouerSon);
		writer.close();
	}

	public void appliquerPreference(FenetreParametre fen,Panneau pan) {
		// lecture du fichier texte
		String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + ".txt";
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
					case "GUIDEE":
						mode = ReadMode.GUIDEE;
						break;
					case "SUIVI":
						mode = ReadMode.SUIVI;
						break;
					case "SEGMENTE":
						mode = ReadMode.SEGMENTE;
						break;
					case "ANTICIPE":
						mode = ReadMode.ANTICIPE;
						break;
					}
					readMode = mode;
					break;
				case 8:
					tempsPause = Integer.valueOf(ligne.split(":")[1]);
					break;
				case 9:
					rejouerSon = Boolean.valueOf(ligne.split(":")[1]);
					break;
				default:
					break;
				}
				i++;
			}
			pan.fenetre.setBounds(x, y, w, h);
			taillePolice = t;
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
			police = ControleurParam.getFont(p, index, Font.BOLD, t);
			((FenetreParametre.PanneauParam) fen.getContentPane()).listeCouleurs
					.setBackground(couleurFond = color);
			pan.editorPane.setFont(police);
			pan.editorPane.setBackground(color);
			Constants.RIGHT_COLOR = rightColor;
			Constants.WRONG_COLOR = wrongColor;
			Constants.WRONG_PHRASE_COLOR = correctionColor;
			tempsPauseEnPourcentageDuTempsDeLecture = tempsPause;
			((FenetreParametre.PanneauParam) fen.getContentPane()).sliderAttente.setValue(tempsPause);
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		pan.rebuildPages();
	}

}
