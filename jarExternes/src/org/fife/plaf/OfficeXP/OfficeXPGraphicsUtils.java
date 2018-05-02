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
import java.util.Map;
import javax.swing.*;


/**
 * Routines used by the OfficeXP Look and Feel components.
 */
public class OfficeXPGraphicsUtils {


    /**
     * Draw a string with the graphics <code>g</code> at location (x,y)
     * just like <code>g.drawString</code> would.
     * The first occurrence of <code>underlineChar</code>
     * in text will be underlined. The matching algorithm is
     * not case sensitive.
     */
	public static void drawString(Graphics g, FontMetrics fm,
							String text, int underlinedChar,
							int x,int y) {

		char lc,uc;
		int index=-1,lci,uci;

		if(underlinedChar != '\0') {

			uc = Character.toUpperCase((char)underlinedChar);
			lc = Character.toLowerCase((char)underlinedChar);

			uci = text.indexOf(uc);
			lci = text.indexOf(lc);

			if(uci == -1)
				index = lci;
			else if(lci == -1)
				index = uci;
			else
				index = (lci < uci) ? lci : uci;
			}

		drawStringUnderlineCharAt(g, fm, text, index, x, y);

	}


    /**
     * Draw a string with the graphics <code>g</code> at location
     * (<code>x</code>, <code>y</code>)
     * just like <code>g.drawString</code> would.
     * The character at index <code>underlinedIndex</code>
     * in text will be underlined. If <code>index</code> is beyond the
     * bounds of <code>text</code> (including < 0), nothing will be
     * underlined.
     *
     * @param g Graphics to draw with
     * @param fm The font metrics to use.  This should have any rendering
     *        hints (such as anti-aliasing) already applied.
     * @param text String to draw
     * @param underlinedIndex Index of character in text to underline
     * @param x x coordinate to draw at
     * @param y y coordinate to draw at
     */
	public static void drawStringUnderlineCharAt(Graphics g, FontMetrics fm,
								String text,
								int underlinedIndex, int x,int y) {

		// Enable text antialiasing.  Note that this will always enable
		// it, regardless of the desktop settings, but I cannot find a way
		// to detect whether AA is enabled across both Java 5 & Java 6.
		// Most Windows users will have AA enabled anyway so it won't even
		// matter.
		Graphics2D g2d = null;
		Object old = null;
		Map aaHints = null;
		if (g instanceof Graphics2D) {
			g2d = (Graphics2D)g;
			aaHints = getDesktopAntiAliasHints();
			if (aaHints!=null) { // Yay - desktop AA!
				old = g2d.getRenderingHints();
				g2d.addRenderingHints(aaHints);
			}
			else { // Default to Java-standard text AA
				old = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
		}

		// Draw the text and underline.
		g.drawString(text,x,y);
		if (underlinedIndex >= 0 && underlinedIndex < text.length() ) {
			//FontMetrics fm = g.getFontMetrics();
			int underlineX = x + fm.stringWidth(
									text.substring(0,underlinedIndex));
			int underlineWidth = fm.charWidth(text.charAt(underlinedIndex));
			int y1 = y + fm.getDescent() - 1;
			g.drawLine(underlineX,y1, underlineX+underlineWidth-1,y1);
		}

		// Set the rendering hint back to its original value.
		if (g2d!=null) {
			if (old instanceof RenderingHints) {
				g2d.setRenderingHints((RenderingHints)old);
			}
			else {
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, old);
			}
		}

	}


	/**
	 * Returns the rendering hints for text that will most accurately reflect
	 * those of the native windowing system.
	 *
	 * @return The rendering hints, or <code>null</code> if they cannot be
	 *         determined.
	 */
	public static Map getDesktopAntiAliasHints() {
		return (Map)Toolkit.getDefaultToolkit().getDesktopProperty(
				"awt.font.desktophints");
	}


    /**
     * Renders a text String in Windows without the mnemonic.  This is here
     * because the WindowsUI hiearchy doesn't match the Component heirarchy. All
     * the overriden paintText methods of the ButtonUI delegates will call this
     * static method.
     *
     * @param g Graphics context
     * @param b Current button to render
     * @param textRect Bounding rectangle to render the text.
     * @param text String to render
     */
    public static void paintText(Graphics g, AbstractButton b,
    				Rectangle textRect, String text, int textShiftOffset) {

		ButtonModel model = b.getModel();
		FontMetrics fm = b.getFontMetrics(g.getFont());
		int mnemIndex = b.getDisplayedMnemonicIndex();

		/* Draw the Text */
		Color color = b.getForeground();
		if(model.isEnabled()) {
		    /*** paint the text normally */
		    g.setColor(color);
		    drawStringUnderlineCharAt(g, fm, text, mnemIndex,
						  textRect.x + textShiftOffset,
						  textRect.y + fm.getAscent() + textShiftOffset);
		}

		else {	/*** paint the text disabled ***/

		    color        = UIManager.getColor("Button.disabledForeground");
		    Color shadow = UIManager.getColor("Button.disabledShadow");

			if (shadow == null) {
			    shadow = b.getBackground().darker();
				g.setColor(shadow);
				drawStringUnderlineCharAt(g, fm,text, mnemIndex, textRect.x,
									textRect.y + fm.getAscent());
		    }

		    if (color == null) {
				color = b.getBackground().brighter();
		    }

		    g.setColor(color);
		    drawStringUnderlineCharAt(g, fm, text, mnemIndex, textRect.x - 1,
		    						textRect.y + fm.getAscent() - 1);

		}

    }


}