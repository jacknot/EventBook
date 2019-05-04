package categories;

import java.io.Serializable;

/**Descrive e genera set di oggetti in grado di contenere una descrizione
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */
public enum CategoryHeading implements Serializable{	

	FOOTBALLMATCH("Partita di Calcio", "Una partita di calcio tra vari generi e età"),
	CONCERT("Concerto Live", "Un Concerto musicale dal vivo");
	
	/**
	 * Definisce un formato per la visualizzazione sottoforma di stringa del contenuto dell'oggetto
	 */

	private final static String TOSTRING_FORMAT = "Nome: %s%nDescrizione: %s%n";
	
	/**
	 * Contiene un nome
	 */
	private String name;
	
	/**
	 * Contiene una descrizione
	 */
	private String description;
	
	/**Costruttore
	 * @param nome Il nome da contenere
	 * @param descrizione La descrizione da contenere
	 */
	private CategoryHeading(String nome, String descrizione) {
		this.name = nome;
		this.description = descrizione;
	}

	/**Restituisce il nome
	 * @return Il nome
	 */
	public String getName() {
		return name;
	}
	
	/**Restituisce la descrizione
	 * @return La descrizione
	 */
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return String.format(TOSTRING_FORMAT, name, description);
	}
}
