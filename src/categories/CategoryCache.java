package categories;

import java.util.ArrayList;

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
	 * array contenente le istanze delle categorie da clonare
	 */
	private static ArrayList<Category> set = new ArrayList<Category>();
	
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
		set.add(new FootballMatch());
	}
	
	/**Restituisce il clone di una particolare istanza di categoria
	 * @param type l'intestazione della categoria di cui si vuole l'istanza
	 * @return l'istanza della categoria di cui si è inserito il nome
	 */
	public Category getCategory(String type) {
		Category cached = set.stream()
						.filter((c)->c.equals(type))
						.findFirst().get();
		return (Category) cached.clone();
	}
}
