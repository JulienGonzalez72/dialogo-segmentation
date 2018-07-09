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

	private Object lock = new Object();
	private int start;
	private int end;
	private JTextField jtf;

	/**
	 * numéro du trou
	 */
	private int n;
	private int phrase;
	private String hiddenWord;

	public Mask() {
	}

	public Mask(int start, int end, JTextField jtf) {
		this.setStart(start);
		this.setEnd(end);
	}

	public void initField(Font font, ActionListener actionListener) {
		this.setJtf(new Field());
		getJtf().addActionListener(actionListener);
		getJtf().setEnabled(false);
		getJtf().setFont(font);
		getJtf().setHorizontalAlignment(SwingConstants.CENTER);
		/// dï¿½sactive le hint ï¿½ partir de la premiï¿½re frappe ///
		getJtf().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (getJtf() instanceof Field) {
					((Field) getJtf()).hint(false);
				}
			}
		});
		add(getJtf());
	}

	public void activate() {
		getJtf().setEnabled(true);
		getJtf().grabFocus();
	}

	public void desactivate() {
		getJtf().setEnabled(false);
	}

	/**
	 * Retourne true si le mot rentrï¿½ par l'utilisateur correspond bien au mot
	 * cachï¿½.
	 */
	public boolean correctWord() {
		return getJtf().getText().equals(getHiddenWord());
	}

	public String toString() {
		return getHiddenWord() + " ( " + getStart() + "/" + getEnd() + " )";
	}

	public void setHint(final long duration) {
		final Field field = (Field) getJtf();
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
		((Field) getJtf()).hint(true);
	}

	private class Field extends JTextField {
		private boolean hint;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			/// filtre anticrï¿½nelage ///
			if (g instanceof Graphics2D) {
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}

			if (hint && getHiddenWord() != null && getText().isEmpty()) {
				FontMetrics fm = g.getFontMetrics();
				g.setColor(Constants.HINT_COLOR);
				g.drawString(getHiddenWord(), getWidth() / 2 - fm.stringWidth(getHiddenWord()) / 2, fm.getHeight());
			}
		}

		public void hint(boolean active) {
			hint = active;
			repaint();
		}
	}

	public int getNbCarac() {
		return getHiddenWord().length();
	}

	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public JTextField getJtf() {
		return jtf;
	}

	public void setJtf(JTextField jtf) {
		this.jtf = jtf;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getPhrase() {
		return phrase;
	}

	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}

	public String getHiddenWord() {
		return hiddenWord;
	}

	public void setHiddenWord(String hiddenWord) {
		this.hiddenWord = hiddenWord;
	}

}