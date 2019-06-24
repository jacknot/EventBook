package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class PrivateSpaceIn implements CommandInterface, NoParameters{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		ctx.getProposalHandler().refresh();
		ctx.getIOStream().writeln("Accesso completato allo spazio personale ('help' per i comandi a disposizione)");
		return true;
	}


}
