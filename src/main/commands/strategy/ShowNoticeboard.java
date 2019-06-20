package main.commands.strategy;

import main.commands.Context;
import utility.StringConstant;

public class ShowNoticeboard extends Commands{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#Commands(String, String, String)
	 */
	public ShowNoticeboard(String name, String description, String syntax) {
		super(name, description, syntax);
	}

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkNoParameter(ctx, args))
			return false;
		ctx.getProposalHandler().refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente utente vedrà la bacheca aggiornata
		String content = ctx.getProposalHandler().showContent();
		if(content.equals(""))
			ctx.getIOStream().write("Nessuna proposta in bacheca!\n");
		else 
			ctx.getIOStream().write("Le proposte in bacheca:\n" + content);
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
