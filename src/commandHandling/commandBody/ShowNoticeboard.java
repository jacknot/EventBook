package commandHandling.commandBody;

import commandHandling.Context;
import utility.StringConstant;

public class ShowNoticeboard implements CommandInterface, NoParameters{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		ctx.getProposalHandler().refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente utente vedrà la bacheca aggiornata
		String content = ctx.getProposalHandler().showContent();
		if(content.equals(""))
			ctx.getIOStream().write("Nessuna proposta in bacheca!\n");
		else 
			ctx.getIOStream().write("Le proposte in bacheca:\n" + content);
		return true;
	}

}
