package fr.lexidia.dialogo.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.lexidia.dialogo.controller.ControllerText;
import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.model.PropertyChangeListenerImpl;

public class TestFrame extends JFrame {

	private ControllerText controller;
	private JPanel panel;
	private JTextField fontSizeField = new JTextField(8);
	private JComboBox<String> fontCombo = new JComboBox<>();
	private JTextField interlineField = new JTextField(8);
	private JCheckBox highlightCheck = new JCheckBox("surlignage vert");
	private JTextField[] marginFields = new JTextField[4];
	private String[] marginLabels = { "Marge du haut", "Marge du bas", "Marge de gauche", "Marge de droite" };
	private JButton startButton = new JButton("Démarrer l'exercice");

	private boolean highlightFromStart;

	private EventDispatcher ed;

	public TestFrame(ControllerText controller, EventDispatcher ed) {
		this.controller = controller;
		this.ed = ed;
		init();
	}

	public void init() {
		setSize(400, 550);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		setContentPane(panel);

		JPanel fontSizePanel = new JPanel();
		fontSizeField.setText(String.valueOf(controller.getFont().getSize()));
		fontSizeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.setFontSize(Integer.parseInt(fontSizeField.getText()));
					ed.dispatch("param_sizePolice", fontSizeField.getText());
				} catch (IllegalArgumentException | IllegalStateException ex) {
					error(ex);
				}
			}
		});
		fontSizePanel.add(new JLabel("Taille de police :"));
		fontSizePanel.add(fontSizeField);
		panel.add(fontSizePanel);

		JPanel fontPanel = new JPanel();
		fontCombo.setModel(new DefaultComboBoxModel<>(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		fontCombo.setSelectedItem(controller.getFont().getFamily());
		fontCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Font oldFont = controller.getFont();
					try {
						controller.setFont(new Font((String) e.getItem(), oldFont.getStyle(), oldFont.getSize()));
						ed.dispatch("param_familyPolice", (String) e.getItem());
					} catch (IllegalArgumentException | IllegalStateException ex) {
						error(ex);
					}
				}
			}
		});
		fontPanel.add(new JLabel("Police :"));
		fontPanel.add(fontCombo);
		panel.add(fontPanel);

		JPanel interlinePanel = new JPanel();
		interlineField.setText(String.valueOf(controller.getLineSpacing()));
		interlineField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.setLineSpacing(Float.parseFloat(interlineField.getText()));
					ed.dispatch("param_interline", interlineField.getText());
				} catch (IllegalArgumentException | IllegalStateException ex) {
					error(ex);
				}
			}
		});
		interlinePanel.add(new JLabel("Interligne :"));
		interlinePanel.add(interlineField);
		panel.add(interlinePanel);

		JPanel highlightPanel = new JPanel();
		highlightCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (highlightFromStart) {
					controller.removeAllHighlights();
				} else {
					controller.highlightUntilPhrase(Color.GREEN, controller.getCurrentPhraseIndex() - 1);
				}
				highlightFromStart = !highlightFromStart;
				ed.dispatch("param_highlightFromStart", highlightFromStart);
			}
		});
		highlightPanel.add(highlightCheck);
		panel.add(highlightPanel);

		JPanel marginPanel = new JPanel();
		for (int i = 0; i < marginFields.length; i++) {
			marginFields[i] = new JTextField(6);
			/// initialise le texte des marges ///
			switch (i) {
			case 0:
				marginFields[0].setText(String.valueOf(truncate(controller.getTopMargin())));
				break;
			case 1:
				marginFields[1].setText(String.valueOf(truncate(controller.getBottomMargin())));
				break;
			case 2:
				marginFields[2].setText(String.valueOf(truncate(controller.getLeftMargin())));
				break;
			case 3:
				marginFields[3].setText(String.valueOf(truncate(controller.getRightMargin())));
				break;
			}
			final int index = i;
			/// modifie les marges en fonction du texte rentré ///
			marginFields[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					float v;
					try {
						v = Float.parseFloat(marginFields[index].getText());
						switch (index) {
						case 0:
							controller.setTopMargin(v);
							ed.dispatch("param_marginT", v);
							break;
						case 1:
							controller.setBottomMargin(v);
							ed.dispatch("param_marginB", v);
							break;
						case 2:
							controller.setLeftMargin(v);
							ed.dispatch("param_marginL", v);
							break;
						case 3:
							controller.setRightMargin(v);
							ed.dispatch("param_marginR", v);
							break;
						}
					} catch (IllegalArgumentException ex) {
						error(ex);
					}
				}
			});
			marginPanel.add(new JLabel(marginLabels[i]));
			marginPanel.add(marginFields[i]);
		}
		panel.add(marginPanel);

		/// met à jour le texte des marges à chaque déplacement d'un slider ///
		controller.addCustomSliderListener(new PropertyChangeListenerImpl(marginFields));

		JPanel startPanel = new JPanel();
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.goTo(0);
				ed.dispatch("startExercice", highlightFromStart);
				startButton.setEnabled(false);
			}
		});
		startPanel.add(startButton);
		panel.add(startPanel);

		setVisible(true);
	}

	private static void error(Exception ex) {
		JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur !", JOptionPane.ERROR_MESSAGE);
	}

	private static float truncate(float value) {
		return (int) (value * 100) / 100f;
	}

	public boolean highlightFromStart() {
		return highlightFromStart;
	}

}