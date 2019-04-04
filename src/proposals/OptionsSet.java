package proposals;

import java.io.Serializable;
import java.util.HashMap;
import java.util.stream.Stream;

import fields.FieldHeading;

/**
 * Classe con il compito di tenere salvate le scelte di un utente a fronte di campi di natura opzionale
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class OptionsSet implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Contiene le coppie: intestazione+preferenza  
	 */
	private HashMap<FieldHeading, Boolean> choices;
	
	/**
	 * Costruttore
	 * @param headings la lista di intestazioni di campi opzionali relativi ai quali si vuole tenere traccia delle scelte dell'utente
	 */
	public OptionsSet(FieldHeading[] headings) {
		choices = new HashMap<FieldHeading, Boolean>();
		Stream.of(headings)
				.filter((h)->h.isOptional())
				.forEach((h)->choices.put(h, false));
	}
	/**
	 * Imposta la preferenza relativa al campo di cui si è inserita l'intestazione
	 * @param heading l'intestazione del campo opzionale
	 * @param choice la preferenza legata al campo
	 * @return True - se la preferenza è stata salvata con successo<br>False - se la preferenza non è stata salvata
	 */
	public boolean makeChoice(FieldHeading heading, boolean choice) {
		if(heading.isOptional() && choices.containsKey(heading)) {
			choices.put(heading, choice);
			return true;
		}
		return false;
	}
	/**
	 * Restituisce la preferenza legata ad un certo campo inserita dall'utente
	 * @param heading l'intestazione del campo opzionale di cui si vuole ottenere la preferenza
	 * @return la preferenza impostata dall'utente
	 * @throws IllegalArgumentException se si è inserita l'intestazione di un campo non opzionale
	 */
	public boolean getChoice(FieldHeading heading) throws IllegalArgumentException{
		if(heading.isOptional() && choices.containsKey(heading))
			return choices.get(heading);
		throw new IllegalArgumentException();	
	}
	/**
	 * Controlla se la preferenza contiene informazioni legate al campo di cui si è inserita l'intestazione
	 * @param heading l'intestazione del campo di cui controllare la presenza di informazioni
	 * @return True - la preferenza  contiene informazioni sul campo<br>False - la preferenza non contiene informazioni legate al campo
	 */
	public boolean contains(FieldHeading heading) {
		return choices.containsKey(heading);
	}
	/**
	 * Verifica se due preferenze sono uguali
	 * @param p la preferenza da confrontare
	 * @return True - le preferenze sono uguali<br>False - le preferenze sono diverse
	 */
	public boolean hasSameChoices(OptionsSet p) {
		return Stream.of(this.getOptions()).allMatch((k)->p.contains(k)) && 
				Stream.of(p.getOptions()).allMatch((k)->this.contains(k));
	}
	/**
	 * Restituisce le intestazioni dei campi sui quali è possibile fare scelte
	 * @return le intestazioni
	 */
	public FieldHeading[] getOptions() {
		return this.choices.keySet().toArray(new FieldHeading[0]);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return choices.toString();
	}
}