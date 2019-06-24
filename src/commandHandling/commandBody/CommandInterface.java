package commandHandling.commandBody;

import commandHandling.Context;

/**
 * Interfaccia funzionale con il compito di definire il comportamento dei comandi attraverso l'utilizzo di parametri
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public interface CommandInterface {
	
	/**
	 * Il metodo che deve essere completato per eseguire un comando specificato dall'utente, dati eventuali parametri
	 * @param args parametri del comando
	 * @param context il contesto nel quale deve agire il comando
	 * @return l'esito del comando
	 */
	public boolean run(String[] args, Context context);
	
	/**
	 * Restituisce il numero di argomenti passati al comando
	 * @param args gli argomenti passati al comando
	 * @return il numero di argomenti passati
	 */
	public default int numberOfArguments(String[] args) {
		return args.length;
	}
	
	/**
	 * Gestisce gli argomenti forniti al comando
	 * @param args gli argomenti forniti al comando
	 * @param context il contesto sul quale deve operare
	 * @return True - se gli argomenti inseriti sono nel numero corretto<br>False - se gli argomenti inseriti non sono nel numero corretto
	 */
	public boolean handleArgumentsNumber(String[] args, Context context);
}
