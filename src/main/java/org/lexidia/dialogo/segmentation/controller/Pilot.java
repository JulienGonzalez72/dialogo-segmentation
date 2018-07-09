package org.lexidia.dialogo.segmentation.controller;

import org.lexidia.dialogo.segmentation.reading.ReadThread;
import org.lexidia.dialogo.segmentation.reading.ReaderFactory;
import org.lexidia.dialogo.segmentation.view.SegmentedTextPanel;

public class Pilot {

	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;
	private SegmentedTextPanel p;
	private ReaderFactory readerFactory;

	/**
	 * Segment actuel
	 */
	private int phrase;
	private boolean started;

	public Pilot(SegmentedTextPanel p) {
		this.p = p;
	}

	/**
	 * Se place sur le segment de numero n et démarre l'algorithme de lecture.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.param.startingPhrase || n > p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Numéro de segment invalide : " + n
					+ " (il doit être compris entre " + p.param.startingPhrase + " et " + p.textHandler.getPhrasesCount() + ")");
		}
		if (readerFactory == null) {
			throw new IllegalArgumentException("Usine de lecture non chargée !");
		}

		/// empêche le redimensionnement de la fenêtre à partir de la première lecture ///
		p.fenetre.setResizable(false);

		// met a jour la barre de progression
		updateBar();

		if (activeThread != null && activeThread.isRunning()) {
			activeThread.doStop();
		}
		
		activeThread = readerFactory.createReadThread();
		activeThread.N = phrase = n;
		activeThread.start();
		
		started = true;
	}

	private void updateBar() {
		p.progressBar.setValue(phrase + 1);
		p.progressBar.setString((phrase + 1) + "/" + (p.textHandler.getPhrasesCount()));
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
	
	public void setReaderFactory(ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public void end() {
		phrase = 0;
		updateBar();
		p.fenetre.setResizable(true);
		started = false;
	}
	
	public void updateCurrentPhrase() {
		if (activeThread != null) {
			phrase = activeThread.N;
		}
		/// met à jour la barre de progression ///
		updateBar();
	}

}
