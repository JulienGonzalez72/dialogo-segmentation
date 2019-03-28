import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class testPatient {
	
	public static void main(String[] args) throws URISyntaxException {
		EventDispatcher ed = new EventDispatcher("Roro", "ortho1",true,args[0]);
		new LSTest(args,ed);
	}

}
