package main;

import java.util.Timer;
import java.util.TimerTask;

public class Player {
	
	private TextHandler text;
	private int currentPhrase;
	private int currentCharacter;
	private boolean playing;
	
	private Timer timer;
	private PlayTask currentTask;
	
	public Player(TextHandler textHandler) {
		text = textHandler;
	}
	
	public void play() {
		timer = new Timer();
		currentTask = new PlayTask();
		timer.scheduleAtFixedRate(currentTask, 0, 20);
		playing = true;
	}
	
	private class PlayTask extends TimerTask {
		private long time;
		public void run() {
			time += 20;
			
			/// simule une lecture de caractère ///
			if (time % Constants.PLAYER_INTERVAL == 0) {
				System.out.print(getCurrentPhrase().charAt(currentCharacter));
				nextCharacter();
			}
		}
	}
	
	private void nextCharacter() {
		currentCharacter++;
		if (isPhraseFinished()) {
			stop();
		}
	}
	
	/**
	 * Arrête la lecture (n'a aucun effet si elle n'est pas en cours).
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
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
		System.out.println();
		currentCharacter = 0;
		currentPhrase++;
		play();
	}
	
	/**
	 * Se place directement à un segment donné sans démarrer le lecteur.
	 */
	public void goTo(int index) {
		stop();
		currentPhrase = index;
		currentCharacter = 0;
	}
	
}
