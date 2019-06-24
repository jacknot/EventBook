package commandHandling.commandBody;

import commandHandling.Context;
import java.util.ArrayList;

import users.User;
import utility.MessageHandler;
import utility.StringConstant;

public class Publish implements CommandInterface, OneParameter, InsertID{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, "Inserisca un parametro", "Inserisca un solo parametro"))
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
	private int sessionContainsID(Context ctx, String[] args) {
		int id = getID(ctx, args, StringConstant.INSERT_NUMBER);
		if(!ctx.getSession().contains(id)) {
			ctx.getIOStream().writeln("Nessuna proposta in lavorazione con questo identificatore");
			id = -1;
		}
		return id;
	}
}
