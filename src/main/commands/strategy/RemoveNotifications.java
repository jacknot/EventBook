package main.commands.strategy;

import main.commands.Context;
import utility.StringConstant;

public class RemoveNotifications extends Commands{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#Commands(String, String, String)
	 */
	public RemoveNotifications(String name, String description, String syntax) {
		super(name, description, syntax);
	}

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkOneParameter(ctx, args))
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

}
