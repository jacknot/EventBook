package users;


/**
 * Quest'interfaccia definisce il comportamento di oggetti in grado di ricevere un messaggio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface Notifiable {
	/**
	 * L'oggetto che implementa questa interfaccia deve poter gestire la ricezione di un messaggio
	 * @param msg il messaggio che deve ricevere
	 */
	public void receive(Message msg);
}
