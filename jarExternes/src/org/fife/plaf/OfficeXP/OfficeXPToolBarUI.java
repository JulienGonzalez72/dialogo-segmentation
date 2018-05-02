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
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.border.Border;

import com.sun.java.swing.plaf.windows.WindowsToolBarUI;


/**
 * The view for toolbars using the Office XP Look and Feel.  This class
 * is overridden so that the button part of the toolbar is painted a color
 * slightly different from the main window color, so that it sticks out just
 * a little, like in Office XP.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class OfficeXPToolBarUI extends WindowsToolBarUI {

	/**
	 * Listens for events in the toolbar.  Currently only listens for
	 * components being added/removed so that we can set their opaquities (sp)
	 * correctly (since a toolbar in Office XP has a different background
	 * color than a standard window).
	 */
	private Listener listener;

	private static final Insets EMPTY_INSETS = new Insets(0,0,0,0);


	public static ComponentUI createUI(JComponent c) {
		return new OfficeXPToolBarUI();
	}


	/**
	 * Returns the sum of the heights of all child components of the
	 * specified component.
	 *
	 * @param c The component.
	 * @return The sum of the heights of all of <code>c</code>'s children.
	 */
	public static final int getContainedComponentsHeight(JComponent c) {
		Component[] components = c.getComponents();
		int num = components.length;
		int height = 0;
		for (int i=0; i<num; i++) {
			height += components[i].getHeight();
		}
		Insets insets = c.getInsets();
		return height + insets.top + insets.bottom;
	}


	/**
	 * Returns the sum of the widths of all child components of the specified
	 * component, plus the width of its insets.
	 *
	 * @param c The component.
	 * @return The sum of the widths of its child components.
	 */
	public static final int getContainedComponentsWidth(JComponent c) {
		Component[] components = c.getComponents();
		int num = components.length;
		int width = 0;
		for (int i=0; i<num; i++) {
			width += components[i].getWidth();
		}
		Insets insets = c.getInsets();
		return width + insets.left + insets.right;
	}


	/**
	 * Installs this UI for a specified toolbar.
	 *
	 * @param c The toolbar.
	 */
	public void installUI(JComponent c) {
		super.installUI(c);
		JToolBar toolBar = (JToolBar)c;
		listener = new Listener(toolBar);	// Get buttons already there.
		toolBar.addContainerListener(listener);	// Get buttons added later.
	}


	/**
	 * Paints the toolbar.  Note that in Office XP, the toolbar actually
	 * consists of two colors:  The standard "window" color, and a slightly
	 * brighter color used as the background for the actual toolbar buttons
	 * and separators.
	 */
	public void paint(Graphics g, JComponent c) {
		paint(g, c, c.getBackground(),
				UIManager.getColor("MenuBar.background"));
	}


	/**
	 * Actual workhorse method for painting the toolbar.
	 *
	 * @param g The graphics context to paint with.
	 * @param c The toolbar to paint.
	 * @param primaryBackground The color to use behind all of the toolbar's
	 *                          widgets.
	 * @param secondaryBackground The main color of the toolbar.
	 */
	public void paint(Graphics g, JComponent c, Color primaryBackground,
					Color secondaryBackground) {

		// Skip painting if the toolbar isn't opaque, to make it easier for
		// folks to embed toolbars in custom components without our fancy
		// painting.
		if (!c.isOpaque()) {
			return;
		}

		// We don't want the toolbar in file choosers to be painted specially.
		Component parent = c.getParent();
		boolean parentIsFileChooser = (parent instanceof JFileChooser);
		if (parentIsFileChooser) {
			super.paint(g, c);
		}

		Rectangle bounds = c.getBounds();
		Border b = c.getBorder();
		Insets insets = b!=null ? b.getBorderInsets(c) : EMPTY_INSETS;

		// Paint the pixels above a toolbar that isn't floating,
		// if applicable.
		if (!isFloating() && !parentIsFileChooser &&
				((JToolBar)c).isBorderPainted()) {
			g.setColor(secondaryBackground);
			g.fillRect(0,0, bounds.width,2);
			bounds.y += 2;
		}
		// Just so we paint the entire toolbar in this case.
		else if (parentIsFileChooser) {
			insets.top = insets.left = insets.bottom = insets.right = 0;
		}

		// First, fill the entire background with toolbar background color.
		g.setColor(secondaryBackground);
		g.fillRect(0,0, bounds.width,bounds.height);

		if (((JToolBar)c).getOrientation() == JToolBar.HORIZONTAL) {
			ComponentOrientation co = toolBar.getComponentOrientation();
			int width = getContainedComponentsWidth(c);
			g.setColor(primaryBackground);
			if (co.isLeftToRight()) {
				g.fillRect(insets.left,insets.top,
					width-insets.left,bounds.height-insets.top);
			}
			else {
				int x = bounds.width-insets.right-width-1;
				g.fillRect(x,insets.top, width,bounds.height-insets.top);
			}
		}
		else { // Vertical.
			int height = getContainedComponentsHeight(c);
			g.setColor(primaryBackground);
			g.fillRect(0,insets.top, bounds.width,height-insets.top);
		}
//		super.paint(g, c);

	}


	/**
	 * Called when the toolbar is made to be floating or docked.  This is
	 * overridden so we can make the border not painted when the toolbar
	 * is floating.
	 */
	public void setFloating(boolean b, java.awt.Point p) {
		toolBar.setBorderPainted(!b);
		super.setFloating(b,p);
	}


	/**
	 * Returns whether a specified component, if added to a
	 * <code>JToolBar</code>, should not be opaque.  Since most widgets added
	 * to a toolbar in the OfficeXP Look and Feel (buttons) need to show
	 * the toolbar's color as their backgrounds, we need a way to see if
	 * a specified component can and should have its opaque property set
	 * to <code>false</code>.<p>
	 *
	 * A component can only be made not opaque if it is a subclass of
	 * <code>JComponent</code>, but some <code>JComponent</code>s should
	 * be kept opaque, such as text fields and combo boxes.  This method
	 * knows about these special cases.
	 *
	 * @param c The component being added to the toolbar.
	 * @return Whether that component is a <code>JComponent</code> and
	 *         needs its opaque property set to <code>false</code>.
	 */
	private static final boolean shouldNotBeOpaque(Component c) {
		return (c instanceof JComponent) && !(c instanceof JTextField) &&
				!(c instanceof JComboBox);
	}


	/**
	 * Uninstalls this UI from a given toolbar.
	 *
	 * @param c The toolbar.
	 */
	public void uninstallUI(JComponent c) {
		JToolBar toolBar = (JToolBar)c;
		listener.restoreOriginalOpaquities();
		toolBar.removeContainerListener(listener);
		listener = null;
		super.uninstallUI(c);
	}


	/**
	 * Listens for components added to/removed from the toolbar and updates
	 * their opaquities as appropriate to ensure the toolbar's gradient
	 * shows beneath them.
	 */
	private static class Listener implements ContainerListener {

		private HashMap origOpaquities;

		/**
		 * Constructor.  This sets the opaquity value for all widgets
		 * currently on the toolbar to <code>false</code>, if appropriate.
		 *
		 * @param toolBar The toolbar whose UI is being set to Office2003.
		 */
		public Listener(final JToolBar toolBar) {

			origOpaquities = new HashMap(8);

			// Postpone setting the opaquity value, as widgets on the
			// toolbar may have their UI's reset after this toolbar does,
			// thus setting their opaquity to the default value and
			// overriding us.
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Component[] comps = toolBar.getComponents();
					for (int i=0; i<comps.length; i++)
						addToolbarComponent(comps[i]);
				}
			});

		}

		/**
		 * Sets the component's opaquity property to <code>false</code> if
		 * it should show the toolbar's gradient background.  This method is
		 * called whenever a widget is added to the toolbar, and is also
		 * called when a toolbar's UI is first set to Office2003, to
		 * initialize all widgets already on the toolbar.
		 *
		 * @param c The component on the toolbar.
		 * @see #removeToolbarComponent
		 */
		private void addToolbarComponent(Component c) {
			if (OfficeXPToolBarUI.shouldNotBeOpaque(c)) {
				JComponent jc = (JComponent)c;
				origOpaquities.put(jc, new Boolean(jc.isOpaque()));
				jc.setOpaque(false);
			}
		}

		/**
		 * Called when a component is added to the toolbar.  This method
		 * sets the component's opaquity property to <code>false</code>, if
		 * appropriate.
		 *
		 * @param e The container event.
		 */
		public void componentAdded(ContainerEvent e) {
			addToolbarComponent(e.getChild());
		}

		/**
		 * Called when a component is removed from the toolbar.  This method
		 * restores the component's original opaquity.
		 *
		 * @param e The container event.
		 */
		public void componentRemoved(ContainerEvent e) {
			Component child = e.getChild();
			removeToolbarComponent(child);
			origOpaquities.remove(child);
		}

		/**
		 * Removes a widget from the cache of widgets on the toolbar
		 * and restores that widget's original opaquity value.  This
		 * method is called whenever a widget is removed from the toolbar,
		 * and is also called on all widgets on the toolbar when its UI
		 * is changed.
		 *
		 * @param c A widget on the toolbar (or used to be on the toolbar)
		 *          whose original opaquity is to be restored.
		 * @see #addToolbarComponent
		 */
		private void removeToolbarComponent(Component c) {
			if (c instanceof JComponent) {
				JComponent jc = (JComponent)c;
				Boolean oldOpaquity = (Boolean)origOpaquities.get(c);
				if (oldOpaquity!=null)
					jc.setOpaque(oldOpaquity.booleanValue());
				// Throws ConcurrentModificationException from
				// restoreOriginalOpaquities.
				//origOpaquities.remove(c);
			}
		}

		/**
		 * This should be called when the UI is being uninstalled from a
		 * toolbar to restore the original opaquity values to all widgets
		 * on the toolbar.
		 */
		public void restoreOriginalOpaquities() {
			Iterator i = origOpaquities.keySet().iterator();
			while (i.hasNext())
				removeToolbarComponent((JComponent)i.next());
			origOpaquities.clear();
		}

	}


}