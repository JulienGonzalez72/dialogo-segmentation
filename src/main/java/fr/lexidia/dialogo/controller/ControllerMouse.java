package fr.lexidia.dialogo.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.model.TextHandler;
import fr.lexidia.dialogo.view.SegmentedTextPanel;

public class ControllerMouse implements MouseListener {

	private SegmentedTextPanel view;
	private TextHandler handler;

	/**
	 * Dernière position absolue du texte sur laquelle l'utilisateur a cliqué.
	 */
	private int lastTextOffset;
	private EventDispatcher ed;

	private Object clickLock = new Object();
	private boolean enabled;

	public ControllerMouse(SegmentedTextPanel p, TextHandler handler, EventDispatcher ed) {
		view = p;
		this.handler = handler;
		this.ed = ed;
	}

	public int getLastTextOffset() {
		return lastTextOffset;
	}

	public void simuleMousePressed(int carret) {
		view.getEditorPane().setCaretPosition(carret);
		lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(), carret);
		synchronized (clickLock) {
			clickLock.notify();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (enabled) {
			lastTextOffset = handler.getAbsoluteOffset(view.getFirstShownPhraseIndex(),
					view.getEditorPane().getCaretPosition());
			synchronized (clickLock) {
				clickLock.notify();
			}
			dispatch("mousePressed", view.getEditorPane().getCaretPosition());
		}
	}

	private void dispatch(String e, Object... o) {
		if (ed.isPatient()) {
			ed.dispatch(e, view.getEditorPane().getCaretPosition());
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

	public void setEnabled(boolean patient) {
		this.enabled = patient;
	}

}
