package users;

import java.io.Serializable;
import java.util.ArrayList;

import dataTypes.CategoriesInterest;
import fields.FieldHeading;
import fields.FieldSet;
import fields.FieldSetFactory;

/**La classe User ha il compito di fornire una struttura adatta a gestire il profilo di un fruitore del social network.<br>
 * Ad ogni profilo è associato un nomignolo, una fascia d'età ed un insieme di categorie di interesse<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Profile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Contiene i campi
	 */
	private FieldSet fields;
	
	private final static String TOSTRING_FORMAT = "Nomignolo: %s%nFascia d'età: %s%nCategorie d'interesse:%n%s%n"; 
	/**
	 * Costruttore
	 */
	public Profile(String name) {
		fields = FieldSetFactory.getInstance().getSet("Profile");
		fields.setValue(FieldHeading.NOMIGNOLO.getName(), name);
	}
	/**
	 * Restituisce il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo di cui si vuole il valore
	 * @return Il valore del campo inserito. Restituisce null se il campo non esiste
	 */
	public Object getValue(String name) {
		return fields.getValue(name);
	}
	
	private CategoriesInterest getCategories(){
		return (CategoriesInterest)fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName());
	}
	
	/**
	 * Modifica la lista di categoria di interesse dell'utente
	 * @param category categoria di riferimento
	 * @param add True se categoria è da aggiungere <br> False se da rimuovere
	 * @return True se operazione completata correttamente <br> False altrimenti
	 */
	public boolean modifyCategory(String category, boolean add) {
		CategoriesInterest cat = getCategories();
		if(add)
			cat.add(category);
		else 	cat.remove(category);
		return setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), cat);
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
	 * Verifica se tra le categorie di interesse dell'utente compare la categoria cercata
	 * @param categoryName nome della categoria
	 * @return True se presente <br> False altrimenti
	 */
	public boolean containsCategory(String categoryName) {
		ArrayList<String> cat = getCategories();
		for(String str: cat) {
			if(str.equals(categoryName)) return true;
		}
		return false;
	}
	
	/**
	 * Restituisce tutti i campi del Profilo
	 * @return campi del Profilo
	 */
	public FieldSet getFields() {
		return fields;
	}
	
	/**
	 * Verifica se l'utente è al primo accesso nella Piattaforma
	 * @return True se al primo accesso <br> False altrimenti
	 */
	public boolean isFirstAccess() {
		return fields.getValue(FieldHeading.FASCIA_ETA_UTENTE.getName())==null && fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName())==null;
	}
	
	/**
	 * Restituisce solo i campi modificabili del Profilo
	 * @return campi modificabili del Profilo
	 */
	public FieldSet getEditableFields() {
		FieldSet editableFields = FieldSetFactory.getInstance().getSet("Profile");		
		editableFields.remove(0); //Rimuove nomignolo
		return editableFields;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(TOSTRING_FORMAT, fields.getValue(FieldHeading.NOMIGNOLO.getName()), fields.getValue(FieldHeading.FASCIA_ETA_UTENTE.getName()), fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName()));
	}
}
