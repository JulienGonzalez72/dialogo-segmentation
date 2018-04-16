package main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class ControlerMouse implements MouseListener, MouseMotionListener {

	public static int nbErreurs;
	Panneau view;
	TextHandler handler;

	public ControlerMouse(Panneau p, TextHandler handler) {
		view = p;
		this.handler = handler;
		nbErreurs = 0;
	}

	public void mousePressed(MouseEvent e) {
		if (!view.player.isBlocked()) {
			view.controlerGlobal.waitForClick(view.nbEssaisRestantPourLeSegmentCourant,e,handler);
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		/*
		// determine le taille courante du screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = view.fenetre.getSize();
		Rectangle screenRect = new Rectangle(view.fenetre.bounds());
		// creer le screenshot
		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage image = robot.createScreenCapture(screenRect);
		// sauvegarde de l'image vers un fichier "png"

		try {
			ImageIO.write(image, "png", new File("screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage image2 = null;
		try {
			image2 = ImageIO.read(new File("screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		int x = arg0.getX();
		int y = arg0.getY();
		
		 // recuperer couleur de chaque pixel
        Color pixelcolor= new Color(image2.getRGB(x, y));
         
        // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
        int r=pixelcolor.getRed();
        int g=pixelcolor.getGreen();
        int b=pixelcolor.getBlue();
        
        System.out.println(r+"/"+g+"/"+b);
        System.out.println(Color.black == new Color(r,b,g)); 
        */
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		if (view.editorPane.contains(x, y)) {
			view.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

	}

}
