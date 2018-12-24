package org.lexidia.dialogo.segmentation.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SegmentedSlider extends JSlider {

	/**
	 * décalage du slider de la marge du bas dû à la barre de progression (à ne plus utiliser)
	 */
	@Deprecated
	public static final float BOTTOM_EXPLICIT_MARGIN = 15;
	
	private PropertyChangeListener sliderListener;
	
	public enum Position {
		LEFT, RIGHT, TOP, BOTTOM;
		boolean isHorizontal() {
			return this == LEFT || this == RIGHT;
		}
	}
	private Position position;
	private SegmentedTextPanel panel;
	private float margin;
	
	public SegmentedSlider(final Position position, final SegmentedTextPanel panel, final float margin) {
		this.position = position;
		this.panel = panel;
		this.margin = margin;
		setOrientation(position.isHorizontal() ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
		setOpaque(false);
		sliderListener = null;
	}
	
	/**
	 * @author Haerwynn
	 */
	public void setSliderListener(PropertyChangeListener sliderListener) {
		this.sliderListener = sliderListener;
	}
	
	public void init() {
		setMinimum((int) margin);
		
		final SegmentedTextPane editorPane = panel.getEditorPane();
		/// ajuste la valeur de départ ///
		setMaximum((position.isHorizontal() ? getWidth() : getHeight()) + getMinimum());
		updateValue(editorPane);
		
		/// changement de valeur ///
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateLine(editorPane);
			}
		});
		
		/// lâché de souris ///
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				editorPane.setLine(null);
				applyChanges(editorPane);
			}
		});
		
		/// lâché de touche de clavier ///
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				editorPane.setLine(null);
				applyChanges(editorPane);
			}
		});
	}
	
	public void updateValue(SegmentedTextPane editorPane) {
		switch (position) {
			case BOTTOM:
				setValue((int) editorPane.getBottomMargin());
				break;
			case LEFT:
				setValue((int) editorPane.getLeftMargin());
				break;
			case RIGHT:
				setValue(getMaximum() - (int) editorPane.getRightMargin() + getMinimum());
				break;
			case TOP:
				setValue(getMaximum() - (int) editorPane.getTopMargin() + getMinimum());
				break;
		}
		editorPane.setLine(null);
	}
	
	private void applyChanges(SegmentedTextPane editorPane) {
		float v = getMarginValue(position);
		String key = null;
		switch (position) {
			case BOTTOM:
				editorPane.setBottomMargin(v);
				key = "bottomMargin";
				break;
			case LEFT:
				editorPane.setLeftMargin(v);
				key = "leftMargin";
				break;
			case RIGHT:
				editorPane.setRightMargin(v);
				key = "rightMargin";
				break;
			case TOP:
				editorPane.setTopMargin(v);
				key = "topMargin";
				break;
		}
		if (key != null && sliderListener != null) {
			PropertyChangeEvent evt = new PropertyChangeEvent(this, key, null, v);
			sliderListener.propertyChange(evt);
		}
		panel.rebuildPages();
	}
	
	private void updateLine(SegmentedTextPane editorPane) {
		float v = getMarginValue(position);
		switch (position) {
			case BOTTOM:
				editorPane.setLine(new Line2D.Float(
						getX() + getWidth(), editorPane.getHeight() - v,
						editorPane.getWidth(), editorPane.getHeight() - v));
				break;
			case LEFT:
				editorPane.setLine(new Line2D.Float(
						v, getY() + getHeight(),
						v, editorPane.getHeight()));
				break;
			case RIGHT:
				editorPane.setLine(new Line2D.Float(
						editorPane.getWidth() - v, getY() + getHeight(),
						editorPane.getWidth() - v, editorPane.getHeight()));
				break;
			case TOP:
				editorPane.setLine(new Line2D.Float(
						getX() + getWidth(), v,
						editorPane.getWidth(), v));
				break;
		}
		editorPane.repaint();
	}
	
	private float getMarginValue(Position position) {
		switch (position) {
			case BOTTOM:
				return getValue();
			case LEFT:
				return getValue();
			case RIGHT:
				return getMaximum() - getValue() + getMinimum();
			case TOP:
				return getMaximum() - getValue() + getMinimum();
		}
		return -1;
	}
	
}
