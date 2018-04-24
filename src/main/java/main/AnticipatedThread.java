package main;

public class AnticipatedThread extends ReadThread {

	private ControlerGlobal controler;

	public AnticipatedThread(ControlerGlobal controler, int N) {
		super(N);
		this.controler = controler;
	}
	
	public void run() {
		/// affichage de la page correspondant au segment N ///
		controler.showPage(controler.getPageOfPhrase(N));
		//surlignage de la phrase
		controler.highlightPhrase(Constants.RIGHT_COLOR, N);
		//chargement du son pour savoir combien de temps on attends
		controler.p.player.load(N);
		/// attente de la fin du temps de pause ///
		controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
		/// play du son correspondant au segment N ///
		controler.play(N);
		/// attente de la fin du son ///
		controler.doWait(controler.getCurrentPhraseDuration(), Constants.CURSOR_LISTEN);
		// enlever surlignages
		controler.removeHighlightPhrase(N);
		/// appel des écouteurs de fin de segment ///
		for (Runnable r : onPhraseEnd) {
			r.run();
		}
	}

}
