package EventBook.versione1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;
import EventBook.versione1.campi.*;

/**Implementazione Design Pattern Factory per generare diversi tipi di contenitore di campi a seconda delle esigenze.<br>
 * Implementa anche il Design Pattern Singleton per evitare di avere istanze inutili della classe. <br>
 * Da modificare in caso di introduzione di una nuova categoria.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class FieldSetFactory {
	

	/**
	 * Istanza della classe necessaria per il design pattern singleton
	 */
	private static FieldSetFactory instance= null;
	/**
	 * Contiene associazioni Stringa - contenitori di campi specifici
	 */
	private HashMap<String, Supplier<FieldSet>> types;
	
	//Design Pattern Singleton
	/**
	 * Costruttore della classe
	 */
	private FieldSetFactory() {
		types = new HashMap<String, Supplier<FieldSet>>();
		types.put(Heading.PARTITADICALCIO.getName(), ()->{
			FieldSet cc = contenitoreComune();
			cc.add(new Field<>(ExpandedHeading.GENERE, String.class));
			cc.add(new Field <>(ExpandedHeading.FASCIADIETA, Interval.class));
			return cc;
		});
	}
	/**Metodo per ottenere un'istanza della factory di contenitori<br>
	 * Necessario per l'implementazione del design pattern singleton
	 * @return l'istanza della factory di contenitori
	 */
	public static FieldSetFactory getInstance() {
		if(instance == null)
			instance = new FieldSetFactory();
		return instance;
	}
	//End Singleton
	
	//Design Pattern Factory
	/**Semplifica la creazione di contenitori di campi contenenti campi standard
	 * @return un contenitore con i campi standard
	 */
	private FieldSet contenitoreComune() {
		FieldSet cc = new FieldSet();
		cc.add(new Field <>(ExpandedHeading.TITOLO, String.class));
		cc.add(new Field <>(ExpandedHeading.NUMEROPARTECIPANTI, Integer.class));
		cc.add(new Field <>(ExpandedHeading.TERMINEISCRIZIONE, LocalDate.class));
		cc.add(new Field <>(ExpandedHeading.LUOGO, String.class));
		cc.add(new Field <>(ExpandedHeading.DATA, LocalDate.class));
		cc.add(new Field <>(ExpandedHeading.ORA, LocalTime.class));
		cc.add(new Field <>(ExpandedHeading.DURATA, Integer.class));
		cc.add(new Field <>(ExpandedHeading.QUOTAINDIVIDUALE, Double.class));
		cc.add(new Field <>(ExpandedHeading.COMPRESONELLAQUOTA, String.class));
		cc.add(new Field <>(ExpandedHeading.DATACONCLUSIVA, LocalDate.class));
		cc.add(new Field <>(ExpandedHeading.ORACONCLUSIVA, LocalTime.class));
		cc.add(new Field <>(ExpandedHeading.NOTE, String.class));
		return cc;
	}
	/**Restituisce un contenitore di campi a seconda del tipo inserito in ingresso
	 * @param tipo il tipo di contenitore da generare
	 * @return il contenitore di campi del tipo desiderato (null se il tipo inserito non è contemplato)
	 */
	public FieldSet getContenitore(String tipo){
		if(!types.containsKey(tipo))
			return null;
		return types.get(tipo).get();
	} 
	//End Factory
}
