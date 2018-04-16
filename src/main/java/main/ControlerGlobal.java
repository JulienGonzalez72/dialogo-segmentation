package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.SwingWorker;

public class ControlerGlobal {

	Panneau p;

	public ControlerGlobal(Panneau p) {
		this.p = p;
	}

	/**
	 * Se place sur le segment de numero n
	 */
	public void goTo(int n) {
		p.player.goTo(n);
		p.showPage(getPageOfPhrase(n));
	}

	/**
	 * Construire les pages à partir du segment de numero spécifié
	 */
	public void buildPages(int startPhrase) {
		p.buildPages(startPhrase);
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void highlightPhrase(Color c, int n) {
		p.surlignerSegment(c, n);
	}

	/**
	 * La page se met en attente d'un clic, jusqu'à ce que :<br>
	 * - soit le clic soit juste, renvoie true <br>
	 * - soit il n'y a plus d'essais, renvoie false
	 */
	public boolean waitForClick(int nbTry, MouseEvent e, TextHandler handler) {
		boolean r = false;
		p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// on ne fait rien en cas de triple clic
		// on ne fait rien si la phrase est en cours de lecture
		if (p.player.isPhraseFinished() && e.getClickCount() < 2) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(p.getNumeroPremierSegmentAffiché(), p.editorPane.getCaretPosition());
			// si le clic est juste
			if (handler.wordPause(offset) && handler.getPhraseIndex(offset) == p.player.getCurrentPhraseIndex()) {
				r = true;
				if (FenetreParametre.modeSurlignage) {
					traitementClicJusteModeSurlignage(offset, handler);
				} else {
					traitementClicJuste(offset);
				}
				// si le clic est faux
			} else {
				r = false;
				if (FenetreParametre.modeSurlignage) {
					traitementClicFauxModeSurlignage(offset, handler);
				} else {
					traitementClicFaux(offset, handler);
				}
			}
		}
		return r;
	}

	private void traitementClicFauxModeSurlignage(int offset, TextHandler handler) {
		p.nbEssaisRestantPourLeSegmentCourant--;
		// si il reste un essai ou qu'une phrase est en train d'être corrigée
		if (p.nbEssaisRestantPourLeSegmentCourant > 0) {
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			// si il ne reste plus d'essais
		} else {
			/// indique l'erreur en rouge ///
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			/// indique la phrase corrigée en bleu ///
			p.indiquerEtCorrigerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex() - 1)),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex())));
			p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		}
	}

	private void traitementClicJusteModeSurlignage(int offset, TextHandler handler) {
		int pauseOffset = handler.endWordPosition(offset);
		// on restaure le nombre d'essais
		p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		/// surlignage ///
		p.editorPane.surlignerPhrase(0, handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), pauseOffset + 1),
				Constants.RIGHT_COLOR);
		p.editorPane.enleverSurlignageRouge();
		// si la page est finis on affiche la suivante
		if (p.pageFinis()) {
			new SwingWorker<Object, Object>() {
				// Ce traitement sera exécuté dans un autre thread :
				protected Object doInBackground() throws Exception {
					Thread.sleep(Constants.PAGE_WAIT_TIME);
					return null;
				}

				// Ce traitement sera exécuté à la fin dans l'EDT
				protected void done() {
					p.player.nextPhrase();
					p.afficherPageSuivante();
				}
			}.execute();
		} else {
			p.player.nextPhrase();
		}

	}

	private void traitementClicFaux(int offset, TextHandler handler) {
		p.nbEssaisRestantPourLeSegmentCourant--;
		// si il reste un essai ou qu'une phrase est en train d'être corrigée
		if (p.nbEssaisRestantPourLeSegmentCourant > 0 || p.editorPane.containsBlueHighlight()) {
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			// si il ne reste plus d'essais
		} else {
			/// indique l'erreur en rouge ///
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			/// indique la phrase corrigée en bleu ///
			p.indiquerEtCorrigerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex() - 1)),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex())));
			p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		}
	}

	public void traitementClicJuste(int offset) {
		// on restaure le nombre d'essais
		p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		p.editorPane.enleverSurlignageBleu();
		p.editorPane.enleverSurlignageRouge();
		// si la page est finis on affiche la suivante
		if (p.pageFinis()) {
			new SwingWorker<Object, Object>() {
				// Ce traitement sera exécuté dans un autre thread :
				protected Object doInBackground() throws Exception {
					Thread.sleep(Constants.PAGE_WAIT_TIME);
					return null;
				}

				// Ce traitement sera exécuté à la fin dans l'EDT
				protected void done() {
					p.player.nextPhrase();
					p.afficherPageSuivante();
				}
			}.execute();
		} else {
			p.player.nextPhrase();
		}
	}

	/**
	 * retourne la apge qui contient le segment, ou -1 si le segment n'existe pas
	 */
	private int getPageOfPhrase(int n) {
		int numeroPage = -1;
		for (Integer i : p.segmentsEnFonctionDeLaPage.keySet()) {
			if (p.segmentsEnFonctionDeLaPage.get(i).contains(n)) {
				numeroPage = i;
				break;
			}
		}
		return numeroPage;
	}

}
