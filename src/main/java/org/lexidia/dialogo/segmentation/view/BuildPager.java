package org.lexidia.dialogo.segmentation.view;

import java.awt.Dimension;
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
	 * Numéro de la page en cours de traitement
	 */
	private int page;
	/**
	 * Texte qu'il reste à afficher
	 */
	private String text;
	/**
	 * Dernier segment à être mis en page
	 */
	private int lastPhrase;
	private int minWidth, minHeight;
	private int maxPhrases;
	/**
	 * Hauteur du texte à mettre en page (par défaut, il correspond à la hauteur de l'editor pane)
	 */
	private float height;
	
	public BuildPager(SegmentedTextPane editorPane, TextHandler textHandler) {
		this.editorPane = editorPane;
		this.textHandler = textHandler;
		this.pages = new HashMap<>();
		this.text = textHandler.getShowText();
		this.editorPane.debugRects.clear();
	}
	
	public Map<Integer, List<Integer>> getPages(int startPhrase) {
		if (height == -1) {
			height = editorPane.getHeight();
		}
		pages.clear();
		editorPane.removeAllHighlights();
		page = 1;
		lastPhrase = startPhrase;
		text = textHandler.getTextFrom(startPhrase);
		while (lastPhrase < textHandler.getPhrasesCount()) {
			List<Integer> phrases = new ArrayList<>();
			/// affiche le texte virtuellement ///
			editorPane.setText(text);
			
			/// cherche l'indice du dernier segment qui rentre ///
			int phrase = getLastVisiblePhrase(lastPhrase);
			
			/// nombre de segments maximum par page ///
			if (maxPhrases > 0) {
				phrase = Math.min(phrase, lastPhrase + maxPhrases - 1);
			}
			
			/// ajoute tous les segments qui rentrent ///
			phrases = new ArrayList<>();
			for (int i = lastPhrase; i <= phrase; i++) {
				phrases.add(i);
			}
			lastPhrase = phrase + 1;
			
			/// enregistre tous les segments trouvés dans une page précise ///
			if (!phrases.isEmpty()) {
				fillPage(phrases);
			}
			/// la page ne peut même pas contenir un seul segment ///
			else {
				Rectangle lastBounds = getLongestBounds(startPhrase);
				minWidth = Math.max(lastBounds.x, editorPane.getWidth());
				minHeight = Math.max(lastBounds.y, (int) height);
				return null;
			}
			
			String newText = textHandler.getTextFrom(lastPhrase);
			/// dernière page ///
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
	 * Retourne le rectangle qui correspond au dernier caractère du plus long segment
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
				/// s'il dépasse de l'editor pane ///
				if (bounds.getY() > height - bounds.getHeight() - Constants.TEXTPANE_MARGIN) {
					return i - 1;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		return textHandler.getPhrasesCount() - 1;
	}
	
	/**
	 * Remplit une page avec une liste de segments et passe à la page suivante.
	 */
	private void fillPage(List<Integer> phrases) {
		pages.put(page, phrases);
		page++;
	}
	
	/**
	 * Retourne la dimension minimale de la fenêtre pour laquelle toute la mise en page peut se faire correctement.
	 */
	public Dimension getMinimumSize() {
		return new Dimension(minWidth, minHeight);
	}
	
	public void setMaxPhrasesByPage(int maxPhrases) {
		this.maxPhrases = maxPhrases;
	}
	
	/**
	 * Fixe la hauteur du texte pour chaque page.
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
}
