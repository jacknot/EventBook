package progetto.versione1;

/**Classe con il compito di definire il comportamento di una categoria
 * 
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public abstract class Categoria {
	//Attributi
	/**
	 * Contiene un insieme di campi
	 */
	private ContenitoreCampi campiComuni;
	
	//Costruttore
	/**
	 * Costruttore
	 */
	public Categoria() {
		campiComuni = ContenitoreCampi.generaCampiComuni();
	}
	/**Visualizza la struttura di quello che pu� contenere in forma testuale
	 * Non visualizza un loro eventuale contenuto
	 * @return La struttura in forma testuale
	 */
	public static String visualizzaCampi(){
		return ContenitoreCampi.visualizzaStruttura(ContenitoreCampi.generaCampiComuni());
	}
	
	//rpova
	
}
