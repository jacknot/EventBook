package EventBook.fruitore;

import java.io.Serializable;

/**La classe Message ha il compito di fornire una struttura adatta a contenere un messaggio.<br>
 * Il messaggio è composto da tre attributi: mittente, oggetto e destinatario.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Messaggio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String addresser;
	private String object;
	private String description;
	
	private final static String FORMAT_TO_STRING ="<%s> %s : %s";
	
	
	/**
	 * Costruttore per la classe messaggio
	 * @param addresser Il mittente del messaggio
	 * @param object L'oggetto del messaggio
	 * @param description La descrizione del messaggio
	 */
	public Messaggio(String addresser, String object, String description) {
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
		return String.format(FORMAT_TO_STRING, addresser, object, description);
	}
	
}
