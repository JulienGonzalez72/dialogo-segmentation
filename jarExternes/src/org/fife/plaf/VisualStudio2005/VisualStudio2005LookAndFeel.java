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

import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

import org.fife.plaf.OfficeXP.OfficeXPLookAndFeel;
import org.fife.plaf.Office2003.*;


/**
 * A Look and Feel designed to mimic Visual Studio 2005.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class VisualStudio2005LookAndFeel extends Office2003LookAndFeel {

	private static final long serialVersionUID = 1L;


	protected Office2003ColorManager createColorManager() {
		return new VisualStudio2005ColorManager();
	}


	public String getDescription() {
		return "The Visual Studio 2005 Look and Feel";
	}


	public String getID() {
		return "VisualStudio2005";
	}


	public String getName() {
		return "Visual Studio 2005";
	}


	protected void initClassDefaults(UIDefaults table) {

		super.initClassDefaults(table);

		// Override the components we wanted painted differently.
		String packageName = "org.fife.plaf.VisualStudio2005.";
		Object[] uiDefaults = {
			"CheckBoxMenuItemUI",	packageName + "VisualStudio2005CheckBoxMenuItemUI",
			"TabbedPaneUI",		packageName + "VisualStudio2005TabbedPaneUI",
			"MenuItemUI",			packageName + "VisualStudio2005MenuItemUI",
			"ToggleButtonUI",		packageName + "VisualStudio2005ToggleButtonUI",
			"ToolBarSeparatorUI",	packageName + "VisualStudio2005ToolBarSeparatorUI",
		};
		table.putDefaults(uiDefaults);

    }


	/**
	 * This is overridden because, by default, a Look and Feel evidently looks
	 * for certain icons in a "./icons/" directory.  We just want to use the
	 * icons used by the Windows Look and Feel; we don't want to supply our
	 * own.
	 */
	protected void initComponentDefaults2003(UIDefaults table)  {

		// Do NOT call this!  This is a separate method.
		//super.initComponentDefaults( table );

		// Initialize any UIResources we'll use below.

		// This is just to make the toolbar appear as though it's not all the way on
		// the left (see Word/Excel 2003).
		Object toolBarBorder = new UIDefaults.ProxyLazyValue("org.fife.plaf.Office2003.Office2003ToolBarBorder");
		Object popupMenuBorder = new UIDefaults.ProxyLazyValue("org.fife.plaf.Office2003.Office2003PopupMenuUI$Office2003PopupBorder");
		Object menuBarBorder = new UIDefaults.ProxyLazyValue("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource",
													null,
													new Object[] { new Insets(0,0,0,0) });
		Object comboBoxBorder = new UIDefaults.ProxyLazyValue("org.fife.plaf.OfficeXP.OfficeXPComboBoxUI$ComboBorder",
													"getComboBorder",
													new Object[] { });

		Class windowsLnFClass = getClass().getSuperclass().getSuperclass();

		Object[] defaults = {

			//"InternalFrame.icon", LookAndFeel.makeIcon(windowsLnFClass, "icons/JavaCup.gif"),
			"Tree.openIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/TreeOpen.gif"),
			"Tree.closedIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/TreeClosed.gif"),
			"Tree.leafIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/TreeLeaf.gif"),
			"OptionPane.errorIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Error.gif"),
			"OptionPane.informationIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Inform.gif"),
			"OptionPane.warningIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Warn.gif"),
			"OptionPane.questionIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Question.gif"),
			"FileView.directoryIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Directory.gif"),
			"FileView.fileIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/File.gif"),
			"FileView.computerIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/Computer.gif"),
			"FileView.hardDriveIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/HardDrive.gif"),
			"FileView.floppyDriveIcon", LookAndFeel.makeIcon(windowsLnFClass, "icons/FloppyDrive.gif"),

			"CheckBoxMenuItem.background",		table.get("checkBoxMenuItemBackground"),
			"ComboBox.border",					comboBoxBorder,
			"Menu.background",					table.get("menuBackground"),
			"Menu.submenuPopupOffsetX",			new Integer(0),
			"Menu.submenuPopupOffsetY",			new Integer(0),
			"MenuBar.background",				table.get("menuBarBackground"),
			"MenuBar.border",					menuBarBorder,
			"MenuItem.background",				table.get("menuItemBackground"),
			"MenuItem.disabledForeground", 		table.get("menuItemDisabledForeground"),
			"PopupMenu.border",					popupMenuBorder,
			"RadioButtonMenuItem.background",		table.get("radioButtonMenuItemBackground"),
			"Separator.foreground",				table.get("separatorForeground"),
			"Separator.background",				table.get("separatorBackground"),
			"TabbedPane.background",				table.get("tabbedPaneBackground"),
			"TabbedPane.selected",				table.get("tabbedPaneSelected"),
			"SplitPane.background",				table.get("menuBarBackground"),
			"ToolBar.background",				table.get("toolBarBackground"),
			"ToolBar.border",					toolBarBorder,
			"ToolBar.shadow",					table.get("toolBarShadow"),

		};

		table.putDefaults(defaults);

		// Add the gradient blue colors for menu bars and toolbars.
		String gradientStr = System.getProperty("officelnf.gradient");
		Boolean useGradient = "true".equals(gradientStr) ? Boolean.TRUE :
												Boolean.FALSE;
		table.put("Office2003LnF.GradientPanels", useGradient);
		table.put("OfficeLnF.ScreenSize",
				Toolkit.getDefaultToolkit().getScreenSize());

		// If the locale is English, update several components' fonts to
		// Tahoma.
		OfficeXPLookAndFeel.possiblyUpdateFonts(table);

	}


}