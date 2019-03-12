package users;

import java.io.Serializable;

/**La classe User ha il compito di fornire una struttura adatta a gestire un fruitore del social network.<br>
 * Ad ogni fruitore è associato, oltre al nome, uno spazio personale inizialmente vuoto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class User implements Serializable, Notifiable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Il nome del fruitore
	 */
	private String name;
	/**
	 * Lo spazio personale del fruitore
	 */
	private PersonalSpace privateSpace;
	
	/**
	 * Costruttore per la classe User
	 * @param name Il nome del fruitore
	 */
	public User(String name) {
		this.name = name;
		this.privateSpace = new PersonalSpace();
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
	public void receive(Message message) {
		privateSpace.add(message);
	}
	
	/**
	 * Rimuove un messaggio 
	 * @param index l'id del messaggio
	 * @return l'esito della rimizione
	 */
	public boolean removeMsg(int index) {
		return privateSpace.remove(index);
	}
	/**
	 * Controlla se i due utenti sono uguali
	 * @param f utente da controllare
	 * @return true se uguali<br>false altrimenti
	 */
	public boolean equals(User f) {
		return this.name.equals(f.name);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
	/**
	 * Restituisce lo spazio personale dell'utente attualmente connesso
	 * @return lo spazio personale dell'utente
	 */
	public PersonalSpace getPrivateSpace() {
		return this.privateSpace;
	}
}
