package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class ShowNotifications implements CommandInterface, NoParameters{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		ctx.getProposalHandler().refresh();
		if(ctx.getSession().noMessages()) 
			ctx.getIOStream().writeln("Nessun messaggio.");
		else
			ctx.getIOStream().writeln(ctx.getSession().showNotification());
		return true;
	}

}
