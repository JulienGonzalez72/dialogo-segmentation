package main;

public class GuidedThread extends ReadThread {
	
	private ControlerGlobal controler;
	
	public GuidedThread(ControlerGlobal controler) {
		this.controler = controler;
	}
	
	public void run() {
		/// mapage et mise en page de la totalit� du texte ///
		controler.rebuildPages();
		/// N = num�ro de segment initial ///
		//int N = FenetreParametre.premierSegment - 1;
		//run(N);
		/// tant que N <= nombre de segments du texte ///
		while (N < controler.getPhrasesCount() - 1) {
			/// affichage de la page correspondant au segment N ///
			controler.showPage(controler.getPageOfPhrase(N));
			/// surlignage du segment N ///
			controler.highlightPhrase(Constants.RIGHT_COLOR, N);
			/// play du son correspondant au segment N ///
			controler.play(N);
			/// attente de la fin du son ///
			controler.doWait(controler.getPhraseDuration(N), Constants.CURSOR_LISTEN);
			/// attente de la fin du temps de pause ///
			controler.doWait(controler.getWaitTime(N), Constants.CURSOR_SPEAK);
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
