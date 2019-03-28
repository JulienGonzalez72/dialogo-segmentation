import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class testTherapist {
	
	public static void main(String[] args) throws URISyntaxException {
		EventDispatcher ed = new EventDispatcher("ortho1", "Roro",false,args[0]);
		new LSTest(args,ed);
	}

}
