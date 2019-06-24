package commandHandling.commandBody;

import commandHandling.Context;
import users.User;

public class WithdrawProposal implements CommandInterface, OneParameter, ProposalHandlerContainsID{
	

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro","Inserisca un solo parametro"))
			return false;
		int id = proposalHandlerContainsID(ctx, args);
		if(id < 0)
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

}
