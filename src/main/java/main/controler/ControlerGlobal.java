package main.controler;

import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.List;

import main.Constants;
import main.model.TextHandler;
import main.reading.AnticipatedThread;
import main.reading.GuidedThread;
import main.reading.HighlightThread;
import main.reading.ReadMode;
import main.reading.ReadThread;
import main.reading.SegmentedThread;
import main.view.FenetreParametre;
import main.view.Panneau;

//import javax.swing.SwingWorker;

public class ControlerGlobal {

	public Panneau p;
	/**
	 * Thread de lecture actif
	 */
	private ReadThread activeThread;

	public ControlerGlobal(Panneau p) {
		this.p = p;
	}

	public int getNbEssaisRestantsPourSegmentCourant() {
		return p.nbEssaisRestantPourLeSegmentCourant;
	}

	public void decrementerEssaisRestants() {
		p.nbEssaisRestantPourLeSegmentCourant--;
	}

	/**
	 * Se place sur le segment de numero n et démarre le lecteur.
	 */
	public void goTo(int n) throws IllegalArgumentException {
		if (n < 0 || n >= p.textHandler.getPhrasesCount()) {
			throw new IllegalArgumentException("Numéro de segment invalide : " + n);
		}
		if (activeThread != null) {
			activeThread.doStop();
		}
		activeThread = getReadThread(n);
		activeThread.onPhraseEnd.add(new Runnable() {
			public void run() {
				/// fin du dernier segment du texte ///
				if (n == p.textHandler.getPhrasesCount() - 2) {
					p.afficherCompteRendu();
				}
				/// passe au segment suivant ///
				else {
					goTo(n + 1);
				}
			}
		});
		activeThread.start();
	}

	/**
	 * Construit les pages à partir du segment de numero spécifié.
	 */
	public void buildPages(int startPhrase) {
		p.buildPages(startPhrase);
	}

	/**
	 * Affiche la page indiquée.
	 */
	public void showPage(int page) {
		p.showPage(page);
	}
	
	/**
	 * Joue un fichier .wav correspondant à un segment de phrase.
	 * On sortira de cette fonction lorsque le fichier .wav aura été totalement joué.
	 */
	public void play(int phrase) {
		p.setCursor(Constants.CURSOR_LISTEN);
		p.player.play(phrase);
		while (true) {
			if (p.player.isPhraseFinished()) {
				p.setCursor(Cursor.getDefaultCursor());
				break;
			}
			/// fixe toujours le curseur d'écoute pendant toute la durée de l'enregistrement ///
			else if (!p.getCursorName().equals(Constants.CURSOR_SPEAK)) {
				p.setCursor(Constants.CURSOR_LISTEN);
			}
		}
	}

	/**
	 * Retourne le nombre de segments total du texte.
	 */
	public int getPhrasesCount() {
		return p.textHandler.getPhrasesCount();
	}

	/**
	 * Retourne la durée en millisecondes de l'enregistrement qui correspond au segment de phrase indiqué.
	 */
	public long getPhraseDuration(int phrase) {
		p.player.load(phrase);
		return p.player.getDuration();
	}

	/**
	 * Retourne la durée en millisecondes de l'enregistrement courant.
	 */
	public long getCurrentPhraseDuration() {
		return p.player.getDuration();
	}

	/**
	 * Retourne le temps d'attente en millisecondes correspondant à l'enregistrement courant.
	 */
	public long getCurrentWaitTime() {
		return (long) (getCurrentPhraseDuration() * FenetreParametre.tempsPauseEnPourcentageDuTempsDeLecture / 100.);
	}

	/**
	 * Mets en pause le thread courant pendant un certain temps.
	 * 
	 * @param time
	 *            le temps de pause, en millisecondes
	 * @param cursorName
	 *            le type de curseur à définir pendant l'attente (peut être
	 *            Constants.CURSOR_SPEAK ou Constants.CURSOR_LISTEN)
	 */
	public void doWait(long time, String cursorName) {
		try {
			p.setCursor(cursorName);
			Thread.sleep(time);
			p.setCursor(Cursor.getDefaultCursor());
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Attente d’un clic de la souris sur le dernier mot du segment.
	 * <ul>
	 * <li>Paramètre d’entrée 1: Numéro de segment</li>
	 * <li>Paramètre d’entrée 2 : Nombre d’essais autorisé</li>
	 * <li>Paramètre de sortie : True ou False (réussite)</li>
	 * <li>On sort de cette fonction lorsqu’un clic a été réalisé.
	 * Si le clic a été réalisé sur le bon mot on sort avec true, et si le clic a été réalisé sur une partie erronée,
	 * on surligne cette partie avec une couleur qui indique une erreur, Rouge ? En paramètre ?
	 * Et on sort avec False.
	 * </ul>
	 */
	public boolean waitForClick(int n, int nbTry) {
		p.controlerMouse.clicking = false;
		while (true) {
			Thread.yield();
			if (p.controlerMouse.clicking) {
				/// cherche la position exacte dans le texte ///
				int offset = p.textHandler.getAbsoluteOffset(p.getNumeroPremierSegmentAffiché(),p.editorPane.getCaretPosition());
				/// si le clic est juste ///
				if (p.textHandler.wordPause(offset) && p.textHandler.getPhraseIndex(offset) == p.player.getCurrentPhraseIndex()) {
					return true;
				}
				/// si le clic est faux ///
				else {
					/// indique l'erreur en rouge ///
					p.indiquerErreur(
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
									p.textHandler.startWordPosition(offset) + 1),
							p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
									p.textHandler.endWordPosition(offset)));
					return false;
				}
			}
		}
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void highlightPhrase(Color c, int n) {
		if (p.textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffiché(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
			p.editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, c);
		}
	}

	/**
	 * Supprime le surlignage qui se trouve sur le segment n. Ne fait rien si ce
	 * segment n'est pas surligné.
	 */
	public void removeHighlightPhrase(int n) {
		int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffiché(), n);
		int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
		p.editorPane.removeHighlight(debutRelatifSegment, finRelativeSegment);
	}

	/**
	 * Arrête l'enregistrement courant et enlève tout le surlignage.
	 */
	public void stopAll() {
		p.player.stop();
		p.editorPane.désurlignerTout();
	}

	/**
	 * Enlève tout le surlignage d'erreur.
	 */
	public void removeWrongHighlights() {
		p.editorPane.enleverSurlignageRouge();
	}

	public boolean doClick() {
		return doClick(p.nbEssaisRestantPourLeSegmentCourant, p.player.getCurrentPhraseIndex());
	}

	/**
	 * La page se met en attente d'un clic, jusqu'à ce que :<br>
	 * - soit le clic soit juste, renvoie true <br>
	 * - soit il n'y a plus d'essais, renvoie false
	 */
	public boolean doClick(int nbTry, int numeroSegmentCourant) {
		p.nbEssaisRestantPourLeSegmentCourant = nbTry;
		p.player.setCurrentPhrase(numeroSegmentCourant);
		boolean r = false;
		// on ne fait rien si la phrase est en cours de lecture
		if (p.player.isPhraseFinished()) {
			/// cherche la position exacte dans le texte ///
			int offset = p.textHandler.getAbsoluteOffset(p.getNumeroPremierSegmentAffiché(),
					p.editorPane.getCaretPosition());
			// si le clic est juste
			if (p.textHandler.wordPause(offset)
					&& p.textHandler.getPhraseIndex(offset) == p.player.getCurrentPhraseIndex()) {
				r = true;
				if (FenetreParametre.readMode == ReadMode.HIGHLIGHT) {
					traitementClicJusteModeSurlignage(offset, p.textHandler);
				} else {
					traitementClicJuste(offset);
				}
				// si le clic est faux
			} else {
				r = false;
				if (FenetreParametre.readMode == ReadMode.HIGHLIGHT) {
					traitementClicFauxModeSurlignage(offset, p.textHandler);
				} else {
					traitementClicFaux(offset, p.textHandler);
				}
			}
		}
		return r;
	}

	/**
	 * Rafraîchit le surlignage (notamment pour le mode surlignage vert) en fonction
	 * du segment actuel. Enlève également le surlignage rouge.
	 */
	public void updateHighlight() {
		if (FenetreParametre.readMode == ReadMode.HIGHLIGHT) {
			p.editorPane.enleverSurlignageVert();
			int pauseOffset = p.textHandler.getPauseOffset(p.player.getCurrentPhraseIndex() - 1);
			p.editorPane.surlignerPhrase(0,
					p.textHandler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), pauseOffset),
					Constants.RIGHT_COLOR);
		}
		p.editorPane.enleverSurlignageRouge();
	}

	private void traitementClicFauxModeSurlignage(int offset, TextHandler handler) {
		p.nbEssaisRestantPourLeSegmentCourant--;
		// si il reste un essai ou qu'une phrase est en train d'être corrigée
		if (p.nbEssaisRestantPourLeSegmentCourant != 0) {
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			// si il ne reste plus d'essais
		} else {
			/// indique l'erreur en rouge ///
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			/// indique la phrase corrigée en bleu ///
			p.indiquerEtCorrigerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex() - 1)),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex())));
		}
	}

	private void traitementClicJusteModeSurlignage(int offset, TextHandler handler) {
		// int pauseOffset = handler.endWordPosition(offset);
		// on restaure le nombre d'essais
		p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		p.editorPane.enleverSurlignageRouge();
		doNext();
	}

	private void traitementClicFaux(int offset, TextHandler handler) {
		p.nbEssaisRestantPourLeSegmentCourant--;
		// si il reste un essai ou qu'une phrase est en train d'être corrigée
		if (p.nbEssaisRestantPourLeSegmentCourant > 0 || p.editorPane.containsBlueHighlight()) {
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			// si il ne reste plus d'essais
		} else {
			/// indique l'erreur en rouge ///
			p.indiquerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.startWordPosition(offset) + 1),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(), handler.endWordPosition(offset)));
			/// indique la phrase corrigée en bleu ///
			p.indiquerEtCorrigerErreur(
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex() - 1)),
					handler.getRelativeOffset(p.getNumeroPremierSegmentAffiché(),
							handler.getPauseOffset(p.player.getCurrentPhraseIndex())));
		}
	}

	public void traitementClicJuste(int offset) {
		// on restaure le nombre d'essais
		p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
		p.editorPane.enleverSurlignageBleu();
		p.editorPane.enleverSurlignageRouge();
		doNext();
	}

	/**
	 * Essaye de passer au segment suivant, attends et passe à la page suivante si
	 * c'était le dernier segment de la page.
	 */
	public void doNext() {
		if (true) {
			goTo(p.player.getCurrentPhraseIndex() + 1);
		}
	}

	/**
	 * Essaye de passer au segment précédent.
	 */
	public void doPrevious() {
		goTo(p.player.getCurrentPhraseIndex() - 1);
	}

	/**
	 * Essaye d'arrêter l'enregistrement en cours.
	 */
	public void doStop() {
		p.player.stop();
		activeThread.doStop();
	}

	/**
	 * Essaye de reprendre l'enregistrement. Si il est déjà démarré, reprend depuis
	 * le début.
	 */
	public void doPlay() {
		goTo(p.player.getCurrentPhraseIndex());
	}

	public void sauvegarder() {

		String fichier = "preference_" + Constants.NOM_ELEVE + ".txt";

		// recueration des lignes deja existantes
		List<String> lignes = new ArrayList<String>();
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			int i = 0;
			// stockage des lignes modifiées
			while ((ligne = br.readLine()) != null) {
				// si c'est la ligne du segment
				if (i == 9) {
					lignes.add("segmentDepart:" + (this.p.player.getCurrentPhraseIndex() + 1));
				} else {
					lignes.add(ligne);
				}
				i++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ecritures des lignes modifiees
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fichier, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (String s : lignes) {
			writer.println(s);
		}
		writer.close();

	}

	/**
	 * Retourne la page qui contient le segment, ou -1 si le segment n'existe pas.
	 */
	public int getPageOfPhrase(int n) {
		int numeroPage = -1;
		for (Integer i : p.segmentsEnFonctionDeLaPage.keySet()) {
			if (p.segmentsEnFonctionDeLaPage.get(i).contains(n)) {
				numeroPage = i;
				break;
			}
		}
		return numeroPage;
	}

	public void highlightUntilPhrase(Color c, int n) {
		p.surlignerJusquaSegment(c, n);
	}

	/**
	 * Créé un processus associé à la lecture d'un seul segment dans le mode de
	 * lecture actuel.
	 */
	public ReadThread getReadThread(int n) {
		ReadThread t;
		switch (FenetreParametre.readMode) {
			case ANTICIPATED:
				t = new AnticipatedThread(this, n);
				break;
			case GUIDED_READING:
				t = new GuidedThread(this, n);
				break;
			case NORMAL:
				t = new SegmentedThread(this, n);
				break;
			case HIGHLIGHT:
				t = new HighlightThread(this, n);
				break;
			default:
				t = null;
				break;
		}
		return t;
	}

}
