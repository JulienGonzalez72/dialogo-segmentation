package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerMask implements ActionListener, KeyListener {

	private boolean enter;

	public ControllerMask() {

	}
	
	public void setEnter(boolean enter) {
		this.enter = enter;
	}

	public boolean isEnter() {
		return enter;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		enter = true;
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
