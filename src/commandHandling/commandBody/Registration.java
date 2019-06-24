package commandHandling.commandBody;

import commandHandling.Context;
import java.util.stream.Stream;

import fields.FieldHeading;
import users.User;

public class Registration implements CommandInterface, OneParameter, ValueRequired{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca il nomignolo del nuovo utente", "Inserisca un solo nomignolo"))
	  		return false;
		if(!ctx.getDatabase().contains(args[0])){
	  		ctx.getDatabase().register(args[0]);
			ctx.getIOStream().writeln("L'utente è stato registrato con successo");
			ctx.getIOStream().writeln("Compilare, se si vuole, il proprio Profilo personale (si lasci il campo vuoto se non lo si vuole compilare):\n");
			User user = ctx.getDatabase().getUser(args[0]);
			FieldHeading[] fields = user.getEditableFields();
			Stream.of(fields)
					.forEach((fh)->{
						ctx.getIOStream().writeln(fh.toString());
						Object obj = acceptValue(ctx, fh, "Inserisca il valore del campo (" + fh.getType().getSimpleName()+ ") : ");
						if(user.setValue(fh.getName(), obj))
							ctx.getIOStream().writeln("\tDato inserito correttamente\n");
						else
							ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
					});
			return true;
	 	}else{
	  		ctx.getIOStream().writeln("Il nome inserito è già esistente");
	  		return false;
	  	}
	}

}
