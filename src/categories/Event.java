package categories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fields.Field;
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
public abstract class Event implements Category,Cloneable,Serializable{

	//Attributi
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contiene i campi
	 */
	private FieldSet fields;
	/**
	 * Contiene l'intestazione della categoria
	 */
	private EventHeading heading;	
	
	/**
	 * Costruttore che istanzia la lista di campi 'fields' con i campi comuni
	 */
	public Event(){
		fields = new FieldSetFactory().commonSet();
	}
	
	//Metodi
	/* (non-Javadoc)
	 * @see categories.Category#getFeatures()
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
	/* (non-Javadoc)
	 * @see categories.Category#getName()
	 */
	public String getName() {
		return heading.getName();
	}
	/* (non-Javadoc)
	 * @see categories.Category#setValue(java.lang.String, java.lang.Object)
	 */
	public boolean setValue(String name, Object nValue) {
		return fields.setValue(name, nValue);
	}
	/* (non-Javadoc)
	 * @see categories.Category#getValue(java.lang.String)
	 */
	public Object getValue(String name) {
		toDefault();
		return fields.getValue(name);
	}
	/* (non-Javadoc)
	 * @see categories.Category#containsField(java.lang.String)
	 */
	public boolean containsField(String field) {
		return fields.contains(field);
	}
	/* (non-Javadoc)
	 * @see categories.Category#isValid()
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
	 * Aggiunge i campi specifici all'insieme di campi comuni della Categoria
	 */
	public abstract void addSpecificFields();
	
	/**
	 * Aggiunge un nuovo campo alla categoria
	 * @param newField Intestazione del nuovo campo da aggiungere
	 */
	public void addField(FieldHeading newField) {
		fields.add(new Field <> (newField));
	}
	
	/**
	 * Imposta l'intestazione della categoria
	 * @param newHeading Intestazione della categoria
	 */
	public void setHeading(EventHeading newHeading) {
		heading = newHeading;
	}
	
	/**
	 * Reimposta l'insieme di campi ad un insieme di default
	 */
	public void resetSet() {
		fields = new FieldSetFactory().commonSet();
	}
	/**
	 * Controlla se le due categorie sono uguali
	 * @param c categoria da confrontare
	 * @return True se uguali<br>False altrimenti
	 */
	public boolean equals(Event c) {
		toDefault();
		return (this.heading.getName().equals(c.heading.getName()) && this.fields.equals(c.fields));
	}
	/* (non-Javadoc)
	 * @see categories.Category#hasName(java.lang.String)
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
	public Event reset() {
		fields.reset();
		return this;
	}
	/* (non-Javadoc)
	 * @see categories.Category#removeOptionalField(fields.FieldHeading)
	 */
	public boolean removeOptionalField(FieldHeading fh) {
		if(fh.isOptional()) {
			return fields.remove(fh.getName());
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see categories.Category#getOptions()
	 */
	public OptionsSet getOptions() {
		return new OptionsSet(Stream.of(FieldHeading.values())
									.filter((fh)->fh.isOptional())
									.filter((fh)->this.containsField(fh.getName()))
									.collect(Collectors.toCollection(ArrayList::new))
									.toArray(new FieldHeading[0])
							);
	}
	
	/*
	 * (non-Javadoc)
	 * @see categories.Category#getFieldSet()
	 */
	public FieldSet getFieldSet() {
		return fields;
	}
	
}
