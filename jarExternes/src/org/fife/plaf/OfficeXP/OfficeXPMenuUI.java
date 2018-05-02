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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;

import com.sun.java.swing.plaf.windows.WindowsMenuUI;


/**
 * Component UI for a menu using the Office XP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPMenuUI extends WindowsMenuUI {

	private boolean isMouseOver = false;

 	// These rects are used for painting and preferred size calculations.
	// They are reused to keep from having to reallocate them.
	protected static final Rectangle zeroRect = new Rectangle(0,0,0,0);
	protected static Rectangle iconRect = new Rectangle();
	protected static Rectangle textRect = new Rectangle();
	protected static Rectangle arrowIconRect = new Rectangle();
	protected static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
	static Rectangle r = new Rectangle();


    protected MouseInputListener createMouseInputListener(JComponent c) {
		return new MouseInputHandler();
    }


	public static ComponentUI createUI(JComponent x) {
		return new OfficeXPMenuUI();
    }


	/**
	 * Horrible, horrible hack to get non-top level menus' heights to be the
	 * same as that of regular menu items.
	 */
	protected Dimension getPreferredMenuItemSize(JComponent c,
									Icon checkIcon,
									Icon arrowIcon,
									int defaultTextIconGap) {

		JMenu menu = (JMenu)c;
		Dimension d = null;

		if (menu.isTopLevelMenu()) {
			d = super.getPreferredMenuItemSize(menu, checkIcon,
							arrowIcon, defaultTextIconGap);
			d.width += 4;	// Allows for the "shadow" to be drawn.
			d.height = 22;
		}
		else { // Menu that's a menu item inside another menu.
			d = getPreferredNonTopLevelMenuSize(menu, checkIcon, arrowIcon,
										defaultTextIconGap);
		}

		return d;

	}




	/**
	 * Computes the preferred dimensions of a menu that is NOT a top-level
	 * menu.  This method is called from <code>getPreferredMenuItemSize</code>
	 * because its super implementation won't properly calculate the
	 * preferred width of a non-top level menu in this LnF (because we always
	 * have an icon's width of space on the left, etc.).
	 */
	protected Dimension getPreferredNonTopLevelMenuSize(JMenu menu,
										Icon checkIcon,
										Icon arrowIcon,
										int defaultTextIconGap) {

		Icon icon = (Icon)menu.getIcon();
		String text = menu.getText();
		Font font = menu.getFont();
		FontMetrics fm = menu.getFontMetrics(font);
		resetRects();

		layoutMenuItem(fm, text, icon, arrowIcon,
					viewRect, iconRect, textRect, arrowIconRect,
					text == null ? 0 : defaultTextIconGap,
					defaultTextIconGap
					);

		// Find the union of the icon and text rects.
		r.setBounds(textRect);
		r = SwingUtilities.computeUnion(iconRect.x, iconRect.y,
                                        iconRect.width, iconRect.height, r);


		// If the width of this menu's text is longer than the parent menu's
		// current longest text, update it.  This is so that other menu
		// items in the parent menu can have their accelerators align.

		// Get the parent, which stores the information.
		Container parent = menu.getParent();

		if (parent!=null && (parent instanceof JComponent)) {

			//Get widest text so far from parent, if no one exists null
			// is returned.
			JComponent p = (JComponent) parent;
			Integer maxTextWidth = (Integer) p.getClientProperty(
								OfficeXPMenuItemUI.MAX_TEXT_WIDTH);
			int maxTextValue = maxTextWidth!=null ?
									maxTextWidth.intValue() : 0;

			//Compare the text widths, and adjust the r.width to the widest.
			if (r.width < maxTextValue)
				r.width = maxTextValue;
			else {
				p.putClientProperty(OfficeXPMenuItemUI.MAX_TEXT_WIDTH,
								new Integer(r.width));
			}

			r.width += defaultTextIconGap;

		}

		// Add in the checkIcon
		r.width += 20;//checkIconRect.width;
		r.width += defaultTextIconGap;

		// Add in the arrowIcon
		r.width += defaultTextIconGap;
		r.width += 12;//arrowIconRect.width;

		// Add in the "padding" on either side of the menu item.
		r.width += 2*defaultTextIconGap;

		Insets insets = menu.getInsets();
		if(insets != null) {
			r.width += insets.left + insets.right;
			r.height += insets.top + insets.bottom;
		}

		// if the width is even, bump it up one. This is critical
		// for the focus dash line to draw properly
		if(r.width%2 == 0)
			r.width++;

		// if the height is even, bump it up one. This is critical
		// for the text to center properly
		if(r.height%2 == 0)
			r.height++;

		return new Dimension((int)r.getWidth(),
							OfficeXPMenuItemUI.MENU_ITEM_HEIGHT);

	}


    /**
     * Get the temporary flag to indicate if the mouse has entered the menu.
     */
    protected boolean isMouseOver() {
		return isMouseOver;
    }


	/**
	 * Compute and return the location of the icons origin, the
	 * location of origin of the text baseline, and a possibly clipped
	 * version of the compound labels string.  Locations are computed
	 * relative to the viewRect rectangle.
	 */
    protected String layoutMenuItem(FontMetrics fm, String text, Icon icon,
					Icon arrowIcon, Rectangle viewRect, Rectangle iconRect,
					Rectangle textRect, Rectangle arrowIconRect,
					int textIconGap, int menuItemGap) {

		OfficeXPMenuItemUI.layoutCompoundLabel(menuItem, fm, text, viewRect,
										iconRect, textRect);

		// Initialize the arrowIcon bounds rectangle width & height.
		if (arrowIcon != null) {
			arrowIconRect.width = arrowIcon.getIconWidth();
			arrowIconRect.height = arrowIcon.getIconHeight();
		}
		else {
			arrowIconRect.width = arrowIconRect.height = 0;
		}

		//Rectangle labelRect = iconRect.union(textRect);

		// Position the Arrow Icon.
		int temp = viewRect.x;// + 6;
		textRect.x += temp;
		iconRect.x += temp;
		if (menuItem.getComponentOrientation().isLeftToRight()) {
			arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap -
							arrowIconRect.width;
		}
		else {
			arrowIconRect.x = viewRect.x + menuItemGap;
		}

		arrowIconRect.y = 8;//labelRect.y + (labelRect.height/2) - arrowIconRect.height/2;

		return text;

	}


    protected void paintBackground(Graphics g, JMenuItem menuItem,
    									Color bgColor) {
		// If the user is running pre-Windows XP, don't do all this jazz.
		if (OfficeXPLookAndFeel.isClassicWindows()) {
			super.paintBackground(g, menuItem, bgColor);
		}
		JMenu menu = (JMenu)menuItem;
		// If this is a submenu, it should be painted just like menu items.
		if (!menu.isTopLevelMenu()) {
			paintSubmenuBackground(g, menu);
		}
		// Otherwise, this is a top-level menu.
		else {
			paintTopLevelMenuBackground(g, menu);
		}
    }


	protected void paintIcon(Graphics g, JMenuItem menuItem) {
		OfficeXPUtilities.paintMenuItemIcon(g, menuItem, iconRect);
	}


	protected void paintMenuItem(Graphics g, JComponent c,
                                     Icon checkIcon, Icon arrowIcon,
                                     Color background, Color foreground,
                                     int defaultTextIconGap) {

		JMenu menu = (JMenu)c;

		// For a top-level menu item, paint regularly (could be optimized...).
		if (menu.isTopLevelMenu())
			super.paintMenuItem(g, c, checkIcon, arrowIcon, background,
							foreground, defaultTextIconGap);

		// Otherwise, it must be painted like an OfficeXPMenuItem
		// (but optimized a tad).
		else {

			JMenuItem b = (JMenuItem) c;
			//ButtonModel model = b.getModel();

			//   Dimension size = b.getSize();
			int menuWidth = b.getWidth();
			int menuHeight = b.getHeight();

			resetRects();

			viewRect.setBounds( 0, 0, menuWidth, menuHeight );

			Font holdf = g.getFont();
			Font f = c.getFont();
			g.setFont( f );
			FontMetrics fm = c.getFontMetrics( f );

			// layout the text and icon
			String text = layoutMenuItem(
				fm, b.getText(), b.getIcon(),
				arrowIcon, viewRect, iconRect, textRect, arrowIconRect,
				b.getText() == null ? 0 : defaultTextIconGap,
				defaultTextIconGap
			);

			// Paint background
			paintBackground(g, b, background);

			Color holdc = g.getColor();

			// Paint the Icon
			paintIcon(g, menuItem);

			// Draw the Text
	 		if(text != null) {
	 			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
	 			if (v != null)
	 				v.paint(g, textRect);
				else
					paintText(g, b, textRect, text);

			}

			// Paint the Arrow
			if (arrowIcon != null) {
				arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
			}

			g.setColor(holdc);
			g.setFont(holdf);

		}

	}


	/*
	 * (non-Javadoc) NOTE:  This is NOT the same as
	 * OfficeXPMenuItemUI.paintBackground() - see "if (model.isSelected())"
	 * line...
	 */
	protected void paintSubmenuBackground(Graphics g, JMenu menu) {

		ButtonModel model = menu.getModel();
		Color oldColor = g.getColor();
		int menuWidth = menu.getWidth();
		int menuHeight = menu.getHeight();

		paintUnarmedBackground(g, menu);

		//if (model.isArmed() || model.isSelected()) {
		if (model.isSelected()) {

			int width = menuWidth - 3;
			int height = menuHeight - 2;

			g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
			g.drawRect(1,0, width,height);
			g.setColor(UIManager.getColor("OfficeLnF.HighlightColor"));
			g.fillRect(2,1, width-1,height-1);
			g.setColor(oldColor);

		}
		else {
			// Do nothing; the background has already been painted above.
		}

		// Add the white line to the bottom item.  Note that this CANNOT be added as
		// a part of the popup menu's border because of Office XP's menu item design;
		// there's an empty line between each menu item, but the top and bottom empty
		// lines are pure background color (no "khaki" on the left).  If you can think
		// of a simpler way to do it, then by all means, go ahead.
		Component parent = menu.getParent();
		if (parent instanceof JPopupMenu) {
			JPopupMenu popupMenu = (JPopupMenu)parent;
			if (popupMenu.getComponentIndex(menu) ==
					popupMenu.getComponentCount()-1) {
				g.setColor(menu.getBackground());
				int y = menuHeight - 1;
				// Do whole line to cover both LTR and RTL.
				g.drawLine(0,y, menuWidth-1,y);
			}
		}

		g.setColor(oldColor);

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

		ButtonModel model = menuItem.getModel();

		if(!model.isEnabled()) {
		    OfficeXPGraphicsUtils.paintText(g, menuItem, textRect, text, 0);
		}
		else {
		    FontMetrics fm = menuItem.getFontMetrics(g.getFont());
		    int mnemonicIndex = menuItem.getDisplayedMnemonicIndex();
	 	    OfficeXPGraphicsUtils.drawStringUnderlineCharAt(g, fm, text,
					mnemonicIndex, textRect.x, textRect.y+fm.getAscent());
		}

    }


	protected void paintTopLevelMenuBackground(Graphics g, JMenu menu) {

		ButtonModel model = menu.getModel();
		Color oldColor = g.getColor();
		int menuWidth = menu.getWidth();
		int menuHeight = menu.getHeight();

		if (model.isArmed() || model.isSelected()) {

			// Fill-in with color.
			g.setColor(UIManager.getColor("OfficeXPLnF.ChosenMenuColor"));
			g.fillRect(0,0, menuWidth,menuHeight);

			// Draw the "border."
			JPopupMenu popupMenu = menu.getPopupMenu();
			JMenuBar menuBar = (JMenuBar)menu.getParent();
			Point menuLocation = menu.getLocation();
			Point popupMenuLocation = SwingUtilities.convertPoint(popupMenu,
									popupMenu.getLocation(), menuBar);
			//int newX = menuLocation.x - popupMenuLocation.x + 1;
			g.setColor(UIManager.getDefaults().getColor("OfficeLnF.MenuBorderColor"));
			// If the popup menu is below the menu bar or the popup isn't visible
			// (happens if the user presses "Alt" to activate the menus).
			if (menuLocation.y<popupMenuLocation.y || !menu.isPopupMenuVisible()) {

				g.drawLine(0,2, 0,menuHeight-1);
				g.drawLine(0,2, menuWidth-5,2);
				g.drawLine(menuWidth-5,2, menuWidth-5,menuHeight-1);
/*
				// Shadow - NOT ACCURATE!!
				g.setColor(new Color(225,225,225));
				g.fillRect(menuWidth-4,6,  4,menuHeight);
				g.setColor(new Color(210,210,210));
				g.fillRect(menuWidth-4,8,  3,menuHeight);
				g.setColor(new Color(180,180,180));
				g.drawLine(menuWidth-4,10,  menuWidth-4,menuHeight);
				g.setColor(new Color(195,195,195));
				g.drawLine(menuWidth-3,10, menuWidth-3,menuHeight);
*/
			}
			// If the popup menu is above the menu bar.
			else {

				g.drawLine(0,0, 0,menuHeight-2);
				g.drawLine(0,menuHeight-2, menuWidth-5,menuHeight-2);
				g.drawLine(menuWidth-5,0, menuWidth-5,menuHeight-2);

				// Shadow ... ???

			}

		}

		else {

			// Needed by both items below.
			g.setColor(menu.getBackground());
			g.fillRect(0,0, menuWidth, menuHeight);

			if (isMouseOver() && model.isEnabled()) {
				g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
				g.drawRect(0,2, menuWidth-5,menuHeight-3);
				g.setColor(UIManager.getColor("OfficeLnF.HighlightColor"));
				g.fillRect(1,3, menuWidth-6,menuHeight-4);
			}

			else {
				// Background filled in above.
			}

		}

		g.setColor(oldColor);

	}


	protected void paintUnarmedBackground(Graphics g, JMenuItem menuItem) {
		OfficeXPUtilities.paintMenuItemBackground(g, menuItem);
	}


    protected void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        viewRect.setBounds(0,0,Short.MAX_VALUE, Short.MAX_VALUE);
    }


    /**
     * Set the temporary flag to indicate if the mouse has entered the menu.
     */
    private void setMouseOver(boolean over) {
		isMouseOver = over;
    }


    /**
     * Overrides the handler in WindowsMenuUI for some GUI changes.
     */
    protected class MouseInputHandler extends WindowsMenuUI.MouseInputHandler {

		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			if (!OfficeXPLookAndFeel.isClassicWindows()) {
				setMouseOver(true);
				menuItem.repaint();
			}
		}

		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			if (!OfficeXPLookAndFeel.isClassicWindows()) {
				setMouseOver(false);
				menuItem.repaint();
			}
		}

    }


}