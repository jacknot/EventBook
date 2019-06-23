package main.commands.strategy;

import java.util.stream.Stream;

import fields.FieldHeading;
import main.commands.Context;
import users.User;
import utility.StringConstant;

public class Registration implements CommandInterface{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(args.length == 0){
	 		ctx.getIOStream().writeln("Inserisca il nomignolo del nuovo utente");
	  		return false;
	  	}else if(!ctx.getDatabase().contains(args[0])){
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
	
	/**
	 * Richiede all'utente di inserire un valore e ne verifica la validità in base al campo
	 * @param ctx il contesto su cui si deve operare
	 * @param field campo su cui verificare la validità del dato
	 * @param message richiesta all'utente di inserire il dato
	 * @return Oggetto correttamente elaborato in base al campo
	 */
	private static Object acceptValue(Context ctx, FieldHeading field, String message) {
		boolean valid = false;
		Object obj = null;
		do {
			String value = ctx.getIOStream().read("\t" + message);
			if(!field.isBinding() && !field.isOptional() && value.isEmpty())
				valid = true;
			ctx.getIOStream().write(StringConstant.NEW_LINE);
			if(field.getClassType().isValidType(value)) {
				obj = field.getClassType().parse(value);
				valid = true;
			}
			if(!valid)
				ctx.getIOStream().writeln("\tIl valore inserito non è corretto.\n\tInserisca qualcosa del tipo: " + field.getClassType().getSyntax());
		}while(!valid);
		return obj;
	}

}
