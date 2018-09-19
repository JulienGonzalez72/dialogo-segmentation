package org.lexidia.dialogo.segmentation.main;

public class MultipleTest {
	
	public static void main(String[] args) {
		for (int s = 20; s < 60; s += 20) {
			for (float f = 0f; f < 0.8f; f += 0.2f) {
				LSTest.main(new String[] {s + "", f + ""});
			}
		}
	}
	
}
