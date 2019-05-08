package categories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fields.FieldHeading;
import fields.FieldSet;
import fields.FieldSetFactory;
import proposals.OptionsSet;

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
	
	/**
	 * Costruttore che istanzia la lista di campi 'fields' con i campi comuni
	 */
	Category(){
		fields = FieldSetFactory.getInstance().commonSet();
	}
	
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
	 * Restituisce il nome della categoria di evento.
	 * @return nome della categoria
	 */
	public String getName() {
		return heading.getName();
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
		toDefault();
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
	 * Controlla se l' evento è valido
	 * @return True - se l'evento è valido<br>False - se l'evento non è valido
	 */
	public boolean isValid() {
		toDefault();
		//contenuto è valido
		if(fields.isValid()
				//la data è seguente o uguale al termine ultimo iscrizione
				&& (((LocalDate)fields.getValue(FieldHeading.DATA.getName()))
										.compareTo((LocalDate)fields.getValue(FieldHeading.TERMINEISCRIZIONE.getName())) >= 0) 
				//il termine ultimo iscrizione segue o eguaglia il termine di ritiro
				&& (((LocalDate)fields.getValue(FieldHeading.TERMINEISCRIZIONE.getName()))
										.compareTo((LocalDate)fields.getValue(FieldHeading.TERMINE_RITIRO.getName())) >= 0)
				//Se presente il termine ultimo di ritiro è minimo il giorno corrente
				&& (((LocalDate)fields.getValue(FieldHeading.TERMINE_RITIRO.getName()))
										.compareTo(LocalDate.now()) >= 0)
				&& (((Double)fields.getValue(FieldHeading.QUOTA.getName())) >= 0)
				&& (fields.getValue(FieldHeading.DATAFINE.getName())==null?true:
						(((LocalDate)fields.getValue(FieldHeading.DATAFINE.getName()))
												.compareTo((LocalDate)fields.getValue(FieldHeading.DATA.getName())) >= 0))
				//pre-impostato dal metodo di reset
				&& (((Integer)fields.getValue(FieldHeading.TOLL_PARTECIPANTI.getName())) >= 0)
				//non ha senso un evento in cui si richiede un solo partecipante
				&& (((Integer)fields.getValue(FieldHeading.NUMPARTECIPANTI.getName())) > 1)
				&& Stream.of(FieldHeading.values())
							.filter((fh)->fh.isOptional())
							.filter((fh)->containsField(fh.getName()))
							//i campi opzionali devono essere compilati dal propositore
							.allMatch((fh)-> fields.getValue(fh.getName())==null?false:(((Double)fields.getValue(fh.getName())) >= 0))
				)
			return true;
		return false;
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
		toDefault();
		return (this.heading.getName().equals(c.heading.getName()) && this.fields.equals(c.fields));
	}
	/**
	 * Controlla se il nome passato per parametro coincide con quello della categoria
	 * @param name nome da confrontare
	 * @return True se uguali<br>False altrimenti
	 */
	public boolean hasName(String name) {
		return (this.heading.getName().equals(name));
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%n", heading.getName()));
		toDefault();
		sb.append(fields.toString());
		return sb.toString();
	}
	/**
	 * Porta l'evento in una condizione di default
	 */
	public void toDefault() {
		if(fields.getValue(FieldHeading.TERMINE_RITIRO.getName()) == null && 
				fields.getValue(FieldHeading.TERMINEISCRIZIONE.getName()) != null)
			fields.setValue(FieldHeading.TERMINE_RITIRO.getName(), ((LocalDate)fields.getValue(FieldHeading.TERMINEISCRIZIONE.getName())));
		if(fields.getValue(FieldHeading.TOLL_PARTECIPANTI.getName()) == null)
			fields.setValue(FieldHeading.TOLL_PARTECIPANTI.getName(), 0);
	}
	
	/**
	 * Resetta tutti i campi della categoria
	 * @return la categoria sulla quale si è eseguito il reset
	 */
	public Category reset() {
		fields.reset();
		return this;
	}
	/**
	 * Rimuove dalla categoria un campo opzionale
	 * @param fh l'intestazione del campo opzionale da rimuovere
	 * @return True - il campo è stato rimosso con successo<br>False - il campo non è stato rimosso
	 */
	public boolean removeOptionalField(FieldHeading fh) {
		if(fh.isOptional()) {
			return fields.remove(fh.getName());
		}
		return false;
	}
	/**
	 * Restituisce l'insieme delle varie opzioni disponibili sulla categoria
	 * @return il set delle varie opzioni
	 */
	public OptionsSet getOptions() {
		return new OptionsSet(Stream.of(FieldHeading.values())
									.filter((fh)->fh.isOptional())
									.filter((fh)->this.containsField(fh.getName()))
									.collect(Collectors.toCollection(ArrayList::new))
									.toArray(new FieldHeading[0])
							);
	}
}
