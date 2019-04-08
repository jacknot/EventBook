package utility;

/**
 * Classe che contiene gran parte delle stringhe usate nelle varie parti del programma
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class StringConstant {
	public static final String NEW_LINE = "\n";
	public static final String EMPTY_STRING = "";
	public static final String INSERT_IDENTIFIER = "Inserisca l'identificatore : ";
	public static final String INSERT_NUMBER = "Il parametro deve essere un numero";
	
	public static final String SPECIFY_CATEGORY_NAME = "Specifichi il nome di una categoria";
	public static final String CATEGORY_NOT_FOUND = "Il nome inserito non appartiene ad una categoria esistente";
	
	public static final String TOO_PARAMETERS = "Sono stati inseriti parametri superflui";
	
	public static final String SAVE_COMPLETED = "completato";
	public static final String SAVE_FAILED = "fallito";
	public static final String WAITING = "> ";
	
	public static final String WELCOME = "Welcome to EventBook";

	public static final String ERROR_UNKNOWN_COMMAND = "Il comando inserito non è stato riconosciuto (inserisci 'help' per vedere i comandi a tua disposizione)";
	
	//Stringhe per State (e Message)
	public static final String UNKNOWN_TITLE = "(titolo mancante)";
	public static final String CONFIRMOBJ = "Conferma evento";
	public static final String FAILUREOBJ = "Fallimento evento";
	public static final String WITHDRAWNOBJ = "Ritiro evento";
	//data ora luogo importo
	public static final String CONFIRMFORMAT = "Siamo lieti di confermare che l'evento %s si terrà il giorno %s alle %s in %s."
													+ "\nPer la partecipazione sono %.2f€.";
	public static final String OPTIONAL_AMOUNT = "\nValutate le sue scelte relative alle voci opzionali si ricorda di "
													+ "portare un totale di %.2f€.";
	public static final String FAILUREFORMAT = "Siamo spiacenti di informarla che l'evento %s non ha raggiunto il numero minimo di iscritti."
													+ "\nL'evento è quindi annullato.";
	public static final String WITHDRAWNFORMAT = "Siamo spiacenti di informarla che l'evento %s è stato ritirato dal proprietario."
													+ "\nL'evento è quindi annullato.";
}
