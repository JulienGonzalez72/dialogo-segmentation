package main;

public abstract class ReadThread extends Thread {
	
	public int N;
	
	public ReadThread() {
		N = FenetreParametre.premierSegment - 1;
	}
	
	/**
	 * Exécute l'algorithme à partir du numéro de segment indiqué en paramètre.
	 */
	public abstract void run(int N);
	
	/**
	 * Se place sur le segment N et démarre l'algorithme de lecture.
	 */
	public void goTo(int N) {
		this.N = N;
	}
	
}
