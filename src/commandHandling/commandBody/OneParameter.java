package commandHandling.commandBody;

import commandHandling.Context;

/**
 * Questa interfaccia viene implementata da tutti i comandi che necessitano di un parametro
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface OneParameter {
	
	/**
	 * Controlla che ci sia un solo parametro
	 * @param args gli argomenti forniti al comando
	 * @param ctx il contesto su cui opera
	 * @param errorZero stringa di errore quando ci sono zero parametri
	 * @param errorMultiple stringa di errore quando ci sono troppi parametri 
	 * @return True - se c'è un solo parametro<br>False - se non c'è un singolo parametro
	 */
	public default boolean check(String[] args, Context ctx, String errorZero, String errorMultiple) {
		if(args.length == 0) {
			ctx.getIOStream().writeln(errorZero);
			return false;
		} else if(args.length > 1) {
			ctx.getIOStream().writeln(errorMultiple);
			return false;
		}
		return true;
	}
}
