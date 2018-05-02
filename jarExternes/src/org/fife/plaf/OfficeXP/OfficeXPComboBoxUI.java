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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;


/**
 * An extension of WindowsComboBoxUI that paints combo boxes like they
 * are "flat style" combo boxes often found in toolbars of more modern
 * Windows XP applications.
 *
 * @author Robert Futrell
 * @version 0.5
 */
public class OfficeXPComboBoxUI extends WindowsComboBoxUI {

	private boolean isMouseOver = false;
	private boolean isFocused = false;
	protected boolean popupVisible;
	private OfficeXPArrowButton xpArrowButton; // For convenience.

	private MouseListener mouseHandler;
	private FocusListener focusListener;
	private PopupMenuListener popupListener;


	/**
	 * Creates a button which will be used as the control to show or hide
	 * the popup portion of the combo box.
	 *
	 * @return A button which represents the popup control.
	 */
	protected final JButton createArrowButton() {
		JButton button = createArrowButtonImpl();
		button.addMouseListener(mouseHandler);
		xpArrowButton = (OfficeXPArrowButton)button;
		return button;
	}


	/**
	 * Creates a button which will be used as the control to show or hide
	 * the popup portion of the combo box.
	 *
	 * @return A button which represents the popup control.
	 */
	protected OfficeXPArrowButton createArrowButtonImpl() {
		return new OfficeXPArrowButton(this);
	}


	/**
	 * Returns a class that listens for focus events in this combo box.
	 *
	 * @return The listener.
	 */
	protected FocusListener createFocusHandler() {
		return new FocusHandler();
	}


	/**
	 * Returns the class that will listen to this combo box's mouse events.
	 *
	 * @return The listener.
	 */
	protected MouseListener createMouseHandler() {
		return new MouseHandler();
	}


	/**
	 * Returns the class that will listen for this combo box's popup events.
	 *
	 * @return The popup listener.
	 */
	protected PopupMenuListener createPopupListener() {
		return new OfficeXPComboPopupListener();
	}


	/**
	 * Returns a new <code>OfficeXPComboBoxUI</code>.
	 *
	 * @return The UI.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new OfficeXPComboBoxUI();
	}


	/**
	 * Returns the color to use for the border of this combo box.  You should
	 * never call this method; it is used internally.
	 *
	 * @return The color to paint the border.
	 */
	public Color getBorderColor() {
		boolean highlighted = (comboBox.isEnabled() &&
							(isFocused() || isMouseOver()));
		return highlighted ?
				UIManager.getColor("OfficeLnF.HighlightBorderColor") :
				comboBox.getBackground();
	}


	/**
	 * Installs needed listeners.
	 */
	protected void installListeners() {

		super.installListeners();

		mouseHandler = createMouseHandler();
		focusListener = createFocusHandler();
		comboBox.addMouseListener(mouseHandler);
		JTextComponent textComponent = (JTextComponent)comboBox.getEditor().
											getEditorComponent();
		textComponent.addMouseListener(mouseHandler);
		textComponent.addFocusListener(focusListener);
		popupListener = createPopupListener();
		comboBox.addPopupMenuListener(popupListener);

	}


	public boolean isFocused() {
		return isFocused;
	}


	public boolean isMouseOver() {
		return isMouseOver;
	}


	public void paintCurrentValueBackground(Graphics g, Rectangle bounds,
											boolean focused) {
		Color old = g.getColor();
		g.setColor(UIManager.getColor(comboBox.isEnabled() ?
				"ComboBox.background" : "ComboBox.disabledBackground"));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g.setColor(old);
	}

    protected void setFocused(boolean focused) {
		isFocused = focused;
		comboBox.repaint(); // Needed by 1.5.0_02.
	}


	protected void setMouseOver(boolean over) {
		isMouseOver = over;
	}


	public void uninstallListeners() {
		comboBox.removeMouseListener(mouseHandler);
		JTextComponent textComponent = (JTextComponent)comboBox.getEditor().
											getEditorComponent();
		textComponent.removeMouseListener(mouseHandler);
		textComponent.removeFocusListener(focusListener);
		// Doesn't need to be done as the button is discarded (?)...
		//xpArrowButton.removeMouseListener(mouseHandler);
		comboBox.removePopupMenuListener(popupListener);
		super.uninstallListeners();
	}


	/**
	 * Border for the combo box; a simple line border whose color changes
	 * depending on the state of the combo box.  Note that this class
	 * implements <code>UIResource</code>.<p>
	 *
	 * Using this custom border instead of a <code>LineBorder</code> allows us
	 * to change the border's color frequently (from mouse events) without
	 * having to repeatedly create new line borders, or cache a line border for
	 * each state.<p>
	 *
	 * NOTE:  This class is a singleton.  In other words, the same border is
	 * used by all JComboBoxes when this LnF is enabled.  The color with which
	 * to paint the border is determined by the combo box's and its UI's state.
	 * This prevents us from having to create a border for each combo, thus
	 * saving memory.
	 */
	public static class ComboBorder extends AbstractBorder
										implements UIResource {

		private static ComboBorder border;

		private static final long serialVersionUID = 1L;

		private ComboBorder() {
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(1, 1, 1, 1);
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.left = insets.top = insets.right = insets.bottom = 1;
			return insets;
		}

		public synchronized static ComboBorder getComboBorder() {
			if (border==null)
				border = new ComboBorder();
			return border;
		}

		public boolean isBorderOpaque() {
			return true; // We always completely fill our bounds.
		}

		public void paintBorder(Component c, Graphics g, int x, int y,
							int width, int height) {

			Color oldColor = g.getColor();

			// This is a workaround to keep OfficeLnFs working with products
			// that use a combobox border on non-combobox components, such
			// as the NetBeans Profiler.  They create a composite component,
			// with one of its subcomponents being a JComboBox.  They then
			// grab the border of the JComboBox, set the border of the
			// composite component to this border, then set the JComboBox's
			// border to null.  While this is valid, as border's don't
			// normally come with any guarantee they'll be used on only one
			// JComponent subclass, it really cramps this class's style...
			// Thanks to Girish for finding this!
			if (c instanceof JComboBox) {
				JComboBox combo = (JComboBox)c;
				if (combo.getUI() instanceof OfficeXPComboBoxUI) {
					OfficeXPComboBoxUI ui = (OfficeXPComboBoxUI)combo.getUI();
					g.setColor(ui.getBorderColor());
				}
				else {
					// Look as normal as possible...
					g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
				}
			}
			else {
				// Look as normal as possible...
				g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
			}

			g.drawRect(x,y, width-1,height-1);
			g.setColor(oldColor);

		}

	}


	/**
	 * Listens for mouse events in this combo box.
	 */
	protected class MouseHandler extends MouseAdapter {

		protected void doMouseEnteredNotFocused() {
			// Disabled combo boxes should not highlight when armed.
			if (comboBox.isEnabled()) {
				xpArrowButton.updatePaintState(OfficeXPArrowButton.ARMED);
			}
		}

		protected void doMouseExitedNotFocused() {
			xpArrowButton.updatePaintState(OfficeXPArrowButton.NORMAL);
		}

		public final void mouseEntered(MouseEvent e) {
			if (!isFocused())
				doMouseEnteredNotFocused();
			setMouseOver(true);
			comboBox.repaint();
		}

		public final void mouseExited(MouseEvent e) {
			if (!isFocused())
				doMouseExitedNotFocused();
			setMouseOver(false);
			comboBox.repaint();
		}

	}


	/**
	 * Listens for focus events in this combo box.
	 */
	protected class FocusHandler implements FocusListener {

		public void focusGained(FocusEvent e) {
			setFocused(true);
			if (!popupVisible)
				xpArrowButton.updatePaintState(OfficeXPArrowButton.ARMED);
		}

		public void focusLost(FocusEvent e) {
			setFocused(false);
			xpArrowButton.updatePaintState(OfficeXPArrowButton.NORMAL);
		}
	}


	/**
	 * Listens for the popup menu being displayed/hidden in this combo box.
	 */
	protected class OfficeXPComboPopupListener implements PopupMenuListener {

		public void popupMenuCanceled(PopupMenuEvent e) {
			popupVisible = false;
			if (isFocused())
				xpArrowButton.updatePaintState(OfficeXPArrowButton.ARMED);
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			popupVisible = false;
			if (isFocused())
				xpArrowButton.updatePaintState(OfficeXPArrowButton.ARMED);
		}

		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			popupVisible = true;
			xpArrowButton.updatePaintState(OfficeXPArrowButton.PRESSED);
		}

	}


}