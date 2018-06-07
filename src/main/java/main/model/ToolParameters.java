package main.model;

import java.awt.Color;
import java.awt.Font;

import main.Constants;

/**
 * Paramètres non spécifiques à un mode de lecture.
 * @author Julien Gonzalez
 */
public class ToolParameters {
	
	public Font font;
	public int panWidth;
	public int panHeight;
	public int panX;
	public int panY;
	public int startingPhrase;
	public String text;
	public Color bgColor = Constants.BG_COLOR;
	
	public ToolParameters(Font font, int panWidth, int panHeight, int panX, int panY, int startingPhrase, String text) {
		this.font = font;
		this.panWidth = panWidth;
		this.panHeight = panHeight;
		this.panX = panX;
		this.panY = panY;
		this.startingPhrase = startingPhrase;
		this.text = text;
	}
	
}
