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
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;


/**
 * Installs Windows UxTheme-specific colors.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPColorManager implements PropertyChangeListener {

	protected static final int COLOR_SCHEME_BLUE		= 0;
	protected static final int COLOR_SCHEME_GREEN	= 1;
	protected static final int COLOR_SCHEME_SILVER	= 2;

	protected static final String DLL_DESKTOP_PROPERTY   = "win.xpstyle.dllName";
	protected static final String STYLE_DESKTOP_PROPERTY = "win.xpstyle.colorName";
	protected static final String THEMEACTIVE_DESKTOP_PROPERTY = "win.xpstyle.themeActive";


	private static final Toolkit toolkit	= Toolkit.getDefaultToolkit();
	private static final int DELTA		= 2;


	/**
	 * Constructor.
	 */
	protected OfficeXPColorManager() {
	}


	/**
	 * Out of paranoia, we'll check RGB values +/- a delta.  This method
	 * is called when trying to divine the current Windows color scheme
	 * based on a Swing UIDefaults color.  Not being certain whether Sun
	 * grabs these values from Windows of hard-codes (possibly incorrect!)
	 * values which may change from one release to the next, we give
	 * ourselves a fudge factor.
	 *
	 * @param value The value to check.
	 * @param target The target value.
	 * @return Whether or not the specified value is "close enough" to the
	 *         target.
	 */
	private static final boolean closeEnough(int value, int target) {
		return (value>=(target-DELTA)) && (value<=(target+DELTA));
	}


	/**
	 * Returns the color scheme to use.
	 *
	 * @param table UI default values gotten from Swing's WindowsLookAndFeel.
	 *        These have not yet been installed.  This method examines them
	 *        in an attempt to determine the current UxTheme color scheme
	 *        being used.
	 * @return An integer representing the color scheme.
	 */
	protected static final int getColorScheme(final UIDefaults table) {

		Color c = table.getColor("Menu.selectionBackground");

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		if (closeEnough(r,49) && closeEnough(g,106) && closeEnough(b,197)) {
			return COLOR_SCHEME_BLUE;
		}
		else if (closeEnough(r,147) && closeEnough(g,160) && closeEnough(b,112)) {
			return COLOR_SCHEME_GREEN;
		}
		else if (closeEnough(r,178) && closeEnough(g,180) && closeEnough(b,191)) {
			return COLOR_SCHEME_SILVER;
		}
		else {	// Unknown color scheme!
			return COLOR_SCHEME_BLUE; // ???
		}

	}


	protected Object[] getColorSchemeBlueDefaults(UIDefaults table) {

		Object menuItemBackgroundColor = new ColorUIResource(252,252,249);
		Object inactiveTextColor		= new ColorUIResource(197,194,184);
		Object highlightColor		= new ColorUIResource(193,210,238);

		Object[] defaults = {

			"Button.disabledForeground",			inactiveTextColor, // Actually JMenuItem's disabled text color (but not for accelerator).

			"CheckBoxMenuItem.background",		menuItemBackgroundColor,
			"CheckBoxMenuItem.disabledForeground",	inactiveTextColor,

			"Menu.background",					menuItemBackgroundColor,
			"MenuItem.background",				menuItemBackgroundColor,
			"MenuItem.disabledForeground",		inactiveTextColor,		// Actually just the accelerator's disabled color.

			"PopupMenu.background",				menuItemBackgroundColor,
			"PopupMenu.disabledForeground",		inactiveTextColor,

			"RadioButtonMenuItem.background",		menuItemBackgroundColor,
			"RadioButtonMenuItem.disabledForeground",inactiveTextColor,

			"Separator.foreground",				inactiveTextColor,
			"Separator.background",				menuItemBackgroundColor,

			"ToolBar.background",				new ColorUIResource(239,237,222),
			"ToolBar.shadow",					new ColorUIResource(191,188,177),

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",		highlightColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",		null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",	new ColorUIResource(152,181,226),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",	null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",		table.getColor("Panel.background"),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",		null,
			"OfficeLnF.HighlightBorderColor",		new ColorUIResource(49,106,197),
			"OfficeLnF.HighlightColor",			highlightColor,
			"OfficeXPLnF.PressedHighlightColor",	new ColorUIResource(152,181,226),
			"OfficeXPLnF.CheckBoxHighlightColor",	new ColorUIResource(225,230,232),
			"OfficeXPLnF.ChosenMenuColor",		table.getColor("ToolBar.background"),
			"OfficeLnF.MenuBorderColor",			new ColorUIResource(138,134,122),
			"OfficeXPLnF.MenuItemBackground",		table.getColor("MenuItem.background"),

		};

		return defaults;

	}


	protected Object[] getColorSchemeGreenDefaults(UIDefaults table) {

		Object menuItemBackgroundColor = new ColorUIResource(252,252,249);
		Object inactiveTextColor		= new ColorUIResource(197,194,184);
		Object highlightColor		= new ColorUIResource(206,209,195);

		Object[] defaults = {

			"Button.disabledForeground",			inactiveTextColor, // Actually JMenuItem's disabled text color (but not for accelerator).

			"CheckBoxMenuItem.background",		menuItemBackgroundColor,
			"CheckBoxMenuItem.disabledForeground",	inactiveTextColor,

			"Menu.background",					menuItemBackgroundColor,
			"MenuItem.background",				menuItemBackgroundColor,
			"MenuItem.disabledForeground",		inactiveTextColor,		// Actually just the accelerator's disabled color.

			"PopupMenu.background",				menuItemBackgroundColor,
			"PopupMenu.disabledForeground",		inactiveTextColor,

			"RadioButtonMenuItem.background",		menuItemBackgroundColor,
			"RadioButtonMenuItem.disabledForeground",inactiveTextColor,

			"Separator.foreground",				inactiveTextColor,
			"Separator.background",				menuItemBackgroundColor,

			"ToolBar.background",				new ColorUIResource(239,237,222),
			"ToolBar.shadow",					new ColorUIResource(191,188,177),

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",		highlightColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",		null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",	new ColorUIResource(201,208,184),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",	null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",		table.getColor("Panel.background"),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",		null,
			"OfficeLnF.HighlightBorderColor",		new ColorUIResource(147,160,112),
			"OfficeLnF.HighlightColor",			highlightColor,
			"OfficeXPLnF.PressedHighlightColor",	new ColorUIResource(201,208,184),
			"OfficeXPLnF.CheckBoxHighlightColor",	new ColorUIResource(234,235,223),
			"OfficeXPLnF.ChosenMenuColor",		table.getColor("ToolBar.background"),
			"OfficeLnF.MenuBorderColor",			new ColorUIResource(138,134,122),
			"OfficeXPLnF.MenuItemBackground",		table.getColor("MenuItem.background"),

		};

		return defaults;

	}


	protected Object[] getColorSchemeSilverDefaults(UIDefaults table) {

		// Unlike blue and green color schemes, the silver scheme's standard
		// panel background color is different from Swing's suggested value.
		Object standardBackground = new ColorUIResource(224,223,227); // Panels, menu bar, etc.
		Object menuItemBackgroundColor = new ColorUIResource(251,250,251);
		Object inactiveTextColor = new ColorUIResource(197,194,184);
		Object highlightColor = new ColorUIResource(199,199,202);

		Object[] defaults = {

			"Button.disabledForeground",			inactiveTextColor, // Actually JMenuItem's disabled text color (but not for accelerator).

			"CheckBoxMenuItem.background",		menuItemBackgroundColor,
			"CheckBoxMenuItem.disabledForeground",	inactiveTextColor,

			"Menu.background",					menuItemBackgroundColor,
			"MenuItem.background",				menuItemBackgroundColor,
			"MenuItem.disabledForeground",		inactiveTextColor,		// Actually just the accelerator's disabled color.

			"MenuBar.background",				standardBackground,

			"Panel.background",					standardBackground,

			"PopupMenu.background",				menuItemBackgroundColor,
			"PopupMenu.disabledForeground",		inactiveTextColor,

			"RadioButtonMenuItem.background",		menuItemBackgroundColor,
			"RadioButtonMenuItem.disabledForeground",inactiveTextColor,

			"Separator.foreground",				inactiveTextColor,
			"Separator.background",				menuItemBackgroundColor,

			"ToolBar.background",				new ColorUIResource(229,228,232),
			"ToolBar.shadow",					new ColorUIResource(191,188,177),

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",		highlightColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",		null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",	new ColorUIResource(210,211,216),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",	null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",		table.getColor("Panel.background"),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",		null,
			"OfficeLnF.HighlightBorderColor",		new ColorUIResource(169,171,181),
			"OfficeLnF.HighlightColor",			highlightColor,
			"OfficeXPLnF.PressedHighlightColor",	new ColorUIResource(210,211,216),
			"OfficeXPLnF.CheckBoxHighlightColor",	new ColorUIResource(233,234,237),
			"OfficeXPLnF.ChosenMenuColor",		table.getColor("ToolBar.background"),
			"OfficeLnF.MenuBorderColor",			new ColorUIResource(126,126,129),
			"OfficeXPLnF.MenuItemBackground",		table.getColor("MenuItem.background"),

		};

		return defaults;

	}


	protected Object[] getColorSchemeWindowsClassicDefaults(UIDefaults table) {

		Object menuItemBackgroundColor = new ColorUIResource(249,248,247);
		Object inactiveTextColor = new ColorUIResource(166,166,166);
		Object highlightColor = new ColorUIResource(182,189,210);

		Object[] defaults = {

			"Button.disabledForeground",			inactiveTextColor, // Actually JMenuItem's disabled text color (but not for accelerator).

			"CheckBoxMenuItem.background",		menuItemBackgroundColor,
			"CheckBoxMenuItem.disabledForeground",	inactiveTextColor,

			"Menu.background",					menuItemBackgroundColor,
			"MenuItem.background",				menuItemBackgroundColor,
			"MenuItem.disabledForeground",		inactiveTextColor,		// Actually just the accelerator's disabled color.

			"PopupMenu.background",				menuItemBackgroundColor,
			"PopupMenu.disabledForeground",		inactiveTextColor,

			"RadioButtonMenuItem.background",		menuItemBackgroundColor,
			"RadioButtonMenuItem.disabledForeground",inactiveTextColor,

			"Separator.foreground",				inactiveTextColor,
			"Separator.background",				menuItemBackgroundColor,

			"ToolBar.background",				new ColorUIResource(219,216,209),
			"ToolBar.shadow",					new ColorUIResource(166,166,166),

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",		highlightColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",		null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",	new ColorUIResource(133,146,181),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",	null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",		table.getColor("Panel.background"),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",		null,
			"OfficeLnF.HighlightBorderColor",		new ColorUIResource(10,36,106),
			"OfficeLnF.HighlightColor",			highlightColor,
			"OfficeXPLnF.PressedHighlightColor",	new ColorUIResource(133,146,181),
			"OfficeXPLnF.CheckBoxHighlightColor",	new ColorUIResource(212,213,216),
			"OfficeXPLnF.ChosenMenuColor",		table.getColor("ToolBar.background"),
			"OfficeLnF.MenuBorderColor",			new ColorUIResource(102,102,102),
			"OfficeXPLnF.MenuItemBackground",		table.getColor("MenuItem.background"),

		};

		return defaults;

	}


	protected String getLookAndFeelClassName() {
		return "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel";
	}


	public void install() {
		toolkit.addPropertyChangeListener(STYLE_DESKTOP_PROPERTY, this);
		toolkit.addPropertyChangeListener(THEMEACTIVE_DESKTOP_PROPERTY, this);
	}


	/**
	 * Listens for desktop property changes that signify the need to
	 * update Look and Feel-specific colors.
	 *
	 * @param e The property change event.
	 */
	public void propertyChange(PropertyChangeEvent e) {

		String name = e.getPropertyName();

		if (STYLE_DESKTOP_PROPERTY.equals(name) ||
				THEMEACTIVE_DESKTOP_PROPERTY.equals(name)) {
			// Unfortunately we must reload the entire look and feel.
			// In the future we should look for a way to simply reload the
			// colors.
			try {
				UIManager.setLookAndFeel(getLookAndFeelClassName());
			} catch (Exception ex) {
				// Never happens.
				ex.printStackTrace();
			}
		}

	}


	/**
	 * Updates the specified UI default values with Look and Feel-specific
	 * colors.
	 *
	 * @param table The UI defaults table.
	 */
	public void updateDefaultColors(UIDefaults table) {

		Object[] defaults = null;

		String theme = System.getProperty(OfficeXPLookAndFeel.THEME_PROPERTY);

		if (OfficeXPLookAndFeel.THEME_BLUE.equals(theme)) {
			defaults = getColorSchemeBlueDefaults(table);
		}
		else if (OfficeXPLookAndFeel.THEME_OLIVE.equals(theme)) {
			defaults = getColorSchemeGreenDefaults(table);
		}
		else if (OfficeXPLookAndFeel.THEME_SILVER.equals(theme)) {
			defaults = getColorSchemeSilverDefaults(table);
		}
		else if (OfficeXPLookAndFeel.THEME_CLASSIC.equals(theme)) {
			defaults = getColorSchemeWindowsClassicDefaults(table);
		}
		else { // No override
			Boolean themeActive = (Boolean)toolkit.getDesktopProperty(
										THEMEACTIVE_DESKTOP_PROPERTY);
			boolean isThemeActive = themeActive!=null ?
								themeActive.booleanValue() : false;
			if (isThemeActive) {
				switch (getColorScheme(table)) {
					case COLOR_SCHEME_BLUE:
						defaults = getColorSchemeBlueDefaults(table);
						break;
					case COLOR_SCHEME_GREEN:
						defaults = getColorSchemeGreenDefaults(table);
						break;
					case COLOR_SCHEME_SILVER:
						defaults = getColorSchemeSilverDefaults(table);
						break;
				}
			}
			else { // "Classic" Windows look.
				defaults = getColorSchemeWindowsClassicDefaults(table);
			}
		}

		table.putDefaults(defaults);

	}


	public void uninstall() {
		toolkit.removePropertyChangeListener(THEMEACTIVE_DESKTOP_PROPERTY,
										this);
		toolkit.removePropertyChangeListener(STYLE_DESKTOP_PROPERTY, this);
	}


}