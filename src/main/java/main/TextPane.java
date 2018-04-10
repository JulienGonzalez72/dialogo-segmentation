package main;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;

public class TextPane extends JTextPane {
	
	public TextPane() {
		setFont(new Font("OpenDyslexic", Font.BOLD, 20));
		setBackground(new Color(255, 255, 150));
	}
	
}
