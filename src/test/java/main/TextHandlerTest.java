package main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.model.TextHandler;
import main.view.Panneau;

public class TextHandlerTest {
	
	private TextHandler handler;
	private String[] words = {
			"surpris", "inattendue,", "moins", "sensations.", "entraîné",
			"nageur,", "Poe,", "maîtres,", "talons", "mer.", "soin"
	};
	
	@Before
	public void setUp() throws Exception {
		handler = new TextHandler(Panneau.getTextFromFile("ressources/textes/20 000 lieux sous les mers"));
	}
	
	@After
	public void tearDown() throws Exception {
		handler = null;
	}
	
	@Test
	public void words() {
		for (int i = 0; i < handler.getPhrases().length && i < words.length; i++) {
			String word = "";
			for (int j = 0; j < handler.getPhrases()[i].length(); j++) {
				char c = handler.getPhrases()[i].charAt(j);
				System.out.println();
				System.out.println("====== " + i + " ======");
				if (handler.wordPause(handler.getAbsoluteOffset(i, j))) {
					word += c;
					System.out.println();
					System.out.println("!!! MATCH pour " + c + " !!!");
				}
			}
			if (word.startsWith(" "))
				word = word.substring(1);
			assertEquals(words[i], word);
		}
	}
	
	@Test
	public void wordPause() {
		assertEquals(false, handler.wordPause(44));
	}
	
}