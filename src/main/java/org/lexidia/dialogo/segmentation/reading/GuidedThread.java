package org.lexidia.dialogo.segmentation.reading;

import org.lexidia.dialogo.segmentation.controller.ControllerText;
import org.lexidia.dialogo.segmentation.main.Constants;

public class GuidedThread /*extends ReadThread*/ {
	
	/*public GuidedThread(ControlerText controler, int N) {
		super(controler, N);
	}
	
	public void run() {
		while (N < controler.getPhrasesCount() - 1 && running) {
			/// r�initiliase l'�tat ///
			controler.stopAll();
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// surlignage du segment N ///
			controler.highlightPhrase(param.rightColor, N);
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
			/// on arr�te l'ex�cution si le thread est termin� ///
			if (!running) {
				return;
			}
			/// suppression du surlignage du segment de phrase N ///
			controler.removeHighlightPhrase(N);
			N++;
			/// appel des �couteurs de fin de segment ///
			for (Runnable r : onPhraseEnd) {
				r.run();
			}
		}
	}*/
	
}
