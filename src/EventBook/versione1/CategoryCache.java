package EventBook.versione1;

import java.util.Hashtable;

/**Classe che consente di implementare il Design Pattern Prototype per ottenere istanze di categorie da un set predefinito.<br>
 * Implementa il Design Pattern Singleton per non avere istanze rindondanti.<br>
 * Da modificare in caso di aggiunta di una nuova categoria.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class CategoryCache {
	
	/**
	 * Istanza necessaria per poter implementare il Design Pattern Singleton
	 */
	private static CategoryCache instance;
	/**
	 * Set contenente le istanze delle categorie da clonare
	 */
	private static Hashtable<String, Category> set = new Hashtable<String, Category>();
	
	/**Metodo per ottenere l'istanza della classe<br>
	 * Necessaria per il Design Pattern Singleton
	 * @return l'istanza della classe
	 */
	public static CategoryCache getInstance() {
		if(instance == null)
			instance = new CategoryCache();
		return instance;
	}
	/**
	 * Costruttore
	 */
	private CategoryCache() {
		loadCache();
	}
	/**
	 * Carica le istanze predefinite da clonare
	 */
	private static void loadCache() {
		FootballMatch p = new FootballMatch();
		set.put(Heading.PARTITADICALCIO.getName(), p);
	}
	
	/**Restituisce il clone di una particolare istanza di categoria
	 * @param name il nome della categoria di cui si vuole l'istanza
	 * @return l'istanza della categoria di cui si � inserito il nome
	 */
	public Category getCategory(String name) {
		Category cached = set.get(name);
		return (Category) cached.clone();
	}
}