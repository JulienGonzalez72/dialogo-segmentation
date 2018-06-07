package main.controler;

import main.reading.ReadThread;
import main.view.TextPanel;

public class Pilot {

	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;
	private TextPanel p;

	/**
	 * Segment actuel
	 */
	private int phrase;

	public Pilot(TextPanel p) {
		this.p = p;
	}

	/**
	 * Se place sur le segment de numero n et démarre l'algorithme de lecture.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.param.startingPhrase - 1 || n > p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Numéro de segment invalide : " + (n + 1));
		}
		if (activeThread == null) {
			throw new IllegalArgumentException("Algorithme de lecture non chargé !");
		}
		activeThread.N = phrase = n;

		/// empêche le redimensionnement de la fenêtre à partir de la première lecture ///
		p.fenetre.setResizable(false);

		// met a jour la barre de progression
		updateBar();

		if (activeThread.isRunning()) {
			activeThread.doStop();
		}
		activeThread.onPhraseEnd.add(new Runnable() {
			public void run() {
				phrase = activeThread.N;
				/// met à jour la barre de progression ///
				updateBar();
			}
		});
		activeThread.start();
	}

	private void updateBar() {
		p.progressBar.setValue(phrase + 1);
		p.progressBar.setString((phrase) + "/" + (p.textHandler.getPhrasesCount() - 1));
	}

	/**
	 * Essaye de passer au segment suivant, passe à la page suivante si c'était le
	 * dernier segment de la page. Déclenche une erreur si on était au dernier
	 * segment du texte.
	 */
	public void doNext() {
		goTo(phrase + 1);
	}

	/**
	 * Essaye de passer au segment précédent. Déclenche une erreur si on était au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		goTo(phrase - 1);
	}

	/**
	 * Essaye d'arrêter l'enregistrement en cours.
	 */
	public void doStop() {
		if (activeThread != null) {
			activeThread.doStop();
		}
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est déjà démarré, reprend depuis
	 * le début.
	 */
	public void doPlay() {
		goTo(phrase);
	}

	/**
	 * Retourne l'indice du segment actuel dans le texte (en partant de 0).
	 */
	public int getCurrentPhraseIndex() {
		return phrase;
	}
	
	public boolean isRunning() {
		return activeThread != null && activeThread.isRunning();
	}

	public boolean hasPreviousPhrase() {
		return phrase > p.param.startingPhrase;
	}

	/**
	 * Charge un thread de la même classe que r. Indispensable avant une quelconque manipulation de contrôle.
	 */
	public void loadReadThread(ReadThread r) {
		this.activeThread = r;
	}

}
