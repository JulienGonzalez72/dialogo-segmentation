package org.lexidia.dialogo.segmentation.dispatcher.json;

import java.awt.Font;

import org.json.JSONObject;
import org.lexidia.dialogo.segmentation.dispatcher.JSONAble;

public class JSONAbleFont implements JSONAble{

	private Font f;
	
	public JSONAbleFont(Font f) {
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
