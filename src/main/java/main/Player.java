package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {

	private TextHandler text;
	private int currentPhrase;
	private int currentCharacter;
	private boolean playing, blocked;

	private Timer timer;
	private PlayTask currentTask;

	private Clip clip;

	public List<Runnable> onPhraseEnd = new ArrayList<>();
	public List<Runnable> onNextPhrase = new ArrayList<>();
	public List<Runnable> onPlay = new ArrayList<>();

	public Player(TextHandler textHandler) {
		text = textHandler;
	}

	public Player(AudioInputStream audioStream) {

	}

	public void play() {
		// stop();
		for (Runnable r : onPlay) {
			r.run();
		}
		try {
			clip = AudioSystem.getClip();
			clip.open(getAudioStream(Constants.AUDIO_FILE_NAME, currentPhrase));
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		timer = new Timer();
		currentTask = new PlayTask();
		timer.scheduleAtFixedRate(currentTask, 0, 20);
		playing = true;
	}

	private class PlayTask extends TimerTask {
		private long time;

		public void run() {
			time += 20;

			/// fin de la phrase ///
			if (isPhraseFinished()) {
				for (Runnable r : onPhraseEnd) {
					r.run();
				}
				stop();
			}

			/// simule une lecture de caractère ///
			else if (time % Constants.PLAYER_INTERVAL == 0) {
				System.out.print(getCurrentPhrase().charAt(currentCharacter));
				nextCharacter();
			}
		}
	}

	private void nextCharacter() {
		currentCharacter++;
	}

	/**
	 * Arrête la lecture (n'a aucun effet si elle n'est pas en cours).
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
		if (clip != null) {
			clip.stop();
		}
		playing = false;
	}

	/**
	 * Indique si le segment a finis d'être prononcé.
	 */
	public boolean isPhraseFinished() {
		return currentCharacter >= getCurrentPhrase().length();
	}

	/**
	 * Indique si le lecteur est en train de prononcer le segment.
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Indique
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * Retourne la phrase courante du lecteur, qu'elle soit finie d'être prononcée
	 * ou non.
	 */
	public String getCurrentPhrase() {
		return text.getPhrase(currentPhrase);
	}

	/**
	 * Retourne l'indice du segment actuel.
	 */
	public int getCurrentPhraseIndex() {
		return currentPhrase;
	}

	/**
	 * Passe au segment suivant et démarre le lecteur.
	 */
	public void nextPhrase() {
		for (Runnable r : onNextPhrase) {
			r.run();
		}
		System.out.println();
		currentCharacter = 0;
		currentPhrase++;
		// play();
	}

	/**
	 * Retourne true si il reste au moins un segment à lire.
	 */
	public boolean hasNextPhrase() {
		return currentPhrase < text.getPhrasesCount() - 1;
	}

	/**
	 * Retourne au segment prédédent et démarre le lecteur.
	 */
	public void previousPhrase() {
		System.out.println();
		currentCharacter = 0;
		currentPhrase--;
		// play();
	}

	/**
	 * Retourne true si il y a au moins un segment avant le segment actuel.
	 */
	public boolean hasPreviousPhrase() {
		return currentPhrase > 0;
	}

	/**
	 * Recommence la phrase.
	 */
	public void repeat() {
		System.out.println();
		currentCharacter = 0;
		// play();
	}

	/**
	 * Se place directement à un segment donné sans démarrer le lecteur.
	 */
	public void goTo(int index) {
		stop();
		currentPhrase = index;
		currentCharacter = 0;
	}

	private static AudioInputStream getAudioStream(String fileName, int n) {
		try {
			return AudioSystem
					.getAudioInputStream(new File("ressources/sounds/" + fileName + "(" + format(n) + ").wav"));
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String format(int n) {
		String str = String.valueOf(n);
		for (int i = str.length(); i < 3; i++) {
			str = "0" + str;
		}
		return str;
	}

}
