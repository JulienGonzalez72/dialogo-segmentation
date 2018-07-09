package org.lexidia.dialogo.segmentation.reading;

import java.util.ArrayList;
import java.util.List;

import org.lexidia.dialogo.segmentation.controller.ControllerText;

public abstract class ReadThread extends Thread {

	/**
	 * Numéro du segment courant
	 **/
	private int N = 1;
	
	/**
	 * Actions a effectuer à  la fin de la lecture du segment
	 **/
	private List<Runnable> onPhraseEnd = new ArrayList<>();
	private boolean running = true;
	private ControllerText controler;

	public ReadThread(ControllerText controler) {
		this.setControler(controler);
	}

	public void doStop() {
		interrupt();
		setRunning(false);
	}
	
	public boolean isRunning() {
		return running && isAlive();
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public List<Runnable> getOnPhraseEnd() {
		return onPhraseEnd;
	}

	public void setOnPhraseEnd(List<Runnable> onPhraseEnd) {
		this.onPhraseEnd = onPhraseEnd;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public ControllerText getControler() {
		return controler;
	}

	public void setControler(ControllerText controler) {
		this.controler = controler;
	}

}
