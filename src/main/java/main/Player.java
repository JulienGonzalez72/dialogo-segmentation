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
	
	public Runnable onPhraseEnd;
	
	public Player(TextHandler textHandler) {
		text = textHandler;
	}
	
	public void play() {
		stop();
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
			
			/// simule une lecture de caract�re ///
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
	 * Arr�te la lecture (n'a aucun effet si elle n'est pas en cours).
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
		playing = false;
	}
	
	/**
	 * Indique si le segment a finis d'�tre prononc�.
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
	 * Retourne la phrase courante du lecteur, qu'elle soit finie d'�tre prononc�e ou non.
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
	 * Passe au segment suivant et d�marre le lecteur.
	 */
	public void nextPhrase() {
		System.out.println();
		currentCharacter = 0;
		currentPhrase++;
		play();
	}
	
	/**
	 * Retourne true si il reste au moins un segment � lire.
	 */
	public boolean hasNextPhrase() {
		return currentPhrase < text.getPhrasesCount() - 1;
	}
	
	/**
	 * Retourne au segment pr�d�dent et d�marre le lecteur.
	 */
	public void previousPhrase() {
		System.out.println();
		currentCharacter = 0;
		currentPhrase--;
		play();
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
		play();
	}
	
	/**
	 * Se place directement � un segment donn� sans d�marrer le lecteur.
	 */
	public void goTo(int index) {
		stop();
		currentPhrase = index;
		currentCharacter = 0;
	}
	
}
