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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.UIManager;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import org.fife.plaf.OfficeXP.OfficeXPGraphicsUtils;


/**
 * Routines used by the Visual Studio 2005 Look and Feel components.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class VisualStudio2005GraphicsUtils {


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
		// W2K Feature: Check to see if the Underscore should be rendered.
		if (WindowsLookAndFeel.isMnemonicHidden())
			mnemIndex = -1;

		/* Draw the Text */
		Color color = b.getForeground();
		if(model.isEnabled()) {
		    /*** paint the text normally */
		    g.setColor(color);
		    OfficeXPGraphicsUtils.drawStringUnderlineCharAt(g,fm,text, mnemIndex,
						  textRect.x + textShiftOffset,
						  textRect.y + fm.getAscent() + textShiftOffset);
		}

		else {	/*** paint the text disabled ***/

		    color        = UIManager.getColor("Button.disabledForeground");
		    Color shadow = UIManager.getColor("Button.disabledShadow");

			if (shadow == null) {
			    shadow = b.getBackground().darker();
				g.setColor(shadow);
				OfficeXPGraphicsUtils.drawStringUnderlineCharAt(g,fm,text,
									mnemIndex, textRect.x,
									textRect.y + fm.getAscent());
		    }

		    if (color == null) {
				color = b.getBackground().brighter();
		    }

		    g.setColor(color);
		    OfficeXPGraphicsUtils.drawStringUnderlineCharAt(g,fm,text,
		    							mnemIndex, textRect.x - 1,
		    							textRect.y + fm.getAscent() - 1);

		}

    }


}