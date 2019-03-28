package proposals;

import java.io.Serializable;
import java.util.HashMap;
import java.util.stream.Stream;

import fields.FieldHeading;

/**
 * Classe con il compito di tenere salvate le scelte di un utente a fronte di campi di natura opzionale
 * @author Matteo
 *
 */
public class Preferenze implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Contiene le coppie: intestazione+preferenza  
	 */
	private HashMap<FieldHeading, Boolean> preferenze;
	
	/**
	 * Costruttore
	 * @param intestazioni la lista di intestazioni di campi opzionali relativi ai quali si vuole tenere traccia delle scelte dell'utente
	 */
	public Preferenze(FieldHeading[] intestazioni) {
		preferenze = new HashMap<FieldHeading, Boolean>();
		Stream.of(intestazioni)
				.filter((h)->h.isOptional())
				.forEach((h)->preferenze.put(h, false));
	}
	/**
	 * Imposta la preferenza relativa al campo di cui si è inserita l'intestazione
	 * @param h l'intestazione del campo opzionale
	 * @param preferenza la preferenza legata al campo
	 * @return True - se la preferenza è stata salvata con successo<br>False - se la preferenza non è stata salvata
	 */
	public boolean impostaPreferenza(FieldHeading h, boolean preferenza) {
		if(h.isOptional() && preferenze.containsKey(h)) {
			preferenze.put(h, preferenza);
			return true;
		}
		return false;
	}
	/**
	 * Restituisce la preferenza legata ad un certo campo inserita dall'utente
	 * @param h l'intestazione del campo opzionale di cui si vuole ottenere la preferenza
	 * @return la preferenza impostata dall'utente
	 * @throws IllegalArgumentException se si è inserita l'intestazione di un campo non opzionale
	 */
	public boolean getPreferenza(FieldHeading h) throws IllegalArgumentException{
		if(h.isOptional() && preferenze.containsKey(h))
			return preferenze.get(h);
		throw new IllegalArgumentException();	
	}
	/**
	 * Controlla se la preferenza contiene informazioni legate al campo di cui si è inserita l'intestazione
	 * @param h l'intestazione del campo di cui controllare la presenza di informazioni
	 * @return True - la preferenza  contiene informazioni sul campo<br>False - la preferenza non contiene informazioni legate al campo
	 */
	public boolean contains(FieldHeading h) {
		return preferenze.containsKey(h);
	}
	/**
	 * Verifica se due preferenze sono uguali
	 * @param p la preferenza da confrontare
	 * @return True - le preferenze sono uguali<br>False - le preferenze sono diverse
	 */
	public boolean sameChoices(Preferenze p) {
		return Stream.of(this.getChoices()).allMatch((k)->p.contains(k)) && 
				Stream.of(p.getChoices()).allMatch((k)->this.contains(k));
	}
	/**
	 * Restitusice le intestazioni dei campi sui quali è possibile fare scelte
	 * @return le intestazioni
	 */
	public FieldHeading[] getChoices() {
		return this.preferenze.keySet().toArray(new FieldHeading[0]);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return preferenze.toString();
	}
}
