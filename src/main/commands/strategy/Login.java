package main.commands.strategy;

import main.commands.Context;

public class Login extends Commands{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#Commands(String, String, String)
	 */
	public Login(String name, String description, String syntax) {
		super(name, description, syntax);
	}

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(args.length == 0){
	 		ctx.getIOStream().writeln("Inserisca il nomignolo di un utente già registrato");
	  		return false;
	  	}else if(ctx.getDatabase().contains(args[0])){
	  		ctx.newSession(args[0]);
	  		ctx.getIOStream().writeln("Accesso eseguito come: " + args[0]);
	  		return true;
	 	}else{
	  		ctx.getIOStream().writeln("Utente non registrato");
	  		return false;
	  	}
	}

}
