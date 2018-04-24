package main;

public class HighlightThread extends ReadThread {

	private ControlerGlobal controler;

	public HighlightThread(ControlerGlobal controler, int N) {
		super(N);
		this.controler = controler;
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
		boolean trouveDuPremierCoup = clicJuste;
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
		if (trouveDuPremierCoup) {
			// surligner phrase avec correction
			controler.highlightPhrase(Constants.RIGHT_COLOR, N);
		}
		// enlever surlignage rouge
		controler.removeWrongHighlights();
		/// appel des écouteurs de fin de segment ///
		for (Runnable r : onPhraseEnd) {
			r.run();
		}
	}

}
