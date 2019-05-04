package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dataTypes.CategoriesOfInterest;
import fields.FieldHeading;
import fields.FieldSet;
import fields.FieldSetFactory;

/**La classe Profile ha il compito di fornire una struttura adatta a gestire il profilo di un fruitore del social network.<br>
 * Ad ogni profilo è associato un nomignolo, una fascia d'età ed un insieme di categorie di interesse<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Profile implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Contiene i campi
	 */
	private FieldSet fields;
	/**
	 * Stringa usata per mostrare le caratteristiche del profilo
	 */
	private final static String TOSTRING_FORMAT = "Nomignolo: %s%nFascia di età utente: %s%nCategorie di interesse:%n%s"; 
	/**
	 * Costruttore
	 * @param name nome del profilo
	 */
	public Profile(String name) {
		fields = FieldSetFactory.getInstance().getSet("Profile");
		fields.setValue(FieldHeading.NOMIGNOLO.getName(), name);
	}
	/**
	 * Restituisce il valore del campo il cui nome è passato come parametro
	 * @param name il nome del campo di cui si vuole il valore
	 * @return Il valore del campo inserito. Restituisce null se il campo non esiste
	 */
	public Object getValue(String name) {
		return fields.getValue(name);
	}
	
	/**
	 * Restituisce l'elenco delle categorie d'interesse contenute nel profilo
	 * @return elenco delle categorie di interesse
	 */
	private CategoriesOfInterest getCategories(){
		return (CategoriesOfInterest)fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName());
	}
	
	/**
	 * Modifica (aggiunge/rimuove) una categoria alla lista delle categorie di interesse dell'utente
	 * @param category nome della categoria da modificare
	 * @param add True - si vuole aggiungere la categoria<br> False - si vuole rimuovere la categoria
	 * @return True - se l'operazione è stata completata correttamente <br> False - altrimenti
	 */
	public boolean modifyCategory(String category, boolean add) {
		CategoriesOfInterest cat = getCategories();
		if(cat == null)
			cat = new CategoriesOfInterest();
		if(add)
			cat.add(category);
		else 	
			cat.remove(category);
		return setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), cat);
	}
	/**
	 * Modifica il valore del campo di cui si è passatp il nome come parametro
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
	 * Verifica se tra le categorie di interesse dell'utente compare la categoria il cui nome è passato come argomento
	 * @param categoryName nome della categoria
	 * @return True - la categoria è di interesse per l'utente<br> False - altrimenti
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
