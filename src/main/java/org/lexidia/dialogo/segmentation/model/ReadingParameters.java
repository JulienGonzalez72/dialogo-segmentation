package org.lexidia.dialogo.segmentation.model;

public class ReadingParameters {

	private int toleratedErrors = 2;
	private int repeatTime = 100;
	private boolean fixedField = false;
	private int hintDuration = 0;
	//public boolean replayPhrase;

	public ReadingParameters() {
		
	}

	public int getToleratedErrors() {
		return toleratedErrors;
	}

	public void setToleratedErrors(int toleratedErrors) {
		this.toleratedErrors = toleratedErrors;
	}

	public int getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(int repeatTime) {
		this.repeatTime = repeatTime;
	}

	public boolean isFixedField() {
		return fixedField;
	}

	public void setFixedField(boolean fixedField) {
		this.fixedField = fixedField;
	}

	public int getHintDuration() {
		return hintDuration;
	}

	public void setHintDuration(int hintDuration) {
		this.hintDuration = hintDuration;
	}

}
