package main.commands.strategy;

import java.util.stream.Stream;

import fields.FieldHeading;
import main.commands.Context;
import proposals.OptionsSet;
import utility.StringConstant;

public class Participate implements CommandInterface{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkOneParameter(ctx, args))
			return false;
		int id = proposalHandlerContainsID(ctx, args);
		if(id<0)
			return false;
			
		if(ctx.getProposalHandler().isOwner(id, ctx.getSession().getOwner())) {
			ctx.getIOStream().writeln("E' il proprietario della proposta, è automaticamente iscritto");
			return false;
		}
		if(ctx.getProposalHandler().isSignedUp(id, ctx.getSession().getOwner())) {
			ctx.getIOStream().writeln("E' già iscritto a questa proposta");
			return false;
		}
		if(ctx.getProposalHandler().isFull(id)) {
			ctx.getIOStream().writeln("La proposta è al completo");
			return false;
		}
		OptionsSet pref = ctx.getProposalHandler().getPreferenze(id);
		FieldHeading[] optionalFields = pref.getOptions();
		if(optionalFields.length != 0) {
			ctx.getIOStream().writeln("Per completare l'iscrizione all'evento compilare i seguenti campi:");
			pref = makeChoices(pref, ctx);
			ctx.getIOStream().writeln(StringConstant.EMPTY_STRING);
		}
		if(!ctx.getProposalHandler().signUp(id, ctx.getSession().getOwner(), pref)) {
			ctx.getIOStream().writeln("L'iscrizione non è andata a buon fine");
			return false;
		}else {
			ctx.getIOStream().writeln("L'iscrizione è andata a buon fine");
			return true;
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
	
	private static int proposalHandlerContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args);
		if(!ctx.getProposalHandler().contains(id)) {
			ctx.getIOStream().writeln("La proposta di cui si è inserito l'identificatore non è presente");
			id = -1;
		}
		return id;
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
