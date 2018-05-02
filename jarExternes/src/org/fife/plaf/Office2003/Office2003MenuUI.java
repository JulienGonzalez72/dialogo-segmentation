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
import java.awt.Point;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.fife.plaf.OfficeXP.OfficeXPMenuUI;


/**
 * Component UI for a menu using the Office 2003 Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003MenuUI extends OfficeXPMenuUI {

	/**
	 * We save the old opaque value in case the user manually set it.
	 * In Java 1.5 we don't need this; we can just use
	 * <code>LookAndFeel.setProperty()</code>, but since we want to remain
	 * compatible with 1.4 (and most people don't mess with the opaquity of
	 * menus anyway), we do it this way.
	 */
	private boolean oldOpaque;


	public static ComponentUI createUI(JComponent x) {
		return new Office2003MenuUI();
    }


	/**
	 * Overridden to make the menu not opaque, so that the menu bar's gradient
	 * shows through.
	 */
	protected void installDefaults() {
		super.installDefaults();
		oldOpaque = menuItem.isOpaque();
		menuItem.setOpaque(false);
	}


	protected void paintIcon(Graphics g, JMenuItem menuItem) {
		Office2003Utilities.paintButtonIcon(g, menuItem, iconRect);
	}


    protected void paintTopLevelMenuBackground(Graphics g, JMenu menu) {

		ButtonModel model = menuItem.getModel();
		Color oldColor = g.getColor();
		int menuWidth = menu.getWidth();
		int menuHeight = menu.getHeight();

		if (model.isArmed() || model.isSelected()) {

			// Draw the "border."
			JPopupMenu popupMenu = menu.getPopupMenu();
			JMenuBar menuBar = (JMenuBar)menu.getParent();
			Point menuLocation = menu.getLocation();
			Point popupMenuLocation = SwingUtilities.convertPoint(
							popupMenu, popupMenu.getLocation(), menuBar);
			//int topLevelMenuWidth = menu.getWidth();
			//int newX = menuLocation.x - popupMenuLocation.x + 1;
			g.setColor(UIManager.getDefaults().getColor("OfficeLnF.MenuBorderColor"));

			// If the popup menu is below the menu bar or the popup isn't visible
			// (happens if the user presses "Alt" to activate the menus).
			if (menuLocation.y<popupMenuLocation.y || !menu.isPopupMenuVisible()) {

				g.drawLine(0,2, 0,menuHeight);
				g.drawLine(0,2, menuWidth-5,2);
				g.drawLine(menuWidth-5,2, menuWidth-5,menuHeight);

				// Fill in the inside of the menu item.
				Color beginColor = UIManager.getColor("Office2003LnF.MenuBarItemSelectedBeginGradientColor");
				Color endColor   = UIManager.getColor("Office2003LnF.MenuBarItemSelectedEndGradientColor");
				Graphics2D g2d = (Graphics2D) g;
				GradientPaint paint = new GradientPaint(
									0.0f, 0.0f, beginColor,
									0.0f, menu.getHeight(), endColor);
				g2d.setPaint(paint);
				g.fillRect(1,3, menuWidth-6,menuHeight-3);

			}

			// If the popup menu is above the menu bar.
			else {

				g.drawLine(0,0, 0,menuHeight-2);
				g.drawLine(0,menuHeight-2, menuWidth-5,menuHeight-2);
				g.drawLine(menuWidth-5,0, menuWidth-5,menuHeight-2);

				// Fill in the inside of the menu item.
				Color beginColor = UIManager.getColor("Office2003LnF.MenuBarItemSelectedBeginGradientColor");
				Color endColor   = UIManager.getColor("Office2003LnF.MenuBarItemSelectedEndGradientColor");
				Graphics2D g2d = (Graphics2D) g;
				GradientPaint paint = new GradientPaint(
									0.0f, 0.0f, beginColor,
									0.0f, menu.getHeight(), endColor);
				g2d.setPaint(paint);
				g.fillRect(1,0, menuWidth-6,menuHeight-3);

			}

		}

		else {

			if (isMouseOver() && model.isEnabled()) {

				g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
				g.drawRect(0,2, menuWidth-5,menuHeight-3);

				Color beginColor = UIManager.getColor("Office2003LnF.MenuBarItemArmedBeginGradientColor");
				Color endColor   = UIManager.getColor("Office2003LnF.MenuBarItemArmedEndGradientColor");

				Graphics2D g2d = (Graphics2D) g;
				GradientPaint paint = new GradientPaint(
									0.0f, 0.0f, beginColor,
									0.0f, menu.getHeight(), endColor);
				g2d.setPaint(paint);
				g.fillRect(1,3, menuWidth-6,menuHeight-4);

			}

			else {
				// Do nothing; since menu items are not opaque in this
				// look and feel, the component behind it (the menu bar)
				// paints in the background pieces we don't.  This is what
				// we want, as this allows the menu bar's gradient to show
				// through.
			}

		}

		g.setColor(oldColor);

    }


	/**
	 * Overridden to restore the old opaquity value.
	 */
	protected void uninstallDefaults() {
		menuItem.setOpaque(oldOpaque);
		super.uninstallDefaults();
	}


}