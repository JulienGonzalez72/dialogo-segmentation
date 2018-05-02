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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.fife.plaf.OfficeXP.OfficeXPMenuBarUI;


/**
 * Component UI for a menu bar using the Office 2003 Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003MenuBarUI extends OfficeXPMenuBarUI  {


	public static ComponentUI createUI(JComponent c) {
		return new Office2003MenuBarUI();
	}


	protected void installDefaults() {

		super.installDefaults();

		// This is overridden because in WindowsMenuBarUI, the value
		// MenuBar.background is overridden.  We want to preserve the
		// value we want.  Simply setting this value in the Look and Feel
		// class won't cut it because of this.
		UIManager.getLookAndFeelDefaults().put("MenuBar.background",
								UIManager.get("menuBarBackground"));

		// NOTE:  In JRE 1.5, they are using a different method for the
		//        menu bar's color, so if it breaks...

	}


	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(640, 21);
	}


	public void paint(Graphics g, JComponent c) {

		// Paint gradient background.
		if (UIManager.getBoolean("Office2003LnF.GradientPanels")) {
			Color c1 = UIManager.getColor("Office2003LnF.PanelGradientColor1");
			Color c2   = UIManager.getColor("Office2003LnF.PanelGradientColor2");
			Graphics2D g2d = (Graphics2D) g;
			Rectangle bounds = c.getBounds();
			Dimension size = UIManager.getDimension("OfficeLnF.ScreenSize");
			GradientPaint paint = new GradientPaint(0,0, c1,
									size.width,bounds.height, c2);
			g2d.setPaint(paint);
			g2d.fill(bounds);
		}

		// Paint solid color background.
		else {
			super.paint(g, c);
		}

	}


}