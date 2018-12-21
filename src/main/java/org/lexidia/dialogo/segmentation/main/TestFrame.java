package org.lexidia.dialogo.segmentation.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.lexidia.dialogo.segmentation.controller.ControllerText;

public class TestFrame extends JFrame {
	
	private ControllerText controller;
	private JPanel panel;
	private JTextField fontSizeField = new JTextField(8);
	private JComboBox<String> fontCombo = new JComboBox<>();
	private JTextField interlineField = new JTextField(8);
	private JCheckBox highlightCheck = new JCheckBox("surlignage vert");
	private JTextField[] marginFields = new JTextField[4];
	private String[] marginLabels = {"Marge du haut", "Marge du bas", "Marge de gauche", "Marge de droite"};
	private JButton startButton = new JButton("D�marrer l'exercice");
	
	private boolean highlightFromStart;
	
	public TestFrame(ControllerText controller) {
		this.controller = controller;
		init();
	}
	
	public void init() {
		setSize(300, 400);
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
				} catch (NumberFormatException ex) {
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
				Font oldFont = controller.getFont();
				controller.setFont(new Font((String) e.getItem(), oldFont.getStyle(), oldFont.getSize()));
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
				} catch (NumberFormatException ex) {
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
				}
				else {
					controller.highlightUntilPhrase(Color.GREEN, controller.getCurrentPhraseIndex() - 1);
				}
				highlightFromStart = !highlightFromStart;
			}
		});
		panel.add(highlightPanel);
		
		JPanel marginPanel = new JPanel();
		for (int i = 0; i < marginFields.length; i++) {
			marginFields[i] = new JTextField(6);
			/// initialise le texte des marges ///
			switch (i) {
				case 0 : marginFields[0].setText(String.valueOf(controller.getTopMargin())); break;
				case 1 : marginFields[1].setText(String.valueOf(controller.getBottomMargin())); break;
				case 2 : marginFields[2].setText(String.valueOf(controller.getLeftMargin())); break;
				case 3 : marginFields[3].setText(String.valueOf(controller.getRightMargin())); break;
			}
			final int index = i;
			/// modifie les marges en fonction du texte rentr� ///
			marginFields[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					float v;
					try {
						v = Float.parseFloat(marginFields[index].getText());
						switch (index) {
							case 0 : controller.setTopMargin(v); break;
							case 1 : controller.setBottomMargin(v); break;
							case 2 : controller.setLeftMargin(v); break;
							case 3 : controller.setRightMargin(v); break;
						}
					} catch (NumberFormatException ex) {
					}
				}
			});
			marginPanel.add(new JLabel(marginLabels[i]));
			marginPanel.add(marginFields[i]);
		}
		panel.add(marginPanel);
		
		/// met � jour le texte des marges � chaque d�placement d'un slider ///
		controller.addCustomSliderListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				float v = (float) evt.getNewValue();
				switch (evt.getPropertyName()) {
					case "topMargin" : marginFields[0].setText(String.valueOf(v)); break;
					case "bottomMargin" : marginFields[1].setText(String.valueOf(v)); break;
					case "leftMargin" : marginFields[2].setText(String.valueOf(v)); break;
					case "rightMargin" : marginFields[3].setText(String.valueOf(v)); break;
				}
			}
		});
		
		JPanel startPanel = new JPanel();
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.goTo(0);
				startButton.setEnabled(false);
			}
		});
		startPanel.add(startButton);
		panel.add(startPanel);
		
		setVisible(true);
	}
	
	public boolean highlightFromStart() {
		return highlightFromStart;
	}
	
}