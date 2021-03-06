package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class Exit implements CommandInterface, NoParameters{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		System.exit(0);
		return true;
	}
}
