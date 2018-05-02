/* ====================================================================
 * 
 * Office Look and Feels License
 * http://sourceforge.net/projects/officelnfs
 *
 * Copyright (c) 2003-2005 Robert Futrell.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The names "Office Look And Feels" and "OfficeLnFs" must not
 *    be used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact robert_futrell@users.sourceforge.net.
 *
 * 4. Products derived from this software may not be called "OfficeLnFs"
 *    nor may "OfficeLnFs" appear in their names without prior written
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */ 
package org.fife.plaf.test;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;


/**
 * A simple test application for the Office Look and Feels.  This is not
 * distributed in the jar, to keep things just a tad smaller.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TestApp extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String LAF_OFFICE_XP = "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel";
	private static final String LAF_OFFICE_2003 = "org.fife.plaf.Office2003.Office2003LookAndFeel";
	private static final String LAF_OFFICE_VS2005 = "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";

	private static final String BASE = "org/fife/plaf/Office2003/";


	public TestApp() {

		JPanel cp = new JPanel(new BorderLayout());

		setJMenuBar(createMenuBar());
		cp.add(createToolBar(), BorderLayout.NORTH);

		setContentPane(cp);
		setTitle("Office LookAndFeels Test Application");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

	}


	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		mb.add(fileMenu);
		
		JMenuItem item = new JMenuItem("<html>This <em>is</em> <span style=\"color:#ff0000\">very</span> <b>HTML</b>!");
		fileMenu.add(item);
		fileMenu.addSeparator();

		JMenuItem[] lafItems = new JMenuItem[4];
		lafItems[0] = new JRadioButtonMenuItem(new LookAndFeelAction("Office XP", LAF_OFFICE_XP));
		lafItems[1] = new JRadioButtonMenuItem(new LookAndFeelAction("Office 2003", LAF_OFFICE_2003));
		lafItems[1].setSelected(true);
		lafItems[2] = new JRadioButtonMenuItem(new LookAndFeelAction("Visual Studio 2005", LAF_OFFICE_VS2005));
		lafItems[3] = new JRadioButtonMenuItem(new LookAndFeelAction("Windows Standard", WindowsLookAndFeel.class.getName()));
		ButtonGroup bg = new ButtonGroup();
		for (int i=0; i<lafItems.length; i++) {
			bg.add(lafItems[i]);
			fileMenu.add(lafItems[i]);
		}
		fileMenu.addSeparator();

		item = new JCheckBoxMenuItem("Right-to-Left Orientation (Click to toggle)");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComponentOrientation o = getComponentOrientation();
				o = o.isLeftToRight() ? ComponentOrientation.RIGHT_TO_LEFT :
					ComponentOrientation.LEFT_TO_RIGHT;
				applyComponentOrientation(o);
				pack();
				repaint();
			}
		});
		fileMenu.add(item);
		fileMenu.addSeparator();

		ClassLoader cl = getClass().getClassLoader();
		int ctrl = getToolkit().getMenuShortcutKeyMask();
		item = new JMenuItem("New File", new ImageIcon(cl.getResource(BASE + "new.gif")));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ctrl));
		fileMenu.add(item);
		item = new JMenuItem("Open File...", new ImageIcon(cl.getResource(BASE + "open.gif")));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctrl));
		fileMenu.add(item);
		item = new JMenuItem("Save File", new ImageIcon(cl.getResource(BASE + "save.gif")));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl));
		fileMenu.add(item);
		fileMenu.addSeparator();

		AbstractAction a = new AbstractAction("Exit") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		fileMenu.add(new JMenuItem(a));

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		mb.add(helpMenu);
		item = new JMenuItem("Help Topics...");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(item);
		helpMenu.addSeparator();
		item = new JMenuItem("About TestApp...");
		helpMenu.add(item);

		return mb;

	}


	private JToolBar createToolBar() {

		ClassLoader cl = getClass().getClassLoader();
		JToolBar tb = new JToolBar();

		tb.add(new JButton(new ImageIcon(cl.getResource(BASE + "new.gif"))));
		tb.add(new JButton(new ImageIcon(cl.getResource(BASE + "open.gif"))));
		tb.add(new JButton(new ImageIcon(cl.getResource(BASE + "save.gif"))));
		tb.addSeparator();
		tb.add(new JButton(new ImageIcon(cl.getResource(BASE + "about.gif"))));
		tb.add(new JButton(new ImageIcon(cl.getResource(BASE + "help.gif"))));

		return tb;

	}


	public void setLookAndFeel(String laf) {
		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) { // Never happens
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TestApp testApp = new TestApp();
				testApp.setLookAndFeel(LAF_OFFICE_2003);
				testApp.setVisible(true);
			}
		});
	}


	private class LookAndFeelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private String laf;

		public LookAndFeelAction(String name, String laf) {
			super(name);
			this.laf = laf;
		}

		public void actionPerformed(ActionEvent e) {
			setLookAndFeel(laf);
		}

	}


}