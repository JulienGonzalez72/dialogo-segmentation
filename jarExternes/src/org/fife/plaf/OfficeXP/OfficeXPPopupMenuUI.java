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
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import com.sun.java.swing.plaf.windows.WindowsPopupMenuUI;


/**
 * The UI for a popup menu using the OfficeXP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPPopupMenuUI extends WindowsPopupMenuUI {


	public static ComponentUI createUI(JComponent x) {
		return new OfficeXPPopupMenuUI();
	}


	/**
	 * The border for Office XP menus.  A simple line border, with a
	 * background-colored line at top and bottom.  Also, if this popup menu
	 * attaches to a menu bar, the border "isn't painted" where the popup menu
	 * attaches to the menu bar.<p>
	 *
	 * This border is set as the default for popup menus in the main Look and
	 * Feel class.<p>
	 *
	 * This class implements <code>UIResource</code> so it functions
	 * properly when switching Look and Feels during runtime.
	 */
	public static class XPPopupBorder extends AbstractBorder
										implements UIResource {

		private static final long serialVersionUID = 1L;

		public XPPopupBorder() {
			super();
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(2,1,1,1);
		}

		public void paintBorder(Component c, Graphics g, int x, int y,
							int width, int height) {

			int right = x + width - 1;
			int bottom = y + height - 1;

			// Draw the main line border.
			g.setColor(UIManager.getDefaults().getColor("OfficeLnF.MenuBorderColor"));
			g.drawLine(x,y, right,y);
			g.drawLine(right,y, right,bottom);
			g.drawLine(right,bottom, x,bottom);
			g.drawLine(x,bottom, x,y);

			// Draw the menu-colored line where this popup attaches to the
			// menu (if applicable).
			Component invoker = ((JPopupMenu)c).getInvoker();
			if (invoker!=null && invoker instanceof JMenu) {
				JMenu menu = (JMenu)invoker;
				Component parent = invoker.getParent();
				if (parent instanceof JMenuBar) {
					JMenuBar menuBar = (JMenuBar)parent;
					Point menuLocation = menu.getLocation();
					Point popupMenuLocation = SwingUtilities.convertPoint(
										c, c.getLocation(), menuBar);
					int topLevelMenuWidth = menu.getWidth() - 7; // "-7" for the shadow.
					g.setColor(UIManager.getColor("OfficeXPLnF.ChosenMenuColor"));
					int newX = menuLocation.x - popupMenuLocation.x + 1;
					if (menuLocation.y < popupMenuLocation.y)
						g.drawLine(newX,y, newX+topLevelMenuWidth,y);
					else
						g.drawLine(newX,bottom, newX+topLevelMenuWidth,bottom);
				}

			} // End of if (invoker!=null && invoker instanceof JMenu).

			// Draw the menu-item colored line at the top of this popup menu.
			g.setColor(c.getBackground());
			g.drawLine(1,1, right-1,1);

		}

	}


}