package EventBook.versione2.fruitore;

import EventBook.versione2.fruitore.Messaggio;

/**
 * Quest'interfaccia definisce il comportamento di oggetti in grado di ricevere un messaggio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface Notificabile {
	/**
	 * L'oggetto che implementa questa interfaccia deve poter gestire la ricezione di un messaggio
	 * @param msg
	 */
	public void ricevi(Messaggio msg);
}
