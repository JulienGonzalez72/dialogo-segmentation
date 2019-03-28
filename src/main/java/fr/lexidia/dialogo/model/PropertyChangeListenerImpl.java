package fr.lexidia.dialogo.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;

public class PropertyChangeListenerImpl implements PropertyChangeListener {

	private JTextField[] marginFields;

	public PropertyChangeListenerImpl(JTextField[] marginFields) {
		this.marginFields = marginFields;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		float v = (float) evt.getNewValue();
		v = truncate(v);
		
		switch (evt.getPropertyName()) {
			case "topMargin" : marginFields[0].setText(String.valueOf(v)); break;
			case "bottomMargin" : marginFields[1].setText(String.valueOf(v)); break;
			case "leftMargin" : marginFields[2].setText(String.valueOf(v)); break;
			case "rightMargin" : marginFields[3].setText(String.valueOf(v)); break;
		}
	}
	
	private static float truncate(float value) {
		return (int) (value * 100) / 100f;
	}

	public JTextField[] getMarginFields() {
		return marginFields;
	}

}
