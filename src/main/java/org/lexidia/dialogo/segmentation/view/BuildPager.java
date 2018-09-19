package org.lexidia.dialogo.segmentation.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;

import org.lexidia.dialogo.segmentation.main.Constants;
import org.lexidia.dialogo.segmentation.model.TextHandler;

public class BuildPager {
	
	private SegmentedTextPane editorPane;
	private TextHandler textHandler;
	private Map<Integer, List<Integer>> pages;
	/**
	 * Hauteur d'un caract�re
	 */
	private float h;
	/**
	 * Num�ro de la page en cours de traitement
	 */
	private int page;
	/**
	 * Texte qu'il reste � afficher
	 */
	private String text;
	/**
	 * Dernier segment � �tre mis en page
	 */
	private int lastPhrase;
	/**
	 * Indice du dernier caract�re trait�
	 */
	private int lastOffset;
	private int minWidth, minHeight;
	
	public BuildPager(SegmentedTextPane editorPane, TextHandler textHandler) {
		this.editorPane = editorPane;
		this.textHandler = textHandler;
		this.pages = new HashMap<>();
		try {
			/// on r�cup�re la hauteur des lignes en fonction de la police s�lectionn�e ///
			this.h = editorPane.getFontMetrics(editorPane.getFont()).getHeight() * (1 + editorPane.getLineSpacing() / 2);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		this.text = textHandler.getShowText();
		this.editorPane.debugRects.clear();
	}
	
	public Map<Integer, List<Integer>> getPages(int startPhrase) {
		pages.clear();
		editorPane.removeAllHighlights();
		lastOffset = 0;
		page = 1;
		lastPhrase = startPhrase;
		text = textHandler.getTextFrom(startPhrase);
		boolean ff = true;
		while (lastPhrase < textHandler.getPhrasesCount()) {
			List<Integer> phrases = new ArrayList<>();
			/// affiche le texte virtuellement ///
			editorPane.setText(text);
			
			try {
				/// micro-attente (pour �viter certains bugs de synchronisation) ///
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*for (int i = 0; i < text.length(); i++) {
				if (ff) {
					try {
						if (false) {
							editorPane.debugRect = editorPane.modelToView(i);
						}
						else {
							editorPane.debugRects.add(editorPane.modelToView(i));							
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
			ff = false;*/
			
			/// cherche l'indice du dernier segment qui rentre ///
			int phrase = getLastVisiblePhrase(lastPhrase);
			
			/// ajoute tous les segments qui rentrent ///
			phrases = new ArrayList<>();
			for (int i = lastPhrase; i <= phrase; i++) {
				phrases.add(i);
			}
			lastPhrase = phrase + 1;
			
			/// enregistre tous les segments trouv�s dans une page pr�cise ///
			if (!phrases.isEmpty()) {
				fillPage(phrases);
			}
			/// la page ne peut m�me pas contenir un seul segment ///
			else {
				Rectangle lastBounds = getLongestBounds(startPhrase);
				minWidth = Math.max(lastBounds.x, editorPane.getWidth());
				minHeight = Math.max(lastBounds.y, editorPane.getHeight());
				return null;
			}
			
			String newText = textHandler.getTextFrom(lastPhrase);
			/// derni�re page ///
			if (newText.equals(text)) {
				if (!pages.get(page - 1).contains(phrase)
						&& phrase >= 0 && phrase < textHandler.getPhrasesCount()) {
					pages.get(page - 1).add(phrase);
				}
				break;
			} else {
				text = newText;
			}
		}
		
		editorPane.repaint();
		return pages;
	}
	
	/**
	 * Retourne le rectangle qui correspond au dernier caract�re du plus long segment
	 * de telle sorte qu'il n'y ait qu'un seul segment par page.
	 */
	private Rectangle getLongestBounds(int startPhrase) {
		Rectangle bounds = null;
		for (int p = startPhrase; p < textHandler.getPhrasesCount(); p++) {
			editorPane.setText(textHandler.getPhrase(p));
			try {
				Rectangle currentBounds = editorPane.modelToView(textHandler.getPhrase(p).length());
				if (bounds == null
						|| (currentBounds.x + currentBounds.y > bounds.x + bounds.y)) {
					bounds = currentBounds;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		int coords = bounds.x + bounds.y;
		return new Rectangle(coords, coords, bounds.width, bounds.height);
	}
	
	/**
	 * Cherche l'indice du dernier segment qui peut rentrer dans l'editor pane.
	 */
	private int getLastVisiblePhrase(int startPhrase) {
		for (int i = startPhrase; i < textHandler.getPhrasesCount(); i++) {
			/// position de la fin du segment dans la page ///
			int endOffset = textHandler.getRelativeOffset(startPhrase, textHandler.getPauseOffset(i) - 1);
			try {
				Rectangle bounds = editorPane.modelToView(endOffset);
				/// s'il d�passe de l'editor pane ///
				if (bounds.getY() > editorPane.getHeight() - bounds.getHeight() - Constants.TEXTPANE_MARGING) {
					return i - 1;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		return textHandler.getPhrasesCount() - 1;
	}
	
	/**
	 * Remplit une page avec une liste de segments et passe � la page suivante.
	 */
	private void fillPage(List<Integer> phrases) {
		pages.put(page, phrases);
		page++;
	}
	
	/**
	 * Retourne la dimension minimale de la fen�tre pour laquelle toute la mise en page peut se faire correctement.
	 */
	public Dimension getMinimumSize() {
		return new Dimension(minWidth, minHeight);
	}
	
}
