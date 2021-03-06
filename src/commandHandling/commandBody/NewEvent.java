package commandHandling.commandBody;

import java.util.stream.Stream;

import commandHandling.Context;
import categories.Category;
import categories.EventCache;
import categories.EventHeading;
import fields.FieldHeading;
import proposals.OptionsSet;
import proposals.ProposalFactory;
import proposals.ProposalInterface;
import utility.StringConstant;

public class NewEvent implements CommandInterface, OneParameter, ValueRequired{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.SPECIFY_CATEGORY_NAME, "Inserisca una sola categoria"))
			return false;
		
		String categoryName = args[0];
		if(!Stream.of(EventHeading.values()).anyMatch((ch)->ch.getName().equalsIgnoreCase(categoryName))) {
			ctx.getIOStream().writeln("Categoria non esistente");
			return false;
		}
		Category event = new EventCache().getCategory(Stream.of(EventHeading.values())
															.filter((ch)->ch.getName().equalsIgnoreCase(categoryName))
															.findFirst().get().getName());
		event = compileNotOptionalFields(ctx, event);
		event = compileOptionalFields(ctx, event);
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
	 * Chiede all'utente di quali campi opzionali vuole usufruire
	 * @param pref Preferenza da impostare
	 * @param ctx Contesto su cui operare
	 * @return le scelte dell'utente
	 */
	private OptionsSet makeChoices(OptionsSet pref, Context ctx) {
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
	
	/**
	 * Consente all'utente di compilare i campi non opzionali
	 * 
	 * @param ctx il contesto su cui operare
	 * @param event l'evento da compilare
	 * @return l'evento modificato
	 */
	private Category compileNotOptionalFields(Context ctx, Category event) {
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
		return event;
	}
	
	/**
	 * Consente di compilare i campi opzionali
	 * @param ctx il contesto su cui operare
	 * @param event l'evento da compilare
	 * @return l'evento compilato
	 */
	private Category compileOptionalFields(Context ctx, Category event) {
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
		return event;
	}

}
