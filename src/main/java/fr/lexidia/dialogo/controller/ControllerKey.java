package fr.lexidia.dialogo.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.Constants;

public class ControllerKey implements KeyListener {

	protected Pilot pilot;

	/**
	 * Moment du dernier clic
	 */
	protected long lastClick;
	protected EventDispatcher ed;

	public ControllerKey(Pilot pilot, EventDispatcher ed) {
		this.pilot = pilot;
		this.ed = ed;
	}
	
	public ControllerKey() {
		this.pilot = null;
	}
	
	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}
	
	public void simuleKeyPressed(int keyCode, long when) {
		doWhenKeyPressed(keyCode,when);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		doWhenKeyPressed(e.getKeyCode(),e.getWhen());
		ed.dispatch("keyEvent",e.getKeyCode(),e.getWhen());
	}

	private void doWhenKeyPressed(int code, long when) {
		if (code == KeyEvent.VK_LEFT) {
			/// recommence le segment ///
			if (when - lastClick > Constants.LEFT_DELAY) {
				pilot.goTo(pilot.getCurrentPhraseIndex());
			}
			/// retourne au segment precedent ///
			else if (pilot.hasPreviousPhrase()) {
				pilot.goTo(pilot.getCurrentPhraseIndex() - 1);
			}
			lastClick = when;
		}

		else if (code == KeyEvent.VK_SPACE) {
			/// pause ///
			if (pilot.isRunning()) {
				pilot.doStop();
			}
			/// reprend le segment ///
			else {
				pilot.doPlay();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
