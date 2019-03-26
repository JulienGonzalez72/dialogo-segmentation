package fr.lexidia.dialogo.dispatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONObject;

import fr.lexidia.dialogo.utils.json.JSONTransformer;

public class EventDispatcher {

	private String user;
	private String ortho;
	private final ClientEndPoint client;
	private Object lock = new Object();

	public EventDispatcher() throws URISyntaxException {
		String uri = "ws://25.31.180.181:8080/WebSocketTest/test";
		user = "Roro";
		ortho = "ortho1";
		client = new ClientEndPoint(new URI(uri + "/" + user));
		client.addMessageHandler(new ClientEndPoint.MessageHandler() {
			public void handleMessage(String message) {
				readMessage(message);
			}
		});
	}

	/**
	 *params = int, string, boolean, long...
	 *objects = other objects
	 */
	public boolean dispatch(Event e, List<Object> objects) {
		System.out.println("DISPACHING : " + e.toString());
		JSONObject j = new JSONObject();
		j.put("user", user);
		j.put("ortho", ortho);
		j.put("event", e.toString());
		if (objects != null) {
			for (int i = 0; i < objects.size(); i++) {
				j.put("object" + i, JSONTransformer.toJSON(objects.get(i)));
			}
		}
		client.sendMessage(j.toString());
		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	private void readMessage(String message) {
		synchronized (lock) {
			lock.notify();
		}
		System.out.println("Message du server reçu : " + message);
	}

}
