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
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.windows.WindowsToolBarSeparatorUI;


/**
 * The separator used in toolbars in the Office2003 Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003ToolBarSeparatorUI extends WindowsToolBarSeparatorUI {


	public static ComponentUI createUI(JComponent c) {
		return new Office2003ToolBarSeparatorUI();
	}


	public void paint(Graphics g, JComponent c) {

		boolean vertical = ((JSeparator)c).getOrientation() == SwingConstants.VERTICAL;
		Dimension size = c.getSize();
		Color temp = g.getColor();
	
		Color shadow = UIManager.getColor("ToolBar.shadow");
		Color highlight = UIManager.getColor("ToolBar.highlight");
	
		if (vertical) {
			int x = (size.width / 2) - 1;
			g.setColor(shadow);
			g.drawLine(x,2, x,size.height-3);

			g.setColor(highlight);
			g.drawLine(x+1,3, x+1, size.height-2);
		}
		else {
			int y = (size.height / 2) - 1;
			g.setColor(shadow);
			g.drawLine(2, y, size.width - 2, y);
			g.setColor(highlight);
			g.drawLine(2, y + 1, size.width - 2, y + 1);
		}

		g.setColor(temp);

	}


}