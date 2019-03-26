package fr.lexidia.dialogo.utils.json;

import java.awt.Color;

import org.json.JSONObject;

import fr.lexidia.dialogo.dispatcher.JSONAble;

public class ColorJ implements JSONAble{

	private Color c;
	
	public ColorJ(Color c) {
		this.c = c;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		j.put("r", c.getRed());
		j.put("v", c.getGreen());
		j.put("b", c.getBlue());
		return j;
	}

}
