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
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

import org.fife.plaf.OfficeXP.OfficeXPLookAndFeel;
import org.fife.plaf.OfficeXP.OfficeXPUtilities;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;


/**
 * A Look and Feel designed to mimic Microsoft Office 2003 applications.
 * It is essentially the Windows Look and Feel, except that the menus
 * and toolbar buttons are painted differently.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003LookAndFeel extends WindowsLookAndFeel {

	/**
	 * Manages Look and Feel-specific colors.  We keep this guy around
	 * to listen for desktop theme changes, so we can update our colors
	 * accordingly (Swing updates standard colors, but Office looks add
	 * their own).
	 */
	protected Office2003ColorManager colorManager;

	private static final long serialVersionUID = 1L;


	protected Office2003ColorManager createColorManager() {
		return new Office2003ColorManager();
	}


	public UIDefaults getDefaults() {
		UIDefaults defaults = super.getDefaults();
		initSystemColorDefaults2003(defaults);
		initComponentDefaults2003(defaults);
		return defaults;
	}


	public String getDescription() {
		return "The Microsoft Office 2003 Look and Feel";
	}


	public String getID() {
		return "Office2003";
	}


	public String getName() {
		return "Office 2003";
	}


	protected void initClassDefaults(UIDefaults table) {

		super.initClassDefaults(table);

		// Override the components we wanted painted differently.
		String packageName = "org.fife.plaf.Office2003.";
		Object[] uiDefaults = {
			"ButtonUI",			packageName + "Office2003ButtonUI",
			"ComboBoxUI",			"org.fife.plaf.OfficeXP.OfficeXPComboBoxUI",
			"SeparatorUI",			"org.fife.plaf.OfficeXP.OfficeXPSeparatorUI",
			"PopupMenuSeparatorUI",	"org.fife.plaf.OfficeXP.OfficeXPSeparatorUI",
			"MenuUI",				packageName + "Office2003MenuUI",
			"MenuItemUI",			packageName + "Office2003MenuItemUI",
			"MenuBarUI",			packageName + "Office2003MenuBarUI",
			"PopupMenuUI",			packageName + "Office2003PopupMenuUI",
			"CheckBoxMenuItemUI",	packageName + "Office2003CheckBoxMenuItemUI",
			"RadioButtonMenuItemUI",	packageName + "Office2003RadioButtonMenuItemUI",
			"ToggleButtonUI",		packageName + "Office2003ToggleButtonUI",
			"ToolBarUI",			packageName + "Office2003ToolBarUI",
			"ToolBarSeparatorUI",	packageName + "Office2003ToolBarSeparatorUI",
		};
		table.putDefaults(uiDefaults);

	}


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

		Class windowsLnFClass = getClass().getSuperclass();

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
			"PopupMenu.border",					popupMenuBorder,
			"RadioButtonMenuItem.background",		table.get("radioButtonMenuItemBackground"),
			"Separator.foreground",				table.get("separatorForeground"),
			"Separator.background",				table.get("separatorBackground"),
			"TabbedPane.background",				table.get("menuBarBackground"),
			"SplitPane.background",				table.get("menuBarBackground"),
			"ToolBar.background",				table.get("toolBarBackground"),
			"ToolBar.border",					toolBarBorder,
			"ToolBar.highlight",				table.get("toolBarHighlight"),
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


	public void initialize() {
		colorManager = createColorManager();
		colorManager.install();
		super.initialize();
	}


	protected void initSystemColorDefaults2003(UIDefaults table) {

		// Do NOT call this!  This is a separate method.
		//super.initSystemColorDefaults(table);

		colorManager.updateDefaultColors(table);
		//loadSystemColors(table, defaultSystemColors, false);//isNativeLookAndFeel());

		// Create the gradient painter that will be used to fill the icon/
		// check mark part of the menu item background.
		Color beginColor = table.getColor("Office2003LnF.MenuItemBeginGradientColor");
		Color endColor   = table.getColor("Office2003LnF.MenuItemEndGradientColor");
		GradientPaint menuItemBGPaint = new GradientPaint(
						0.0f,0.0f,						beginColor,
						OfficeXPUtilities.LEFT_EDGE_WIDTH,0.0f, endColor);
		table.put("OfficeLnF.MenuItemIconAreaPaint", menuItemBGPaint);

	}


	public void uninitialize() {
		super.uninitialize();
		colorManager.uninstall();
		colorManager = null;
	}


}