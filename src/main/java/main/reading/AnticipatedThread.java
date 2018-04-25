package main.reading;

import main.Constants;
import main.controler.ControlerGlobal;

public class AnticipatedThread extends ReadThread {

	public AnticipatedThread(ControlerGlobal controler, int N) {
		super(controler,N);
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
		// enlever surlignages
		controler.removeHighlightPhrase(N);
		/// appel des écouteurs de fin de segment ///
		for (Runnable r : onPhraseEnd) {
			r.run();
		}
	}

}
