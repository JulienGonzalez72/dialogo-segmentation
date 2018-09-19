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
	}
	
	public Map<Integer, List<Integer>> getPages(int startPhrase) {
		pages.clear();
		editorPane.removeAllHighlights();
		lastOffset = 0;
		page = 1;
		lastPhrase = startPhrase;
		text = textHandler.getTextFrom(startPhrase);
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
			
			/// on cherche la position dans le texte du caract�re le plus proche du coin inf�rieur droit de la page ///
			int off = getLastVisibleOffset(lastPhrase);
			
			/// on parcourt chaque caract�re jusqu'� cette position ///
			phrases = getPhrases(off);
			
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
			
			String newText = textHandler.getShowText().substring(lastOffset);
			/// derni�re page ///
			if (newText.equals(text)) {
				int lastPhraseIndex = textHandler.getPhraseIndex(off);
				if (!pages.get(page - 1).contains(lastPhraseIndex)
						&& lastPhraseIndex >= 0 && lastPhraseIndex < textHandler.getPhrasesCount()) {
					pages.get(page - 1).add(lastPhraseIndex);
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
	 * Ajoute dans une liste tous les segments qui rentrent enti�rement jusqu'� la position off dans l'editor pane.
	 */
	private List<Integer> getPhrases(int off) {
		List<Integer> phrases = new ArrayList<>();
		for (int i = lastOffset; i < off; i++) {
			int phraseIndex = textHandler.getPhraseIndex(i);
			if (phraseIndex == -1) {
				lastOffset = text.length();
			}
			/// on ajoute le segment s'il rentre en entier ///
			if (phraseIndex >= lastPhrase && !phrases.contains(phraseIndex)
					&& phraseIndex != textHandler.getPhraseIndex(off)
					&& textHandler.isPhraseLastOffset(i)) {
				lastPhrase = phraseIndex + 1;
				phrases.add(phraseIndex);
				lastOffset = i;
			}
		}
		return phrases;
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
	 * Cherche la position absolue du caract�re le plus proche du coin inf�rieur droit de l'editor pane.
	 */
	private int getLastVisibleOffset(int phrase) {
		return textHandler.getAbsoluteOffset(phrase,
				editorPane.viewToModel(getEndPoint()));
	}
	
	/**
	 * Retourne le point qui correspond au coin inf�rieur gauche de l'editor pane.
	 */
	private Point getEndPoint() {
		return new Point((int) Constants.TEXTPANE_MARGING,
				(int) (editorPane.getHeight() - Constants.TEXTPANE_MARGING));
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
