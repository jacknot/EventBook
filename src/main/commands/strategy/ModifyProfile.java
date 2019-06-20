package main.commands.strategy;

import java.util.stream.Stream;

import categories.EventHeading;
import fields.FieldHeading;
import main.commands.Context;
import utility.StringConstant;

public class ModifyProfile extends Commands{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#Commands(String, String, String)
	 */
	public ModifyProfile(String name, String description, String syntax) {
		super(name, description, syntax);
	}

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkNoParameter(ctx, args))
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
			//inserisci valore del campo da modificare
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
	
	/**
	 * Controlla se nella chiamata di un comando non è stato passato nessun parametro
	 * @param ctx Contesto su cui operare
	 * @param args Parametri passati al comando
	 * @return True - Se non è stato passato nessun parametro <br> False - altrimenti
	 */
	private static boolean checkNoParameter(Context ctx, String args[]) {
		if(args.length != 0) {
			ctx.getIOStream().writeln(StringConstant.TOO_PARAMETERS);
			return false;
		}
		return true;
	}

}
