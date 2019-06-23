package main.commands.strategy;

import java.util.ArrayList;

import main.commands.Context;
import users.User;
import utility.StringConstant;

public class Invite implements CommandInterface{

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

}
