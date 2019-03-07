package EventBook.campi;

import java.io.Serializable;

import EventBook.versione2.ClassType;

/**La classe Campo ha il compito di fornire una struttura adatta a contenere una descrizione ed un valore.<br>
 * Il valore che contiene può essere di diversi tipi, per ogni campo questo deve essere specificato.<br>
 * @param <T> Il tipo di valore che un campo può contenere.
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Field <T> implements Serializable{
	
	//Attributi
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Il tipo del campo
	 */
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
	 * @param type tipo del campo
	 * @param value Il valore assunto dal campo
	 */
	public Field(ExpandedHeading head, Class<T> type, T value) {//PROBABILMENTE NON SERVE
		this.heading = head;
		this.value = value;
		this.type = type;
	}
	
	/**Costruttore del campo, imposta il suo valore a null 
	 * @param head La descrizione del campo
	 */
	public Field(ExpandedHeading head) {
		this.heading = head;
		this.type = (Class<T>) head.getType(); //Siamo sicuri che il cast sia sicuro
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
	
	public ClassType getClassType(){
		return heading.getClassType();
	}
	
	/**Restituisce se il campo è obbligatorio
	 * @return True - è obbligatorio <br> False - non è obbligatorio
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
	 * @param nValue Il nuovo valore del campo
	 * @return l'esito della modifica
	 */
	public boolean setValue(Object nValue){
		try {
			this.value = type.cast(nValue);
			return true;
		}catch(ClassCastException e) {
			return false;
		}
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
		return String.format(FORMAT_TO_STRING, heading.getName(), type.getSimpleName(), value!=null?value.toString():"");
	}
	/**
	 * Controlla se i campi sono uguali
	 * @param f il campo da confrontare
	 * @return True - sono uguali<br>False - sono diversi
	 */
	public boolean equals(Field<?> f) {
		return heading.getName().equals(f.getName()) && type.equals(f.type) && value.equals(f.value);
	}
}
