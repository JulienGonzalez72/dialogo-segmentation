package main.controler;

import java.awt.event.*;

import main.model.TextHandler;
import main.view.TextPanel;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	TextPanel view;
	TextHandler handler;
	
	/**
	 * Dernière position absolue du texte sur laquelle l'utilisateur a cliqué.
	 */
	public int lastTextOffset;
	
	/**
	 * Si l'utilisateur est en train de cliquer.
	 */
	public boolean clicking;

	public ControlerMouse(TextPanel p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		clicking = true;
		lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(),
				view.editorPane.getCaretPosition());
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		clicking = false;
	}

}
