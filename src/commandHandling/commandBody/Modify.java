package commandHandling.commandBody;

import java.util.stream.Stream;

import commandHandling.Context;
import fields.FieldHeading;

public class Modify implements CommandInterface, OneParameter, ValueRequired, SessionContainsID{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro", "Inserisca un solo parametro"))
			return false;
		
		boolean valid = false;
		int id = sessionContainsID(ctx, args);
		if(id<0)
			return false;
		
		FieldHeading field = FieldHeading.TITOLO;
		String newField = ctx.getIOStream().read("Inserisca il nome del campo da modificare : ");
		if(Stream.of(FieldHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
			field = Stream.of(FieldHeading.values())
					.filter((fh)->fh.getName().equalsIgnoreCase(newField))
					.findAny()
					.get();
		else {
			ctx.getIOStream().writeln("Il nome inserito non appartiene ad un campo");
			return false;
		}
		
		Object obj = null;
		obj = acceptValue(ctx, field, String.format("Inserisca il nuovo valore (%s) : ", field.getType().getSimpleName()));
		
		valid = false;
		do {
			ctx.getIOStream().writeln("E' sicuro di voler modificare ?");
			String newValue = "";
			if(obj!=null)
				newValue = obj.toString();
			String confirm = ctx.getIOStream()
									.read("Proposta : " + id + ", Campo : " + field.getName() + ", Nuovo valore: " + newValue 
											+ " [y|n]>");
			if(confirm.equalsIgnoreCase("n")) {
				valid = true;
				ctx.getIOStream().writeln("La modifica è stata annullata");
				return false;
			}else if(confirm.equalsIgnoreCase("y"))
				valid = true;
		}while(!valid);
		if(ctx.getSession().modifyProposal(id, field.getName(), obj)) {
			ctx.getIOStream().writeln("La modifica ha avuto successo");
			return true;
		}else {
			ctx.getIOStream().writeln("La modifica non ha avuto successo");
			return false;
		}
	}

}
