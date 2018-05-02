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
package org.fife.plaf.Office2003;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import org.fife.plaf.OfficeXP.OfficeXPToolBarBorder;


/**
 * Border used by toolbars in Office 2003 Look and Feel.  Note that this border
 * is what draws the "grip" at the left.  Note that this class implements
 * <code>UIResource</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003ToolBarBorder extends OfficeXPToolBarBorder
									implements UIResource {

	private static final int GRIP_WIDTH		= 9;

	private static final long serialVersionUID = 1L;


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
	 * Paints the toolbar border, which is a column of dots.
	 *
	 * @param c The toolbar for which to paint the border.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param width The width of the toolbar.
	 * @param height The height of the toolbar.
	 */
	public void paintBorder(Component c, Graphics g, int x, int y,
							int width, int height) {

		if ( ((JToolBar)c).isFloatable() ) {
			int orientation = ((JToolBar)c).getOrientation();
			g.setColor(UIManager.getColor(
								"Office2003LnF.ToolBarGripLightColor"));
			paintGripDots(c, g, x+1,y+1, width,height, orientation);
			g.setColor(UIManager.getColor(
								"Office2003LnF.ToolBarGripDarkColor"));
			paintGripDots(c, g, x,y, width,height, orientation);
		} // End of if ( ((JToolBar)c).isFloatable() ).

	}


	/**
	 * Paints a series of dots.
	 *
	 * @param c The toolbar for which to paint the border.
	 * @param The graphics context in which to paint.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 * @param width The width of the toolbar.
	 * @param height The height of the toolbar.
	 * @param toolBarOrientation The orientation of the toolbar.
	 */
	private static final void paintGripDots(Component c, Graphics g,
							int x, int y, int width, int height,
							int toolBarOrientation) {

		if (toolBarOrientation==JToolBar.HORIZONTAL) {
			int start  = c.getComponentOrientation().isLeftToRight() ?
						(x+5) : (x+width-6);
			int end    = start + 2;
			int bottom = y + height - 7;
			y += 4;
			while (y < bottom) {
				g.fillRect(start,y, end-start,2);
				y += 4;
			}
		}

		else { // JToolBar.VERTICAL
			int start = y + 6;
			int end   = start + 2;
			int right = x + width - 5;
			x += 4;
			while (x < right) {
				g.fillRect(x,start, 2,end-start);
				x += 4;
			}
		}

	}


}