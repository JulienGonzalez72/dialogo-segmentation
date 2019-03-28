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
		EventDispatcher ed = new EventDispatcher("ortho1", "Roro",false,args[0]);
		new LSTest(args,ed);
		ed = new EventDispatcher("Roro", "ortho1",true,args[0]);
		new LSTest(args,ed);
	}

}
