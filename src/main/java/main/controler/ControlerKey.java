package main.controler;

import java.awt.event.*;
import main.Constants;

public class ControlerKey implements KeyListener {

	ControlerText controler;

	/**
	 * Moment du dernier clic
	 */
	private long lastClick;

	public ControlerKey(ControlerText c) {
		this.controler = c;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			/// recommence le segment ///
			if (e.getWhen() - lastClick > Constants.LEFT_DELAY) {
				controler.p.pilot.goTo(controler.p.player.getCurrentPhraseIndex());
			}
			/// retourne au segment précédent ///
			else if (controler.p.player.hasPreviousPhrase()) {
				controler.p.pilot.goTo(controler.p.player.getCurrentPhraseIndex() - 1);
			}
			lastClick = e.getWhen();
		}

		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			/// pause ///
			if (controler.p.player.isPlaying()) {
				controler.p.player.stop();
			}
			/// reprend le segment ///
			else {
				controler.p.player.play();
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
