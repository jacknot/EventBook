package users;

import java.io.Serializable;

import ClassiPerV4.Profilo;
import fields.FieldHeading;

/**La classe User ha il compito di fornire una struttura adatta a gestire un fruitore del social network.<br>
 * Ad ogni fruitore è associato, oltre al nome, uno spazio personale inizialmente vuoto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Il nome del fruitore
	 */
	//Modifica e aggiunta per la versione 4
	//private String name;
	private Profilo profile;
	/**
	 * Lo spazio personale del fruitore
	 */
	private PersonalSpace privateSpace;
	
	/**
	 * Costruttore per la classe User
	 * @param name Il nome del fruitore
	 */
	public User(String name) {
		this.profile = new Profilo(name);
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
		//Modifica per la versione 4
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
	 * Modifica il valore del campo (del profilo) di cui si è inserito il nome
	 * @param name il nome del campo
	 * @param nValue il nuovo valore del campo
	 * @return True - il campo è stato modificato<br>False - il campo non è stato modificato
	 */
	public boolean modifyProfileField(String name, Object nValue) {
		return profile.setValue(name, nValue);
	}
	
	//Metodo aggiunto per la versione 4
	/**
	 * Visualizza il profilo dell'utente
	 * @return Stringa che rappresenta il profilo
	 */
	public String showProfile() {
		return profile.toString();
	}
	
	public boolean containsCategory(String categoryName) {
		return profile.containsCategory(categoryName);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		//Modifica per la versione 4 -> E' meglio mettere qua dentro showProfile()???
		return profile.getValue(FieldHeading.NOMIGNOLO.getName()).toString();
	}
}
