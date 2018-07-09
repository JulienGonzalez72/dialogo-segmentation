package org.lexidia.dialogo.segmentation.view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.lexidia.dialogo.segmentation.main.Constants;

public class Mask extends JInternalFrame {

	public Object lock = new Object();
	public int start;
	public int end;
	public JTextField jtf;

	/**
	 * num�ro du trou
	 */
	public int n;
	public int phrase;
	public String hiddenWord;

	public Mask() {
	}

	public Mask(int start, int end, JTextField jtf) {
		this.start = start;
		this.end = end;
	}

	public void initField(Font font, ActionListener actionListener) {
		this.jtf = new Field();
		jtf.addActionListener(actionListener);
		jtf.setEnabled(false);
		jtf.setFont(font);
		jtf.setHorizontalAlignment(SwingConstants.CENTER);
		/// d�sactive le hint � partir de la premi�re frappe ///
		jtf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (jtf instanceof Field) {
					((Field) jtf).hint(false);
				}
			}
		});
		add(jtf);
	}

	public void activate() {
		jtf.setEnabled(true);
		jtf.grabFocus();
	}

	public void desactivate() {
		jtf.setEnabled(false);
	}

	/**
	 * Retourne true si le mot rentr� par l'utilisateur correspond bien au mot
	 * cach�.
	 */
	public boolean correctWord() {
		return jtf.getText().equals(hiddenWord);
	}

	public String toString() {
		return hiddenWord + " ( " + start + "/" + end + " )";
	}

	public void setHint(final long duration) {
		final Field field = (Field) jtf;
		field.hint(true);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			private long time;

			public void run() {
				time += 20;
				if (time >= duration) {
					field.hint(false);
					cancel();
				}
			}
		}, 0, 20);
	}

	public void setHint() {
		((Field) jtf).hint(true);
	}

	private class Field extends JTextField {
		private boolean hint;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			/// filtre anticr�nelage ///
			if (g instanceof Graphics2D) {
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}

			if (hint && hiddenWord != null && getText().isEmpty()) {
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Constants.HINT_COLOR);
				g.drawString(hiddenWord, getWidth() / 2 - fm.stringWidth(hiddenWord) / 2, fm.getHeight());
			}
		}

		public void hint(boolean active) {
			hint = active;
			repaint();
		}
	}

	public int getNbCarac() {
		return hiddenWord.length();
	}

}