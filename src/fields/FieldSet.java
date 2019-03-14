package fields;

import java.io.Serializable;
import java.util.ArrayList;

/**Definisce una struttura dati con il compito di gestire di diversi campi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class FieldSet extends ArrayList<Field<?>> implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String INTERLINE = "%n*******************************%n";
	
	/**
	 * Controlla se il contenitore contiene un campo
	 * @param name Il nome del campo da cercare
	 * @return True - se il contenitore contiene almeno un campo chiamo in questo modo <br>False - se il contenitore non contiene il campo
	 */
	public boolean contains(String name) {
		if( this.stream()
				.anyMatch((f)->f.getName().equalsIgnoreCase(name)))
			return true;
		return false;
	}
	/**Restituisce un campo il cui nome corrisponde a quello inserito
	 * @param name Il nome del campo che si vuole selezionare
	 * @return Il campo con il nome inserito, null altrimenti
	 */
	private Field<?> getField(String name) {
		Field<?> ris = this.stream()
				.filter((c)->c.getName().equals(name))
				.findFirst()
				.get();
		return ris;
	}
	
	/**
	 * Restituisce il contenuto del campo di cui si è inserito il nome
	 * @param name il nome del campo di cui si vuole il contenuto
	 * @return il suo contenuto ( null se non esiste campo con il nome inserito )
	 */
	public Object getValue(String name) {
		if(!contains(name))
			return null;
		return getField(name).getValue();
	}
	/**
	 * Restituisce il tipo del campo di cui si è inserito il nome
	 * @param name il nome del campo di cui si vuole il tipo
	 * @return il tipo del campo. Restituisce null se il campo non esiste
	 */
	public Class<?> getType(String name){
		if(!contains(name))
			return null;
		return getField(name).getType();		
	}
	/**
	 * Imposta il valore del campo di cui si è inserito il nome.<br>
	 * Se il nuovo valore non è del tipo appropriato non viene fatta alcuna modifica
	 * @param name il nome del campo di cui modificare il valore
	 * @param nValue il nuovo valore
	 * @return l'esito della modifica
	 */
	public boolean setValue(String name, Object nValue){
		if(contains(name)) 
			return getField(name).setValue(nValue);
		return false;
	}
	/**
	 * Controlla se un set di campi è valido ( i campi obbligatori non hanno valore nullo) 
	 * @return  True - se il set è valido<br>False - se il set non è valido
	 */
	public boolean isValid() {
		if( this.stream()
				.filter((c)->c.isBinding())
				.filter((c)->c.isEmpty())
				.count() > 0)
			return false;
		return true;
	}
	//Visione sottoforma di stringa del contenitore
	
	/**
	 * Restituisce le caratteristiche di tutti i campi contenuti in forma testuale
	 * @return La forma testuale di tutte le caratteristiche dei campi contenuti
	 */
	public String getFeatures() {
		StringBuilder sb = new StringBuilder();
		this.stream()
			.forEachOrdered((f)->sb.append(f.getFeatures()).append(String.format(INTERLINE)));
		return sb.toString();
	}
	
	/**
	 * Controlla se i due FieldSet sono uguali
	 * @param set FieldSet da confrontare
	 * @return True se tutti i valori sono uguali<br>False altrimenti
	 */
	public boolean equals(FieldSet set) {
		return ((this.isValid() && set.isValid()) && this.stream().filter((f) -> f.isBinding()).allMatch((f) -> f.getValue().equals(set.getValue(f.getName()))));
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.stream()
			.forEachOrdered((f)->sb.append(f.toString() + "\n"));
		return sb.toString();
	}
}
