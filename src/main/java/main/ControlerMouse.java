package main;

import java.awt.event.*;
import javax.swing.SwingWorker;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	Panneau view;
	TextHandler handler;

	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		// on ne fait rien en cas de triple clic
		// on ne fait rien si le clic est sur un mot déjà surligné en vert
		if (view.editorPane.getCaretPosition() > view.editorPane.indiceDernierCaractereSurligné
				&& e.getClickCount() < 2) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(view.getNumeroPremierSegmentAffiché(),
					view.editorPane.getCaretPosition());
			// si le clic est juste
			if (handler.wordPause(offset) && handler.getPhraseIndex(offset) == view.segmentActuel) {
				traitementClicJuste(offset);
				// si le clic est faux
			} else {
				view.nbEssaisRestantPourLeSegmentCourant--;
				// si il reste un essai
				if (view.nbEssaisRestantPourLeSegmentCourant > 0) {
					view.indiquerErreur(
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(),
									handler.startWordPosition(offset) + 1),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(),
									handler.endWordPosition(offset)));
					// si il ne reste plus d'essais
				} else {
					view.indiquerEtCorrigerErreur(
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(),
									handler.getPauseOffset(view.segmentActuel - 1)),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(),
									handler.getPauseOffset(view.segmentActuel)));

					view.segmentActuel++;
					view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
					// si la page est finis on affiche la suivante
					if (view.pageFinis()) {
						new SwingWorker<Object, Object>() {
							// Ce traitement sera exécuté dans un autre thread :
							protected Object doInBackground() throws Exception {
								Thread.sleep(Constants.PAGE_WAIT_TIME);
								return null;
							}

							// Ce traitement sera exécuté à la fin dans l'EDT
							protected void done() {
								view.afficherPageSuivante();
							}
						}.execute();
					}
				}
			}
		}
	}

	public void traitementClicJuste(int offset) {
		int pauseOffset = handler.endWordPosition(offset);
		// on restaure le nombre d'essais
		view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		/// surlignage ///
		view.editorPane.surlignerPhrase(0,
				handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(), pauseOffset + 1),
				Constants.RIGHT_COLOR);
		view.editorPane.enleverSurlignageRouge();
		view.segmentActuel++;
		// si la page est finis on affiche la suivante
		if (view.pageFinis()) {
			new SwingWorker<Object, Object>() {
				// Ce traitement sera exécuté dans un autre thread :
				protected Object doInBackground() throws Exception {
					Thread.sleep(Constants.PAGE_WAIT_TIME);
					return null;
				}

				// Ce traitement sera exécuté à la fin dans l'EDT
				protected void done() {
					view.afficherPageSuivante();
				}
			}.execute();
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

}
