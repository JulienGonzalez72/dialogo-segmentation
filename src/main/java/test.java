import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class test {
	
	/*
	 * Probl�me : la fen�tre P ne fait pas la m�me taille que la fen�tre T
	 * Cause peu probable : la taille de l'�cran P et de l'�cran T est diff�rente
	 * Cause probable : swing utilise diff�rents �l�ments selon l'ordinateur
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
