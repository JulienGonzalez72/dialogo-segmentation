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


import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.ComponentUI;
import java.awt.Dimension;
import java.awt.Graphics;

import com.sun.java.swing.plaf.windows.WindowsSeparatorUI;


/**
 * Component UI for a separator using the Office XP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPSeparatorUI extends WindowsSeparatorUI {


	public static ComponentUI createUI( JComponent c ) {
		return new OfficeXPSeparatorUI();
	}


	public Dimension getPreferredSize( JComponent c ) {
		return new Dimension(0,2);
	}


	public void paint(Graphics g, JComponent c) {
		// Must distinguish here as JSeparators can be added to panels as
		// "spacers," in which case we don't want to change their appearance.
		if (c.getParent() instanceof JPopupMenu)
			paintMenuSeparator(g, c);
		else
			super.paint(g, c);
	}


	protected void paintMenuSeparator(Graphics g, JComponent c) {
		OfficeXPUtilities.paintMenuItemBackground(g, c);
		g.setColor(c.getForeground());
		if (c.getComponentOrientation().isLeftToRight()) {
			g.drawLine(31,0, c.getWidth(),0);
		}
		else {
			g.drawLine(0,0, c.getWidth()-31,0);
		}
	}


}