package commandHandling.commandBody;

import java.util.ArrayList;

import commandHandling.Context;
import users.User;


public class Invite implements CommandInterface, OneParameter, ProposalHandlerContainsID{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro", "Inserisca un solo parametro"))
			return false;
		int id = proposalHandlerContainsID(ctx, args);
		if(id < 0)
			return false;
		User owner = ctx.getSession().getOwner();
		if(!ctx.getProposalHandler().isOwner(id, owner)) {
			ctx.getIOStream().writeln("La proposta non è di sua proprietà");
			return false;
		}
		ArrayList<User> userList = ctx.getProposalHandler().searchBy(owner, ctx.getProposalHandler().getCategory(id));
		if(userList.isEmpty()) {
			ctx.getIOStream().writeln("Nessun utente trovato da invitare a questa proposta");
			return false;
		}
		ctx.getIOStream().writeln("Utenti da invitare: " + userList.toString());
		String confirm = ctx.getIOStream().read("Vuole mandare un invito a tutti?" + "\n[y|n]> ");
		ArrayList<User> receivers = new ArrayList<>();
		receivers.addAll(userList);
		if(confirm.equalsIgnoreCase("n")) {							
			userList.stream()
						.forEach(( u )->{
							String answer = ctx.getIOStream().read("Vuole invitare " + u.getName() + " ? [y|n]> ");
							if(answer.equalsIgnoreCase("y")) 
								ctx.getIOStream().writeln("L'utente verrà invitato");
							else if(answer.equalsIgnoreCase("n")) {
								ctx.getIOStream().writeln("L'utente non verrà invitato ");
								receivers.remove(u);
							}else {
								ctx.getIOStream().writeln("Il valore non è valido. L'utente non verrà invitato");
								receivers.remove(u);
							}
						});
		}else if(!confirm.equalsIgnoreCase("y")){
			ctx.getIOStream().writeln("L'invio non verrà effettuato, non è stato inserito una conferma corretta");
			return false;
		}
		if(ctx.getProposalHandler().inviteTo(id, receivers)) {
			ctx.getIOStream().writeln("Gli utenti sono stati inviati con successo");
			return true;
		}else {
			ctx.getIOStream().writeln("Impossibile spedire gli inviti");
			return false;
		}
	}
}
