package main.commands.strategy;

import java.util.stream.Stream;

import fields.FieldHeading;
import main.commands.Context;
import utility.StringConstant;

public class Modify extends Commands{
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#Commands(String, String, String)
	 */
	public Modify(String name, String description, String syntax) {
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
		//inserisci id proposta
		boolean valid = false;
		int id = sessionContainsID(ctx, args);
		if(id<0)
			return false;
		try {
			id = Integer.parseInt(args[0]);
			if(!ctx.getSession().contains(id)) {
				ctx.getIOStream().writeln("Nessuna proposta in lavorazione con questo identificatore");
				return false;
			}
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
			return false;
		}
		//inserisci nome del campo da modificare
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
		//inserisci valore del campo da modificare
		Object obj = null;
		obj = acceptValue(ctx, field, String.format("Inserisca il nuovo valore (%s) : ", field.getType().getSimpleName()));
		//conferma modifica
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
		//modifica effetiva
		if(ctx.getSession().modifyProposal(id, field.getName(), obj)) {
			ctx.getIOStream().writeln("La modifica ha avuto successo");
			return true;
		}else {
			ctx.getIOStream().writeln("La modifica non ha avuto successo");
			return false;
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
	
	/**
	 * Controlla se la Sessione corrente contiene una proposta con l'id specificato,
	 * in caso affermativo lo ritorna
	 * @param ctx Contesto
	 * @param args Argomenti
	 * @return l'id della proposta, -1 se non trovata.
	 */
	private static int sessionContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args);
		if(!ctx.getSession().contains(id)) {
			ctx.getIOStream().writeln("Nessuna proposta in lavorazione con questo identificatore");
			id = -1;
		}
		return id;
	}
	
	private static int getID(Context ctx, String[] args) {
		int id = -1;
		try {
			id = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
			id = -1;
		}
		return id;
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
