package commandHandling.commandBody;

import commandHandling.Context;

public class Login implements CommandInterface, OneParameter{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca il nomignolo di un utente già registrato", "Inserisca un solo nomignolo"))
			return false;
		if(ctx.getDatabase().contains(args[0])){
	  		ctx.newSession(args[0]);
	  		ctx.getIOStream().writeln("Accesso eseguito come: " + args[0]);
	  		return true;
	 	}else{
	  		ctx.getIOStream().writeln("Utente non registrato");
	  		return false;
	  	}
	}
}
