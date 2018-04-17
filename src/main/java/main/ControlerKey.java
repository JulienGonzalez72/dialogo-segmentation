package main;

import java.awt.event.*;

public class ControlerKey implements KeyListener {

	private Player player;
	private long lastClick;
	
	public Panneau p;
	
	public ControlerKey(Player player) {
		this.player = player;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			/// recommence le segment ///
			if (e.getWhen() - lastClick > Constants.LEFT_DELAY) {
				player.repeat();
			}
			/// retourne au segment précédent ///
			else if (player.hasPreviousPhrase()) {
				player.previousPhrase();
			}
			lastClick = e.getWhen();
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			/// pause ///
			if (player.isPlaying()) {
				player.stop();
			}
			/// reprend le segment ///
			else {
				player.play();
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
