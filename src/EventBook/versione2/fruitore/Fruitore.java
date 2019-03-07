package EventBook.versione2.fruitore;

import java.io.Serializable;

/**La classe User ha il compito di fornire una struttura adatta a gestire un fruitore del social network.<br>
 * Ad ogni fruitore è associato, oltre al nome, uno spazio personale inizialmente vuoto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Fruitore implements Serializable, Notificabile{

	private String name;
	private SpazioPersonale privateSpace;
	
	/**
	 * Costruttore per la classe User
	 * @param name Il nome del fruitore
	 */
	public Fruitore(String name) {
		this.name = name;
		this.privateSpace = new SpazioPersonale();
	}
	
	/**
	 * Ritorna il nome del fruitore
	 * @return Il nome del fruitore
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Aggiunge un messaggio allo spazio personale
	 * @param message Il messaggio da aggiungere
	 */
	public void ricevi(Messaggio message) {
		privateSpace.add(message);
	}
	
	/**
	 * Rimuovere un messaggio presente nello spazio personale
	 * @param title il titolo del messaggio da rimuovere
	 */
	public void remove(String title) {
		privateSpace.remove(title);
	}
	
	/**
	 * Ritorna la lista dei messaggi contenuta nello spazio personale
	 * @return La lista dei messaggi contenuta nello spazio personale
	 */
	public String getPrivateSpace() {
		return privateSpace.toString();
	}
	
	/**
	 * Controlla se i due utenti sono uguali
	 * @param f utente da controllare
	 * @return true se uguali<br>false altrimenti
	 */
	public boolean equals(Fruitore f) {
		return this.name.equals(f.name);
	}
}
