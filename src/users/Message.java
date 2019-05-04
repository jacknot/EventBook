package users;

import java.io.Serializable;

/**La classe Message ha il compito di fornire una struttura adatta a contenere un messaggio.<br>
 * Il messaggio è composto da tre attributi: mittente, oggetto e destinatario.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final static String TOSTRING_FORMAT ="Mittente: %s\nOggetto: %s\nCorpo: %s";
	
	/**
	 * Il mittente
	 */
	private String addresser;
	/**
	 * L'oggetto del messaggio
	 */
	private String object;
	/**
	 * La descrizione del messaggio
	 */
	private String description;
	
	/**
	 * Costruttore per la classe messaggio
	 * @param addresser Il mittente del messaggio
	 * @param object L'oggetto del messaggio
	 * @param description La descrizione del messaggio
	 */
	public Message(String addresser, String object, String description) {
		this.addresser = addresser;
		this.object = object;
		this.description = description;
	}
	
	/**
	 * Restituisce il mittente del messaggio
	 * @return Il mittente del messaggio
	 */
	public String getAddresser() {
		return addresser;
	}
	
	/**
	 * Restituisce l'oggetto del messaggio
	 * @return L'oggetto del messaggio
	 */
	public String getObject() {
		return object;
	}
	/**Imposta l'oggetto del messaggio
	 * @param str l'oggetto del messaggio
	 */
	public void setObject(String str) {
		object = str;
	}
	/**
	 * Restituisce la descrizione del messaggio
	 * @return La descrizione del messaggio
	 */
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(TOSTRING_FORMAT, addresser, object, description);
	}
	
}
