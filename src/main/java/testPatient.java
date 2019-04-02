import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class testPatient {
	
	public static void main(String[] args) throws URISyntaxException {
		String ip = "82.234.201.61";
		if(args.length > 1) {
			ip = args[0];
		}
		EventDispatcher ed = new EventDispatcher("Roro", "ortho1",true,ip);
		new LSTest(args,ed);
	}

}
