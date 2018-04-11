package main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

	public void mouseClicked(MouseEvent e) {
		//si le clic est juste
		if (handler.wordPause(view.getNumeroPremierSegmentAffiché(), view.editorPane.getCaretPosition())) {
			int pauseOffset = handler.endWordPosition(view.editorPane.getCaretPosition());
			view.editorPane.surlignerPhrase(pauseOffset + 1,
					Color.GREEN);
			System.out.println(handler.getPauseIndex(pauseOffset + 1));
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
		//si le clic est faux 
		} else {
			view.nbEssaisRestantPourLeSegmentCourant--;
			if ( view.nbEssaisRestantPourLeSegmentCourant > 0 ){
				view.indiquerErreur();
			} else {
				view.indiquerEtCorrigerErreur();
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
