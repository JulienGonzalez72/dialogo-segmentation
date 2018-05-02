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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;


/**
 * Component UI for a radio button menu item using the Office XP Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPRadioButtonMenuItemUI extends OfficeXPMenuItemUI {


    public static ComponentUI createUI(JComponent b) {
        return new OfficeXPRadioButtonMenuItemUI();
    }


    protected String getPropertyPrefix() {
		return "RadioButtonMenuItem";
    }


	/**
	 * Paints the actual radio button.
	 *
	 * @param g
	 * @param x
	 * @param y
	 */
	protected void paintCheckImpl(Graphics g, int x, int y) {
	    g.fillRect(x+8, y+9, 4, 2);
	    g.fillRect(x+9, y+8, 2, 4);
	}


	/**
	 * Overridden to fix a bug in 1.4 JRE's.
	 */
	public void processMouseEvent(JMenuItem item, MouseEvent e,
					MenuElement path[], MenuSelectionManager manager) {
		Point p = e.getPoint();
		if(p.x >= 0 && p.x < item.getWidth() &&
				p.y >= 0 && p.y < item.getHeight()) {
			if(e.getID() == MouseEvent.MOUSE_RELEASED) {
				manager.clearSelectedPath();
				item.doClick(0);
				item.setArmed(false);
			}
			else {
				manager.setSelectedPath(path);
			}
		}
		else if(item.getModel().isArmed()) {
			MenuElement newPath[] = new MenuElement[path.length-1];
			int i, c;
			for(i=0,c=path.length-1; i<c; i++)
				newPath[i] = path[i];
			manager.setSelectedPath(newPath);
		}
	}


}