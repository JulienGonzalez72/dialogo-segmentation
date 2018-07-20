package org.lexidia.dialogo.segmentation.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerMask implements ActionListener, KeyListener {

	private Object fillLock = new Object();
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		synchronized (fillLock) {
			fillLock.notify();
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
	
	public void waitForFill() {
		synchronized (fillLock) {
			try {
				fillLock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
