package main.reading;

import java.util.ArrayList;
import java.util.List;

import main.controler.ControlerText;

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
	public ControlerText controler;

	public ReadThread(ControlerText controler) {
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
