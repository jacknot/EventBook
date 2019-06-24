package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;
import java.util.stream.Stream;
import categories.EventHeading;
import fields.FieldHeading;

public class ModifyProfile implements CommandInterface, NoParameters, ValueRequired{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		FieldHeading[] fields = ctx.getSession().getOwner().getEditableFields();
		FieldHeading field = FieldHeading.TITOLO;
		String newField = ctx.getIOStream().read("Inserisca il nome del campo che vuole modificare : ");
		if(!Stream.of(fields).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField))) {
			ctx.getIOStream().writeln("Il nome inserito non appartiene ad un campo modificabile");
			return false;
		}
		
		field = Stream.of(fields)
						.filter((fh)->fh.getName().equalsIgnoreCase(newField))
						.findFirst().get();
		if(field.getName().equals(FieldHeading.CATEGORIE_INTERESSE.getName())) {
			boolean add = true;
			String confirm = ctx.getIOStream().read("Inserisca la modalità di modifica: [\"a\" - aggiungi | \"r\" - togli] > ");
			if(confirm.equalsIgnoreCase("r"))
				add = false;
			else if(!confirm.equalsIgnoreCase("a")){
				ctx.getIOStream().writeln("Il valore inserito non è corretto");
				return false;
			}
			String categoryName = ctx.getIOStream().read("Inserisca il nome della categoria da " + (add?"aggiungere":"rimuovere") + "> ");
			if(!Stream.of(EventHeading.values()).anyMatch((fh) -> fh.getName().equalsIgnoreCase(categoryName))) {
				ctx.getIOStream().writeln("Il nome inserito non appartiene ad una categoria");
				return false;
			}
			String cat = Stream.of(EventHeading.values())
								.filter((fh) -> fh.getName().equalsIgnoreCase(categoryName))
								.findFirst().get()
								.getName();
			if(ctx.getSession().getOwner().modifyCategory(cat, add)) {
				ctx.getIOStream().writeln("Categori d'interesse modificate con successo");
				return true;
			}else {
				ctx.getIOStream().writeln("La modifica non ha avuto successo");
				return false;
			}
		}else {

			Object obj = null;
			obj = acceptValue(ctx, field, String.format("Inserisca il nuovo valore (%s) : ", field.getType().getSimpleName()));
			if(ctx.getSession().getOwner().setValue(field.getName(), obj)) {
				ctx.getIOStream().writeln("La modifica è avvenuta con successo");
				return true;
			}
			ctx.getIOStream().writeln("La modifica non ha avuto successo");
			return false;
		}		
	}

}
