package commandHandling.commandBody;

import commandHandling.Context;
import java.util.stream.Stream;

import fields.FieldHeading;
import proposals.OptionsSet;
import utility.StringConstant;

public class Participate implements CommandInterface, OneParameter, ProposalHandlerContainsID{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro", "Inserisca un solo parametro"))
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
}
