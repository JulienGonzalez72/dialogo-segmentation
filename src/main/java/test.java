import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class test {
	
	/*
	 * Problème : la fenêtre P ne fait pas la même taille que la fenêtre T
	 * Cause peu probable : la taille de l'écran P et de l'écran T est différente
	 * Cause probable : swing utilise différents éléments selon l'ordinateur
	 * 
	 */
	
	public static void main(String[] args) throws URISyntaxException {
		String ip = "82.234.201.61";
		if(args.length > 1) {
			ip = args[0];
		}
	    EventDispatcher ed = new EventDispatcher("ortho1", "Roro",false,ip);
		new LSTest(args,ed);
		ed = new EventDispatcher("Roro", "ortho1",true,ip);
		new LSTest(args,ed);	
	}

}
