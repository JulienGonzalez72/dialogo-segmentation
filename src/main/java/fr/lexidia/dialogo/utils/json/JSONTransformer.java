package fr.lexidia.dialogo.utils.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONTransformer {
	
	/**
	 * Parse a json to obtain the corresponding object.<br>
	 * The class of this object must contains EXACTLY the same fields than the entries of the json.
	 * @param basicInstance an empty instance of an object (same class than the returned one)
	 * @param json the json to parse
	 * @return the parsed object
	 */
	public static <T> T parse(T basicInstance, JSONObject json) {
		List<Field> fields = getAllFields(basicInstance.getClass());
		for (String key : json.keySet()) {
			try {
				Field field = getField(fields, key);
				field.setAccessible(true);
				field.set(basicInstance, json.get(key));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return basicInstance;
	}
	
	/**
	 * Transform any object in a json representation by assigning each of its fields with an entry.<br>
	 * All inherited and non-static fields are taken into account.<br>
	 * <br>
	 * If obj is a primitive object, it will give a json with one entry like this :<br>
	 * <code>{"value":0}</code>
	 * @param obj the object to transform
	 * @return the json representation
	 */
	public static <T> JSONObject toJSON(T obj) {
		if (obj == null) {
			return null;
		}
		JSONObject json = new JSONObject();
		for (Field field : getAllFields(obj.getClass())) {
			if (!Modifier.isStatic(field.getModifiers())) {
				try {
					field.setAccessible(true);
					json.put(field.getName(), field.get(obj));
				} catch (JSONException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}
	
	private static <T> List<Field> getAllFields(Class<T> c) {
		List<Field> fields = new ArrayList<Field>();
		Class<? super T> superClass = c;
		do {
			fields.addAll(filterDeclaredFields(superClass));
		} while ((superClass = superClass.getSuperclass()) != null);
		return fields;
	}
	
	private static List<Field> filterDeclaredFields(Class<?> c) {
		return Arrays.asList(c.getDeclaredFields())
				.stream()
				.filter(f -> !Modifier.isStatic(f.getModifiers()))
				.collect(Collectors.toList());
	}
	
	private static Field getField(List<Field> fields, String name) {
		return fields.stream()
				.filter(f -> f.getName().equals(name))
				.findFirst()
				.orElse(null);
	}

}
