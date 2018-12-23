package progetto.versione1;

/**La classe Campo ha il compito di fornire una struttura adatta a contenere una descrizione ed un valore
 * 
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 * Il valore che contiene puù essere di diversi tipi, per ogni campo questo deve essere specificato
 * @param <T> Il tipo di valore che un campo può contenere
 */
public class Campo <T>{
	//Attributi
	/**
	 * Contiene le informazioni generali di un campo
	 */
	private IntestazioneEspansa intestazione;
	/**
	 * Contiene il valore del campo
	 */
	private T valore;
	/**
	 * La stringa usata per mostrare le principali caratteristiche del campo
	 */
	private final static String STRINGA_TO_STRING ="%sValore: %s%n";
	
	//Costruttore
	/**Costruttore per la classe campo
	 * @param intestazione La descrizione del campo
	 * @param valore Il valore assunto dal campo
	 */
	public Campo(IntestazioneEspansa intestazione, T valore) {
		this.intestazione = intestazione;
		this.valore = valore;
	}
	
	/**Costruttore del campo, imposta il suo valore a null 
	 * @param intestazione La descrizione del campo
	 */
	public Campo(IntestazioneEspansa intestazione) {
		this.intestazione = intestazione;
		this.valore = null;
	}
	
	//Metodi
	/**Restituisce il nome del campo
	 * @return Il nome del campo
	 */
	public String getNome() {
		return intestazione.getNome();
	}
	/**Restituisce la descrizione del campo
	 * @return la descrizione del campo
	 */
	public String getDescrizione() {
		return intestazione.getDescrizione();
	}
	/**Restituisce se il campo è obbligatorio
	 * @return True -> è obbligatorio \n False -> non è obbligatorio
	 */
	public boolean isObbligatorio() {
		return intestazione.isObbligatorio();
	}
	/**Restituisce l'attuale valore del campo
	 * @return il valore attuale
	 */
	public T getValore() {
		return valore;
	}
	
	/**Imposta il valore del campo sovrascrivendo il suo vecchio valore
	 * @param valore Il nuovo valore del campo
	 */
	public void setValore(T valore) {
		this.valore = valore;
	}
	
	/**Controlla se non ï¿½ stato assegnato un valore al campo
	 * @return True -> non è ancora stato dato un valore al campo \nFalse -> è già stato dato un valore al campo
	 */
	public boolean isEmpty() {
		return valore == null;
	}
	/**Visualizza le caratteristiche del campo in forma testuale
	 * @return Le caratteristiche del campo
	 */
	public String visualizzaCaratteristiche() {
		return intestazione.toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(STRINGA_TO_STRING, intestazione.toString(), valore);
	}
}
