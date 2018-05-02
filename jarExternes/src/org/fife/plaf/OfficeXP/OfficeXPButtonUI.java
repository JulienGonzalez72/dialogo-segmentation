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
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;


/**
 * Extension of <code>WindowsButtonUI</code> that paints toolbar buttons like
 * they are painted in Microsoft Office XP applications.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPButtonUI extends WindowsButtonUI {

	private static MouseInputHandler mouseInputHandler;

	private boolean isMouseOver = false;

	private Rectangle viewRect = new Rectangle();
	private Rectangle iconRect = new Rectangle();
	private Rectangle textRect = new Rectangle();

	/**
	 * This is the width and height of toolbar buttons in Office XP
	 * applications that are only an icon, no text (which is almost all of
	 * them).
	 */
	private static final int PREFERRED_TOOLBAR_BUTTON_SIZE	= 23;


    public static ComponentUI createUI(JComponent c){
		return new OfficeXPButtonUI();
    }


	protected synchronized MouseInputHandler getMouseInputHandler() {
		if (mouseInputHandler==null)
			mouseInputHandler = new MouseInputHandler();
		return mouseInputHandler;
	}


	public Dimension getPreferredSize(JComponent c) {
		Component parent = c.getParent();
		if (parent!=null && (parent instanceof JToolBar)) {
			Dimension preferredSize = super.getPreferredSize(c);
			if (preferredSize.width<PREFERRED_TOOLBAR_BUTTON_SIZE)
				preferredSize.width = PREFERRED_TOOLBAR_BUTTON_SIZE;
			if (preferredSize.height<PREFERRED_TOOLBAR_BUTTON_SIZE)
				preferredSize.height = PREFERRED_TOOLBAR_BUTTON_SIZE;
			return preferredSize;
		}
		return super.getPreferredSize(c);
	}

		
	public void installListeners(AbstractButton b) {
		super.installListeners(b);
		mouseInputHandler = getMouseInputHandler();
		b.addMouseListener(mouseInputHandler);
	}


	protected boolean isMouseOver() {
		return isMouseOver;
	}


	public void paint(Graphics g, JComponent c) {
		AbstractButton button = (AbstractButton)c;
		if (button.getParent() instanceof JToolBar) {
			paintToolbarButton(g, button);
		}
		else {
			super.paint(g, c);
		}
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
		g.setColor(UIManager.getColor("OfficeXPLnF.PressedHighlightColor"));
		g.fillRect(1,1, width-1,height-1);
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
		g.setColor(UIManager.getColor("OfficeLnF.HighlightColor"));
		g.fillRect(1,1, width-1,height-1);
	}


	public void paintToolbarButton(Graphics g, JComponent c) {

		// Paint the button's background.
		paintToolbarButtonBackground(g,c);

		// Get the bounds for the text and icon.
		AbstractButton b = (AbstractButton) c;
		Icon icon = b.getIcon();
		String text = b.getText();
		FontMetrics fm = c.getFontMetrics(g.getFont());
		Insets i = c.getInsets();
		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = b.getWidth() - (i.right + viewRect.x);
		viewRect.height = b.getHeight() - (i.bottom + viewRect.y);
		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
		text = SwingUtilities.layoutCompoundLabel(
			c, fm, text, icon,
			b.getVerticalAlignment(), b.getHorizontalAlignment(),
			b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
			viewRect, iconRect, textRect, 
			text==null ? 0 : b.getIconTextGap());

		// Actually paint the text and icon.
		if (text!=null && !text.equals("")) {
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
			if (v != null)
				v.paint(g, textRect);
			else
				paintText(g, c, textRect, text);
		}
		paintToolbarButtonIcon(g,c, iconRect);

	}


	protected void paintToolbarButtonBackground(Graphics g, JComponent c) {

		AbstractButton button = (AbstractButton)c;
		ButtonModel model = button.getModel();

		int width = button.getWidth() - 1;
		int height = button.getHeight() - 1;

		// Before you try to "optimize" the below, remember this:
		// model.isEnabled() returns whether the button is enabled.
		// model.isArmed() returns true ONLY IF both the button is depressed
		//                 (model.isPressed()) and the mouse is over the
		//                 button.
		// isMouseOver() returns true ONLY IF the mouse is over the button AND
		//               this button is the one over which the mouse was
		//               originally depressed.  (This is equivalent to
		//               model.isRollover() in Sun's JRE 1.5, but not 1.4).
		// model.isPressed() returns true if the user has down-clicked on the
		//               button and not yet released the mouse button.
		// model.isSelected() returns true if the button is "pressed."  I
		//               THINK that for regular JButtons, this is equivalent
		//               to model.isPressed(), but for JToggleButtons, this
		//               is whether or not the button is currently in its
		//               down position (i.e., the user may have clicked and
		//               released the mouse button already).
		// model.isRollover() returns true if the mouse is over the button.
		//               Note that the behavior for this changes between Sun's
		//               1.4 and 1.5 Windows JREs...

		// This case is when the button isn't even enabled.
		if (!model.isEnabled()) {
			// Do nothing.
		}

		// This case is when the user has down-clicked, and the mouse is
		// still over the button.
		else if (model.isArmed()) {
			OfficeXPButtonUI.paintArmedToolbarButtonBackground(g,
												width, height);
		}

		// This case is when the mouse is hovering over the button, or
		// they've left-clicked, but moved the mouse off the button.
		else if (isMouseOver() || model.isPressed()) {
			OfficeXPButtonUI.paintMouseOverToolbarButtonBackground(g,
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
	protected void paintToolbarButtonIcon(Graphics g, JComponent c,
									Rectangle iconRect) {

		// NOTE:  This method is NOT the same as OfficeXPToggleButtonUI's
		// paintToolbarToggleButtonIcon() so we cannot factor it out!

		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		Icon icon;

		if (!model.isEnabled()) {
			OfficeXPUtilities.paintDisabledButtonIcon(g, b, iconRect);
		}
		else if (model.isArmed()) {
			icon = (Icon)b.getIcon();
			if (icon!=null)
				icon.paintIcon(c,g, iconRect.x, iconRect.y);
		}
		else if (isMouseOver() || model.isPressed()) {
			icon = (Icon)b.getDisabledIcon();
			// Necessary as some icons (like JInternalFrame's) don't have
			// disabled icons.
			if (icon!=null) {
				icon.paintIcon(c,g, iconRect.x+1, iconRect.y+1);
				icon = (Icon)b.getIcon();
				// Not sure why we'd have a disabled icon but not a regular
				// one, but better safe than sorry...
				if (icon!=null)
					icon.paintIcon(c,g, iconRect.x-1, iconRect.y-1);
			}
			else {
				icon = b.getIcon();
				if (icon!=null)
					icon.paintIcon(c,g, iconRect.x, iconRect.y);
			}
		}
		else {
			icon = (Icon)b.getIcon();
			if (icon!=null)
				icon.paintIcon(c,g, iconRect.x, iconRect.y);
		}

    }


    protected void setMouseOver(boolean over) {
		isMouseOver = over;
	}


	public void uninstallListeners(AbstractButton b) {
		b.removeMouseListener(mouseInputHandler);
		super.installListeners(b);
	}


	/**
	 * Listens for mouse events in all Office XP buttons, although it's only
	 * really interested in toolbar buttons (FIXME:  It'd be nice to be able
	 * to just check the buttons' parents to see whether they're instances
	 * of <code>JToolBar</code> to add this listener, but then we'd
	 * need to add hierarchy listeners or something similar to each button
	 * in case they add/remove it during runtime, which would be even
	 * more overhead...).  It's here so that we can have a toolbar button
	 * display the "rollover" effect only if no other toolbar button is
	 * depressed.  For example, if you mouse click on one button, but before
	 * you release it, you move the mouse over other buttons.  The other
	 * buttons will have their <code>model.isRollover()</code> method return
	 * <code>true</code>, which will result in us having two buttons
	 * highlighted at the same time, which isn't what we want.<p>
	 *
	 * Note that in Sun's JRE 1.5, it appears that the above scenario is
	 * "fixed;" that is, we don't need this mouse input handler.  In 1.5,
	 * clicking on a button and moving the mouse over other buttons before
	 * releasing the mouse button does NOT make the "other buttons" have their
	 * <code>model.isRollover()</code> method return <code>true</code>.  But,
	 * since we're remaining backwards-compatible with 1.4, we're keeping this
	 * here.
	 */
	protected static class MouseInputHandler extends MouseInputAdapter {

		/**
		 * Flag for whether some toolbar button is depressed.  This is to
		 * prevent the "rollover" property from making two toolbar buttons
		 * appear highlighted at the same time.
		 */
		private boolean someButtonDepressed;

		public MouseInputHandler() {
		}

		public void mousePressed(MouseEvent e) {
			someButtonDepressed = true;
		}

		public void mouseReleased(MouseEvent e) {
			someButtonDepressed = false;
		}

		public void mouseEntered(MouseEvent e) {
			if (!someButtonDepressed) {
				AbstractButton b = (AbstractButton)e.getSource();
				OfficeXPButtonUI ui = (OfficeXPButtonUI)b.getUI();
				ui.setMouseOver(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			AbstractButton b = (AbstractButton)e.getSource();
			OfficeXPButtonUI ui = (OfficeXPButtonUI)b.getUI();
			ui.setMouseOver(false);
		}

	}


}