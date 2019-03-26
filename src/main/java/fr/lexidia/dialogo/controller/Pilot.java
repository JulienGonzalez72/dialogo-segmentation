package fr.lexidia.dialogo.controller;

import fr.lexidia.dialogo.model.LsSentenceNumberDisplayHolder;
import fr.lexidia.dialogo.reading.ReadThread;
import fr.lexidia.dialogo.reading.ReaderFactory;
import fr.lexidia.dialogo.view.SegmentedTextPanel;
import fr.lexiphone.player.impl.SentenceNumber;

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
	 * Se place sur le segment de numero n et d�marre l'algorithme de lecture.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < p.getParam().getStartingPhrase() || n > p.getTextHandler().getPhrasesCount() - 1) {
			throw new IllegalArgumentException("Num�ro de segment invalide : " + n
					+ " (il doit �tre compris entre " + p.getParam().getStartingPhrase() + " et " + p.getTextHandler().getPhrasesCount() + ")");
		}
		if (readerFactory == null) {
			throw new IllegalArgumentException("Usine de lecture non charg�e !");
		}

		/// emp�che le redimensionnement de la fen�tre � partir de la premi�re lecture ///
		p.getFenetre().setResizable(false);
		
		/// fait dispara�tre les sliders qui servent � ajuster les marges ///
		p.setSlidersVisible(false);
		
		// met a jour la barre de progression
		updateBar();

		if (activeThread != null && activeThread.isRunning()) {
			activeThread.doStop();
		}
		
		activeThread = readerFactory.createReadThread();
		activeThread.setN(phrase = n);
		activeThread.start();
		
		started = true;
	}
	
	private void updateBar() {
		p.getProgressBar().setValue(phrase + 1);
		p.getProgressBar().setString((phrase + 1) + "/" + (p.getTextHandler().getPhrasesCount()));
		if (LsSentenceNumberDisplayHolder.getInstance().isOn()) {
            SentenceNumber sn = LsSentenceNumberDisplayHolder.getInstance().getSentenceNumber();
            sn.setRealSentenceNumber(phrase);
            //p.getProgressBar().setMaximum(sn.getNumberOfSentences());
            p.getProgressBar().setString(sn.toString());
        } else {
            p.getProgressBar().setValue(phrase + 1);
            p.getProgressBar().setString((phrase + 1) + "/" + (p.getTextHandler().getPhrasesCount()));
        }
	}

	/**
	 * Essaye de passer au segment suivant, passe � la page suivante si c'�tait le
	 * dernier segment de la page. D�clenche une erreur si on �tait au dernier
	 * segment du texte.
	 */
	public void doNext() {
		goTo(phrase + 1);
	}

	/**
	 * Essaye de passer au segment pr�c�dent. D�clenche une erreur si on �tait au
	 * premier segment du texte.
	 */
	public void doPrevious() {
		goTo(phrase - 1);
	}

	/**
	 * Essaye d'arr�ter l'enregistrement en cours.
	 */
	public void doStop() {
		if (activeThread != null) {
			activeThread.doStop();
		}
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est d�j� d�marr�, reprend depuis
	 * le d�but.
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
		return phrase > p.getParam().getStartingPhrase();
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
		p.getFenetre().setResizable(true);
		started = false;
	}
	
	public void updateCurrentPhrase() {
		if (activeThread != null) {
			phrase = activeThread.getN();
		}
		/// met � jour la barre de progression ///
		updateBar();
	}

}
