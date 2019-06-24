package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class PrivateSpaceOut implements CommandInterface, NoParameters{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		ctx.getIOStream().writeln("Ritorno in Home");
		return true;
	}
}
