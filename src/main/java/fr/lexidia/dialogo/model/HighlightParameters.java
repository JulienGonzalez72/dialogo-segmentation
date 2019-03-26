package fr.lexidia.dialogo.model;

import java.awt.Color;

import fr.lexidia.dialogo.main.Constants;

public class HighlightParameters {

	private Color rightColor = Constants.RIGHT_COLOR;
	private Color correctionColor = Constants.WRONG_PHRASE_COLOR;
	private Color wrongColor = Constants.WRONG_COLOR;
	
	public Color getWrongColor() {
		return wrongColor;
	}
	public void setWrongColor(Color wrongColor) {
		this.wrongColor = wrongColor;
	}
	public Color getCorrectionColor() {
		return correctionColor;
	}
	public void setCorrectionColor(Color correctionColor) {
		this.correctionColor = correctionColor;
	}
	public Color getRightColor() {
		return rightColor;
	}
	public void setRightColor(Color rightColor) {
		this.rightColor = rightColor;
	}
	
	
}
