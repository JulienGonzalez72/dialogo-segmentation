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
package org.fife.plaf.OfficeXP;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.UIManager;


/**
 * The button used by <code>OfficeXPComboBoxUI</code> as the button for
 * the combo box.
 *
 * @author Robert Futrell
 * @version 0.5
 */
public class OfficeXPArrowButton extends JButton {

	public static final int NORMAL		= 1;
	public static final int ARMED			= 2;
	public static final int PRESSED		= 3;

	protected OfficeXPComboBoxUI comboBoxUI;

	private int paintState;				// One of the 3 choices above.

	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new <code>OfficeXPArrowButton</code>.
	 *
	 * @param comboBoxUI The UI of the parent combo box.
	 */
	public OfficeXPArrowButton(OfficeXPComboBoxUI comboBoxUI) {
		super();
		this.comboBoxUI = comboBoxUI;
		setRequestFocusEnabled(false);
	}


	public Dimension getMaximumSize() {
		return getPreferredSize();
	}


	public Dimension getMinimumSize() {
		return getPreferredSize();
	}


	public Dimension getPreferredSize() {
		return new Dimension(16, 16);
	}


	public boolean isFocusable() {
		return false;
	}


	public void paint(Graphics g) {

		int w = getSize().width;
		int h = getSize().height;
		Color origColor = g.getColor();
		//boolean isPressed = getModel().isPressed();
		boolean isEnabled = isEnabled();

		// Paint the background.
		paintBackground(g, w,h);

		// Draw line on left-hand side (part of the mouse-over highlight).
		g.setColor(comboBoxUI.getBorderColor());
		g.drawLine(0,0, 0,h-1);

		// If there's no room to draw arrow, bail
		if(h < 5 || w < 5) {
			g.setColor(origColor);
			return;
		}

		// Draw the arrow
		int size = Math.min((h - 4) / 3, (w - 4) / 3);
		size = Math.max(size, 2);
		paintTriangle(g, (w - size) / 2, (h - size) / 2,
					size, isEnabled);

		g.setColor(origColor);

	}


	/**
	 * Paints the background of this arrow button.
	 *
	 * @param g The graphics context.
	 * @param width The width of the button.
	 * @param height The height of the button.
	 */
	protected void paintBackground(Graphics g, int width, int height) {

		Color c1, c2;

		switch (paintState) {

			case ARMED:
				c1 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Armed.Gradient1");
				c2 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Armed.Gradient2");
				break;

			case PRESSED:
				c1 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Selected.Gradient1");
				c2 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Selected.Gradient2");
				break;

			default://case NORMAL:
				c1 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Normal.Gradient1");
				c2 = UIManager.getColor("OfficeLnF.ComboBox.Arrow.Normal.Gradient2");
				break;

		}

		paintBackgroundImpl(g, c1, c2, width, height);

	}


	protected void paintBackgroundImpl(Graphics g, Color c1, Color c2,
								int width, int height) {
		if (c2==null || c1==c2) {
			g.setColor(c1);
			g.fillRect(1,0, width-1, height);
		}
		else {
			Graphics2D g2d = (Graphics2D) g;
			GradientPaint paint = new GradientPaint(0.0f, 0.0f,     c1,
											0.0f, height-1, c2);
			g2d.setPaint(paint);
			g2d.fill(new Rectangle(1,0, width-1,height));
		}
	}


	protected void paintTriangle(Graphics g, int x, int y, int size, 
							boolean isEnabled) {

		Color oldColor = g.getColor();
		int mid, i, j;

		j = 0;
		size = Math.max(size, 2);
		mid = (size / 2) - 1;
	
		g.translate(x, y);
		g.setColor(this.getForeground());

		j = 0;
		for(i = size-1; i >= 0; i--)   {
			g.drawLine(mid-i, j, mid+i, j);
			j++;
		}

		g.translate(-x, -y);	
		g.setColor(oldColor);

	}


	public void updatePaintState(int state) {
		paintState = state;
	}


}