package users;

import java.io.Serializable;

import fields.FieldHeading;
import proposals.OptionsSet;

/**La classe Subscriber ha il compito di fornire una struttura adatta a gestire un iscritto ad una proposta.<br>
 * Ogni oggetto Subscriber associa ad un User, l'insieme delle Preferenze relative alla proposta al quale è iscritto<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Subscriber implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * L'utente iscritto
	 */
	private User user;
	/**
	 * Le preferenze dell'iscritto
	 */
	private OptionsSet choices;
	
	/**
	 * Costruttore per la classe Subscriber
	 * @param user l'User iscritto
	 * @param choices l'insieme delle scelte fatte dall'utente
	 */
	public Subscriber(User user, OptionsSet choices) {
		this.user = user;
		this.choices = choices;
	}
	/**
	 * Ritorna l'utente a cui la classe fa riferimento
	 * @return l'utente di riferimento
	 */
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Ritorna le scelte fatte dall'utente
	 * @return le scelte fatte dall'utente
	 */
	public OptionsSet getChoices() {
		return choices;
	}
	/**
	 * Ritorna il nome dell'utente
	 * @return Il nome dell'utente
	 */
	public String getName() {
		return user.getName();
	}
	
	/**
	 * Aggiunge un messaggio allo spazio personale dell'utente
	 * @param message Il messaggio da aggiungere
	 */
	public void receive(Message message) {
		user.receive(message);
	}
	
	/**
	 * Controlla se i due Subscriber siano uguali
	 * @param s Subscriber da confrontare
	 * @return True - se uguali<br>False  - altrimenti
	 */
	public boolean equals(Subscriber s) {
		return getName().equals(s.getName());
	}

	/**
	 * Restituisce la scelta legata ad un certo campo fatta dall'utente
	 * @param heading l'intestazione del campo opzionale di cui si vuole ottenere la scelta
	 * @return la scelta dell'utente
	 * @throws IllegalArgumentException se il campo inserito non fa parte delle opzioni disponibili dall'utente
	 */
	public boolean getChoice(FieldHeading heading) throws IllegalArgumentException{
		return choices.getChoice(heading);
	}
	
	/**
	 * Controlla se si può fare una scelta sul campo di cui si è inserita l'intestazione
	 * @param heading l'intestazione del campo da controllare
	 * @return True - possono essere fatte scelte sul campo<br>False - non si possono effettuare scelte sul campo inserito
	 */
	public boolean contains(FieldHeading heading) {
		return choices.contains(heading);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return user.toString();
	}
}
