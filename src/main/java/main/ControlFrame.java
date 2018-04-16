package main;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ControlFrame extends JFrame {
	
	private static int imageSize = 40;
	private static Image previousIcon, playIcon, pauseIcon, nextIcon;
	
	private JPanel panel = new JPanel();
	private JButton previousButton = new JButton();
	private JButton playButton = new JButton();
	private JButton nextButton = new JButton();
	private Player player;
	
	static {
		loadImages();
	}
	
	public ControlFrame(Player p) {
		player = p;
		setTitle("Contrôle");
		setContentPane(panel);
		setSize(250, 90);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		
		panel.add(previousButton);
		previousButton.setIcon(new ImageIcon(previousIcon));
		previousButton.setEnabled(player.hasPreviousPhrase());
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.previousPhrase();
				updateButtons();
			}
		});
		
		panel.add(playButton);
		playButton.setIcon(new ImageIcon(playIcon));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (player.isPlaying()) {
					player.stop();
					playButton.setIcon(new ImageIcon(playIcon));
				}
				else if (player.isPhraseFinished()) {
					player.repeat();
					playButton.setIcon(new ImageIcon(pauseIcon));
				}
				else {
					player.play();
					playButton.setIcon(new ImageIcon(pauseIcon));
				}
				updateButtons();
			}
		});
		
		panel.add(nextButton);
		nextButton.setIcon(new ImageIcon(nextIcon));
		nextButton.setEnabled(player.hasNextPhrase());
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.nextPhrase();
				updateButtons();
			}
		});
		
		player.onPhraseEnd.add(() -> {
			playButton.setIcon(new ImageIcon(playIcon));
		});
	}
	
	public void updateButtons() {
		previousButton.setEnabled(player.hasPreviousPhrase());
		nextButton.setEnabled(player.hasNextPhrase());
	}
	
	private static void loadImages() {
		previousIcon = getIcon("previous_icon.png");
		playIcon = getIcon("play_icon.png");
		pauseIcon = getIcon("pause_icon.png");
		nextIcon = getIcon("next_icon.png");
	}
	
	private static Image getIcon(String imageName) {
		try {
			return ImageIO.read(new File("ressources/images/" + imageName)).getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
