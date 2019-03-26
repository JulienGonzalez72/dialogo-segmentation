package fr.lexidia.dialogo.utils.json;

import java.awt.Font;

import org.json.JSONObject;

import fr.lexidia.dialogo.dispatcher.JSONAble;

public class FontJ implements JSONAble{

	private Font f;
	
	public FontJ(Font f) {
		this.f = f;
	}
	
	@Override
	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		j.put("family", f.getFamily());
		j.put("style", f.getStyle());
		j.put("size", f.getSize());
		return j;
	}

}
