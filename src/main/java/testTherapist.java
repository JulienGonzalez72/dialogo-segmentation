import java.net.URISyntaxException;

import fr.lexidia.dialogo.dispatcher.EventDispatcher;
import fr.lexidia.dialogo.main.LSTest;

public class testTherapist {
	
	public static void main(String[] args) throws URISyntaxException {
		String ip = "82.234.201.61";
		if(args.length > 0) {
			ip = args[0];
		}
		EventDispatcher ed = new EventDispatcher("ortho1", "Roro",false,ip);
		new LSTest(args,ed);
	}

}
