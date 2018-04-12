package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.*;
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
		// on ne fait rien en cas de triple clic
		// on ne fait rien si le clic est sur un mot d�j� surlign� en vert
		if (view.editorPane.getCaretPosition() > view.editorPane.indiceDernierCaractereSurlign�
				&& e.getClickCount() < 2) {
			/// cherche la position exacte dans le texte ///
			int offset = handler.getAbsoluteOffset(view.getNumeroPremierSegmentAffich�(),
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
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.startWordPosition(offset) + 1),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.endWordPosition(offset)));
					// si il ne reste plus d'essais
				} else {
					view.indiquerEtCorrigerErreur(
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.getPauseOffset(view.segmentActuel - 1)),
							handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(),
									handler.getPauseOffset(view.segmentActuel)));

					view.segmentActuel++;
					view.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
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
				handler.getRelativeOffset(view.getNumeroPremierSegmentAffich�(), pauseOffset + 1),
				Constants.RIGHT_COLOR);
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
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_P:
			FenetreParametre.editorPane = view.editorPane;
			FenetreParametre.fen.setVisible(true);
			int x = 4 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
			int y = 4 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
			FenetreParametre.fen.setLocation(x, y);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).champNbFautesTolerees
					.setEnabled(false);
			break;
		case KeyEvent.VK_R:
			FenetreParametre.editorPane = null;
			view.fenetre.setVisible(false);
			FenetreParametre.fen.setVisible(true);
			((FenetreParametre.PanneauParam) FenetreParametre.fen.getContentPane()).champNbFautesTolerees
					.setEnabled(true);
			break;
		default:
			System.out.println("Touche non trait�e.");
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

}
