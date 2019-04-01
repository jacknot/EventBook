package utility;

/**
 * Contiene tutte le stringhe costanti usate
 * @author Matteo
 *
 */
public class StringConstant {
	public static final String NEW_LINE = "\n";
	public static final String EMPTY_STRING = "";
	public static final String INSERT_IDENTIFIER = "Inserisci l'identificatore : ";
	public static final String INSERT_NUMBER = "Inserisci un numero";
	
	public static final String SAVE_COMPLETED = "completato";
	public static final String SAVE_FAILED = "fallito";
	public static final String WAITING = "> ";
	
	public static final String WELCOME = "Welcome to EventBook";
	public static final String EXITMSG = "Bye Bye";

	public static final String ERROR_UNKNOWN_COMMAND = "Il comando inserito non è stato riconosciuto ('help' per i comandi a disposizione)";
	
	//Stringhe per State (e Message)
	public static final String UNKNOWN_TITLE = "(titolo mancante)";
	public static final String CONFIRMOBJ = "Conferma evento";
	public static final String FAILUREOBJ = "Fallimento evento";
	public static final String WITHDRAWNOBJ = "Ritiro evento";
	//data ora luogo importo
	public static final String CONFIRMFORMAT = "Siamo lieti di confermare che l'evento %s si terrà il giorno %s alle %s in %s."
													+ "\nPer la partecipazione sono %f€.";
	public static final String FAILUREFORMAT = "Siamo spiacenti di informarla che l'evento %s non ha raggiunto il numero minimo di iscritti."
													+ "\nL'evento è quindi annullato.";
	public static final String WITHDRAWNFORMAT = "Siamo spiacenti di informarla che l'evento %s è stato ritirato dal proprietario."
			+ "\nL'evento è quindi annullato.";
}
