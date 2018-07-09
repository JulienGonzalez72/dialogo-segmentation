package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.*;

import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.view.SegmentedTextPanel;

public class ControllerMouse implements MouseListener {

	public static int nbErreurs;
	SegmentedTextPanel view;
	TextHandler handler;
	
	/**
	 * Dernière position absolue du texte sur laquelle l'utilisateur a cliqué.
	 */
	public int lastTextOffset;
	
	/**
	 * Si l'utilisateur est en train de cliquer.
	 */
	public boolean clicking;

	public ControllerMouse(SegmentedTextPanel p, TextHandler handler) {
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
