package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

/**
 * Questa interfaccia viene implementata dai comandi che necessitano di verificare se l'id di una proposta è contenuta in bacheca
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public interface SessionContainsID extends InsertID{
	
	/**
	 * Controlla se la Sessione corrente contiene una proposta con l'id specificato,
	 * in caso affermativo lo ritorna
	 * @param ctx Contesto
	 * @param args Argomenti
	 * @return l'id della proposta, -1 se non trovata.
	 */
	public default int sessionContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args, StringConstant.INSERT_NUMBER);
		if(!ctx.getSession().contains(id)) {
			ctx.getIOStream().writeln("Nessuna proposta in lavorazione con questo identificatore");
			id = -1;
		}
		return id;
	}

}
