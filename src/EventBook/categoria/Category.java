package EventBook.categoria;

import java.io.Serializable;
import java.time.LocalDate;

import EventBook.campi.ExpandedHeading;
import EventBook.campi.FieldSet;


/**
 * Classe con il compito di definire il comportamento di una categoria.<br>
 * Implementa l'interfaccia Cloneable per poter consentire l'utilizzo del Design Pattern Prototype.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public abstract class Category implements Cloneable,Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contiene i campi
	 */
	protected FieldSet fields;
	/**
	 * Contiene l'intestazione della categoria
	 */
	protected Heading heading;
	
	//metodi
	
	/**
	 * Visualizza la struttura di quello che pu� contenere in forma testuale.<br>
	 * Non visualizza un loro eventuale contenuto.
	 * @return La struttura in forma testuale
	 */
	public String getFeatures() {
		return fields.getFeatures();
	}
	/**
	 * Restituisce la descrizione della categoria di evento.
	 * @return la descrizione della categoria
	 */
	public String getDescription() {
		return heading.toString();
	}
	/**
	 * Modifica il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo
	 * @param nValue il nuovo valore del campo
	 */
	public boolean setValue(String name, Object nValue) {
		return fields.setValue(name, nValue);
	}
	/**
	 * Restituisce il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo di cui si vuole il valore
	 * @return Il valore del campo inserito. Restituisce null se il campo non esiste
	 */
	public Object getValue(String name) {
		return fields.getValue(name);
	}
	/**
	 * Controlla se la categoria contiene il campo di cui si è inserito il nome
	 * @param field il nome del campo da controllare
	 * @return True - contiene il campo<br>False - non contiene il campo
	 */
	public boolean containsField(String field) {
		return fields.contains(field);
	}
	/**
	 * Controlla se un evento è valido
	 * @return True - se l'evento è valido<br>False - se l'evento non è valido
	 */
	public boolean isValid() {
		//controllo sulla sequenzialità delle date
		return fields.isValid() && ((LocalDate)fields.getValue(ExpandedHeading.DATA.getName()))
										.compareTo((LocalDate)fields.getValue(ExpandedHeading.TERMINEISCRIZIONE.getName())) >= 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	/**
	 * Controlla se le due categorie sono uguali
	 * @param c categoria da confrontare
	 * @return True se uguali<br>False altrimenti
	 */
	public boolean equals(Category c) {
		return (this.heading.getName().equals(c.heading.getName()) && this.fields.equals(c.fields));
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%n", heading.getName()));
		sb.append(fields.toString());
		return sb.toString();
	}
}
