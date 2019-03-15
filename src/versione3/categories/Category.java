package versione3.categories;

import java.io.Serializable;
import java.time.LocalDate;

import fields.FieldHeading;
import fields.FieldSet;


/**
 * Classe con il compito di definire il comportamento di una categoria.<br>
 * Implementa l'interfaccia Cloneable per poter consentire l'utilizzo del Design Pattern Prototype.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public abstract class Category implements Cloneable,Serializable{
	
	//Attributi
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contiene i campi
	 */
	protected FieldSet fields;
	/**
	 * Contiene l'intestazione della categoria
	 */
	protected CategoryHeading heading;
	
	//Metodi
	
	/**
	 * Visualizza la struttura della categoria e di quello che può contenere (in forma testuale).<br>
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
	 * @return True - il campo è stato modificato<br>False - il campo non è stato modificato
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
		return fields.isValid() && ((LocalDate)fields.getValue(FieldHeading.DATA.getName()))
										.compareTo((LocalDate)fields.getValue(FieldHeading.TERMINEISCRIZIONE.getName())) >= 0;
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
	/**
	 * Controlla se il nome passato per parametro coincide con quello della categoria
	 * @param name nome da confrontare
	 * @return True se uguali<br>False altrimenti
	 */
	public boolean equals(String name) {
		return (this.heading.getName().equals(name));
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
