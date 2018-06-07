package main.controler;

import java.awt.event.*;

import main.model.TextHandler;
import main.view.TextPanel;

public class ControlerMouse implements MouseListener {

	public static int nbErreurs;
	TextPanel view;
	TextHandler handler;
	
	/**
	 * Icone de la souris lors d'une phase d'écoute
	 */
	public boolean clicking;

	public ControlerMouse(TextPanel p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		clicking = true;
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
