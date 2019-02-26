package EventBook.versione2.stati;

import EventBook.versione2.*;
import EventBook.versione2.fruitore.*;

/**
 * Il comportamento della proposta quando questa è considerata fallita
 * @author Matteo
 *
 */
public class Fallito implements Stato{
	
	/* (non-Javadoc)
	 * @see EventBook.versione2.Stato#update(EventBook.versione2.Proposta)
	 */
	public void update(Proposta p) {
		p.send(new Messaggio());
	}
}
