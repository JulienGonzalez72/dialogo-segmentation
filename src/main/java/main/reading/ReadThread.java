package main.reading;

import java.util.ArrayList;
import java.util.List;

import main.controler.ControlerText;

public abstract class ReadThread extends Thread {

	/**
	 * Numero du segment courant
	 **/
	public int N;

	/**
	 * Actions a effectuer � la fin de la lecture du segment
	 **/
	public List<Runnable> onPhraseEnd = new ArrayList<>();
	public boolean running = true;
	public ControlerText controler;

	public ReadThread(ControlerText controler, int N) {
		this.controler = controler;
		this.N = N;
	}

	public void doStop() {
		interrupt();
		running = false;
	}

}
