package main;

public class GuidedThread extends Thread {
	
	private ControlerGlobal controler;
	
	public GuidedThread(ControlerGlobal controler) {
		this.controler = controler;
	}
	
	public void run() {
		/// mapage et mise en page de la totalité du texte ///
		controler.rebuildPages();
		/// N = numéro de segment initial ///
		int N = FenetreParametre.premierSegment - 1;
		/// tant que N <= nombre de segments du texte ///
		while (N < controler.getPhrasesCount() - 1) {
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// surlignage du segment N ///
			controler.highlightPhrase(Constants.RIGHT_COLOR, N);
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du son ///
			try {
				Thread.sleep(controler.getPhraseDuration(N));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			/// attente de la fin du temps de pause ///
			try {
				Thread.sleep(controler.getWaitTime(N));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/// suppression du surlignage du segment de phrase N ///
			controler.removeHighlightPhrase(N);
			N++;
		}
	}
	
}
