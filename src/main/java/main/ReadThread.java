package main;

import java.util.*;

public abstract class ReadThread extends Thread {
	
	public int N;
	public List<Runnable> onPhraseEnd = new ArrayList<>();
	public boolean running = true;
	public ControlerGlobal controler;
	
	public ReadThread(ControlerGlobal controler, int N) {
		this.controler = controler;
		this.N = N;
	}
	
	public void doStop() {
		interrupt();
		running = false;
	}
	
}
