package main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.text.*;

public class ControlerMouse implements MouseListener {

	Panneau view;

	public ControlerMouse(Panneau p) {
		view = p;
	}

	public void mouseClicked(MouseEvent e) {
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
