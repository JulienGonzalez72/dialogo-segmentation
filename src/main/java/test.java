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
		EventDispatcher ed = new EventDispatcher("ortho1", "Roro",false,args[0]);
		new LSTest(args,ed);
		ed = new EventDispatcher("Roro", "ortho1",true,args[0]);
		new LSTest(args,ed);
	}

}
