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
		if (view.editorPane.getCaretPosition() > view.editorPane.indiceDernierCaractereSurligne) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(view.getNumeroPremierSegmentAffich�(), view.editorPane.getCaretPosition());
			
			//si le clic est juste
			System.out.println(handler.getPhraseIndex(offset));
			if (handler.wordPause(offset) && handler.getPhraseIndex(offset) == view.segmentActuel) {
				int pauseOffset = handler.endWordPosition(offset);
				view.editorPane.surlignerPhrase(handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(), pauseOffset + 1),
						Color.GREEN);
				view.segmentActuel++;
				
				// on restaure le nombre d'essais
				view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
				
				// si la page est finis on affiche la suivante
				if (view.pageFinis()) {
					
					new SwingWorker<Object, Object>() {
							// Ce traitement sera ex�cut� dans un autre thread :
						protected Object doInBackground() throws Exception {
							Thread.sleep(3000);
							return null;
						}
							// Ce traitement sera ex�cut� � la fin dans l'EDT
						protected void done() {
							view.afficherPageSuivante();
						}
					}.execute();
				}
			// si le clic est faux
			} else {
				view.nbEssaisRestantPourLeSegmentCourant--;
				if (view.nbEssaisRestantPourLeSegmentCourant > 0) {
					view.indiquerErreur();
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
