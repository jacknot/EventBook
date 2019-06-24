package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class ShowWorkInProgress implements CommandInterface, NoParameters{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		String proposals = ctx.getSession().showInProgress();
		if(proposals.equals(""))
			ctx.getIOStream().write("Nessuna proposta in lavorazione!\n");
		else 
			ctx.getIOStream().write("Le proposte in lavorazione:\n" + ctx.getSession().showInProgress());
		return true;
	}

}
