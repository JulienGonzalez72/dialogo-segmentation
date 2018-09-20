package org.lexidia.dialogo.segmentation.model;

import java.awt.Color;
import java.awt.Font;

import org.lexidia.dialogo.segmentation.main.Constants;

/**
 * Paramètres non spécifiques à un mode de lecture.
 * @author Julien Gonzalez
 */
public class ToolParameters {
	
	private Font font;
	private int panWidth;
	private int panHeight;
	private int panX;
	private int panY;
	private int startingPhrase;
	private int maxPhrases;
	private String text;
	private Color bgColor = Constants.BG_COLOR;
	
	public ToolParameters(Font font, int panWidth, int panHeight, int panX, int panY, int startingPhrase, int maxPhrases, String text) {
		this.setFont(font);
		this.setPanWidth(panWidth);
		this.setPanHeight(panHeight);
		this.setPanX(panX);
		this.setPanY(panY);
		this.setStartingPhrase(startingPhrase);
		this.setMaxPhrases(maxPhrases);
		this.setText(text);
	}
	
	public int getMaxPhrases() {
		return maxPhrases;
	}
	
	public void setMaxPhrases(int maxPhrases) {
		this.maxPhrases = maxPhrases;
	}
	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getPanWidth() {
		return panWidth;
	}

	public void setPanWidth(int panWidth) {
		this.panWidth = panWidth;
	}

	public int getPanHeight() {
		return panHeight;
	}

	public void setPanHeight(int panHeight) {
		this.panHeight = panHeight;
	}

	public int getPanX() {
		return panX;
	}

	public void setPanX(int panX) {
		this.panX = panX;
	}

	public int getPanY() {
		return panY;
	}

	public void setPanY(int panY) {
		this.panY = panY;
	}

	public int getStartingPhrase() {
		return startingPhrase;
	}

	public void setStartingPhrase(int startingPhrase) {
		this.startingPhrase = startingPhrase;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
}
