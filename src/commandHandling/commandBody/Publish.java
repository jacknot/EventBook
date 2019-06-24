package commandHandling.commandBody;

import commandHandling.Context;
import java.util.ArrayList;

import users.User;
import utility.MessageHandler;
import utility.StringConstant;

public class Publish implements CommandInterface{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!checkOneParameter(ctx, args))
			return false;

		int id = sessionContainsID(ctx, args);
		if(id<0)
			return false;

		if(ctx.getProposalHandler().add(ctx.getSession().getProposal(id))) {
			String categoryName = ctx.getSession().getProposal(id).getCategoryName();
			ctx.getSession().removeProposal(id);
			ctx.getIOStream().writeln("Proposta aggiunta con successo");
			ArrayList<User> receivers = ctx.getDatabase().searchBy(categoryName);
			receivers.remove(ctx.getSession().getOwner());
			new MessageHandler().notifyByInterest(receivers, categoryName);
			return true;
		}else {
			ctx.getIOStream().writeln("La proposta inserita non è valida");
			return false;
		}	
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
