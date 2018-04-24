package main;

public class SegmentedThread extends ReadThread {

	public SegmentedThread(ControlerGlobal controler, int N) {
		super(controler,N);
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
		// attente d'un clic
		boolean clicJuste = controler.waitForClick(N, FenetreParametre.nbFautesTolerees);
		boolean rejouer = true;
		while (!clicJuste) {
			// surligner phrase avec correction
			controler.highlightPhrase(Constants.WRONG_PHRASE_COLOR, N);
			if (FenetreParametre.rejouerSon) {
				if (rejouer) {
					/// play du son correspondant au segment N ///
					controler.play(N);
					/// attente de la fin du son ///
					controler.doWait(controler.getCurrentPhraseDuration(), Constants.CURSOR_LISTEN);
					rejouer = false;
				}
			}
			clicJuste = controler.waitForClick(N, FenetreParametre.nbFautesTolerees);
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
