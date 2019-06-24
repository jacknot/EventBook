package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

/**
 * Questa interfaccia viene implementata dai comandi che necessitano di verificare se l'id di una proposta è contenuta in bacheca
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public interface ProposalHandlerContainsID extends InsertID{

	/**
	 * Verifica se l'id di una proposta è contenuta in bacheca
	 * @param ctx il contesto su cui opera
	 * @param args gli argomenti del metodo
	 * @return l'id della proposta, -1 se non trovata.
	 */
	public default int proposalHandlerContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args, StringConstant.INSERT_NUMBER);
		if(!ctx.getProposalHandler().contains(id)) {
			ctx.getIOStream().writeln("La proposta di cui si è inserito l'identificatore non è presente");
			id = -1;
		}
		return id;
	}
}
