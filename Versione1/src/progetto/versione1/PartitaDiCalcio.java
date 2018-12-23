package progetto.versione1;

/**Classe con il compito di istanziare eventi legati ad una partita di calcio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class PartitaDiCalcio extends Categoria{
	
	private static final Intestazione INTESTAZIONE = Intestazione.PARTITADICALCIO;
	
	/**
	 * Contiene i campi legati esclusivamente alla partita di calcio
	 */
	private ContenitoreCampi campiSpecifici;
	
	public PartitaDiCalcio() {
		super();
		campiSpecifici = ContenitoreCampi.generaCampiSpecifici(INTESTAZIONE.getNome());
	}
	
	public static String visualizzaDescrizione() {
		return INTESTAZIONE.toString();
	}
	
	/**Visualizza la struttura dei suoi campi
	 * Non visualizza il loro eventuale contenuto
	 * @return La struttura dei suoi campi
	 */
	public static String visualizzaCampi() {
		StringBuilder sb = new StringBuilder();
		//eventuale visualizzazione intestazione
		sb.append(Categoria.visualizzaCampi());
		sb.append(ContenitoreCampi.visualizzaStruttura(ContenitoreCampi.generaCampiSpecifici(INTESTAZIONE.getNome())));
		return sb.toString();
	}
}
