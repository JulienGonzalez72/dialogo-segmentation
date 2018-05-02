package main.reading;

import main.Constants;
import main.controler.ControlerText;

public class SegmentedThread extends ReadThread {

	public SegmentedThread(ControlerText controler, int N) {
		super(controler, N);
	}

	public void run() {
		while (N < controler.getPhrasesCount() - 1 && running) {
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
			int nbTry = controler.p.param.nbFautesTolerees;
			boolean doOne = true;
			// tant que on a pas fait le bon clic
			while (!controler.waitForClick(N)) {
				nbTry--;
				if (nbTry == 0) {
					if (doOne) {
						// incrémentation des erreurs de segments
						controler.incrementerErreurSegment();
						doOne = false;
					}
					// surligner phrase avec correction
					controler.highlightPhrase(Constants.WRONG_PHRASE_COLOR, N);
					// rejouer son
					controler.play(N);
				} else {
					if (controler.p.param.rejouerSon) {
						controler.play(N);
					}
				}
			}
			// enlever surlignage
			controler.removeHighlightPhrase(N);
			controler.removeWrongHighlights();
			N++;
			/// appel des écouteurs de fin de segment ///
			for (Runnable r : onPhraseEnd) {
				r.run();
			}
		}

	}

}
