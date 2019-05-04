package utility;

/**
 * Interfaccia funzionale con il compito di definire la metodologia di estrazione di un oggetto a partire da una stringa
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 * @param <T> Il tipo di dato che si vuole ottenere dalla stringa che si sta elaborando
 */
public interface Parser<T> {
	
	/**
	 * Il metodo che deve essere completato per poter analizzare la stringa
	 * @param value la stringa da analizzare
	 * @return il tipo di dato voluto
	 */
	public T parse(String value);
	
}