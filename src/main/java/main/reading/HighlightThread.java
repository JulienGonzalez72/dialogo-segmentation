package main.reading;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import main.Constants;
import main.controller.ControllerText;

public class HighlightThread /*extends ReadThread*/ {

	/*public HighlightThread(ControlerText controler, int N) {
		super(controler, N);
	}

	*//**
	 * m�morisation des surlignages
	 *//*
	private static Map<Integer, Color> coloriage = new HashMap<>();

	*//**
	 * derni�re phrase trait�e
	 *//*
	private static int lastN = 0;

	public void run() {
		while (N < controler.getPhrasesCount() - 1 && running) {
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			// si on a avanc� dans le texte
			if (lastN < N) {
				// on surligne depuis le d�part jusqu'� l'arriv�e
				for (int i = lastN; i < N; i++) {
					// si le segment n'a pas de couleur retenue
					if (!coloriage.containsKey(i)) {
						// on le colorie
						if (pageActuelleContient(i)) {
							controler.highlightPhrase(param.rightColor, i);
						}
						// et on stocke sa couleur
						coloriage.put(i, param.rightColor);
					} else {
						// on le colorie avc la couleur stock�e
						if (pageActuelleContient(i)) {
							controler.highlightPhrase(coloriage.get(i), i);
						}
					}
				}
				// si on a recul� dans le texte
			} else if (lastN > N) {
				// on d�surligne depuis le d�part jusqu'� l'arriv�e et on d�stocke la couleur
				for (int i = lastN; i > N; i--) {
					controler.removeHighlightPhrase(i);
					coloriage.remove(i - 1);
				}
			}
			// si changement de page il y a r�cup�ration des anciens surlignages
			if (!pageActuelleContient(lastN) && lastN > N) {
				for (int i = 0; i < coloriage.keySet().size(); i++) {
					// sauf celui de a phrase actuelle
					if (pageActuelleContient(i) && i != N) {
						controler.highlightPhrase(coloriage.get(i), i);
					}
				}
			}
			// mise a jour du segment pr�c�dent
			lastN = N;
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getCurrentWaitTime(), Constants.CURSOR_SPEAK);
			// restauration du nombre d'essais
			int nbTry = controler.p.param.nbFautesTolerees;
			// tant que on a pas fait le bon clic
			boolean doOne = true;
			while (!controler.waitForClick(N)) {
				// d�cr�mentation du nombre d'essais restants
				nbTry--;
				if (nbTry <= 0) {
					/// on arr�te l'ex�cution si le thread est termin� ///
					if (!running) {
						return;
					}
					if (doOne) {
						// incr�mentation des erreurs de segments
						controler.incrementerErreurSegment();
						doOne = false;
					}
					// surligner phrase avec correction
					controler.highlightPhrase(param.correctionColor, N);
					// stockage coloriage
					coloriage.put(N, param.correctionColor);
					// rejouer son
					controler.play(N);
				} else {
					if (controler.p.param.rejouerSon) {
						controler.play(N);
					}
				}
			}
			// si on a plus d'essais restants
			if (nbTry == controler.p.param.nbFautesTolerees) {
				/// on arr�te l'ex�cution si le thread est termin� ///
				if (!running) {
					return;
				}
				// surlignage du segment
				controler.highlightPhrase(param.rightColor, N);
				// stockage du coloriage du segment
				coloriage.put(N, param.rightColor);
			}
			// enlever surlignage
			controler.removeWrongHighlights();
			/// on arr�te l'ex�cution si le thread est termin� ///
			if (!running) {
				return;
			}
			N++;
			/// appel des �couteurs de fin de segment ///
			for (Runnable r : onPhraseEnd) {
				r.run();
			}
		}
	}

	private boolean pageActuelleContient(int segment) {
		return controler.p.phrasesInFonctionOfPages.get(controler.p.pageActuelle).contains(segment);
	}*/

}
