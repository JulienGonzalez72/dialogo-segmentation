package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingWorker;

public class ControlerMouse implements MouseListener, KeyListener {

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
		// on ne fait rien si le clic est sur un mot d�j� surlign� en vert
		if (view.editorPane.getCaretPosition() > view.editorPane.indiceDernierCaractereSurligne) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(view.getNumeroPremierSegmentAffich�(),
					view.editorPane.getCaretPosition());

			// si le clic est juste
			if (handler.wordPause(offset) && handler.getPhraseIndex(offset) == view.segmentActuel) {
				int pauseOffset = handler.endWordPosition(offset);
				// on restaure le nombre d'essais
				view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;

				/// surlignage ///
				view.editorPane.surlignerPhrase(0,
						handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(), pauseOffset + 1),
						Panneau.RIGHT_COLOR);
				view.editorPane.enleverSurlignageRouge();

				view.segmentActuel++;
				// si la page est finis on affiche la suivante
				if (view.pageFinis()) {

					new SwingWorker<Object, Object>() {
						// Ce traitement sera ex�cut� dans un autre thread :
						protected Object doInBackground() throws Exception {
							Thread.sleep(PAGE_WAIT_TIME);
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
					view.indiquerErreur(
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.startWordPosition(offset) + 1),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.endWordPosition(offset)));
				} else {
					view.indiquerEtCorrigerErreur(
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.startWordPosition(offset) + 1),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.endWordPosition(offset)));

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

	public void keyPressed(KeyEvent e) {
		//p = 80, r = 82
		if ( e.getKeyCode() == 80) {
			FenetreParametre.fenExercice = view.editorPane;
			FenetreParametre.fen.setVisible(true);
		} else if ( e.getKeyCode() == 82) {
			FenetreParametre.fenExercice = null;
			view.fenetre.setVisible(false);
			FenetreParametre.fen.setVisible(true);
		}else {
			System.out.println(e.getKeyCode());
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
