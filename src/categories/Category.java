package categories;

import fields.FieldHeading;
import fields.FieldSet;
import proposals.OptionsSet;

/**
 * Interfaccia che definisce il comportamento delle Categorie. 
 * Segue il principio DIP (Dependency Inversion Principle).
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface Category {
	/**
	 * Restituisce il nome della categoria di evento.
	 * @return nome della categoria
	 */
	public String getName();
	/**
	 * Modifica il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo
	 * @param nValue il nuovo valore del campo
	 * @return True - il campo è stato modificato<br>False - il campo non è stato modificato
	 */
	public boolean setValue(String name, Object nValue);
	/**
	 * Restituisce il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo di cui si vuole il valore
	 * @return Il valore del campo inserito. Restituisce null se il campo non esiste
	 */
	public Object getValue(String name);
	/**
	 * Controlla se l' evento è valido
	 * @return True - se l'evento è valido<br>False - se l'evento non è valido
	 */
	public boolean isValid();
	/**
	 * Controlla se il nome passato per parametro coincide con quello della categoria
	 * @param name nome da confrontare
	 * @return True se uguali<br>False altrimenti
	 */
	public boolean hasName(String name);
	/**
	 * Restituisce l'insieme delle varie opzioni disponibili sulla categoria
	 * @return il set delle varie opzioni
	 */
	public OptionsSet getOptions();
	/**
	 * Visualizza la struttura della categoria e di quello che può contenere (in forma testuale).<br>
	 * Non visualizza un loro eventuale contenuto.
	 * @return La struttura in forma testuale
	 */
	public String getFeatures();
	/**
	 * Rimuove dalla categoria un campo opzionale
	 * @param fh l'intestazione del campo opzionale da rimuovere
	 * @return True - il campo è stato rimosso con successo<br>False - il campo non è stato rimosso
	 */
	public boolean removeOptionalField(FieldHeading fh);
	/**
	 * Controlla se la categoria contiene il campo di cui si è inserito il nome
	 * @param field il nome del campo da controllare
	 * @return True - contiene il campo<br>False - non contiene il campo
	 */
	public boolean containsField(String field);
	
	public default boolean equals(Category otherCategory) {
		return this.hasName(otherCategory.getName()) && this.getFieldSet().equals(otherCategory.getFieldSet());
	}
	
	public FieldSet getFieldSet();
	
}
