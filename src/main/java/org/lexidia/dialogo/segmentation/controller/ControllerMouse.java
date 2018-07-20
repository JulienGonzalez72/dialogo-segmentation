package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.lexidia.dialogo.segmentation.model.TextHandler;
import org.lexidia.dialogo.segmentation.view.SegmentedTextPanel;

public class ControllerMouse implements MouseListener {

	private SegmentedTextPanel view;
	private TextHandler handler;
	
	/**
	 * Dernière position absolue du texte sur laquelle l'utilisateur a cliqué.
	 */
	private int lastTextOffset;
	
	private Object clickLock = new Object();

	public ControllerMouse(SegmentedTextPanel p, TextHandler handler) {
		view = p;
		this.handler = handler;
	}
	
	public int getLastTextOffset() {
		return lastTextOffset;
	}

	public void mousePressed(MouseEvent e) {
		lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(),
				view.getEditorPane().getCaretPosition());
		
		synchronized(clickLock) {
			clickLock.notify();
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
	
	/**
	 * Méthode bloquante jusqu'à ce que l'utilisateur ait cliqué sur la fenêtre.
	 */
	public void waitForClick() {
		synchronized (clickLock) {
			try {
				clickLock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
	}

}
