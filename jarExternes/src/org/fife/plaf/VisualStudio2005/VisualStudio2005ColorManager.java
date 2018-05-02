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

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

import org.fife.plaf.Office2003.Office2003ColorManager;


/**
 * Installs Windows UxTheme-specific colors.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class VisualStudio2005ColorManager extends Office2003ColorManager {


	/**
	 * Constructor.
	 */
	protected VisualStudio2005ColorManager() {
	}


	protected Object[] getColorSchemeBlueDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(0xE7, 0xE3, 0xD7);
		Object office2003MenuBackground		= new ColorUIResource(0xFC, 0xFC, 0xF9);
		Object armedColor					= new ColorUIResource(0xC1, 0xD2, 0xEE);

		Object[] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				armedColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0x98, 0xB5, 0xE2),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(0xF4, 0xF2, 0xED),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(0xD0, 0xCE, 0xBC),

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(225, 230, 232),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			new ColorUIResource(49, 106, 197),

			"OfficeLnF.HighlightBorderColor",						new ColorUIResource(0x31, 0x6A, 0xC5),
			"OfficeLnF.HighlightColor",							new ColorUIResource(0xC1, 0xD2, 0xEE),

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(0xFF, 0xFF, 0xFF),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(0xC1, 0xBE, 0xB3),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(0xFE, 0xFE, 0xFB),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(0xC4, 0xC3, 0xAC),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(0xE5, 0xE5, 0xD7),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(0xF3, 0xF2, 0xE7),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(0xFA, 0xF9, 0xF5),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(0xBC, 0xBC, 0xA1),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(0xA3, 0xA3, 0x7C),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(0x98, 0xB5, 0xE2),
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(0x98, 0xB5, 0xE2),
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		armedColor,
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		armedColor,

			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(251, 251, 249),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(247, 245, 239),
			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		armedColor,
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			armedColor,
			"OfficeLnF.MenuBorderColor",							new ColorUIResource(0x8A, 0x86, 0x7A),

			// Real items.
			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"menuItemDisabledForeground",							new ColorUIResource(0xC5, 0xC2, 0xB6),
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(0xC5, 0xC2, 0xB8),
			"tabbedPaneSelected",								new ColorUIResource(0xFF, 0xFF, 0xFF),
			"tabbedPaneBackground",								new ColorUIResource(0xF7, 0xF6, 0xEF),
			"separatorForeground",								new ColorUIResource(0xC5, 0xC2, 0xB6),
			"separatorBackground",								office2003MenuBackground,

			// VS2005 LnF-specific.
			"VisualStudio2005.TabBorderColor",						new ColorUIResource(0x7F, 0x9D, 0xB9),
			"VisualStudio2005.BackgroundTabBorderColor",				new ColorUIResource(0xAC, 0xA8, 0x99),
			"VisualStudio2005.SelectedCBMenuItemBorderColor",			new ColorUIResource(75, 75, 111),

		};

		return defaults;

	}


	protected Object[] getColorSchemeGreenDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(233, 232, 219);
		Object office2003MenuBackground		= new ColorUIResource(252, 252, 249);
		Object highlightBorderColor			= new ColorUIResource(147, 160, 112);
		Object armedColor					= new ColorUIResource(182, 198, 141);

		Object[] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				armedColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			highlightBorderColor,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(244, 242, 237),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(208, 206, 188),

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(194, 207, 158),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			highlightBorderColor,

			"OfficeLnF.HighlightBorderColor",						highlightBorderColor,
			"OfficeLnF.HighlightColor",							armedColor,

			"Office2003LnF.ToolBarGripLightColor",					new ColorUIResource(255, 255, 255),
			"Office2003LnF.ToolBarGripDarkColor",					new ColorUIResource(193, 190, 179),

			"Office2003LnF.MenuItemBeginGradientColor",				new ColorUIResource(254, 254, 251),
			"Office2003LnF.MenuItemEndGradientColor",				new ColorUIResource(196, 195, 172),

			"Office2003LnF.PanelGradientColor1",					new ColorUIResource(229, 229, 215),
			"Office2003LnF.PanelGradientColor2",					new ColorUIResource(243, 242, 231),

			"Office2003LnF.ToolBarBeginGradientColor",				new ColorUIResource(250, 249, 245),
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(192, 192, 168),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(163, 163, 124),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	highlightBorderColor,
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		highlightBorderColor,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		armedColor,
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		armedColor,

			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(251, 251, 249),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(247, 245, 239),
			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		armedColor,
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			armedColor,
			"OfficeLnF.MenuBorderColor",							new ColorUIResource(138, 134, 122),

			// Real items.
			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"menuItemDisabledForeground",							new ColorUIResource(197, 194, 184),
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(197, 194, 184),
			"tabbedPaneSelected",								new ColorUIResource(255, 255, 255),
			"tabbedPaneBackground",								new ColorUIResource(247, 246, 239),
			"separatorForeground",								new ColorUIResource(197, 194, 184),
			"separatorBackground",								office2003MenuBackground,

			// VS2005 LnF-specific.
			"VisualStudio2005.TabBorderColor",						new ColorUIResource(127, 157, 185),
			"VisualStudio2005.BackgroundTabBorderColor",				new ColorUIResource(172, 168, 153),
			"VisualStudio2005.SelectedCBMenuItemBorderColor",			highlightBorderColor,

		};

		return defaults;

	}


	protected Object[] getColorSchemeSilverDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(223, 223, 234);
		Object office2003MenuBackground		= new ColorUIResource(253, 250, 255);

		Object[] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				new ColorUIResource(0xFF, 0xF4, 0xCC),
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				new ColorUIResource(0xFF, 0xD4, 0x97),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			new ColorUIResource(0xFE, 0x91, 0x4E),
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			new ColorUIResource(0xFF, 0xCB, 0x87),
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
			"Office2003LnF.ToolBarEndGradientColor",				new ColorUIResource(153, 151, 181),
			"Office2003LnF.ToolBarBottomBorderColor",				new ColorUIResource(124, 124, 148),
			"Office2003LnF.ToolBarBackgroundColor",					standardOffice2003Blue,
			"Office2003LnF.ToolBarButtonArmedBeginGradientColor",		new ColorUIResource(0xFF, 0xF4, 0xCC),
			"Office2003LnF.ToolBarButtonArmedEndGradientColor",		new ColorUIResource(0xFF, 0xD0, 0x91),
			"Office2003LnF.ToolBarButtonSelectedBeginGradientColor",	new ColorUIResource(0xFE, 0x91, 0x4E),
			"Office2003LnF.ToolBarButtonSelectedEndGradientColor",		new ColorUIResource(0xFF, 0xD3, 0x8E),

			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(232, 233, 241),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(186, 185, 205),
			"Office2003LnF.MenuBarItemArmedBeginGradientColor",		new ColorUIResource(255, 244, 204),
			"Office2003LnF.MenuBarItemArmedEndGradientColor",			new ColorUIResource(255, 214, 154),
			"OfficeLnF.MenuBorderColor",							new ColorUIResource(124, 124, 148),

			// Real items.
			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"menuItemDisabledForeground",							new ColorUIResource(141, 141, 141),
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(110, 109, 143),
			"tabbedPaneSelected",								new ColorUIResource(255, 255, 255),
			"tabbedPaneBackground",								new ColorUIResource(246, 246, 249),
			"separatorForeground",								new ColorUIResource(110, 109, 143),
			"separatorBackground",								office2003MenuBackground,

			// VS2005 LnF-specific.
			"VisualStudio2005.TabBorderColor",						new ColorUIResource(127, 157, 185),
			"VisualStudio2005.BackgroundTabBorderColor",				new ColorUIResource(157, 157, 161),
			"VisualStudio2005.SelectedCBMenuItemBorderColor",			new ColorUIResource(75, 75, 111),

		};

		return defaults;

	}


	protected Object[] getColorSchemeWindowsClassicDefaults(UIDefaults table) {

		// Create some standard colors.
		Object standardOffice2003Blue			= new ColorUIResource(221, 218, 212);
		Object office2003MenuBackground		= new ColorUIResource(249, 248, 247);
		Object armedColor					= new ColorUIResource(182, 189, 210);
		Object selectedColor				= new ColorUIResource(133, 146, 181);

		Object[] defaults = {

			"OfficeLnF.ComboBox.Arrow.Armed.Gradient1",				armedColor,
			"OfficeLnF.ComboBox.Arrow.Armed.Gradient2",				null,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient1",			selectedColor,
			"OfficeLnF.ComboBox.Arrow.Selected.Gradient2",			null,
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient1",				new ColorUIResource(241, 239, 237),
			"OfficeLnF.ComboBox.Arrow.Normal.Gradient2",				new ColorUIResource(221, 218, 211),

			"Office2003LnF.CBMenuItemCheckBGColor",					new ColorUIResource(212, 213, 216),
			"Office2003LnF.CBMenuItemCheckBGSelectedColor",			selectedColor,

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
			"Office2003LnF.MenuBarItemSelectedBeginGradientColor",		new ColorUIResource(251, 251, 249),
			"Office2003LnF.MenuBarItemSelectedEndGradientColor",		new ColorUIResource(247, 245, 239),

			"OfficeLnF.MenuBorderColor",							new ColorUIResource(102, 102, 102),

			// Real items.
			"checkBoxMenuItemBackground",							office2003MenuBackground,
			"menuBackground",									office2003MenuBackground,
			"menuBarBackground",								standardOffice2003Blue,
			"menuItemBackground",								office2003MenuBackground,
			"menuItemDisabledForeground",							new ColorUIResource(166, 166, 166),
			"radioButtonMenuItemBackground",						office2003MenuBackground,
			//"toolBarBackground",								standardOffice2003Blue, // Not done because of toolbars in JFileChooser.
			"toolBarShadow",									new ColorUIResource(166, 166, 166),
			"tabbedPaneSelected",								new ColorUIResource(255, 255, 255),
			"tabbedPaneBackground",								new ColorUIResource(212, 208, 200),
			"separatorForeground",								new ColorUIResource(166, 166, 166),
			"separatorBackground",								office2003MenuBackground,

			// VS2005 LnF-specific.
			"VisualStudio2005.TabBorderColor",						new ColorUIResource(128, 128, 128),
			"VisualStudio2005.BackgroundTabBorderColor",				new ColorUIResource(128, 128, 128),
			"VisualStudio2005.SelectedCBMenuItemBorderColor",			new ColorUIResource(10, 36, 106),

		};

		return defaults;

	}


	protected String getLookAndFeelClassName() {
		return "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";
	}


}