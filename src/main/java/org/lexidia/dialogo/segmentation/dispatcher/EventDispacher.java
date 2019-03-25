package org.lexidia.dialogo.segmentation.dispatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONObject;


public class EventDispacher {

	private String user;
	private final ClientEndPoint client;
	private Object lock = new Object();

	public EventDispacher() throws URISyntaxException {
		String uri = "ws://25.31.180.181:8080/WebSocketTest/test";
		user = "Roro";
		client = new ClientEndPoint(new URI(uri + "/" + user));
		client.addMessageHandler(new ClientEndPoint.MessageHandler() {
			public void handleMessage(String message) {
				readMessage(message);
			}
		}); 
	}

	public boolean dispatch(Event e, List<JSONAble> params) {
		System.out.println("DISPACHING : " + e.toString());
		JSONObject j = new JSONObject();
		j.put("user", user);
		j.put("event", e.toString());
		for (int i = 0; i < params.size(); i++) {
			j.put("object"+i,params.get(i).toJson());
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
