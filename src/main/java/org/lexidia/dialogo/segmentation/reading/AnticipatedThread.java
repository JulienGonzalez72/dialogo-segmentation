package org.lexidia.dialogo.segmentation.reading;

import org.lexidia.dialogo.segmentation.controller.ControllerText;
import org.lexidia.dialogo.segmentation.main.Constants;

public class AnticipatedThread /*extends ReadThread*/ {

	/*public AnticipatedThread(ControlerText controler, int N) {
		super(controler, N);
	}

	public void run() {
		while (N < controler.getPhrasesCount() - 1 && running) {
			/// réinitiliase l'état ///
			controler.stopAll();
			/// chargement du son ///
			controler.loadSound(N);
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// surlignage du segment N ///
			controler.highlightPhrase(param.rightColor, N);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
			/// on arr�te l'ex�cution si le thread est termin� ///
			if (!running) {
				return;
			}
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// on arréte l'exécution si le thread est terminé ///
			if (!running) {
				return;
			}
			/// suppression du surlignage du segment de phrase N ///
			controler.removeHighlightPhrase(N);
			N++;
			/// appel des écouteurs de fin de segment ///
			for (Runnable r : onPhraseEnd) {
				r.run();
			}
		}
	}*/

}
