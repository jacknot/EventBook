package commandHandling.commandBody;

import commandHandling.Context;

/**
 * Questa interfaccia viene implementata dai comandi che necessitano l'inserimento di un ID
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface InsertID {
	
	/**
	 * Elabora il primo argomento con l'obiettivo di ottenere un dato
	 * @param ctx il contesto su cui opera
	 * @param args gli argomenti del metodo
	 * @param err la stringa di errore
	 * @return il valore della traduzione
	 */
	public default int getID(Context ctx, String[] args, String err) {
		int id = -1;
		try {
			id = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(err);
			id = -1;
		}
		return id;
	}
	
}
