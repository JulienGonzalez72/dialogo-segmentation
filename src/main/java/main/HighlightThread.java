package main;

public class HighlightThread extends Thread {

	private ControlerGlobal controler;

	public HighlightThread(ControlerGlobal controler) {
		this.controler = controler;
	}

	public void run() {
		/// mapage et mise en page de la totalité du texte ///
		controler.rebuildPages();
		/// N = numéro de segment initial ///
		int N = FenetreParametre.premierSegment - 1;
		/// tant que N <= nombre de segments du texte ///
		while (N < controler.getPhrasesCount()) {
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du son ///
			controler.doWait(controler.getPhraseDuration(N), Constants.CURSOR_LISTEN);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getWaitTime(N), Constants.CURSOR_SPEAK);
			while (true) {
				controler.removeWrongHighlights();
				/// attente d'un clic sur le dernier mot du segment N ///
				boolean rightClick = controler.waitForClick(N, FenetreParametre.nbFautesTolerees);
				/// si échec ///
				if (!rightClick) {
					// on decremente le nombre d'essais restants
						/// surlignage du segment de phrase N ///
						controler.highlightUntilPhrase(Constants.WRONG_PHRASE_COLOR, N);
						/// play du son correspondant au segment N ///
						controler.play(N);
						/// attente de la fin du son ///
						controler.doWait(controler.getPhraseDuration(N), Constants.CURSOR_LISTEN);
				} else {
					break;
				}
			}
			/// N=N+1 ///
			N++;
		}

	}

}
