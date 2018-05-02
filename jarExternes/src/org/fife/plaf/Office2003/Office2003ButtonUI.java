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
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.fife.plaf.OfficeXP.OfficeXPButtonUI;


/**
 * Extension of <code>WindowsButtonUI</code> that paints toolbar buttons like
 * they are painted in Microsoft Office 2003 applications.
 *
 * @author Robert Futrell
 * @version 1.1
 */
public class Office2003ButtonUI extends OfficeXPButtonUI {


	/**
	 * Constructor.
	 */
	public Office2003ButtonUI() {
		super();
	}


	/**
	 * Creates a UI for a button.
	 *
	 * @param c A button that needs a UI.
	 * @return The UI for the button.
	 */
	public static ComponentUI createUI(JComponent c){
		return new Office2003ButtonUI();
	}


	/**
	 * Paints the background for an armed toolbar button.
	 *
	 * @param g The graphics context.
	 * @param width The width of the button.
	 * @param height The height of the button.
	 */
	static void paintArmedToolbarButtonBackground(Graphics g,
										int width, int height) {
		g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
		g.drawRect(0,0, width,height);
		Color beginColor = UIManager.getColor("Office2003LnF.ToolBarButtonSelectedBeginGradientColor");
		Color endColor   = UIManager.getColor("Office2003LnF.ToolBarButtonSelectedEndGradientColor");
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint paint = new GradientPaint(0.0f,0.0f, beginColor,
										0.0f,height, endColor);
		g2d.setPaint(paint);
		g2d.fill(new Rectangle(1,1, width-1,height-1));
	}


	/**
	 * Paints the background for a toolbar button with the mouse hovering
	 * over it.
	 *
	 * @param g The graphics context.
	 * @param width The width of the button.
	 * @param height The height of the button.
	 */
	static void paintMouseOverToolbarButtonBackground(Graphics g,
											int width, int height) {
		g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
		g.drawRect(0,0, width,height);
		Color beginColor = UIManager.getColor("Office2003LnF.ToolBarButtonArmedBeginGradientColor");
		Color endColor   = UIManager.getColor("Office2003LnF.ToolBarButtonArmedEndGradientColor");
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint paint = new GradientPaint(0.0f, 0.0f, beginColor,
										0.0f, height, endColor);
		g2d.setPaint(paint);
		g2d.fill(new Rectangle(1,1, width-1,height-1));
	}


	/**
	 * Paints the background of the given (toolbar) button.
	 *
	 * @param g The graphics context.
	 * @param c The button, whose parent is a <code>JToolBar</code>.
	 */
	protected void paintToolbarButtonBackground(Graphics g, JComponent c) {

		AbstractButton button = (AbstractButton)c;
		ButtonModel model = button.getModel();

		int width = button.getWidth() - 1;
		int height = button.getHeight() - 1;

		// This case is when the button isn't even enabled.
		if (!model.isEnabled()) {
			/* Do nothing. */
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
			/* Do nothing. */
		}

	}


	/**
	 * Paints the icon for the specified button.
	 *
	 * @param g The graphics context.
	 * @param c The button.
	 * @param iconRect The area in which to paint the icon.
	 */
	protected void paintToolbarButtonIcon(Graphics g, JComponent c,
									Rectangle iconRect) {
		Office2003Utilities.paintButtonIcon(g, c, iconRect);
    }


}