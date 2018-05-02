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

import java.awt.*;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


/**
 * Utility routines for the OfficeXP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPUtilities {

	/**
	 * Size of the specially-colored area on the left-hand side of menu items.
	 */
	public static final int LEFT_EDGE_WIDTH			= 24;

	/**
	 * Composite used to create disabled icons that show their background's
	 * color through.
	 */
	public static final AlphaComposite ICON_COMPOSITE =
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f);

	private static final boolean IS_XP = System.getProperty("os.name").
			toLowerCase().indexOf("windows xp")>-1;


	/**
	 * Creates a dark gray, rounded-edge border with a blue title.
	 *
	 * @param title The title for the border.
	 * @return The border.
	 */
	public static Border createWindowsXPBorder(String title) {
		return createWindowsXPBorder(title, new Color(140,140,140));
	}


	/**
	 * Creates a rounded-edge border with a blue title.
	 *
	 * @param title The title for the border.
	 * @param borderColor The color to use for the border.
	 * @return The border.
	 */
	public static Border createWindowsXPBorder(String title,
										Color borderColor) {
		if (title.charAt(title.length()-1) != ':')
			title += ":";
		return BorderFactory.createTitledBorder(
								//new LineBorder(borderColor, 1, true),
								new RoundedLineBorder(borderColor, 1),
								title,
								TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION,
								null,
								Color.BLUE.brighter()
							);
	}


	/**
	 * Returns whether the OS is Windows XP.
	 *
	 * @return Whether the OS is Windows XP.
	 */
	public static boolean isXP() {
		return IS_XP;
	}


	/**
	 * Paints the "disabled" icon for a given button (toolbar button, menu
	 * item, etc.) with an alpha composite so the background shows through a
	 * little bit.
	 *
	 * @param g The graphics context with which to paint.
	 * @param b The button whose disabled button we should paint.  If it has
	 *        no disabled icon, nothing is painted.
	 * @param iconRect The bounds in which to paint the icon.
	 */
	public static void paintDisabledButtonIcon(Graphics g, AbstractButton b,
									Rectangle iconRect) {
		Icon icon = (Icon)b.getDisabledIcon();
		// Necessary as some icons (like JInternalFrame's) don't have
		// disabled icons.
		if (icon!=null) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setComposite(OfficeXPUtilities.ICON_COMPOSITE);
			icon.paintIcon(b, g2d, iconRect.x, iconRect.y);
			g2d = null;
		}
	}


	/**
	 * Paints the background for a menu item (or a menu or separator in a
	 * menu).
	 *
	 * @param g The graphics context.
	 * @param c The menu item.
	 */
	public static void paintMenuItemBackground(Graphics g, Component c) {
		Graphics2D g2d = (Graphics2D) g;
		Paint menuItemBGPaint = (Paint)UIManager.get(
								"OfficeLnF.MenuItemIconAreaPaint");
		g2d.setPaint(menuItemBGPaint);
		int width = c.getWidth();
		int mainWidth = width - LEFT_EDGE_WIDTH;
		int height = c.getHeight();
		if (c.getComponentOrientation().isLeftToRight()) {
			g.fillRect(0,0,  LEFT_EDGE_WIDTH,height);
			g.setColor(c.getBackground());
			g.fillRect(LEFT_EDGE_WIDTH,0, mainWidth,height);
		}
		else {
			// Hack - translate so cached GradientPaints paint correctly.
			g2d.translate(width, height);
			g2d.rotate(Math.PI);
			g2d.fillRect(0,0,  LEFT_EDGE_WIDTH,height);
			g2d.rotate(Math.PI);//-Math.PI);
			g2d.translate(-width, -height);
			g2d.setColor(c.getBackground());
			g2d.fillRect(0,0, mainWidth,height);
		}
	}


	/**
	 * Paints the icon for a menu item (or a submenu in a menu).
	 *
	 * @param g The graphics context.
	 * @param menuItem The menu item.
	 * @param iconRect The rectangle in which to paint the icon.
	 */
	public static void paintMenuItemIcon(Graphics g, JMenuItem menuItem,
									Rectangle iconRect) {

		Icon icon;
		ButtonModel model = menuItem.getModel();

		if(!model.isEnabled()) {
			OfficeXPUtilities.paintDisabledButtonIcon(g, menuItem, iconRect);
		}

		else if(model.isPressed() && model.isArmed()) {
			icon = (Icon) menuItem.getPressedIcon();
			if(icon == null) {
				// Use default icon
				icon = (Icon) menuItem.getIcon();
			}
			if (icon!=null)
				icon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
		}

		else {
			icon = (Icon) menuItem.getIcon();
			if (icon!=null) {
				if (model.isArmed()|| (menuItem instanceof JMenu && model.isSelected())) {
					Icon disabledIcon = (Icon)menuItem.getDisabledIcon();
					// Sometimes this is null.  This will result in no
					// shadow under the "raised up" icon, but oh well...
					if (disabledIcon!=null)
						disabledIcon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
					icon.paintIcon(menuItem,g, iconRect.x-2,iconRect.y-2);
				}
				else
					icon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
			}
		}

	}


	/**
	 * A line border with rounded edges.
	 */
	static class RoundedLineBorder implements Border {

		private Color borderColor;
		private int thickness;


		/**
		 * Creates a new <code>RoundedLineBorder</code> with default color
		 * border.
		 *
		 * @param thickness The thickness of the border.
		 */
		public RoundedLineBorder(int thickness) {
			this(new Color(140,140,140), thickness);
		}

		/**
		 * Creates a new <code>RoundedLineBorder</code>.
		 *
		 * @param borderColor The color you'd like to use for the border.
		 * @param thickness The thickness of the border.
		 */
		public RoundedLineBorder(Color borderColor, int thickness) {
			this.borderColor = borderColor;
			this.thickness = thickness;
		}

		/**
		 * Returns the insets of the border.
		 *
		 * @param c The component for which this border insets value applies.
		 * @return The insets of the border (i.e., the "size" of the border).
		 */
		public Insets getBorderInsets (Component c) {
			return new Insets(thickness, thickness, thickness, thickness);
		}

	    /**
     	* Returns whether or not the border is opaque.
	     */
	    public boolean isBorderOpaque() {
     	   return false; 	// Muse "see through" the rounded edges.
	    }

		/**
		 * Paints the border to <code>g</code>.
		 */
		public void paintBorder(Component c, Graphics g, int x, int y,
							int width, int height) {
			Color oldColor = g.getColor();
			g.setColor(borderColor);
			for(int i = 0; i < thickness; i++)
				g.drawRoundRect(x+i,y+i, width-i-i-1,height-i-i-1,
								thickness+7, thickness+7);
			g.setColor(oldColor);
		}

	}


}