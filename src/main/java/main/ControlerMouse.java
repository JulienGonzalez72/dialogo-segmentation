package main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.text.*;

<<<<<<< HEAD
public class ControlerMouse implements MouseListener{
	
	private Panneau view;
	private TextHandler handler;
	
	public ControlerMouse(Panneau p, TextHandler handler){
=======
public class ControlerMouse implements MouseListener {

	Panneau view;

	public ControlerMouse(Panneau p) {
>>>>>>> a759dec80509cf7b562d839c99832cfd29bb5bdc
		view = p;
		this.handler = handler;
	}

	public void mouseClicked(MouseEvent e) {
<<<<<<< HEAD
		if (handler.correctPause(view.editorPane.getCaretPosition()))
			view.editorPane.insert(view.editorPane.getCaretPosition(), "/");
=======
		String texteAvecCesure = view.texteAvecCesure;
		TextPane panel = view.editorPane;
		int positionClic = panel.getCaretPosition();
		TextPane panelCesure = new TextPane();
		panelCesure.setText(texteAvecCesure);
		try {
			System.out.println(panel.getText(positionClic, 1));
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		view.editorPane.surlignerPhrase(positionClic, Color.green);
>>>>>>> a759dec80509cf7b562d839c99832cfd29bb5bdc
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}


}
