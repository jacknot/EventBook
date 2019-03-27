package users;

import fields.FieldHeading;
import proposals.Preferenze;

/**La classe Subscriber ha il compito di fornire una struttura adatta a gestire un iscritto ad una proposta.<br>
 * Ogni oggetto SUbscriber associa ad un User, l'insieme delle Preferenze relative alla proposta al quale è iscritto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Subscriber {
	/**
	 * L'utente iscritto
	 */
	private User user;
	/**
	 * Le preferenze dell'iscritto
	 */
	private Preferenze preferenze;
	
	/**
	 * Costruttore per la classe Subscriber
	 * @param user l'User iscritto
	 * @param preferenze l'insieme delle Preferenze dell'User
	 */
	public Subscriber(User user, Preferenze preferenze) {
		this.user = user;
		this.preferenze = preferenze;
	}
	
	/**
	 * Ritorna l'User
	 * @return l'User
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Ritorna le Preferenze
	 * @return le Preferenze
	 */
	public Preferenze getPreferenze() {
		return preferenze;
	}
	
	//Metodi che servono dall'utente
	
	/**
	 * Ritorna il nome dell'User
	 * @return Il nome dell'User
	 */
	public String getName() {
		//Modifica per la versione 4
		return user.getName();
	}
	
	/**
	 * Aggiunge un messaggio allo spazio personale dell'User
	 * @param message Il messaggio da aggiungere
	 */
	public void receive(Message message) {
		user.receive(message);
	}
	
	/**
	 * Controlla se i due Subscriber sono uguali
	 * @param s Subscriber da controllare
	 * @return true se uguali<br>false altrimenti
	 */
	public boolean equals(Subscriber s) {
		return getName().equals(s.getName());
	}
	
	//Metodi che servono
	
	/**
	 * Restituisce la preferenza legata ad un certo campo inserita dall'utente
	 * @param h l'intestazione del campo opzionale di cui si vuole ottenere la preferenza
	 * @return la preferenza impostata dall'utente
	 */
	public boolean getPreferenza(FieldHeading h) throws IllegalArgumentException{
		return preferenze.getPreferenza(h);
	}
	
	/**
	 * Controlla se la preferenza contiene informazioni legate al campo di cui si è inserita l'intestazione
	 * @param h l'intestazione del campo di cui controllare la presenza di informazioni
	 * @return True - la preferenza  contiene informazioni sul campo<br>False - la preferenza non contiene informazioni legate al campo
	 */
	public boolean contains(FieldHeading h) {
		return preferenze.contains(h);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return user.toString();
	}
}
