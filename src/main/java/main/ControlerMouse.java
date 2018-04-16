package main;

import java.awt.event.*;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	Panneau view;
	TextHandler handler;

	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		if (!view.player.isBlocked()) {
			view.controlerGlobal.waitForClick(view.nbEssaisRestantPourLeSegmentCourant,e,handler);
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

}
