package progetto.versione1;

/**Descrive e genera set di oggetti in grado di contenere una descrizione
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */
public enum Intestazione {	

	PARTITADICALCIO("Partita di Calcio", "Una partita di calcio tra vari generi e età");
	
	/**
	 * Definisce un formato per la visualizzazione sottoforma di stringa del contenuto dell'oggetto
	 */

	private final static String STRINGA_TO_STRING = "Nome: %s%nDescrizione: %s%n";
	
	/**
	 * Contiene un nome
	 */
	private String nome;
	/**
	 * Contiene una descrizione
	 */
	private String descrizione;
	
	/**Costruttore
	 * @param nome Il nome da contenere
	 * @param descrizione La descrizione da contenere
	 */
	private Intestazione(String nome, String descrizione) {
		this.nome = nome;
		this.descrizione = descrizione;
	}

	/**Restituisce il nome
	 * @return Il nome
	 */
	public String getNome() {
		return nome;
	}
	
	/**Restituisce la descrizione
	 * @return La descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return String.format(STRINGA_TO_STRING, nome, descrizione);
	}
}
