package main;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class ControlFrame extends JFrame {
	
	private static int imageSize = 40;
	private static Image previousIcon, playIcon, pauseIcon, nextIcon, repeatIcon;
	
	private JPanel panel = new JPanel();
	private JButton previousButton = new JButton();
	private JButton playButton = new JButton();
	private JButton nextButton = new JButton();
	private JButton repeatButton = new JButton();
	private Player player;
	
	private boolean usable = true;
	
	static {
		loadImages();
	}
	
	public ControlFrame(Player p, ControlerGlobal controler) {
		player = p;
		setTitle("Contrôle");
		setContentPane(panel);
		setSize(325, 90);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setFocusableWindowState(false);
		setVisible(true);
		
		panel.add(previousButton);
		previousButton.setIcon(new ImageIcon(previousIcon));
		previousButton.setEnabled(player.hasPreviousPhrase());
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controler.doPrevious();
				updateButtons();
			}
		});
		
		panel.add(playButton);
		playButton.setIcon(new ImageIcon(playIcon));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (player.isPlaying()) {
					player.pause();
				}
				else if (player.isPhraseFinished()) {
					player.repeat();
				}
				else {
					player.play();
				}
				updateButtons();
			}
		});
		
		panel.add(nextButton);
		nextButton.setIcon(new ImageIcon(nextIcon));
		nextButton.setEnabled(player.hasNextPhrase());
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controler.doNext();
				updateButtons();
			}
		});
		
		panel.add(repeatButton);
		repeatButton.setIcon(new ImageIcon(repeatIcon));
		repeatButton.setEnabled(false);
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.repeat();
				updateButtons();
			}
		});
		
		Runnable update = () -> {
			updateButtons();
		};
		player.onPhraseEnd.add(update);
		player.onBlockEnd.add(update);
		player.onPlay.add(update);
	}
	
	public void updateButtons() {
		if (usable) {
			previousButton.setEnabled(player.hasPreviousPhrase());
			playButton.setEnabled(true);
			playButton.setIcon(new ImageIcon(player.isPlaying() ? pauseIcon : playIcon));
			nextButton.setEnabled(player.hasNextPhrase());
			repeatButton.setEnabled(player.isPlaying());
		}
		else {
			previousButton.setEnabled(false);
			playButton.setEnabled(false);
			nextButton.setEnabled(false);
			repeatButton.setEnabled(false);
		}
	}
	
	public void disableAll() {
		usable = false;
		updateButtons();
	}
	
	public void enableAll() {
		usable = true;
		updateButtons();
	}
	
	private static void loadImages() {
		previousIcon = getIcon("previous_icon.png");
		playIcon = getIcon("play_icon.png");
		pauseIcon = getIcon("pause_icon.png");
		nextIcon = getIcon("next_icon.png");
		repeatIcon = getIcon("repeat_icon.png");
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
