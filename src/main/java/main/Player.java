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
	private boolean playing, blocked;

	private Timer timer;
	private PlayTask playTask;
	private WaitTask waitTask;

	private Clip clip;
	private long lastPosition;

	public List<Runnable> onPhraseEnd = new ArrayList<>();
	public List<Runnable> onNextPhrase = new ArrayList<>();
	public List<Runnable> onPlay = new ArrayList<>();
	/**
	 * Ecouteurs qui s'enclenchent lorsque le temps de pause après la fin de
	 * l'enregistrement se termine.
	 */
	public List<Runnable> onBlockEnd = new ArrayList<>();

	public Player(TextHandler textHandler) {
		text = textHandler;
	}

	public Player(AudioInputStream audioStream) {

	}

	/**
	 * Démarre la lecture (n'a aucun effet si la lecture est déjà démarrée).
	 */
	public void play() {
		if (playing) {
			return;
		}
		try {
			clip = AudioSystem.getClip();
			clip.open(getAudioStream(Constants.AUDIO_FILE_NAME, currentPhrase));
			clip.setMicrosecondPosition(lastPosition);
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		timer = new Timer();
		playTask = new PlayTask();
		timer.scheduleAtFixedRate(playTask, 0, 20);
		playing = true;
		for (Runnable r : onPlay) {
			r.run();
		}
	}

	private class PlayTask extends TimerTask {
		public void run() {
			/// fin de la phrase ///
			if (isPhraseFinished()) {
				stop();
				lastPosition = 0;
				blocked = true;
				waitTask = new WaitTask();
				timer.scheduleAtFixedRate(waitTask, 0, 20);
				for (Runnable r : onPhraseEnd) {
					r.run();
				}
			}
		}
	}

	private class WaitTask extends TimerTask {
		private long time;

		public void run() {
			time += 20;

			/// fin du blocage ///
			if (blocked && time > clip.getMicrosecondPosition() / 1000) {
				blocked = false;
				cancel();
				for (Runnable r : onBlockEnd) {
					r.run();
				}
			}
		}
	}

	/**
	 * Arrête la lecture (n'a aucun effet si elle n'est pas en cours).
	 */
	public void stop() {
		if (playTask != null) {
			playTask.cancel();
		}
		if (clip != null) {
			clip.stop();
		}
		playing = false;
	}

	/**
	 * Mets en pause l'enregistrement.
	 */
	public void pause() {
		stop();
		lastPosition = clip.getMicrosecondPosition();
	}

	/**
	 * Indique si le segment a finis d'être prononcé.
	 */
	public boolean isPhraseFinished() {
		return clip != null ? clip.getFramePosition() == clip.getFrameLength() : false;
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
		currentPhrase++;
		repeat();
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
		currentPhrase--;
		repeat();
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
		stop();
		play();
	}

	/**
	 * Se place directement à un segment donné sans démarrer le lecteur.
	 */
	public void goTo(int index) {
		stop();
		currentPhrase = index;
	}

	private static AudioInputStream getAudioStream(String fileName, int n) {
		try {
			return AudioSystem.getAudioInputStream(
					new File("ressources/sounds/" + fileName + "/" + fileName + "(" + format(n + 1) + ").wav"));
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
