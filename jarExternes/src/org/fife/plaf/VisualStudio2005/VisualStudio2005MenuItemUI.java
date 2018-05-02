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
package org.fife.plaf.VisualStudio2005;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.fife.plaf.Office2003.Office2003MenuItemUI;


/**
 * UI for menu items using the Visual Studio 2005 Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class VisualStudio2005MenuItemUI extends Office2003MenuItemUI {


    public static ComponentUI createUI(JComponent c) {
        return new VisualStudio2005MenuItemUI();
    }


	/**
	 * Overridden because this LnF has an "extra" border around the check
	 * when the menu item is armed.
	 *
	 * @param g The graphics context.
	 * @param menuItem The menu item.
	 */
	protected void paintCheck(Graphics g, JMenuItem menuItem) {
		super.paintCheck(g, menuItem);
		if(menuItem.isSelected() && menuItem.getModel().isArmed()) {
			int x = iconRect.x-2;
			if (!menuItem.getComponentOrientation().isLeftToRight()) {
				x++; // ???
			}
			int y = 1;
			int width = 19;
			int height = 19;
			if (menuItem.getComponentOrientation().isLeftToRight()) {
				
			}
			Color oldColor = g.getColor();
			g.setColor(UIManager.getColor(
					"VisualStudio2005.SelectedCBMenuItemBorderColor"));
			g.drawRect(x,y, width,height);
			g.setColor(oldColor);
		}
	}


    /**
     * Method which renders the text of the current menu item.
     *
     * @param g Graphics context
     * @param menuItem Current menu item to render
     * @param textRect Bounding rectangle to render the text.
     * @param text String to render
     */
    protected void paintText(Graphics g, JMenuItem menuItem,
    							Rectangle textRect, String text) {
		//ButtonModel model = menuItem.getModel();
		VisualStudio2005GraphicsUtils.paintText(g, menuItem, textRect,
										text, 0);
    }


}