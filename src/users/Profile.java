package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private final static String TOSTRING_FORMAT = "Nomignolo: %s%nFascia di età utente: %s%nCategorie di interesse:%n%s"; 
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
		else 	
			cat.remove(category);
		return setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), cat);
	}
	/**
	 * Modifica il valore del campo di cui si è inserito il nome
	 * @param name il nome del campo
	 * @param nValue il nuovo valore del campo
	 * @return True - il campo è stato modificato<br>False - il campo non è stato modificato
	 */
	public boolean setValue(String name, Object nValue) {
		if(!name.equals(FieldHeading.NOMIGNOLO.getName()))
			return fields.setValue(name, nValue);
		return false;
	}
	
	/**
	 * Verifica se tra le categorie di interesse dell'utente compare la categoria cercata
	 * @param categoryName nome della categoria
	 * @return True se presente <br> False altrimenti
	 */
	public boolean containsCategory(String categoryName) {
		ArrayList<String> cat = getCategories();
		if(cat != null) {
			for(String str: cat) {
				if(str.equals(categoryName)) return true;
			}
		}		
		return false;
	}
	/**
	 * Restituisce l'intestazione dei campi modificabili del profilo
	 * @return l'intestazione dei campi modificabili del Profilo
	 */
	public FieldHeading[] getEditableFields() {
		return Stream.of(FieldHeading.values())
				.filter(( fh )-> fields.contains(fh.getName()))
				.filter(( fh )-> !fh.equals(FieldHeading.NOMIGNOLO))
				.collect(Collectors.toCollection(ArrayList::new))
				.toArray(new FieldHeading[0]);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(TOSTRING_FORMAT, fields.getValue(FieldHeading.NOMIGNOLO.getName()), 
												fields.getValue(FieldHeading.FASCIA_ETA_UTENTE.getName()), 
												fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName())).toString();
	}
}
