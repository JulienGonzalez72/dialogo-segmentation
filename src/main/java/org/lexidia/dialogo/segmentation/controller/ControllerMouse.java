package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.view.SegmentedTextPanel;

public class ControllerMouse implements MouseListener {

	public static int nbErreurs;
	private SegmentedTextPanel view;
	private TextHandler handler;
	
	/**
	 * Dernière position absolue du texte sur laquelle l'utilisateur a cliquÃ©.
	 */
	private int lastTextOffset;

	public ControllerMouse(SegmentedTextPanel p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}
	
	public int getLastTextOffset() {
		return lastTextOffset;
	}

	public void mousePressed(MouseEvent e) {
		lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(),
				view.getEditorPane().getCaretPosition());
		
		synchronized(ControllerText.lock) {
			ControllerText.lock.notify();
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
