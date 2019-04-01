package fields;

import java.io.Serializable;

import dataTypes.ClassType;

/**La classe Campo ha il compito di fornire una struttura adatta a contenere una descrizione ed un valore.<br>
 * Il valore che contiene può essere di diversi tipi, per ogni campo questo deve essere specificato.<br>
 * @param <T> Il tipo di valore che un campo può contenere.
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Field <T> implements Serializable{
	
	//Attributi
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contiene le informazioni generali di un campo (nome, descrizione, obbligatorietà, opzionalità, tipo)
	 */
	private FieldHeading heading;
	
	/**
	 * Contiene il valore del campo
	 */
	private T value;
	
	/**
	 * Stringa usata per mostrare le principali caratteristiche del campo
	 */
	private final static String FORMAT_TO_STRING ="%s <%s> : %s";
	
	//Costruttori
	
	/**Costruttore del campo, imposta il suo valore a null 
	 * @param head La descrizione del campo
	 */
	public Field(FieldHeading head) {
		this.heading = head;
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
		//Il cast non genera problemi dato che stiamo dando un tipo consistente con l'intestazione del campo ( head )
		return (Class<T>) heading.getType();
	}
	
	/**
	 * Restituisce il tipo del campo
	 * @return il tipo del campo
	 */
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
			this.value = getType().cast(nValue);
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
		return String.format(FORMAT_TO_STRING, heading.getName(), getType().getSimpleName(), value!=null?value.toString():"");
	}
	/**
	 * Controlla se i campi sono uguali
	 * @param f il campo da confrontare
	 * @return True - sono uguali<br>False - sono diversi
	 */
	public boolean equals(Field<?> f) {
		return heading.getName().equals(f.getName()) && getType().equals(f.getType()) && value.equals(f.value);
	}
	
	/**
	 * Imposta il valore del campo ad un valore di default
	 */
	public void reset() {
		value = null;
	}
}
