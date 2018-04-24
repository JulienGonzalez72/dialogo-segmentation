package main;

public abstract class ReadThread extends Thread {
	
	public int N;
	
	public ReadThread() {
		N = FenetreParametre.premierSegment - 1;
	}
	
	/**
	 * Ex�cute l'algorithme � partir du num�ro de segment indiqu� en param�tre.
	 */
	public abstract void run(int N);
	
	/**
	 * Se place sur le segment N et d�marre l'algorithme de lecture.
	 */
	public void goTo(int N) {
		this.N = N;
	}
	
}
