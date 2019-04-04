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
	private PersonalSpace personalSpace;
	
	/**
	 * Costruttore
	 * @param name Il nome dell'utente
	 */
	public User(String name) {
		this.profile = new Profile(name);
		this.personalSpace = new PersonalSpace();
	}
	
	/**
	 * Ritorna il nome dell'utente
	 * @return Il nome dell'utente
	 */
	public String getName() {
		return profile.getValue(FieldHeading.NOMIGNOLO.getName()).toString();
	}
	
	/**
	 * Aggiunge un messaggio allo spazio personale
	 * @param message Il messaggio da aggiungere
	 */
	public void receive(Message message) {
		personalSpace.add(message);
	}
	
	/**
	 * Rimuove un messaggio 
	 * @param id l'identificatore del messaggio
	 * @return l'esito della rimozione
	 */
	public boolean removeMsg(int id) {
		return personalSpace.remove(id);
	}
	
	/**
	 * Controlla se i due utenti sono uguali
	 * @param f utente da confrontare
	 * @return True - se i due utenti sono uguali<br>False - altrimenti
	 */
	public boolean equals(User f) {
		return getName().equals(f.getName());
	}
	
	/**
	 * Restituisce il contenuto dello spazio personale dell'utente sotto forma di testo
	 * @return il contenuto dello spazio personale come testo
	 */
	public String showNotifications() {
		return this.personalSpace.toString();
	}
	
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
	 * Modifica il valore del campo di cui è passato il nome come parametro
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
	 * @param add True - se categoria è da aggiungere <br> False - se la categoria è da rimuovere
	 * @return True se l'operazione è stata completata con successo<br> False - altrimenti
	 */
	public boolean modifyCategory(String category, boolean add) {
		return profile.modifyCategory(category, add);
	}
	
	/**
	 * Verifica se tra le categorie di interesse dell'utente compare la categoria il cui nome è passato come parametro
	 * @param categoryName nome della categoria da verificare
	 * @return True - se la categoria è di interesse per l'utente<br> False - la categoria non è di interesse per l'utente
	 */
	public boolean containsCategory(String categoryName) {
		return profile.containsCategory(categoryName);
	}
	
	/**
	 * Verifica se lo spazio personale è vuoto
	 * @return True - se non è presente alcun messaggio<br> False - se lo spazio personale contiene dei messaggi
	 */
	public boolean noMessages() {
		return personalSpace.noMessages();
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return profile.getValue(FieldHeading.NOMIGNOLO.getName()).toString();
	}
}
