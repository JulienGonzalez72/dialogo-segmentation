package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class ControlerMouse implements MouseListener{
	
	Panneau view;
	
	public ControlerMouse(Panneau p){
		view = p;
	}

	public void mouseClicked(MouseEvent e) {
		JTextPane panel = view.editorPane;
		int positionClic = panel.getCaretPosition();
		int longueur = 1;
		String word = " ";
		try {
			if ( Character.isAlphabetic(panel.getText(positionClic+1, 1).toCharArray()[0])){
				word += panel.getText(positionClic+1, 1);
			}
		} catch (BadLocationException e2) {
			
		}
		while( Character.isAlphabetic(word.toCharArray()[word.length()-1])){
			longueur++;
			try {
				word += panel.getText(positionClic+longueur, 1);
			} catch (BadLocationException e1) {
				break;
			}
		}
		try {
			System.out.println(panel.getText(positionClic, longueur));
		} catch (BadLocationException e1) {
			
		}
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
