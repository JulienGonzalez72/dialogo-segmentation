package main;

import java.io.*;
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
	
	public Runnable onPhraseEnd;
	public Runnable onNextPhrase;
	
	public Player(TextHandler textHandler) {
		text = textHandler;
	}
	
	public Player(AudioInputStream audioStream) {
		
	}
	
	public void play(AudioInputStream audioStream) {
		stop();
		try {
			clip = AudioSystem.getClip();
			clip.open(audioStream);
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
				if (onPhraseEnd != null)
					onPhraseEnd.run();
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
		clip.stop();
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
	 * Retourne la phrase courante du lecteur, qu'elle soit finie d'être prononcée ou non.
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
		if (onNextPhrase != null)
			onNextPhrase.run();
		System.out.println();
		currentCharacter = 0;
		currentPhrase++;
		//play();
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
		//play();
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
		//play();
	}
	
	/**
	 * Se place directement à un segment donné sans démarrer le lecteur.
	 */
	public void goTo(int index) {
		stop();
		currentPhrase = index;
		currentCharacter = 0;
	}
	
	public static AudioInputStream getAudioStream(String fileName) {
		return AudioSystem.getAudioInputStream(new File(arg0))
	}
	
}
