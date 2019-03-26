package fr.lexidia.dialogo.controller;

import java.awt.event.*;

import fr.lexidia.dialogo.main.Constants;

public class ControllerKey implements KeyListener {

	protected Pilot pilot;

	/**
	 * Moment du dernier clic
	 */
	protected long lastClick;

	public ControllerKey(Pilot pilot) {
		this.pilot = pilot;
	}
	
	public ControllerKey() {
		this.pilot = null;
	}
	
	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			/// recommence le segment ///
			if (e.getWhen() - lastClick > Constants.LEFT_DELAY) {
				pilot.goTo(pilot.getCurrentPhraseIndex());
			}
			/// retourne au segment precedent ///
			else if (pilot.hasPreviousPhrase()) {
				pilot.goTo(pilot.getCurrentPhraseIndex() - 1);
			}
			lastClick = e.getWhen();
		}

		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
