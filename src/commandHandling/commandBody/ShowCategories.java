package commandHandling.commandBody;

import commandHandling.Context;
import java.util.stream.Stream;

import categories.EventHeading;
import utility.StringConstant;

public class ShowCategories implements CommandInterface, NoParameters{


	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.TOO_PARAMETERS))
			return false;
		ctx.getIOStream().writeln("Le categorie disponibili: ");
		Stream.of(EventHeading.values()).forEach((ch)->ctx.getIOStream().writeln("\t" + ch.getName()));
		return true;
	}
	
}
