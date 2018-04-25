package main.reading;

import main.Constants;
import main.controler.ControlerGlobal;
import main.view.FenetreParametre;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HighlightThread extends ReadThread {

	public HighlightThread(ControlerGlobal controler, int N) {
		super(controler, N);
	}

	static Map<Integer, Color> coloriage = new HashMap<>();
	static int lastN = 0;

	public void run() {
		/// affichage de la page correspondant au segment N ///
		controler.showPage(controler.getPageOfPhrase(N));
		// surlignage si suivant
		if (lastN < N) {
			for (int i = 0; i < N; i++) {
				if (!coloriage.containsKey(i)) {
					if (pageActuelleContient(i)) {
						controler.highlightPhrase(Constants.RIGHT_COLOR, i);
					}
					coloriage.put(i, Constants.RIGHT_COLOR);
				} else {
					if (pageActuelleContient(i)) {
						controler.highlightPhrase(coloriage.get(i), i);
					}
				}
			}
			// surlignage si précédent
		} else if (lastN > N) {
			System.out.println("lastN : "+lastN+" / N : "+N);
			for (int i = lastN; i > N; i--) {
				controler.removeHighlightPhrase(i);
				coloriage.remove(i);
			}
		}
		System.out.println(coloriage.toString());
		// si changement de page il y a récupération des anciens surlignages
		if (!pageActuelleContient(lastN) && lastN > N) {
			for (int i = 0; i < coloriage.keySet().size(); i++) {
				// sauf celui de a phrase actuelle
				if (pageActuelleContient(i) && i != N) {
					controler.highlightPhrase(coloriage.get(i), i);
				}
			}
		}
		lastN = N;
		/// play du son correspondant au segment N ///
		controler.play(N);
		/// attente de la fin du temps de pause ///
		controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
		int nbTry = FenetreParametre.nbFautesTolerees;
		// tant que on a pas fait le bon clic
		while (!controler.waitForClick(N, nbTry)) {
			nbTry--;
			if (nbTry == 0) {
				// surligner phrase avec correction
				controler.highlightPhrase(Constants.WRONG_PHRASE_COLOR, N);
				// stockage coloriage
				coloriage.put(N, Constants.WRONG_PHRASE_COLOR);
				// rejouer son
				controler.play(N);
			} else {
				if (FenetreParametre.rejouerSon) {
					controler.play(N);
				}
			}
		}
		if (nbTry == FenetreParametre.nbFautesTolerees) {
			controler.highlightPhrase(Constants.RIGHT_COLOR, N);
			// stockage coloriage
			coloriage.put(N, Constants.RIGHT_COLOR);
		}
		// enlever surlignage
		controler.removeWrongHighlights();
		/// on arrête l'exécution si le thread est terminé ///
		if (!running) {
			return;
		}
		/// appel des écouteurs de fin de segment ///
		for (Runnable r : onPhraseEnd) {
			r.run();
		}
	}

	private boolean pageActuelleContient(int segment) {
		return controler.p.segmentsEnFonctionDeLaPage.get(controler.p.pageActuelle).contains(segment);
	}

}
