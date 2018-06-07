package main.controler;

import main.reading.ReadThread;
import main.view.TextPanel;

// TODO classe non fonctionnelle
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
	 * Se place sur le segment de numero n et démarre le lecteur.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.param.startingPhrase - 1 || n >= p.textHandler.getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Numéro de segment invalide : " + n);
		}
		phrase = n;
		p.editorPane.enleverSurlignageRouge();

		/// empêche le redimensionnement de la fenêtre lors de la première lecture ///
		p.fenetre.setResizable(false);

		// met a jour la barre de progression
		updateBar();

		if (activeThread != null) {
			activeThread.doStop();
		}
		activeThread.onPhraseEnd.add(new Runnable() {
			public void run() {
				phrase = activeThread.N;
				/// fin du dernier segment du texte ///
				if (phrase == p.textHandler.getPhrasesCount() - 1) {
					p.afficherCompteRendu();
				}
				/// met à jour la barre de progression ///
				else {
					updateBar();
				}
			}
		});
		activeThread.start();
	}

	private void updateBar() {
		p.progressBar.setValue(getCurrentPhraseIndex());
		p.progressBar.setString((getCurrentPhraseIndex() + 1) + "/" + (p.textHandler.getPhrasesCount() - 1));
	}

	/**
	 * Essaye de passer au segment suivant, passe à la page suivante si c'était le
	 * dernier segment de la page. Déclenche une erreur si on était au dernier
	 * segment du texte.
	 */
	public void doNext() {
		goTo(p.player.getCurrentPhraseIndex() + 1);
	}

	/**
	 * Essaye de passer au segment précédent. Déclenche une erreur si on était au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		goTo(p.player.getCurrentPhraseIndex() - 1);
	}

	/**
	 * Essaye d'arrêter l'enregistrement en cours.
	 */
	public void doStop() {
		p.player.stop();
		if (activeThread != null) {
			activeThread.doStop();
		}
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est déjà démarré, reprend depuis
	 * le début.
	 */
	public void doPlay() {
		goTo(p.player.getCurrentPhraseIndex());
	}

	public int getCurrentPhraseIndex() {
		return phrase;
	}

	public boolean isPlaying() {
		return p.player.isPlaying();
	}

	public boolean hasPreviousPhrase() {
		return p.player.hasPreviousPhrase();
	}

	/**
	 * Charge un thread de la même classe que r Et initialisé au segment n
	 */
	public void loadReadThread(ReadThread r) {
		this.activeThread = r;
	}

}
