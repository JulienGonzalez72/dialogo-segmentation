package main;

<<<<<<< HEAD
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

=======
>>>>>>> 7f22890932cfc28b2329d634c0514304c18eb641
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
<<<<<<< HEAD
		if (!view.player.isBlocked()) {
			view.controlerGlobal.waitForClick(view.nbEssaisRestantPourLeSegmentCourant,e,handler);
		}
=======
		view.controlerGlobal.waitForClick(view.nbEssaisRestantPourLeSegmentCourant,e,handler);
>>>>>>> 7f22890932cfc28b2329d634c0514304c18eb641
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
