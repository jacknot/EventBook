package ClassiPerV4;

import java.text.DateFormat.Field;
import java.util.ArrayList;

import fields.FieldHeading;
import fields.FieldSet;
import fields.FieldSetFactory;

/**La classe User ha il compito di fornire una struttura adatta a gestire il profilo di un fruitore del social network.<br>
 * Ad ogni profilo è associato un nomignolo, una fascia d'età ed un insieme di categorie di interesse<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Profilo {
	/**
	 * Contiene i campi
	 */
	protected FieldSet fields; //è da tenere protected?
	
	private final static String TOSTRING_FORMAT = "Nomignolo: %s%nFascia d'età: %s%nCategorie d'interesse:%n%s%n"; 
	/**
	 * Costruttore
	 */
	public Profilo(String name) {
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
	
	private ArrayList<String> getCategories(){
		return (ArrayList<String>)fields.getValue(FieldHeading.CATEGORIE_INTERESSE.getName());
	}
	public boolean modifyCategory(String category, boolean add) {
		ArrayList<String> cat = getCategories();
		if(add)
			cat.add(category);
		else 	cat.remove(category);
		return fields.setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), cat);
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
	
	public boolean containsCategory(String categoryName) {
		ArrayList<String> cat = getCategories();
		for(String str: cat) {
			if(str.equals(categoryName)) return true;
		}
		return false;
	}
	
	public String toString() {
		/*StringBuilder sb = new StringBuilder();
		sb.append(fields.toString());*/
		//CAMBIA
		return String.format(TOSTRING_FORMAT, fields.get(0).getValue(), fields.get(1).getValue(), fields.get(2).getValue());
	}
}
