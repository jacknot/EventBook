package EventBook.versione2;

import java.util.HashMap;

public class Protocollo extends HashMap<String, Runnable>{
	public static Protocollo init() {
		Protocollo p = new Protocollo();
		
		return p;
	}
}
