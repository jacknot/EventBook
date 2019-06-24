package commandHandling.commandBody;

import commandHandling.Context;

/**
 * Questa interfaccia viene implementata dai comandi che non necessitano di parametri
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface NoParameters {
	/**
	 * Controlla che non ci sono parametri aggiunti
	 * @param args i parametri forniti al comando
	 * @param ctx il contesto su cui operare
	 * @param error string di errore in caso ci siano troppi parametri
	 * @return True - se non ci sono parametri aggiuntivi<br>False - se ci sono parametri aggiuntivi
	 */
	public default boolean check(String[] args, Context ctx, String error) {
		if(args.length != 0) {
			ctx.getIOStream().writeln(error);
			return false;
		}
		return true;
	}
}
