package main.commands.strategy;

import java.util.stream.Stream;

import categories.Category;
import categories.EventCache;
import categories.EventHeading;
import fields.FieldHeading;
import main.commands.Context;
import proposals.OptionsSet;
import proposals.ProposalFactory;
import proposals.ProposalInterface;
import utility.StringConstant;

public class NewEvent implements CommandInterface{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(args.length == 0) {
			ctx.getIOStream().writeln(StringConstant.SPECIFY_CATEGORY_NAME);
			return false;
		}
		String categoryName = args[0];
		if(!Stream.of(EventHeading.values()).anyMatch((ch)->ch.getName().equalsIgnoreCase(categoryName))) {
			ctx.getIOStream().writeln("Categoria non esistente");
			return false;
		}
		Category event = new EventCache().getCategory(Stream.of(EventHeading.values())
															.filter((ch)->ch.getName().equalsIgnoreCase(categoryName))
															.findFirst().get().getName());
		//campi facoltativi/obbligatori
		Stream.of(FieldHeading.values())
				.filter(( fd )->event.containsField(fd.getName()))
				.filter(( fd )->!fd.isOptional())
				.forEachOrdered(( fd )->{	
					ctx.getIOStream().writeln(fd.toString());
					Object obj = acceptValue(ctx, fd, "Inserisca il valore del campo (" + fd.getType().getSimpleName()+ ") : ");
					if(event.setValue(fd.getName(), obj))
						ctx.getIOStream().writeln("\tDato inserito correttamente\n");
					else
						ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
				});
		//campi opzionali
		Stream.of(FieldHeading.values())
				.filter(( fd )->event.containsField(fd.getName()))
				.filter(( fd )->fd.isOptional())
				.forEachOrdered(( fd )->{
					boolean valid = false;
					boolean keepField = false;
					do {
						String confirm = ctx.getIOStream().read(String.format("\n%s\n\tVuole utilizzare questo campo opzionale? [y|n] > ", fd.toString()));
						if(confirm.equalsIgnoreCase("y")) {
							valid = true;
							keepField = true;
						}else if(confirm.equalsIgnoreCase("n")) 
							valid = true;
						else
							ctx.getIOStream().writeln("\n\tIl valore inserito è errato: inserisca 'y' o 'n'");
					}while(!valid);
					if(!keepField) {
						ctx.getIOStream().writeln("\tIl campo opzionale " + fd.getName() + " non verrà inserito nella categoria");
						event.removeOptionalField(fd);
					}else {
						ctx.getIOStream().writeln("\tIl campo opzionale " + fd.getName() + " verrà inserito nella categoria");
						Object obj = acceptValue(ctx, fd, "Inserisca il valore del campo (" + fd.getType().getSimpleName()+ ") : ");
						if(event.setValue(fd.getName(), obj))
							ctx.getIOStream().writeln("\tDato inserito correttamente\n");
						else
							ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
					}
				});
		//iscrizione proprietario
		ProposalInterface p = ProposalFactory.newProposal(event);
		OptionsSet pref = p.getOptions();
		pref = makeChoices(pref, ctx);
		ctx.getIOStream().writeln(StringConstant.EMPTY_STRING);
		p.setOwner(ctx.getSession().getOwner(), pref);
		if(ctx.getSession().addProposal(p)) {
			ctx.getIOStream().writeln("La proposta è stata aggiunta alle proposte in lavorazione");
			return true;
		}else {
			ctx.getIOStream().writeln("La proposta non è stata aggiunta");
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
	 * Chiede all'utente di quali campi opzionali vuole usufruire
	 * @param pref Preferenza da impostare
	 * @param ctx Contesto su cui operare
	 * @return le scelte dell'utente
	 */
	private static OptionsSet makeChoices(OptionsSet pref, Context ctx) {
		Stream.of(pref.getOptions())
				.forEach((fh)->{
					boolean confirm = false;
					boolean valid = false;
					do {
						String ok = ctx.getIOStream().read(String.format("\n%s\n\tVuole sceglierlo? [y|n] > ", fh.toString()));
						if(ok.equalsIgnoreCase("y")) {
							confirm = true;
							valid = true;
						}else if(ok.equalsIgnoreCase("n"))
							valid = true;
						else
							ctx.getIOStream().writeln("\n\tIl valore inserito non è corretto: inserisca 'y' o 'n'");
					}while(!valid);
					pref.makeChoice(fh, confirm);
				});
		return pref;
	}

}
