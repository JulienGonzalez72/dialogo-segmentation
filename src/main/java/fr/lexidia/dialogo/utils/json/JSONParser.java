package fr.lexidia.dialogo.utils.json;

import java.awt.Font;

import org.json.JSONObject;

public class JSONParser {
	
	public static Font pFont(JSONObject jo) {
		return new Font(jo.getString("name"),jo.getInt("style"),jo.getInt("size"));
	}

}
