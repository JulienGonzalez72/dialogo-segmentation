package org.lexidia.dialogo.segmentation.main;

public class MultipleTest {
	
	public static void main(String[] args) {
		for (int s = 40; s < 70; s += 20) {
			for (float f = 0f; f < 0.8f; f += 0.2f) {
				LSTest.main(new String[] {s + "", f + ""});
			}
		}
	}
	
}
