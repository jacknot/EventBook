package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class RemoveNotifications implements CommandInterface, OneParameter{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro", "Inserisca un solo parametro"))
			return false;
		int id = -1;
		try {
			id = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
			return false;
		}
		if(!ctx.getSession().getOwner().removeMsg(id)) {
			ctx.getIOStream().writeln("La rimozione non è andata a buon fine");
			return false;
		}else {
			ctx.getIOStream().writeln("Rimossa correttamente");
			return true;
		}
	}

}
