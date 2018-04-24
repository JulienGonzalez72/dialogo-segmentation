package main;

import java.awt.event.*;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	Panneau view;
	TextHandler handler;
	
	public boolean clicking;

	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		clicking = true;
		/*if (!view.player.isBlocked()  && e.getClickCount() < 2) {
			view.controlerGlobal.doClick(view.nbEssaisRestantPourLeSegmentCourant,view.player.getCurrentPhraseIndex());
		}*/
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
