package fr.lexidia.dialogo.main;

public class MultipleTest {
	
	public static void main(String[] args) {
		for (int s = 20; s < 60; s += 20) {
			for (float f = 1f; f < 2f; f += 0.2f) {
				LSTest.main(new String[] {s + "", f + ""});
			}
		}
	}
	
}
