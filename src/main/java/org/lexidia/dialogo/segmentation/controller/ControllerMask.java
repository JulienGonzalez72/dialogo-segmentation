package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerMask implements ActionListener, KeyListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		synchronized (ControllerText.lockFill) {
			ControllerText.lockFill.notify();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
