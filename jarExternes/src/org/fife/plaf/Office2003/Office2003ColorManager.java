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

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

import org.fife.plaf.OfficeXP.OfficeXPColorManager;


/**
 * Installs Windows UxTheme-specific colors.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Office2003ColorManager extends OfficeXPColorManager {


	/**
	 * Constructor.
	 */
	protected Office2003ColorManager() {
	}


	protected Object[] getColorSchemeBlueDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(0xab, 0xc8, 0xf6);
		Object office2003MenuBackground		= new ColorUIResource(0xf6, 0xf6, 0xf6);

		Object[] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(0xFF, 0xF4, 0xCC),
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(0xFF, 0xD4, 0x97),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0xFE, 0x91, 0x4E),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(0xFF, 0xCB, 0x87),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(0xc3, 0xda, 0xf9),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				null,

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(0xFF, 0xC0, 0x6F),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(0xFE, 0x80, 0x3E),

			"OfficeLnF.HighlightBorderColor",						new ColorUIResource(0x00, 0x00, 0x80),
			"OfficeLnF.HighlightColor",							new ColorUIResource(0xFF, 0xEE, 0xC2),

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(0xFF, 0xFF, 0xFF),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(0x27, 0x41, 0x76),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(0xE3, 0xEF, 0xFF),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(0x87, 0xAD, 0xE4),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(0x9E, 0xBE, 0xF5),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(0xC3, 0xDA, 0xF9),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(0xDD, 0xEC, 0xFE),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(0x81, 0xA9, 0xE2),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(0x3B, 0x61, 0x9C),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(0xFF, 0xF4, 0xCC),
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(0xFF, 0xD0, 0x91),
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(0xFE, 0x91, 0x4E),
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(0xFF, 0xD3, 0x8E),

			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(0xFF, 0xF4, 0xCC),
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			new ColorUIResource(0xFF, 0xD6, 0x9A),
			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(0xE3, 0xEF, 0xFF),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(0x93, 0xB5, 0xE7),

			"OfficeLnF.MenuBorderColor",							new ColorUIResource(0x00, 0x2d, 0x96),

			"checkBoxMenuItemBackground",							office2003MenuBackground,

			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(0x6A, 0x8C, 0xCB),
			"toolBarHighlight",									new ColorUIResource(0xF1, 0xF9, 0xFF),
			"separatorForeground",								new ColorUIResource(0x6A, 0x8C, 0xCB),
			"separatorBackground",								office2003MenuBackground,

		};

		return defaults;

	}


	protected Object[] getColorSchemeGreenDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(242, 241, 228);
		Object office2003MenuBackground		= new ColorUIResource(244, 244, 238);

		Object [] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(0xFF, 0xF4, 0xCC),
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(0xFF, 0xD4, 0x97),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0xFE, 0x91, 0x4E),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(0xFF, 0xCB, 0x87),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				table.getColor("Panel.background"),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				null,

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(255, 192, 111),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(254, 128, 62),

			"OfficeLnF.HighlightBorderColor",						new ColorUIResource(63, 93, 56),
			"OfficeLnF.HighlightColor",							new ColorUIResource(255, 238, 194),

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(81, 94, 51),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(255, 255, 237),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(184, 199, 146),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(217, 217, 167),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(242, 240, 228),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(244, 247, 222),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(177, 198, 140),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(96, 128, 88),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(255, 208, 145),
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(254, 145, 78),
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(255, 211, 142),

			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			new ColorUIResource(255, 214, 154),
			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(246, 240, 213),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(194, 206, 159),

			"OfficeLnF.MenuBorderColor",							new ColorUIResource(117, 141, 94),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(96, 128, 88),
			"toolBarHighlight",									new ColorUIResource(244, 247, 222),
			"separatorForeground",								new ColorUIResource(96, 128, 88),
			"separatorBackground",								office2003MenuBackground,

		};

		return defaults;

	}


	protected Object[] getColorSchemeSilverDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(243, 243, 247);
		Object office2003MenuBackground		= new ColorUIResource(253, 250, 255);

		Object [] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(255, 242, 200),
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(255, 210, 148),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(254, 149, 82),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(255, 207, 139),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(237, 237, 245),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(179, 178, 201),

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(255, 192, 111),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(254, 128, 62),

			"OfficeLnF.HighlightBorderColor",						new ColorUIResource(75, 75, 111),
			"OfficeLnF.HighlightColor",							new ColorUIResource(255, 238, 194),

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(84, 84, 117),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(249, 249, 255),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(159, 157, 185),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(215, 215, 229),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(243, 243, 247),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(243, 244, 250),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(152, 151, 181),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(124, 124, 148),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(255, 208, 145),
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(254, 145, 78),
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(255, 211, 142),

			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			new ColorUIResource(255, 214, 154),
			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(232, 233, 241),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(186, 185, 205),

			"OfficeLnF.MenuBorderColor",							new ColorUIResource(124, 124, 148),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(110, 109, 143),
			"toolBarHighlight",									new ColorUIResource(255, 255, 255),
			"separatorForeground",								new ColorUIResource(110, 109, 143),
			"separatorBackground",								office2003MenuBackground,

		};

		return defaults;

	}


	protected Object[] getColorSchemeWindowsClassicDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(246, 245, 244);
		Object office2003MenuBackground		= new ColorUIResource(249, 248, 247);
		Object armedColor					= new ColorUIResource(182, 189, 210);
		Object selectedColor				= new ColorUIResource(133, 146, 181);

		Object [] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				armedColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			selectedColor,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(241, 239, 237),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(221, 218, 211),

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(212, 213, 216),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(133, 146, 181),

			"OfficeLnF.HighlightBorderColor",						new ColorUIResource(10, 36, 106),
			"OfficeLnF.HighlightColor",							new ColorUIResource(182, 189, 210),

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(160, 160, 160),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(249, 248, 247),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(215, 211, 204),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(212, 208, 200),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(245, 245, 244),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(245, 244, 242),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(213, 210, 202),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(213, 210, 202),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		armedColor,
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		armedColor,
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	selectedColor,
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		selectedColor,

			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		armedColor,
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			armedColor,
			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(249, 247, 246),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(237, 235, 232),

			"OfficeLnF.MenuBorderColor",							new ColorUIResource(102, 102, 102),

			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(166, 166, 166),
			"toolBarHighlight",									new ColorUIResource(255, 255, 255),
			"separatorForeground",								new ColorUIResource(166, 166, 166),
			"separatorBackground",								office2003MenuBackground,

		};

		return defaults;

	}


	protected String getLookAndFeelClassName() {
		return "org.fife.plaf.Office2003.Office2003LookAndFeel";
	}


}