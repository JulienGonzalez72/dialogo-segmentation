package fr.lexidia.dialogo.dispatcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Therapist {
	
	static JFrame frame;
	static JPanel pan;
	static JLabel test;
	static ClientEndPoint ortho;

	public static void main(String[] args) throws URISyntaxException {
		initConnection();
		initTestFrame();
	}

	private static void initTestFrame() {
		frame = new JFrame("Test");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pan = new JPanel();
		pan.setBackground(Color.WHITE);
		pan.setLayout(new GridLayout(1, 1));
		
		test = new javax.swing.JLabel("Portez ce vieux whisky au juge blond qui fume");
		test.setHorizontalAlignment(JLabel.CENTER);
		pan.add(test);
		
		frame.setContentPane(pan);
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	private static void initConnection() throws URISyntaxException {
		String uri = "ws://25.31.180.181:8080/WebSocketTest/test";
		String user = "ortho1";
		ortho = new ClientEndPoint(new URI(uri + "/" + user));
		ortho.addMessageHandler(new ClientEndPoint.MessageHandler() {
			public void handleMessage(String message) {
				manageEvent(message);
			}
		});
	}

	private static void manageEvent(String message) {
		System.out.println();
		System.out.println("Message reçu");
		JSONParser parser = new JSONParser();
		JSONObject jo = null;
		try {
			jo = (JSONObject) parser.parse(message);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String userName = (String) jo.get("user");
		String event = (String) jo.get("event");
		System.out.println("Auteur : " + userName);
		System.out.println("Event : " + event);
		switch (event) {
		case "setFont":
			JSONObject j = (JSONObject) jo.get("object0");
			String family = (String) j.get("family");
			int style = toInt(j, "style");
			int size = toInt(j, "size");
			Font f = new Font(family, style, size);
			test.setFont(f);
			System.out.println("Font updated");
			break;
		}
		String rep = "Font bien transmis :)";
		ortho.sendMessage("OK-REPONSE_"+userName+"_"+rep);
		System.out.println();
	}
	
	private static int toInt(JSONObject j, String string) {
		return ((Long) j.get(string)).intValue();
	}
	
}
