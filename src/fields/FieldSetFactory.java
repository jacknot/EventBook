package fields;

import java.util.*;
import java.util.function.Supplier;

import categories.CategoryHeading;


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
		types.put(CategoryHeading.FOOTBALLMATCH.getName(), ()->{
			FieldSet cc = commonSet();
			cc.add(new Field<>(FieldHeading.GENERE));
			cc.add(new Field <>(FieldHeading.FASCIA_ETA));
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
	private FieldSet commonSet() {
		FieldSet cc = new FieldSet();
		cc.add(new Field <>(FieldHeading.TITOLO));
		cc.add(new Field <>(FieldHeading.NUMPARTECIPANTI));
		cc.add(new Field <>(FieldHeading.TERMINEISCRIZIONE));
		cc.add(new Field <>(FieldHeading.LUOGO));
		cc.add(new Field <>(FieldHeading.DATA));
		cc.add(new Field <>(FieldHeading.ORA));
		cc.add(new Field <>(FieldHeading.DURATA));
		cc.add(new Field <>(FieldHeading.QUOTA));
		cc.add(new Field <>(FieldHeading.COMPRESO_QUOTA));
		cc.add(new Field <>(FieldHeading.DATAFINE));
		cc.add(new Field <>(FieldHeading.ORAFINE));
		cc.add(new Field <>(FieldHeading.NOTE));
		cc.add(new Field <>(FieldHeading.TOLL_PARTECIPANTI));
		cc.add(new Field <>(FieldHeading.TERMINE_RITIRO));
		return cc;
	}
	/**Restituisce un contenitore di campi a seconda del tipo inserito in ingresso
	 * @param type il tipo di contenitore da generare
	 * @return il contenitore di campi del tipo desiderato (null se il tipo inserito non è contemplato)
	 */
	public FieldSet getSet(String type){
		if(!types.containsKey(type))
			return null;
		return types.get(type).get();
	} 
	//End Factory
}
