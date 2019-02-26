package EventBook.versione1.campi;

/**La classe Campo ha il compito di fornire una struttura adatta a contenere una descrizione ed un valore.<br>
 * Il valore che contiene può essere di diversi tipi, per ogni campo questo deve essere specificato.<br>
 * @param <T> Il tipo di valore che un campo può contenere.
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Field <T>{
	
	//Attributi
	
	private final Class<T> type;
	/**
	 * Contiene le informazioni generali di un campo
	 */
	private ExpandedHeading heading;
	/**
	 * Contiene il valore del campo
	 */
	private T value;
	/**
	 * La stringa usata per mostrare le principali caratteristiche del campo
	 */
	private final static String FORMAT_TO_STRING ="%s <%s> : %s%n";
	
	//Costruttori
	
	/**Costruttore per la classe campo
	 * @param head La descrizione del campo
	 * @param value Il valore assunto dal campo
	 */
	public Field(ExpandedHeading head, Class<T> type, T value) {
		this.heading = head;
		this.value = value;
		this.type = type;
	}
	
	/**Costruttore del campo, imposta il suo valore a null 
	 * @param head La descrizione del campo
	 */
	public Field(ExpandedHeading head, Class<T> type) {
		this.heading = head;
		this.type = type;
		this.value = null;
	}
	
	//Metodi
	
	/**Restituisce il nome del campo
	 * @return Il nome del campo
	 */
	public String getName() {
		return heading.getName();
	}
	/**Restituisce il tipo con il quale il campo è stato parametrizzato
	 * @return il tipo di dati che il campo può contenere
	 */
	public Class<T> getType(){
		return type;
	}
	/**Restituisce se il campo è obbligatorio
	 * @return True - è obbligatorio \n False - non è obbligatorio
	 */
	public boolean isBinding() {
		return heading.isBinding();
	}
	/**Restituisce l'attuale valore del campo
	 * @return il valore attuale
	 */
	public T getValue() {
		return value;
	}
	/**Imposta il valore del campo sovrascrivendo il suo vecchio valore.<br>
	 * Se il tipo del valore non è appropriato il vecchio valore non viene aggiornato
	 * @param nValue Il nuovo valore del campo
	 */
	public void setValue(Object nValue){
		if(type.isAssignableFrom(nValue.getClass()))
			this.value = type.cast(nValue);	
	}
	/**Controlla se non è stato assegnato un valore al campo
	 * @return True - non è ancora stato dato un valore al campo<br>False - è già stato dato un valore al campo
	 */
	public boolean isEmpty() {
		return value == null;
	}
	
	//visione del campo sotto forma di stringa
	
	/**Visualizza le caratteristiche del campo in forma testuale
	 * @return Le caratteristiche del campo
	 */
	public String getFeatures() {
		return heading.toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(FORMAT_TO_STRING, heading.getName(), type.getName(), value!=null?value.toString():"");
	}
}
