package fr.lexidia.dialogo.view;

/*import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;*/
import javax.swing.JPanel;
/*import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.lexidia.dialogo.segmentation.main.Constants;*/

public class ControlPanel extends JPanel {

	/*private static int imageSize = Constants.CONTROL_IMAGE_SIZE;
	private static Image previousIcon, playIcon, pauseIcon, nextIcon, repeatIcon;

	private JButton previousButton = new JButton();
	private JButton playButton = new JButton();
	private JButton nextButton = new JButton();
	private JButton repeatButton = new JButton();
	public JTextField goToField = new JTextField();
	private TextPanel pan;

	private boolean usable = true;

	static {
		loadImages();
	}

	public ControlPanel(TextPanel p, final FenetreParametre fen) {
		this.pan = p;

		add(previousButton);
		previousButton.setIcon(new ImageIcon(previousIcon));
		previousButton.setEnabled(false);
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.controlerGlobal.doPrevious();
				updateButtons();
			}
		});

		add(playButton);
		playButton.setIcon(new ImageIcon(playIcon));
		playButton.setEnabled(false);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pan.player.isPlaying()) {
					pan.controlerGlobal.doStop();
				} else {
					pan.controlerGlobal.doPlay();
					fen.setStartParametersEnabled(false);
				}
				updateButtons();
			}
		});

		add(nextButton);
		nextButton.setIcon(new ImageIcon(nextIcon));
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.controlerGlobal.doNext();
				fen.setStartParametersEnabled(false);
				updateButtons();
			}
		});

		add(repeatButton);
		repeatButton.setIcon(new ImageIcon(repeatIcon));
		repeatButton.setEnabled(false);
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.controlerGlobal.doPlay();
				updateButtons();
			}
		});

		JLabel goToLabel = new JLabel("Passer au segment :");
		goToLabel.setFont(goToLabel.getFont().deriveFont(Font.ITALIC));
		add(goToLabel);
		add(goToField);
		goToField.setPreferredSize(new Dimension(40, 20));
		goToField.setEnabled(false);
		goToField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n;
				try {
					n = Integer.parseInt(goToField.getText()) - 1;
					pan.controlerGlobal.goTo(n);
					fen.setStartParametersEnabled(false);
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, "Num�ro de segment incorrect : " + goToField.getText());
				}
				updateButtons();
			}
		});
	}

	*//**
	 * M�thode qui s'ex�cute lorsque les contr�les sont pr�ts � �tre effectifs.
	 *//*
	public void init() {
		Runnable update = new Runnable() {
			public void run() {
				updateButtons();
			}
		};
		pan.player.onPhraseEnd.add(update);
		pan.player.onBlockEnd.add(update);
		pan.player.onPlay.add(update);
		enableAll();
	}

	*//**
	 * Actualise l'�tat de tous les composants de la fen�tre de contr�le.
	 *//*
	public void updateButtons() {
		if (usable) {
			previousButton.setEnabled(pan.player.hasPreviousPhrase());
			playButton.setEnabled(true);
			playButton.setIcon(new ImageIcon(pan.player.isPlaying() ? pauseIcon : playIcon));
			nextButton.setEnabled(pan.player.hasNextPhrase());
			repeatButton.setEnabled(pan.player.isPlaying());
			goToField.setEnabled(true);
		} else {
			previousButton.setEnabled(false);
			playButton.setEnabled(false);
			playButton.setIcon(new ImageIcon(playIcon));
			nextButton.setEnabled(false);
			repeatButton.setEnabled(false);
			goToField.setEnabled(false);
		}
	}

	public void disableAll() {
		usable = false;
		updateButtons();
	}

	*//**
	 * D�sactive tous les boutons de la fen�tre de contr�le puis les r�-active apr�s
	 * le temps duration.
	 *//*
	public void disableAll(final long duration) {
		disableAll();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				enableAll();
			}
		});
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
			return ImageIO.read(new File("ressources/images/" + imageName)).getScaledInstance(imageSize, imageSize,
					Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}*/

}
