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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.plaf.ComponentUI;
import javax.swing.*;

import org.fife.plaf.OfficeXP.OfficeXPToggleButtonUI;


/**
 * Toggle button UI for the Office 2003 Look and Feel.
 *
 * @author Robert Futrell
 * @version 0.5
 */
public class Office2003ToggleButtonUI extends OfficeXPToggleButtonUI {


    public static ComponentUI createUI(JComponent c){
		return new Office2003ToggleButtonUI();
    }


	protected void paintToolbarToggleButtonBackground(Graphics g,
												JComponent c) {

		AbstractButton button = (AbstractButton)c;
		ButtonModel model = button.getModel();

		int width = button.getWidth() - 1;
		int height = button.getHeight() - 1;

		// This case is when the button isn't even enabled.
		if (!model.isEnabled()) {
			// Do nothing.
		}

		// If the toggle button is depressed...
		else if (model.isSelected()) {

			// And the mouse is over the button OR they've clicked a
			// mouse button and (maybe) since moved the mouse.
			if (isMouseOver() || model.isPressed()) {
				Office2003ButtonUI.paintArmedToolbarButtonBackground(g,
												width, height);
			}

			// And the mouse isn't over the button and they haven't clicked
			// a mouse button.
			else {
				g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
				g.drawRect(0,0, width,height);
				Color beginColor = UIManager.getColor("Office2003LnF.ToolBarButtonSelectedBeginGradientColor");
				Color endColor   = UIManager.getColor("Office2003LnF.ToolBarButtonSelectedEndGradientColor");
				Graphics2D g2d = (Graphics2D) g;
				GradientPaint paint = new GradientPaint(0.0f,height, beginColor,
												0.0f,0.0f, endColor);
				g2d.setPaint(paint);
				g2d.fill(new Rectangle(1,1, width-1,height-1));
			}

		}

		// This case is when the user has down-clicked, and the mouse is
		// still over the button.
		else if (model.isArmed()) {
			Office2003ButtonUI.paintArmedToolbarButtonBackground(g,
												width, height);
		}

		// This case is when the mouse is hovering over the button, or
		// they've left-clicked, but moved the mouse off the button.
		else if (isMouseOver() || model.isPressed()) {
			Office2003ButtonUI.paintMouseOverToolbarButtonBackground(g,
												width, height);
		}

		// This case is when the user hasn't clicked on the button, nor is
		// the mouse over it.
		else {
			// Do nothing.
		}

	}


	/**
	 * Paints the icon for the specified button.
	 *
	 * @param g The graphics context.
	 * @param c The button.
	 * @param iconRect The area in which to paint the icon.
	 */
	protected void paintToolbarToggleButtonIcon(Graphics g, JComponent c,
										Rectangle iconRect) {
		Office2003Utilities.paintButtonIcon(g, c, iconRect);
    }


}