package org.lexidia.dialogo.segmentation.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.lexidia.dialogo.segmentation.controller.ControllerText;

public class TestFrame extends JFrame {
	
	private ControllerText controller;
	private JPanel panel;
	private JTextField fontField = new JTextField(10);
	
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
		setContentPane(panel);
		fontField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size = Integer.parseInt(fontField.getText());
				controller.setFont(controller.getFont().deriveFont((float) size));
			}
		});
		panel.add(new JLabel("Police :"));
		panel.add(fontField);
		
		setVisible(true);
	}
	
}