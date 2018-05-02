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
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import com.sun.java.swing.plaf.windows.WindowsMenuItemUI;


/**
 * UI for menu items using the OfficeXP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPMenuItemUI extends WindowsMenuItemUI {

	static final int MENU_ITEM_HEIGHT	= 23;

	static final String MAX_TEXT_WIDTH =  "maxTextWidth";
	static final String MAX_ACC_WIDTH  =  "maxAccWidth";

	// These rects are used for painting and preferredsize calculations.
	// They used to be regenerated constantly.  Now they are reused.
	protected static final Rectangle zeroRect = new Rectangle(0,0,0,0);
	protected static Rectangle iconRect = new Rectangle();
	protected static Rectangle textRect = new Rectangle();
	protected static Rectangle acceleratorRect = new Rectangle();
	protected static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
	static Rectangle r = new Rectangle();


	protected MouseInputListener createMouseInputListener(JComponent c) {
		return new RMouseInputHandler();
	}


    public static ComponentUI createUI(JComponent c) {
        return new OfficeXPMenuItemUI();
    }


    protected Dimension getPreferredMenuItemSize(JComponent c,
                                                     Icon checkIcon,
                                                     Icon arrowIcon,
                                                     int defaultTextIconGap) {
		JMenuItem b = (JMenuItem) c;
		Icon icon = (Icon) b.getIcon();
		String text = b.getText();
		KeyStroke accelerator =  b.getAccelerator();
		String acceleratorText = "";

		if (accelerator != null) {
			int modifiers = accelerator.getModifiers();
			if (modifiers > 0) {
				acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
				acceleratorText += "+";
			}
			int keyCode = accelerator.getKeyCode();
			if (keyCode != 0) {
				acceleratorText += KeyEvent.getKeyText(keyCode);
			}
			else {
				acceleratorText += accelerator.getKeyChar();
			}
		}

		Font font = b.getFont();
		FontMetrics fm = c.getFontMetrics(font);

		resetRects();

		layoutMenuItem(
				fm, text, acceleratorText, icon, checkIcon, arrowIcon,
				viewRect, iconRect, textRect, acceleratorRect,
				text == null ? 0 : defaultTextIconGap,
				defaultTextIconGap
				);

		// Find the union of the icon and text rects.
		r.setBounds(textRect);
		r = SwingUtilities.computeUnion(iconRect.x, iconRect.y,
								iconRect.width, iconRect.height, r);

		// To make the text and accelerators of each menu item in a menu
		// align, we'll check the width of each for this menu item, and if
		// either value is greater than the parent menu's cached current
		// greatest value, it is updated.  Since all menu items do this
		// (even child JMenus, but they do it in OfficeXPMenuUI), the
		// parent menu will have the proper values.

		// We need a JComponent-derived parent in order to store the
		// width properties.
		Container parent = menuItem.getParent();
		if (parent != null && parent instanceof JComponent) {

			JComponent p = (JComponent) parent;

			// Get widest values so far from parent, or null if none yet.
			Integer maxTextWidth = (Integer) p.getClientProperty(MAX_TEXT_WIDTH);
			Integer maxAccWidth = (Integer) p.getClientProperty(MAX_ACC_WIDTH);
			int maxTextValue = maxTextWidth!=null ? maxTextWidth.intValue() : 0;
			int maxAccValue = maxAccWidth!=null ? maxAccWidth.intValue() : 0;

			//Compare the text widths, and adjust the r.width to the widest.
			if (r.width < maxTextValue) {
				r.width = maxTextValue;
			}
			else {
				p.putClientProperty(MAX_TEXT_WIDTH, new Integer(r.width) );
			}

			// Compare the accelarator widths.
			if (acceleratorRect.width > maxAccValue) {
				maxAccValue = acceleratorRect.width;
				p.putClientProperty(MAX_ACC_WIDTH, new Integer(acceleratorRect.width) );
			}

			// Add on the widest accelerator.
			r.width += maxAccValue;
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

		Insets insets = b.getInsets();
		if(insets != null) {
			r.width += insets.left + insets.right;
			r.height += insets.top + insets.bottom;
		}

		// if the width is even, bump it up one. This is critical
		// for the focus dash line to draw properly
		if(r.width%2 == 0) {
			r.width++;
		}

		// if the height is even, bump it up one. This is critical
		// for the text to center properly
		if(r.height%2 == 0) {
			r.height++;
		}

		return new Dimension((int)r.getWidth(), MENU_ITEM_HEIGHT);

	}


   /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     */
    public static String layoutCompoundLabel(JComponent c, FontMetrics fm,
    									String text, Rectangle viewR,
    									Rectangle iconR, Rectangle textR) {

		boolean ltr = true;
		if (c!=null) ltr = c.getComponentOrientation().isLeftToRight();
		//int hAlign = ltr ? SwingConstants.RIGHT : SwingConstants.LEFT;
		int hTextPos = ltr ? SwingConstants.LEFT : SwingConstants.RIGHT;

		// Note that iconR won't matter if an icon doesn't exist, so it
		// doesn't matter that it's in the same (x,y) location as the text.
		iconR.width  = 20;
		iconR.height = 20;
		if (hTextPos==SwingConstants.LEFT) {
			iconR.x = 4;
		}
		else {
			iconR.x = viewR.x+viewR.width-20-1;
		}
		iconR.y = viewR.y + (viewR.height/2) - 7; //(iconR.height/2);

		// Initialize the text bounds rectangle textR.  If a null
		// or and empty String was specified we substitute "" here
		// and use 0,0,0,0 for textR.
		boolean textIsEmpty = (text == null) || text.equals("");
		if (textIsEmpty) {
			textR.width = textR.height = 0;
			text = "";
		}
		else {

			View v = (c != null) ? (View) c.getClientProperty("html") : null;
			if (v != null) {
				textR.width = (int) v.getPreferredSpan(View.X_AXIS);
				textR.height = (int) v.getPreferredSpan(View.Y_AXIS);
			}
			else {
				textR.width = SwingUtilities.computeStringWidth(fm,text);
				textR.height = fm.getHeight();
			}

            /* If the label text string is too wide to fit within the available
             * space, "..." and as many characters as will fit will be
             * displayed instead.
             */
            int availTextWidth = viewR.width - 32;

            if (textR.width > availTextWidth) {
			    String clipString = "...";
			    int totalWidth = SwingUtilities.computeStringWidth(fm,clipString);
			    int nChars;
			    for(nChars = 0; nChars < text.length(); nChars++) {
					totalWidth += fm.charWidth(text.charAt(nChars));
					if (totalWidth > availTextWidth)
					    break;
			    }
			    text = text.substring(0, nChars) + clipString;
			    textR.width = SwingUtilities.computeStringWidth(fm,text);
            }

		}

		// If this is a top-level menu, there will be no icon.
		if ((c instanceof JMenu) && ((JMenu)c).isTopLevelMenu()) {
			int nonShadowWidth = viewR.width - 4;
			textR.x = viewR.x + nonShadowWidth/2 - textR.width/2;	// Text will be centered.
		}
		else
			// Text will be placed after the icon/check/whatever if it's not
			// a top-level menu item.
			if (hTextPos==SwingConstants.LEFT) {
				textR.x = 32;
			}
			else { // SwingConstants.RIGHT
				//Integer i = (Integer)c.getClientProperty(MAX_ACC_WIDTH);
				//int maxAccWidth = i==null ? 0 : i.intValue();
				textR.x = viewR.x+viewR.width -(32 + /*defaultTextIconGap*/5 + textR.width);
			}

		// Must compute text's y-coordinate after textR.height has been computed.
		textR.y = viewR.y + (viewR.height/2) - (textR.height/2);

        return text;

    }


	/**
	 * Compute and return the location of the icons origin, the
	 * location of origin of the text baseline, and a possibly clipped
	 * version of the compound labels string.  Locations are computed
	 * relative to the viewRect rectangle.
	 */
	protected String layoutMenuItem(FontMetrics fm, String text,
				String acceleratorText, Icon icon, Icon checkIcon,
				Icon arrowIcon, Rectangle viewRect, Rectangle iconRect,
				Rectangle textRect, Rectangle acceleratorRect,
				int textIconGap, int menuItemGap) {

		// Lay out the text and icon rectangles.
		layoutCompoundLabel(menuItem, fm, text, viewRect, iconRect, textRect);

		// Give dimensions to the accelerator text rectangle if the text is
		// actually something.
		if (!acceleratorText.equals("")) {
			acceleratorRect.width = SwingUtilities.computeStringWidth( fm, acceleratorText );
			acceleratorRect.height = fm.getHeight();
			if (menuItem.getComponentOrientation().isLeftToRight()) {
				acceleratorRect.x = viewRect.x + viewRect.width - 12/*arrowIconRect.width*/
									- menuItemGap - acceleratorRect.width;
			}
			else {
				acceleratorRect.x = viewRect.x + 20;
			}
			acceleratorRect.y = textRect.y;
		}

		//Rectangle labelRect = iconRect.union(textRect);

        return text;

    }


    public void paint(final Graphics g, final JComponent c) {
        paintMenuItem(g, c, checkIcon, arrowIcon,
                      selectionBackground, selectionForeground,
                      defaultTextIconGap);
    }



    /**
     * Draws the background of the menu item.
     *
     * @param g the paint graphics
     * @param menuItem menu item to be painted
     * @param bgColor selection background color
     */
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {

		ButtonModel model = menuItem.getModel();
		Color oldColor = g.getColor();
		int menuItemWidth = menuItem.getWidth();
		int menuItemHeight = menuItem.getHeight();

		paintUnarmedBackground(g, menuItem);

		// 20061120 fabio: Highlight shouldn't be painted when item is disabled
		if (model.isArmed() && menuItem.isEnabled()) {

			int width = menuItemWidth - 3;
			int height = menuItemHeight - 2;

			g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
			g.drawRect(1,0, width,height);
			g.setColor(UIManager.getColor("OfficeLnF.HighlightColor"));
			g.fillRect(2,1, width-1,height-1);

		} else {
			// Do nothing; the background has already been painted above.
		}

		// Add the white line to the bottom item.  Note that this CANNOT be added as
		// a part of the popup menu's border because of Office XP's menu item design;
		// there's an empty line between each menu item, but the top and bottom empty
		// lines are pure background color (no "khaki" on the left).  If you can think
		// of a simpler way to do it, then by all means, go ahead.
		JPopupMenu popupMenu = (JPopupMenu)menuItem.getParent();
		if (popupMenu.getComponentIndex(menuItem) == popupMenu.getComponentCount()-1) {
			g.setColor(menuItem.getBackground());
			int y = menuItemHeight - 1;
			// Do whole line to cover both LTR and RTL.
			g.drawLine(0,y, menuItemWidth-1,y);
		}

		g.setColor(oldColor);

    }


	protected void paintCheck(Graphics g, JMenuItem menuItem) {
		ButtonModel model = menuItem.getModel();
		if(menuItem.isSelected()) {
			int x = iconRect.x-2;
			if (!menuItem.getComponentOrientation().isLeftToRight()) {
				x++; // ???
			}
			int y = 1;
			int width = 19;//20;//checkIconRect.width;
			int height = 19;//20;//checkIconRect.height;
			Color oldColor = g.getColor();
			g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
			g.drawRect(x,y, width,height);
			if (model.isArmed())
				g.setColor(UIManager.getColor("OfficeXPLnF.PressedHighlightColor"));
			else
				g.setColor(UIManager.getColor("OfficeXPLnF.CheckBoxHighlightColor"));
			g.fillRect(x+1,y+1, width-1,height-1);
			g.setColor(oldColor);
			paintCheckImpl(g, x, y);
		}
	}


	/**
	 * Paints the actual check box.
	 * @param g
	 * @param x
	 * @param y
	 */
	protected void paintCheckImpl(Graphics g, int x, int y) {
		x += 3;
		y += 3;
		g.drawLine(x+9, y+3, x+9, y+3);
		g.drawLine(x+8, y+4, x+9, y+4);
		g.drawLine(x+7, y+5, x+9, y+5);
		g.drawLine(x+6, y+6, x+8, y+6);
		g.drawLine(x+3, y+7, x+7, y+7);
		g.drawLine(x+4, y+8, x+6, y+8);
		g.drawLine(x+5, y+9, x+5, y+9);
		g.drawLine(x+3, y+5, x+3, y+5);
		g.drawLine(x+3, y+6, x+4, y+6);
	}


	protected void paintIcon(Graphics g, JMenuItem menuItem) {
		OfficeXPUtilities.paintMenuItemIcon(g, menuItem, iconRect);
	}


	protected void paintMenuItem(Graphics g, JComponent c,
                                     Icon checkIcon, Icon arrowIcon,
                                     Color background, Color foreground,
                                     int defaultTextIconGap) {

		JMenuItem b = (JMenuItem) c;
		ButtonModel model = b.getModel();

		int menuWidth = b.getWidth();
		int menuHeight = b.getHeight();

		resetRects();

		viewRect.setBounds( 0, 0, menuWidth, menuHeight );

		Font holdf = g.getFont();
		Font f = c.getFont();
		g.setFont(f);
		FontMetrics fm = c.getFontMetrics(f);

		// get Accelerator text
		KeyStroke accelerator =  b.getAccelerator();
		String acceleratorText = "";
		if (accelerator != null) {
			int modifiers = accelerator.getModifiers();
			if (modifiers > 0) {
				acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
				acceleratorText += "+";
			}

			int keyCode = accelerator.getKeyCode();
			if (keyCode != 0)
				acceleratorText += KeyEvent.getKeyText(keyCode);
            	else
				acceleratorText += accelerator.getKeyChar();
		}

		// layout the text and icon
		String text = layoutMenuItem(
			fm, b.getText(), acceleratorText, b.getIcon(),
			checkIcon, arrowIcon,
			viewRect, iconRect, textRect, acceleratorRect,
			b.getText() == null ? 0 : defaultTextIconGap,
			defaultTextIconGap
		);

		// Paint background
		paintBackground(g, b, background);

		Color holdc = g.getColor();

		// Paint the Check
		boolean isCheckOrRadio = (c instanceof JCheckBoxMenuItem) ||
							(c instanceof JRadioButtonMenuItem);
		if (checkIcon != null && isCheckOrRadio)
			paintCheck(g, menuItem);

		// Paint the Icon
		if(b.getIcon()!=null && !isCheckOrRadio)
			paintIcon(g, menuItem);

		// Draw the Text
		if(text != null) {
			View v = (View)c.getClientProperty(BasicHTML.propertyKey);
			if (v != null) {
				v.paint(g, textRect);
			}
			else {
				paintText(g, b, textRect, text);
			}
		}

		// Draw the Accelerator Text
		if(!acceleratorText.equals("")) {

			//Get the maxAccWidth from the parent to calculate the offset.
			int accOffset = 0;
			Container parent = menuItem.getParent();
			if (parent != null && parent instanceof JComponent) {

				JComponent p = (JComponent) parent;
				Integer maxValueInt = (Integer) p.getClientProperty(OfficeXPMenuItemUI.MAX_ACC_WIDTH);
				int maxValue = maxValueInt != null ?
								maxValueInt.intValue() : acceleratorRect.width;

				//Calculate the offset, with which the accelerator texts will be drawn.
				accOffset = maxValue - acceleratorRect.width;

			}

			// Ensure all accelerators are right-aligned for RTL.
			if (!c.getComponentOrientation().isLeftToRight()) {
				Integer maxValueInt = null;
				if (parent!=null && parent instanceof JComponent) {
					maxValueInt = (Integer)((JComponent)b.getParent()).
						getClientProperty(OfficeXPMenuItemUI.MAX_ACC_WIDTH);
				}
				int maxValue = maxValueInt!=null ? maxValueInt.intValue() :
											acceleratorRect.width;
				accOffset = 0;
				acceleratorRect.x = 20 + maxValue - acceleratorRect.width;
			}

			if(!model.isEnabled()) {
				// *** paint the acceleratorText disabled

				if ( disabledForeground != null ) {
					g.setColor( disabledForeground );
					OfficeXPGraphicsUtils.drawString(g,fm,acceleratorText,0,
									acceleratorRect.x - accOffset,
									acceleratorRect.y + fm.getAscent());
				}
				else {
					g.setColor(b.getBackground().brighter());
					OfficeXPGraphicsUtils.drawString(g,fm,acceleratorText,0,
									acceleratorRect.x - accOffset,
									acceleratorRect.y + fm.getAscent());
					g.setColor(b.getBackground().darker());
					OfficeXPGraphicsUtils.drawString(g,fm,acceleratorText,0,
									acceleratorRect.x - accOffset - 1,
									acceleratorRect.y + fm.getAscent() - 1);
				}

			}
			else {
				// *** paint the acceleratorText normally
				g.setColor( acceleratorForeground );
				OfficeXPGraphicsUtils.drawString(g,fm,acceleratorText, 0,
									acceleratorRect.x - accOffset,
									acceleratorRect.y + fm.getAscent());
			}

		}

		g.setColor(holdc);
		g.setFont(holdf);

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
			Color oldColor = g.getColor();
			OfficeXPGraphicsUtils.drawStringUnderlineCharAt(g,fm,text,mnemonicIndex,
							textRect.x, textRect.y + fm.getAscent());
			g.setColor(oldColor);
		}

    }


	protected void paintUnarmedBackground(Graphics g, JMenuItem menuItem) {
		OfficeXPUtilities.paintMenuItemBackground(g, menuItem);
	}


    protected void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        viewRect.setBounds(0,0,Short.MAX_VALUE, Short.MAX_VALUE);
        r.setBounds(zeroRect);
    }


	private class RMouseInputHandler extends MouseInputHandler {

		public void mouseExited(MouseEvent mouseEvent) {
			MenuSelectionManager menuSelectionManager = MenuSelectionManager.defaultManager();
			int mask = InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK |
					InputEvent.BUTTON3_MASK;
			if ((mouseEvent.getModifiers() & mask) != 0) {
				MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
			}
			else {
				MenuElement path[] = menuSelectionManager.getSelectedPath();
				if (path.length > 1 && !(path[path.length-1] instanceof JPopupMenu)) {
					MenuElement newPath[] = new MenuElement[path.length-1];
					for (int i = 0; i < path.length - 1; i++)
						newPath[i] = path[i];
					menuSelectionManager.setSelectedPath(newPath);
				}
			}
		}

	}


}