package users;

import java.io.Serializable;

import fields.FieldHeading;

/**La classe User ha il compito di fornire una struttura adatta a gestire un fruitore del social network.<br>
 * Ad ogni fruitore è associato, oltre al nome, uno spazio personale inizialmente vuoto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Il profilo legato al fruitore
	 */
	private Profile profile;
	/**
	 * Lo spazio personale del fruitore
	 */
	private PersonalSpace privateSpace;
	
	/**
	 * Costruttore per la classe User
	 * @param name Il nome del fruitore
	 */
	public User(String name) {
		this.profile = new Profile(name);
		this.privateSpace = new PersonalSpace();
	}
	
	/**
	 * Ritorna il nome del fruitore
	 * @return Il nome del fruitore
	 */
	public String getName() {
		//Modifica per la versione 4
		return profile.getValue(FieldHeading.NOMIGNOLO.getName()).toString();
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
		return getName().equals(f.getName());
	}
	/**
	 * Restituisce il contenuto dello spazio personale dell'utente sotto forma di testo
	 * @return il contenuto dello spazio personale come testo
	 */
	public String showNotifications() {
		return this.privateSpace.toString();
	}
	
	//Metodo aggiunto per la versione 4
	/**
	 * Visualizza il profilo dell'utente
	 * @return Stringa che rappresenta il profilo
	 */
	public String showProfile() {
		return profile.toString();
	}
	
	/**
	 * Restituisce l'intestazione dei campi modificabili del Profilo
	 * @return l'intestazione dei campi modificabili del Profilo
	 */
	public FieldHeading[] getEditableFields() {
		return profile.getEditableFields();
	}
	
	/**
	 * Modifica il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo
	 * @param nValue il nuovo valore del campo
	 * @return True - il campo è stato modificato<br>False - il campo non è stato modificato
	 */
	public boolean setValue(String name, Object nValue) {
		return profile.setValue(name, nValue);
	}
	
	/**
	 * Modifica la lista di categoria di interesse dell'utente
	 * @param category categoria di riferimento
	 * @param add True se categoria è da aggiungere <br> False se da rimuovere
	 * @return True se operazione completata correttamente <br> False altrimenti
	 */
	public boolean modifyCategory(String category, boolean add) {
		return profile.modifyCategory(category, add);
	}
	
	/**
	 * Verifica se tra le categorie di interesse dell'utente compare la categoria cercata
	 * @param categoryName nome della categoria
	 * @return True se presente <br> False altrimenti
	 */
	public boolean containsCategory(String categoryName) {
		return profile.containsCategory(categoryName);
	}
	
	/**
	 * Verifica la presenza o meno di messaggi nello spazio personale
	 * @return True se nessun messaggio presente <br> False altrimenti
	 */
	public boolean noMessages() {
		return privateSpace.noMessages();
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		//Modifica per la versione 4 -> E' meglio mettere qua dentro showProfile()???
		return profile.getValue(FieldHeading.NOMIGNOLO.getName()).toString();
	}
}
