package main;

public class AnticipatedThread extends ReadThread {
	
	private ControlerGlobal controler;
	
	public AnticipatedThread(ControlerGlobal controler) {
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
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getWaitTime(N), Constants.CURSOR_SPEAK);
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du son ///
			controler.doWait(controler.getPhraseDuration(N), Constants.CURSOR_LISTEN);
			/// suppression du surlignage du segment de phrase N ///
			controler.removeHighlightPhrase(N);
			/// N=N+1 ///
			N++;
		}
	}

	@Override
	public void run(int N) {
	}
	
}
