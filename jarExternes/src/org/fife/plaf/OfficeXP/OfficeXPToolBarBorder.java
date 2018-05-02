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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;


/**
 * Border used by toolbars in Office XP Look and Feel.  Note that this border
 * is what draws the "grip" at the left.  Note that this class implements
 * <code>UIResource</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPToolBarBorder extends AbstractBorder
							implements UIResource {

	private static final int GRIP_WIDTH		= 6;

	private static final long serialVersionUID = 1L;


	/**
	 * Creates the border.
	 */
	public OfficeXPToolBarBorder() {
		super();
	}


	/**
	 * Returns the size of the border on each of the four sides of a toolbar.
	 *
	 * @param c The toolbar for which to get the border size.
	 * @return The size of the border.
	 */
	public Insets getBorderInsets(Component c) {
		return getBorderInsets(c, new Insets(0,0,0,0));
	}


	/**
	 * Returns the size of the border on each of the four sides of a toolbar.
	 *
	 * @param c The toolbar for which to get the border size.
	 * @param newInsets An insets instance which is modified and returned.
	 * @return The size of the border.
	 */
	public Insets getBorderInsets(Component c, Insets newInsets) {

		JToolBar toolBar = (JToolBar)c;

		// If the toolbar isn't floatable, don't add space for the grip lines.
		if ( toolBar.isFloatable() && toolBar.isBorderPainted()) {
			if ( toolBar.getOrientation()==JToolBar.HORIZONTAL ) {
				if (toolBar.getComponentOrientation().isLeftToRight()) {
					newInsets.left = getGripWidth();
				}
				else {
					newInsets.right = getGripWidth();
				}
			}
			else { // Vertical
				newInsets.top = getGripWidth();
			}
		}

		// Add 2 pixels to the top of a toolbar.  This space is colored
		// regular window color, and serves as a very small divider between
		// multiple toolbars.
		newInsets.top += 2;

		// Rollover/selection blue boxes don't touch the very bottom of the
		// toolbar, so we need a slight margin here.
		newInsets.bottom += 2;

		Insets margin = toolBar.getMargin();
		if (margin!=null) {
			newInsets.left		+= margin.left;
			newInsets.top		+= margin.top;
			newInsets.right	+= margin.right;
			newInsets.bottom	+= margin.bottom;
		}

		return newInsets;

	}


	/**
	 * Returns the width (or height, if this toolbar is vertical) of the
	 * "grip" portion of this toolbar.  This assumes that the grip is visible
	 * (i.e., the toolbar is floatable).
	 *
	 * @return The grip width.
	 */
	protected int getGripWidth() {
		return GRIP_WIDTH;
	}


	/**
	 * Paints the toolbar border, which is a column of short horizontal lines.
	 *
	 * @param c The toolbar for which to paint the border.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param width The width of the toolbar.
	 * @param height The height of the toolbar.
	 */
	public void paintBorder(Component c, Graphics g, int x, int y,
								int width, int height) {

		JToolBar toolBar = (JToolBar)c;

		if (toolBar.isFloatable()) {

			g.setColor(UIManager.getColor("ToolBar.shadow"));

			if (toolBar.getOrientation()==JToolBar.HORIZONTAL) {
				int start  = c.getComponentOrientation().isLeftToRight() ?
							(x+3) : (x+width-4);
				int end    = start + 2;
				int bottom = y + height - 4;
				y += 4;
				while (y < bottom) {
					g.drawLine(start,y, end,y);
					y += 2;
				}
			}

			else { // Vertical
				int start = y + 3;
				int end   = start + 3;
				int right = x + width - 4;
				x += 4;
				while (x < right) {
					g.drawLine(x,start, x,end);
					x += 2;
				}
			}

		} // End of if (toolBar.isFloatable()).

	}


}