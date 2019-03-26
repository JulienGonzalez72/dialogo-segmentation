package fr.lexidia.dialogo.dispatcher;

import java.awt.Color;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONObject;

import com.alee.laf.WebLookAndFeel;

import fr.lexidia.dialogo.controller.ControllerText;
import fr.lexidia.dialogo.main.LSTest;
import fr.lexidia.dialogo.main.LSTest.LSThread;
import fr.lexidia.dialogo.reading.ReadThread;
import fr.lexidia.dialogo.reading.ReaderFactory;
import fr.lexidia.dialogo.utils.json.JSONParser;
import fr.lexidia.dialogo.view.SegmentedTextFrame;

public class Therapist {

	private static ControllerText controler;
	private static ClientEndPoint ortho;

	public static void main(String[] args) throws URISyntaxException{
		initConnection();
		initFrame();
	}

	private static void initFrame() {
		if (LSTest.WEBLAF) {
			WebLookAndFeel.install();
			UIManager.put("TextPaneUI", javax.swing.plaf.basic.BasicTextPaneUI.class.getCanonicalName());
		}
		else {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		final SegmentedTextFrame frame = new SegmentedTextFrame("Dialogo - Lecture segmentée"); // le titre
		String file = "resources/textes/Amélie la sorcière" + (LSTest.WRAPPED_TEXT ? "" : "_oneline") + ".txt";
		frame.init(LSTest.getTextFromFile(file), 0, new Font(Font.DIALOG, Font.PLAIN, 20), 100, 100, 20.25f, 9f);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.start();
		frame.setOnInit(new Runnable() {
			public void run() {
				controler = new ControllerText(frame,false);
				controler.setHighlightColors(Color.GREEN, Color.RED, Color.CYAN);
				controler.setLineSpacing(0.2f);
				controler.setMaxPhrasesByPage(LSTest.MAX_PHRASES_BY_PAGE);
				controler.setReaderFactory(new ReaderFactory() {
					public ReadThread createReadThread() {
						return new LSThread(controler);
					}
				});
				
				//because we are in Therapist mode
				controler.setBackgroundColor(Color.WHITE);
				frame.setTitle("THERAPIST VIEW OF PATIENT FRAME");

				if (LSTest.START_EXERCICE) {
					controler.goTo(0);
				}
			}
		});
	}
	
	static class TherapistThread extends ReadThread{
		public TherapistThread(ControllerText controler) {
			super(controler);
		}
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
		try {
			System.out.println();
			System.out.println("Message reçu");
			JSONObject jo = new JSONObject(message);
			String userName = (String) jo.get("user");
			String event = (String) jo.get("event");
			System.out.println("Auteur : " + userName);
			System.out.println("Event : " + event);
			switch (event) {
			case "setFont":
				JSONObject j = (JSONObject) jo.get("object0");
				Font f = JSONParser.pFont(j);
				controler.setFont(f);
				break;
			}
			String rep = "Font bien transmis :)";
			ortho.sendMessage("OK-REPONSE_" + userName + "_" + rep);
			System.out.println("Réponse renvoyée");
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}

}
