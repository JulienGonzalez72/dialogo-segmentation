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

	/**
	 * Si l'utilisateur est en train de cliquer.
	 */
	private boolean clicking;

	public ControllerMouse(SegmentedTextPanel p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}
	
	public int getLastTextOffset() {
		return lastTextOffset;
	}

	public void mousePressed(MouseEvent e) {
		clicking = true;
		lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(),
				view.getEditorPane().getCaretPosition());
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

	public boolean isClicking() {
		return clicking;
	}

	public void setClicking(boolean clicking) {
		this.clicking = clicking;
	}

}
