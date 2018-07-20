package org.lexidia.dialogo.segmentation.model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lexidia.dialogo.segmentation.main.Constants;
//import org.lexidia.dialogo.segmentation.main.Parametres;

/**
 * Classe pour tester le son.
 * @author Julien Gonzalez
 */
public class Player /*extends AbstractDelegatingBasicController<BasicController>*/ {

	/**
	 * Liste des fichiers audio nécessaires é l'enregistrement en cours chargés au début, associés é leurs numéros de segment. 
	 */
	private static Map<Integer, File> tracks;
	
	private TextHandler text;
	private int currentPhrase;
	private boolean playing, blocked;

	private Timer timer;
	private PlayTask playTask;
	private WaitTask waitTask;

	private Clip clip;
	private long lastPosition;

	/**
	 * Si un temps de pause s'effectue aprés l'enregistrement (valeur par défaut =
	 * <code>false</code>).
	 */
	private boolean waitAfter = false;

	/**
	 * Ecouteurs qui s'enclenchent lorsque un segment a finis d'étre prononcé.
	 */
	private List<Runnable> onPhraseEnd = new ArrayList<>();
	private List<Runnable> onNextPhrase = new ArrayList<>();
	private List<Runnable> onPreviousPhrase = new ArrayList<>();
	/**
	 * Ecouteurs qui s'enclenchent lorsque l'enregistrement est lancé.
	 */
	private List<Runnable> onPlay = new ArrayList<>();
	/**
	 * Ecouteurs qui s'enclenchent lorsque le temps de pause de
	 * l'enregistrement se termine.
	 */
	private List<Runnable> onBlockEnd = new ArrayList<>();
	/**
	 * Ecouteurs qui se déclenchent lorsque l'utilisateur est mis en attente pour répéter.
	 */
	private List<Runnable> onWait = new ArrayList<>();
	
	//private Parametres param;
	
	//private Mp3Wrapper wrapper;

	public Player(TextHandler textHandler/*, Parametres param*/) {
		/*super(null);
		IPlayerConfiguration config = new IPlayerConfiguration() {
			@Override
			public String getSamplesRootPath() {
				return "ressources/sounds";
			}
			@Override
			public float getPlayerSpeedModifier() {
				return 1;
			}
			@Override
			public float getPlayerAutoPauseFactor() {
				return 1;
			}
			@Override
			public int getPlayerAutoPauseConstant() {
				return 1;
			}
			@Override
			public float getBasicPlayerSetPositionFactor() {
				return 1;
			}
			@Override
			public int getBasicPlayerSetPositionConstant() {
				return 1;
			}
		};
		setDelegate(new AutoPauseMarkerFilePlayer(new JlayerPlayer(), config));
		wrapper = getWrapper(Constants.AUDIO_FILE_NAME);
		try {
			super.open(wrapper);
		} catch (BasicPlayerException ex) {
			ex.printStackTrace();
		}*/
		text = textHandler;
		//this.param = param;
	}

	/**
	 * Charge l'enregistrement correspondant é un segment précis.
	 */
	public void load(int phrase) {
		currentPhrase = phrase;
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(tracks.get(Constants.HAS_INSTRUCTIONS ? phrase + 2 : phrase + 1)));
			clip.setMicrosecondPosition(lastPosition);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Joue un segment de phrase.
	 */
	public void play(int phrase) {
		stop();
		lastPosition = 0;
		currentPhrase = phrase;
		play();
	}

	/**
	 * Démarre la lecture (n'a aucun effet si la lecture est déjé démarrée).
	 */
	public void play() {
		if (playing) {
			return;
		}
		load(currentPhrase);
		//try {
			clip.start();
			//super.play(10000);
		//} catch (BasicPlayerException ex) {
			//ex.printStackTrace();
		//}
		//timer = new Timer();
		//playTask = new PlayTask();
		//timer.scheduleAtFixedRate(playTask, 0, 20);
		playing = true;
		for (Runnable r : getOnPlay()) {
			r.run();
		}
	}

	private class PlayTask extends TimerTask {
		public void run() {
			/// fin de la phrase ///
			if (isPhraseFinished()) {
				stop();
				lastPosition = 0;
				for (Runnable r : getOnPhraseEnd()) {
					r.run();
				}
				if (isWaitAfter()) {
					doWait();
				}
			}
		}
	}

	/**
	 * Marque un temps de pause. Ne fait rien si la pause est en cours.
	 */
	public void doWait() {
		if (blocked)
			return;
		if (clip == null) {
			load(currentPhrase);
		}
		blocked = true;
		waitTask = new WaitTask();
		if (playing) {
			timer.cancel();
		}
		timer = new Timer();
		timer.scheduleAtFixedRate(waitTask, 0, 20);
		for (Runnable r : getOnWait()) {
			r.run();
		}
	}

	private class WaitTask extends TimerTask {
		private long time;

		public void run() {
			time += 20;

			/// fin du blocage ///
			if (blocked && time > clip.getMicrosecondLength() / 1000
					/* param.tempsPauseEnPourcentageDuTempsDeLecture / 100.*/) {
				blocked = false;
				cancel();
				for (Runnable r : getOnBlockEnd()) {
					r.run();
				}
			}
		}
	}

	/**
	 * Arréte la lecture (n'a aucun effet si elle n'est pas en cours).
	 */
	public void stop() {
		if (clip != null) {
			clip.stop();
		}
		if (playTask != null) {
			playTask.cancel();
			playTask = null;
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
	 * Indique si le segment a finis d'étre prononcé.
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
	 * Retourne la phrase courante du lecteur, qu'elle soit finie d'étre prononcée
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
		for (Runnable r : getOnNextPhrase()) {
			r.run();
		}
		currentPhrase++;
		repeat();
	}

	/**
	 * Retourne true si il reste au moins un segment é lire.
	 */
	public boolean hasNextPhrase() {
		return currentPhrase < text.getPhrasesCount() - 2;
	}

	/**
	 * Retourne au segment prédédent et démarre le lecteur.
	 */
	public void previousPhrase() {
		for (Runnable r : getOnPreviousPhrase()) {
			r.run();
		}
		currentPhrase--;
		repeat();
	}

	/**
	 * Retourne true si il y a au moins un segment avant le segment actuel.
	 */
	public boolean hasPreviousPhrase() {
		return currentPhrase > /*param.startingPhrase - 1*/0;
	}

	/**
	 * Recommence la phrase.
	 */
	public void repeat() {
		lastPosition = 0;
		stop();
		play();
	}

	/**
	 * Se place directement é un segment donné sans démarrer le lecteur.
	 */
	public void goTo(int index) {
		lastPosition = 0;
		stop();
		currentPhrase = index;
	}
	
	/**
	 * Retourne la durée en millisecondes de l'enregistrement courant.
	 */
	public long getDuration() {
		return clip != null ? clip.getMicrosecondLength() / 1000 : 0;
	}
	
	/*private static Mp3Wrapper getWrapper(String fileName) {
		return new Mp3Wrapper(new File("ressources/sounds/" + fileName + "/" + fileName + ".mp3"));
	}*/

	private static String format(int n) {
		String str = String.valueOf(n);
		for (int i = str.length(); i < 3; i++) {
			str = "0" + str;
		}
		return str;
	}

	public void setCurrentPhrase(int currentPhrase) {
		this.currentPhrase = currentPhrase;
	}
	
	/*public void setParameters(Parametres param) {
		this.param = param;
	}*/
	
	/**
	 * Charge tous les fichiers audio pour les rendre préts é l'utilisation.
	 */
	public static void loadAll(String filePath, String fileName, int startPhrase, int endPhrase) throws IOException {
		tracks = new HashMap<>();
		for (int i = startPhrase; i <= endPhrase; i++) {
			String num = format(i);
			String path = filePath + "/" + fileName + "(" + num + ").wav";
			File file = new File(path);
			if (file.exists()) {
				tracks.put(i, file);
			}
			else {
				throw new IOException(path + " (Le fichier spécifié est introuvable)");
			}
		}
	}

	public boolean isWaitAfter() {
		return waitAfter;
	}

	public void setWaitAfter(boolean waitAfter) {
		this.waitAfter = waitAfter;
	}

	public List<Runnable> getOnPhraseEnd() {
		return onPhraseEnd;
	}

	public void setOnPhraseEnd(List<Runnable> onPhraseEnd) {
		this.onPhraseEnd = onPhraseEnd;
	}

	public List<Runnable> getOnNextPhrase() {
		return onNextPhrase;
	}

	public void setOnNextPhrase(List<Runnable> onNextPhrase) {
		this.onNextPhrase = onNextPhrase;
	}

	public List<Runnable> getOnPreviousPhrase() {
		return onPreviousPhrase;
	}

	public void setOnPreviousPhrase(List<Runnable> onPreviousPhrase) {
		this.onPreviousPhrase = onPreviousPhrase;
	}

	public List<Runnable> getOnPlay() {
		return onPlay;
	}

	public void setOnPlay(List<Runnable> onPlay) {
		this.onPlay = onPlay;
	}

	public List<Runnable> getOnBlockEnd() {
		return onBlockEnd;
	}

	public void setOnBlockEnd(List<Runnable> onBlockEnd) {
		this.onBlockEnd = onBlockEnd;
	}

	public List<Runnable> getOnWait() {
		return onWait;
	}

	public void setOnWait(List<Runnable> onWait) {
		this.onWait = onWait;
	}

	/*@Override
	public void disactivateControls() {
	}

	@Override
	public void reactivateControls() {
	}*/

}
