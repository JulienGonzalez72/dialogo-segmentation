package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.List;

import javax.swing.SwingWorker;

public class ControlerGlobal {

	Panneau p;

	public ControlerGlobal(Panneau p) {
		this.p = p;
	}

	/**
	 * Se place sur le segment de numero n
	 */
	public void goTo(int n) {
		p.player.goTo(n);
		p.showPage(getPageOfPhrase(n));
	}

	/**
	 * Construit les pages à partir du segment de numero spécifié
	 */
	public void buildPages(int startPhrase) {
		p.buildPagesByJulien(startPhrase);
	}

	/**
	 * Colorie le segment numero n en couleur c
	 */
	public void highlightPhrase(Color c, int n) {
		if (p.textHandler.getPhrase(n) != null) {
			int debutRelatifSegment = p.textHandler.getRelativeStartPhrasePosition(p.getNumeroPremierSegmentAffiché(),
					n);
			int finRelativeSegment = debutRelatifSegment + p.textHandler.getPhrase(n).length();
			p.editorPane.surlignerPhrase(debutRelatifSegment, finRelativeSegment, Constants.RIGHT_COLOR);
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

	public boolean waitForClick() {
		return waitForClick(p.nbEssaisRestantPourLeSegmentCourant, p.player.getCurrentPhraseIndex());
	}

	/**
	 * La page se met en attente d'un clic, jusqu'à ce que :<br>
	 * - soit le clic soit juste, renvoie true <br>
	 * - soit il n'y a plus d'essais, renvoie false
	 */
	public boolean waitForClick(int nbTry, int numeroSegmentCourant) {
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
		if (p.nbEssaisRestantPourLeSegmentCourant > 0) {
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
			p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
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
			p.nbEssaisRestantPourLeSegmentCourant = Panneau.defautNBEssaisParSegment;
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
		// si la page est finis on affiche la suivante
		if (p.pageFinis()) {
			p.controlFrame.disableAll();
			new SwingWorker<Object, Object>() {
				// Ce traitement sera exécuté dans un autre thread :
				protected Object doInBackground() throws Exception {
					Thread.sleep(Constants.PAGE_WAIT_TIME);
					return null;
				}

				// Ce traitement sera exécuté à la fin dans l'EDT
				protected void done() {
					if (p.hasNextPage()) {
						p.controlFrame.enableAll();
						p.player.nextPhrase();
						p.afficherPageSuivante();
					} else {
						p.afficherCompteRendu();
					}
				}
			}.execute();
		} else {
			p.player.nextPhrase();
		}
		if (FenetreParametre.readMode != ReadMode.GUIDED_READING) {
			updateHighlight();
		} else {
			p.editorPane.désurlignerTout();
			highlightPhrase(Constants.RIGHT_COLOR, p.player.getCurrentPhraseIndex());
		}
	}

	public void sauvegarder() {

		String fichier = "preference.txt";

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
	 * Essaye de passer au segment précédent.
	 */
	public void doPrevious() {
		// si on était au premier segment de la page on affiche la page précédente
		if (p.player.getCurrentPhraseIndex() == p.getNumeroPremierSegmentAffiché()) {
			p.player.previousPhrase();
			p.afficherPagePrecedente();
		} else {
			p.player.previousPhrase();
		}
		if (FenetreParametre.readMode != ReadMode.GUIDED_READING) {
			updateHighlight();
		} else {
			p.editorPane.désurlignerTout();
			highlightPhrase(Constants.RIGHT_COLOR, p.player.getCurrentPhraseIndex());
		}
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

}
