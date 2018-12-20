package org.lexidia.dialogo.segmentation.main;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.event.ListDataListener;

import org.lexidia.dialogo.segmentation.controller.ControllerText;

public class TestFrame extends JFrame {
	
	private ControllerText controller;
	private JPanel panel;
	private JTextField fontSizeField = new JTextField(8);
	private JComboBox<String> fontCombo = new JComboBox<>();
	private JTextField interlineField = new JTextField(8);
	private JButton startButton = new JButton("Démarrer l'exercice");
	
	public TestFrame(ControllerText controller) {
		this.controller = controller;
		init();
	}
	
	public void init() {
		setSize(300, 300);
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
		
		JPanel startPanel = new JPanel();
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.goTo(0);
			}
		});
		startPanel.add(startButton);
		panel.add(startPanel);
		
		setVisible(true);
	}
	
}