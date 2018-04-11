package main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	public Panneau view;
	TextHandler handler;


	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mouseClicked(MouseEvent e) {
<<<<<<< HEAD
		if (handler.wordPause(view.editorPane.getCaretPosition()))
			view.editorPane.surlignerPhrase(0, handler.endWordPosition(view.editorPane.getCaretPosition()) + 1, Color.GREEN);
		if (handler.correctPause(view.editorPane.getCaretPosition())){
			//view.editorPane.surlignerPhrase(debut,fin, Color.GREEN);
		} else {
			//view.editorPane.gererErreur(bonnePosition);
=======
		if (handler.wordPause(view.editorPane.getCaretPosition())) {
			view.editorPane.surlignerPhrase(handler.endWordPosition(view.editorPane.getCaretPosition()) + 1,
					Color.GREEN);
			view.segmentActuel++;
		}
		//si la page est finis on affiche la suivante
		if (view.pageFinis()) {
			view.afficherPageSuivante();
>>>>>>> 8844eaee15dc161098fcaf4a8da8cd2a911ea4ea
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
