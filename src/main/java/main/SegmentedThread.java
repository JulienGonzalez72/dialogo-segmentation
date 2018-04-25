package main;

public class SegmentedThread extends ReadThread {

	public SegmentedThread(ControlerGlobal controler, int N) {
		super(controler, N);
	}

	public void run() {
		/// affichage de la page correspondant au segment N ///
		controler.showPage(controler.getPageOfPhrase(N));
		/// play du son correspondant au segment N ///
		controler.play(N);
		/// attente de la fin du son ///
		controler.doWait(controler.getCurrentPhraseDuration(), Constants.CURSOR_LISTEN);
		/// attente de la fin du temps de pause ///
		controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
		int nbTry = FenetreParametre.nbFautesTolerees;
		//tant que on a pas fait le bon clic
		while (!controler.waitForClick(N, nbTry)) {
			nbTry--;
			if (nbTry == 0) {
				// surligner phrase avec correction
				controler.highlightPhrase(Constants.WRONG_PHRASE_COLOR, N);
				// rejouer son
				controler.play(N);
				/// attente de la fin du son ///
				controler.doWait(controler.getCurrentPhraseDuration(), Constants.CURSOR_LISTEN);
			} else {
				if (FenetreParametre.rejouerSon) {
					controler.play(N);
					/// attente de la fin du son ///
					controler.doWait(controler.getCurrentPhraseDuration(), Constants.CURSOR_LISTEN);
				}
			}
		}
		// enlever surlignage
		controler.removeHighlightPhrase(N);
		controler.removeWrongHighlights();
		/// appel des écouteurs de fin de segment ///
		for (Runnable r : onPhraseEnd) {
			r.run();
		}

	}

}
