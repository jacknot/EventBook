package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class ShowWorkInProgress implements CommandInterface{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkNoParameter(ctx, args))
			return false;
		String proposals = ctx.getSession().showInProgress();
		if(proposals.equals(""))
			ctx.getIOStream().write("Nessuna proposta in lavorazione!\n");
		else 
			ctx.getIOStream().write("Le proposte in lavorazione:\n" + ctx.getSession().showInProgress());
		return true;
	}
	
	/**
	 * Controlla se nella chiamata di un comando non è stato passato nessun parametro
	 * @param ctx Contesto su cui operare
	 * @param args Parametri passati al comando
	 * @return True - Se non è stato passato nessun parametro <br> False - altrimenti
	 */
	private static boolean checkNoParameter(Context ctx, String args[]) {
		if(args.length != 0) {
			ctx.getIOStream().writeln(StringConstant.TOO_PARAMETERS);
			return false;
		}
		return true;
	}
}
