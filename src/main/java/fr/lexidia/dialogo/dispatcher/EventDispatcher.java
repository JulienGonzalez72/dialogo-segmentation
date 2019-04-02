package fr.lexidia.dialogo.dispatcher;

import java.awt.Color;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;

import fr.lexidia.dialogo.controller.ControllerText;
import fr.lexidia.dialogo.main.LSTest;

public class EventDispatcher {

	private boolean test = false;
	private String user;
	private String dest;
	private boolean isPatient;
	private ClientEndPoint client;
	private Object lock = new Object();
	private ControllerText ctl;

	public EventDispatcher(String user, String dest, boolean isPatient, String ip) throws URISyntaxException {
		if(!test) {
			String uri = "ws://"+ip+":8080/WebSocketTest/test";
			this.user = user;
			this.dest = dest;
			this.isPatient = isPatient;
			client = new ClientEndPoint(new URI(uri + "/" + user));
			client.addMessageHandler(new ClientEndPoint.MessageHandler() {
				public void handleMessage(String message) {
					read(message);
				}
			});	
		}		
	}

	public void addControllerText(ControllerText ctl) {
		this.ctl = ctl;
	}

	/**
	 * params = int, string, boolean, long...
	 * objects = other objects
	 */
	public boolean dispatch(String e, Object... objects) {
		if(test) {
			return true;
		}
		System.out.println("DISPACHING : " + e.toString());
		JSONObject j = new JSONObject();
		j.put("user", user);
		j.put("dest", dest);
		j.put("event", e);
		if (objects != null) {
			int i = 0;
			for (Object o : objects) {
				j.put(i + "", o);
				i++;
			}
		}
		client.sendMessage(j.toString());
		if (!e.equals("reply")) {
			try {
				synchronized (lock) {
					lock.wait();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}

	private void read(String message) {
		String trace = "";
		try {
			JSONObject jo = new JSONObject(message);
			trace = jo.toString();
			String event = (String) jo.get("event");
			System.out.println();
			System.out.println("Event : " + event);
			if (event.equals("reply")) {
				System.out.println(jo.getString("content"));
				synchronized (lock) {
					lock.notify();
				}
			} else {
				manageEvent(jo, event);
				JSONObject rep = new JSONObject();
				rep.put("user", user);
				rep.put("dest", dest);
				rep.put("event", "reply");
				rep.put("content", "Transmission ok");
				client.sendMessage(rep.toString());
				System.out.println("Réponse renvoyée");
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(trace);
			e.printStackTrace();
		}
	}

	private void manageEvent(JSONObject jo, String event) {
		switch (event) {
		case "keyEvent":
			ctl.keyEvent(jo.getInt("0"),jo.getLong("1"));
			break;
		case "resize":
			ctl.reSize(jo.getInt("0"),jo.getInt("1"));
			break;
		case "mousePressed":
			int carret = jo.getInt("0");
			ctl.mousePressed(carret);
			break;
		case "param_familyPolice":
			Font oldFont = ctl.getFont();
			ctl.setFont(new Font(jo.getString("0"), oldFont.getStyle(), oldFont.getSize()));
			break;
		case "param_highlightFromStart":
			LSTest.distantParam_highlightFromStart = jo.getBoolean("0");
			if (!LSTest.distantParam_highlightFromStart) {
				ctl.removeAllHighlights();
			} else {
				ctl.highlightUntilPhrase(Color.GREEN, ctl.getCurrentPhraseIndex() - 1);
			}
			break;
		case "param_interline":
			ctl.setLineSpacing(jo.getFloat("0"));
			break;
		case "param_marginT":
			ctl.setTopMargin(jo.getFloat("0"));
			break;
		case "param_marginB":
			ctl.setBottomMargin(jo.getFloat("0"));
			break;
		case "param_marginL":
			ctl.setLeftMargin(jo.getFloat("0"));
			break;
		case "param_marginR":
			ctl.setRightMargin(jo.getFloat("0"));
			break;
		case "param_sizePolice":
			ctl.setFontSize(jo.getInt("0"));
			break;
		case "startExercice":
			LSTest.distantParam_highlightFromStart = jo.getBoolean("0");
			ctl.goTo(0);
			break;
		default:
			System.out.println("EVENT INCONNU : " + event);
			System.exit(-2);
		}
	}

	public boolean isPatient() {
		return isPatient;
	}

}
