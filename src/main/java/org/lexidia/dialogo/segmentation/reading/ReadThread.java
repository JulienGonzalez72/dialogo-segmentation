package org.lexidia.dialogo.segmentation.reading;

import java.util.ArrayList;
import java.util.List;

import org.lexidia.dialogo.segmentation.controller.ControllerText;

public abstract class ReadThread extends Thread {

	/**
	 * Numéro du segment courant
	 **/
	public int N = 1;
	
	/**
	 * Actions a effectuer à la fin de la lecture du segment
	 **/
	public List<Runnable> onPhraseEnd = new ArrayList<>();
	public boolean running = true;
	public ControllerText controler;

	public ReadThread(ControllerText controler) {
		this.controler = controler;
	}

	public void doStop() {
		interrupt();
		running = false;
	}
	
	public boolean isRunning() {
		return running && isAlive();
	}

}
