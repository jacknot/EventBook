package commandHandling.commandBody;

import commandHandling.Context;
import users.User;
import utility.StringConstant;

public class WithdrawProposal implements CommandInterface{
	

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkOneParameter(ctx, args))
			return false;
		int id = proposalHandlerContainsID(ctx, args);
		if(id<0)
			return false;
		User owner = ctx.getSession().getOwner();
		if(!ctx.getProposalHandler().isOwner(id, owner)) {
			ctx.getIOStream().writeln("La proposta non è di sua proprietà");
			return false;
		}
		if(ctx.getProposalHandler().withdraw(id, owner)) {
			ctx.getIOStream().writeln("La proposta è stata ritirata con successo");
			return true;
		}
		ctx.getIOStream().writeln("Impossibile ritirare la proposta");
		return false;
	}
	
	/**
	 * Controlla se nella chiamata di un comando è stato passato un parametro
	 * @param ctx Contesto su cui operare
	 * @param args Parametri passati al comando
	 * @return True - Se è stato passato un unico parametro <br> False - altrimenti
	 */
	private static boolean checkOneParameter(Context ctx, String args[]) {
		if(args.length == 0) {
			ctx.getIOStream().writeln("Inserisca un parametro");
			return false;
		} else if(args.length > 1) {
			ctx.getIOStream().writeln("Inserisca un solo parametro");
			return false;
		}
		return true;
	}
	
	private static int getID(Context ctx, String[] args) {
		int id = -1;
		try {
			id = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
			id = -1;
		}
		return id;
	}
	
	private static int proposalHandlerContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args);
		if(!ctx.getProposalHandler().contains(id)) {
			ctx.getIOStream().writeln("La proposta di cui si è inserito l'identificatore non è presente");
			id = -1;
		}
		return id;
	}

}
