package fr.lexidia.dialogo.main;

import java.awt.Font;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import fr.lexidia.dialogo.controller.Pilot;

/**
 * Classe utilitaire d'assertion qui lance des exceptions, elle sert notamment au ControllerText.
 * @author Julien Gonzalez
 */
public final class Assert {
	
	public static void assertFont(Font font) throws IllegalArgumentException {
		assertNotNull(font, "font");
		assertPositive(font.getSize(), "Font size");
		assertNotEmpty(font.getFamily(), "Font family");
	}
	
	public static void assertNotNull(Object obj, String varName) throws IllegalArgumentException {
		if (obj == null) {
			error(varName + " is null !");
		}
	}
	
	public static void assertNotEmpty(String str, String varName) throws IllegalArgumentException {
		assertNotNull(str, varName);
		if (str.isEmpty()) {
			error(varName + " must not be empty !");
		}
	}
	
	public static void assertPositiveOrNull(double d, String varName) throws IllegalArgumentException {
		if (d < 0) {
			error(varName + " (" + d + ") must be positive or null !");
		}
	}
	
	public static void assertPositive(double d, String varName) throws IllegalArgumentException {
		if (d <= 0) {
			error(varName + " (" + d + ") must be strictly positive !");
		}
	}
	
	@SafeVarargs
	public static <T> void assertContains(T obj, String varName, T... possibleValues) throws IllegalArgumentException {
		if (Arrays.binarySearch(possibleValues, obj, null) < 0) {
			error("Unacceptable value for " + varName + " (possible values are " + Arrays.toString(possibleValues) + ")");
		}
	}
	
	public static void assertGreater(double d1, double d2, String varName) throws IllegalArgumentException {
		if (d1 <= d2) {
			error(varName + " (" + d1 + ") must be greater than " + d2 + " !");
		}
	}
	
	public static void assertGreaterOrEquals(double d1, double d2, String varName) throws IllegalArgumentException {
		if (d1 < d2) {
			error(varName + " (" + d1 + ") must be greater or equals than " + d2 + " !");
		}
	}
	
	public static void assertNotStarted(Pilot pilot, String action) throws IllegalStateException {
		if (pilot.hasStarted()) {
			throw new IllegalStateException("Can't " + action + " while the exercise has started !");
		}
	}
	
	public static void assertSwingThread(String action) throws IllegalThreadStateException {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalThreadStateException("Trying to " + action + " in a no-Swing Thread !");
		}
	}
	
	private static void error(String message) throws IllegalArgumentException {
		throw new IllegalArgumentException(message);
	}
	
}