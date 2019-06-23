package main.commands.strategy;

import main.commands.Context;
import users.User;
import utility.StringConstant;

public class Unsubscribe implements CommandInterface{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkNoParameter(ctx, args))
			return false;
		ctx.getProposalHandler().refresh();
		User actualUser = ctx.getSession().getOwner();
		if(ctx.getProposalHandler().countUserSubscription(actualUser) == 0) {
			ctx.getIOStream().writeln("Non è iscritto ad alcuna proposta");
			return false;
		}
		ctx.getIOStream().writeln(ctx.getProposalHandler().showUserSubscription(actualUser));
		int id = -1;
		try {
			id = Integer.parseInt(ctx.getIOStream().read(StringConstant.INSERT_IDENTIFIER));
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
			return false;
		}
		if(ctx.getProposalHandler().isOwner(id, actualUser)) {
			ctx.getIOStream().writeln("E' il propositore, non può disiscriversi.");
			return false;
		}
		if(!ctx.getProposalHandler().isSignedUp(id, actualUser)) {
			ctx.getIOStream().writeln("Non è iscritto a questa proposta");
			return false;
		}
		if(ctx.getProposalHandler().unsubscribe(id , actualUser)) {
			ctx.getIOStream().writeln("La disiscrizione è andata a buon fine");
			return true;
		}else {
			ctx.getIOStream().writeln("Impossibile disicriversi dalla proposta");
			return false;
		}
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
