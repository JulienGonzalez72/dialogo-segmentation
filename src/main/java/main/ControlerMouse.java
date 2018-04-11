package main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingWorker;

public class ControlerMouse implements MouseListener {

	/**
	 * Temps d'attente entre chaque page
	 */
	public static final long PAGE_WAIT_TIME = 1000;

	public static int nbErreurs;
	Panneau view;
	TextHandler handler;

	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mouseClicked(MouseEvent e) {
		// on ne fait rien si le clic est sur un mot déjà surligné en vert
		if (view.editorPane.getCaretPosition() > view.editorPane.indiceDernierCaractereSurligne) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(view.getNumeroPremierSegmentAffiché(),
					view.editorPane.getCaretPosition());

			// si le clic est juste
			if (handler.wordPause(offset) && handler.getPhraseIndex(offset) == view.segmentActuel) {
				int pauseOffset = handler.endWordPosition(offset);
				// on restaure le nombre d'essais
				view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
				
				/// surlignage ///
				view.editorPane.surlignerPhrase(
						0, handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(), pauseOffset + 1), Panneau.RIGHT_COLOR);
				view.editorPane.enleverSurlignageRouge();
				
				view.segmentActuel++;
				// si la page est finis on affiche la suivante
				if (view.pageFinis()) {

					new SwingWorker<Object, Object>() {
							// Ce traitement sera exécuté dans un autre thread :
						protected Object doInBackground() throws Exception {
							Thread.sleep(3000);
							return null;
						}
							// Ce traitement sera exécuté à la fin dans l'EDT
						protected void done() {
							view.afficherPageSuivante();
						}
					}.execute();
				}
				// si le clic est faux
			} else {
				view.nbEssaisRestantPourLeSegmentCourant--;
				if (view.nbEssaisRestantPourLeSegmentCourant > 0) {
					view.indiquerErreur(handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(), handler.startWordPosition(offset)),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
				} else {
					view.indiquerEtCorrigerErreur();
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
