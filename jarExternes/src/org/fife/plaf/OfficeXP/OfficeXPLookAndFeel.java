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

import java.awt.Color;
import java.awt.Insets;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;


/**
 * A Look and Feel designed to mimic Microsoft Office XP applications.
 * It is essentially the Windows Look and Feel, except that the menus
 * and toolbar buttons are painted differently.<p>
 *
 * This look tries to sync itself with the current Windows color scheme
 * (in Windows XP, there's "blue," "olive green," "silver" and
 * "Windows Classic," which is used when XP styles are disabled).  If
 * you wish to override this behavior, and keep your application using
 * the "blue" color scheme, for example, try setting
 * <code>THEME_PROPERTY</code> before setting the look and feel.  For
 * example,<p>
 *
 * <pre>
 * System.setProperty(OfficeXPLookAndFeel.THEME_PROPERTY, OfficeXPLookAndFeel.THEME_BLUE);
 * UIManager.setLookAndFeel("org.fife.plaf.OfficeXP.OfficeXPLookAndFeel");
 * </pre>
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPLookAndFeel extends WindowsLookAndFeel {

	/**
	 * Property that, if set to "blue", "olive", "silver" or "classic"
	 * will override the Windows system theme.
	 */
	public static final String THEME_PROPERTY	= "OfficeLnFs.theme";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the blue
	 * color scheme.
	 */
	public static final String THEME_BLUE		= "blue";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the olive
	 * green color scheme.
	 */
	public static final String THEME_OLIVE		= "olive";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the silver
	 * color scheme.
	 */
	public static final String THEME_SILVER		= "silver";

	/**
	 * Value for <code>THEME_PROPERTY</code> specifying the
	 * Windows Classic color scheme.
	 */
	public static final String THEME_CLASSIC	= "classic";


	/**
	 * Manages Look and Feel-specific colors.  We keep this guy around
	 * to listen for desktop theme changes, so we can update our colors
	 * accordingly (Swing updates standard colors, but Office looks add
	 * their own).
	 */
	private OfficeXPColorManager colorManager;


	private static final long serialVersionUID = 1L;

	protected OfficeXPColorManager createColorManager() {
		return new OfficeXPColorManager();
	}


	public String getDescription() {
		return "The Microsoft Office XP Look and Feel";
	}


	public String getID() {
		return "OfficeXP";
	}


	public String getName() {
		return "Office XP";
	}


	protected void initClassDefaults(UIDefaults table) {

		super.initClassDefaults(table);

		// Override the components we wanted painted differently.
		String packageName = "org.fife.plaf.OfficeXP.";
		Object[] uiDefaults = {
			"ButtonUI",			packageName + "OfficeXPButtonUI",
			"ComboBoxUI",			packageName + "OfficeXPComboBoxUI",
			"SeparatorUI",			packageName + "OfficeXPSeparatorUI",
			"PopupMenuSeparatorUI",	packageName + "OfficeXPSeparatorUI",
			"MenuUI",				packageName + "OfficeXPMenuUI",
			"MenuItemUI",			packageName + "OfficeXPMenuItemUI",
			"MenuBarUI",			packageName + "OfficeXPMenuBarUI",
			"PopupMenuUI",			packageName + "OfficeXPPopupMenuUI",
			"CheckBoxMenuItemUI",	packageName + "OfficeXPCheckBoxMenuItemUI",
			"RadioButtonMenuItemUI",	packageName + "OfficeXPRadioButtonMenuItemUI",
			"ToggleButtonUI",		packageName + "OfficeXPToggleButtonUI",
			"ToolBarUI",			packageName + "OfficeXPToolBarUI",
		};

		table.putDefaults(uiDefaults);

	}


	/**
	 * This is overridden because, by default, a Look and Feel evidently looks
	 * for certain icons in a "./icons/" directory.  We just want to use the
	 * icons used by the Windows Look and Feel; we don't want to supply our
	 * own.
	 */
	protected void initComponentDefaults(UIDefaults table)  {

		super.initComponentDefaults( table );

		// Some objects we'll be using below.
		Object popupMenuBorder = new UIDefaults.ProxyLazyValue("org.fife.plaf.OfficeXP.OfficeXPPopupMenuUI$XPPopupBorder");
		Object toolBarBorder = new UIDefaults.ProxyLazyValue("org.fife.plaf.OfficeXP.OfficeXPToolBarBorder");
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

			"ComboBox.border",						comboBoxBorder,

			"Menu.submenuPopupOffsetX",			new Integer(0),
			"Menu.submenuPopupOffsetY",			new Integer(0),

			"MenuBar.border",					menuBarBorder,

			"PopupMenu.border",					popupMenuBorder,

			"ToolBar.border",					toolBarBorder,

		};

		table.putDefaults(defaults);

		// Get initial Look and Feel-specific colors.
		colorManager.updateDefaultColors(table);

		// Create the painter that will be used to fill the icon/check mark
		// part of the menu item background.
		Color iconAreaColor = table.getColor("OfficeXPLnF.ChosenMenuColor");
		table.put("OfficeLnF.MenuItemIconAreaPaint", iconAreaColor);


		// If the locale is English, update several components' fonts to
		// Tahoma.
		OfficeXPLookAndFeel.possiblyUpdateFonts(table);

	}


	public void initialize() {
		colorManager = createColorManager();
		colorManager.install();
		super.initialize();
	}


	/**
	 * Updates some components to use Tahoma as their font.  This is because
	 * Windows Look and Feel does not set Tahoma for all components that use
	 * it, even as of 1.5.  Note that we only do this for the English locale,
	 * as some locales, such as Japanese, don't use Tahoma (as it does not
	 * support the language's characters).
	 *
	 * @param table The table in which to set the font values.
	 */
	public static void possiblyUpdateFonts(UIDefaults table) {

		// Check this way "just in case locale string changes..."
		Locale l = JComponent.getDefaultLocale();
		if (l.getLanguage().equals(Locale.ENGLISH.getLanguage())) {

			// Create our Tahoma font proxy value.
			Object plain = new Integer(java.awt.Font.PLAIN);
			Object tahoma = new UIDefaults.ProxyLazyValue(
						"javax.swing.plaf.FontUIResource",
						null,
						new Object[] {"Tahoma", plain, new Integer(11)});

			String javaVersion = System.getProperty("java.version");
			boolean is14 = javaVersion.indexOf("1.4")==0;

			// Some updates are only needed for 1.4.x.
			if (is14) {
				Object[] fonts = {
					"Button.font", tahoma,
					"CheckBox.font", tahoma,
					"CheckBoxMenuItem.font", tahoma,
					"ComboBox.font", tahoma,
					"Label.font", tahoma,
					"List.font", tahoma,
					"Menu.font", tahoma,
					"MenuBar.font", tahoma,
					"MenuItem.font", tahoma,
					"OptionPane.font", tahoma,
					"OptionPane.messageFont", tahoma,
					"OptionPane.buttonFont", tahoma,
					"Panel.font", tahoma,
					"PopupMenu.font", tahoma,
					"RadioButton.font", tahoma,
					"RadioButtonMenuItem.font", tahoma,
					"ScrollPane.font", tahoma,
					"Spinner.font", tahoma,
					"TabbedPane.font", tahoma,
					"Table.font", tahoma,
					"TableHeader.font", tahoma,
					"TitledBorder.font", tahoma,
					"ToggleButton.font", tahoma,
					"ToolBar.font", tahoma,
					"ToolTip.font", tahoma,
					"Tree.font", tahoma,
					"Viewport.font", tahoma,
				};
				table.putDefaults(fonts);
			} // End of if (is14).

			// Other updates are still needed even as of 1.5.0_02.
			Object[] fonts = {
				"EditorPane.font", tahoma,
				"TextArea.font", tahoma,
				"TextField.font", tahoma,
				"TextPane.font", tahoma,
			};
			table.putDefaults(fonts);

		} // End of if (locale==Locale.English);

	}


	public void uninitialize() {
		super.uninitialize();
		colorManager.uninstall();
		colorManager = null;
	}


}