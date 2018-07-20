package org.lexidia.dialogo.segmentation.main;

/*import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.lexidia.dialogo.segmentation.reading.ReadMode;
import org.lexidia.dialogo.segmentation.view.FenetreParametre;
import org.lexidia.dialogo.segmentation.view.SegmentedTextFrame;*/

@Deprecated
public class Parametres {

	/*public Font police = FenetreParametre.getFont(null, 0, Font.BOLD, Constants.DEFAULT_FONT_SIZE);
	public int taillePolice = Constants.DEFAULT_FONT_SIZE;
	public Color bgColor = Constants.BG_COLOR;
	public Color rightColor = Constants.RIGHT_COLOR;
	public Color wrongColor = Constants.WRONG_COLOR;
	public Color correctionColor = Constants.WRONG_PHRASE_COLOR;
	public int startingPhrase = 1;
	public int nbFautesTolerees = 2;
	public int tempsPauseEnPourcentageDuTempsDeLecture = 100;
	public ReadMode readMode = ReadMode.SEGMENTE;
	public boolean rejouerSon = true;
	public int panWidth = Constants.DEFAULT_PAN_WIDTH;
	public int panHeight = Constants.DEFAULT_PAN_HEIGHT;
	public int panX = Constants.DEFAULT_PAN_X;
	public int panY = Constants.DEFAULT_PAN_Y;

	public Parametres(ReadMode readMode) {
		this.readMode = readMode;
	}

	public static String fromColorToString(Color c) {
		return (c.getRed() + "/" + c.getGreen() + "/" + c.getBlue());
	}

	public static Color fromStringToColor(String s) {
		String[] temp = s.split("/");
		return new Color(Integer.valueOf(temp[0]), Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
	}

	public void stockerPreference() {
		Properties prop = new Properties();
		prop.put("w", String.valueOf(panWidth));
		prop.put("h", String.valueOf(panHeight));
		prop.put("x", String.valueOf(panX));
		prop.put("y", String.valueOf(panY));
		prop.put("taillePolice", String.valueOf(taillePolice));
		prop.put("typePolice", police.getFontName());
		prop.put("couleurFond", fromColorToString(bgColor));
		prop.put("couleurBonne", fromColorToString(rightColor));
		prop.put("couleurFausse", fromColorToString(wrongColor));
		prop.put("couleurCorrection", fromColorToString(correctionColor));
		prop.put("tempsAttente", String.valueOf(tempsPauseEnPourcentageDuTempsDeLecture));
		prop.put("rejouerSon", String.valueOf(rejouerSon));
		prop.put("premierSegment", String.valueOf(startingPhrase));
		prop.put("nbEssais", String.valueOf(nbFautesTolerees));
		String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + "_" + readMode + ".txt";
		OutputStream ops = null;
		try {
			ops = new FileOutputStream(fichier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			prop.store(ops, "Sauvegarde");
			ops.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	*//**
	 * Applique les preferences de taille et position uniquement
	 *//*
	public void appliquerPreferenceTaillePosition(TextFrame fen) {
		fen.setBounds(panX, panY, panWidth, panHeight);
	}

	*//**
	 * Essaye de charger les param�tres associ�s � un mode de lecture, retourne les
	 * param�tres par d�faut sinon.
	 *//*
	public static Parametres load(ReadMode readMode) {
		Parametres p = new Parametres(readMode);

		String fichier = "./ressources/preferences/preference_" + Constants.NOM_ELEVE + "_" + readMode + ".txt";
		InputStream ips = null;
		try {
			ips = new FileInputStream(fichier);
		} catch (Exception e) {
			return p;
		}
		Properties pro = new Properties();
		try {
			pro.load(ips);
		} catch (IOException e) {
			e.printStackTrace();
		}

		p.taillePolice = Integer.valueOf(pro.getProperty("taillePolice"));
		String police = pro.getProperty("typePolice");
		int index = 999;
		if (police.equals("OpenDyslexic") || police.equals("OpenDyslexic Bold")) {
			index = 0;
		}
		if (police.equals("Andika")) {
			index = 1;
		}
		if (police.equals("Lexia")) {
			index = 2;
		}
		p.police = FenetreParametre.getFont(police, index, Font.BOLD, p.taillePolice);
		p.bgColor = fromStringToColor(pro.getProperty("couleurFond"));
		p.police = FenetreParametre.getFont(police, index, Font.BOLD, p.taillePolice);
		p.rightColor = fromStringToColor(pro.getProperty("couleurBonne"));
		p.wrongColor = fromStringToColor(pro.getProperty("couleurFausse"));
		p.correctionColor = fromStringToColor(pro.getProperty("couleurCorrection"));
		p.tempsPauseEnPourcentageDuTempsDeLecture = Integer.valueOf(pro.getProperty("tempsAttente"));
		p.panX = Integer.valueOf(pro.getProperty("x"));
		p.panY = Integer.valueOf(pro.getProperty("y"));
		p.panWidth = Integer.valueOf(pro.getProperty("w"));
		p.panHeight = Integer.valueOf(pro.getProperty("h"));
		p.startingPhrase = Integer.valueOf(pro.getProperty("premierSegment"));
		p.nbFautesTolerees = Integer.valueOf(pro.getProperty("nbEssais"));

		return p;
	}

	*//**
	 * Essaye de charger les param�tres associ�s � tous les modes de lecture
	 * existants.
	 *//*
	public static Map<ReadMode, Parametres> loadAll() {
		Map<ReadMode, Parametres> params = new HashMap<>();
		for (ReadMode rm : ReadMode.values()) {
			params.put(rm, load(rm));
		}
		return params;
	}*/

}
