package commandHandling.commandBody;

import java.util.stream.Stream;

import categories.Category;
import categories.EventCache;
import categories.EventHeading;
import commandHandling.Context;
import utility.StringConstant;

public class Description implements CommandInterface, OneParameter{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(!check(args, ctx, StringConstant.SPECIFY_CATEGORY_NAME, "Inserisca una sola categoria"))
			return false;
		if(Stream.of(EventHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
			Category event = new EventCache().getCategory(Stream.of(EventHeading.values())
										.filter((ch)->ch.getName().equalsIgnoreCase(args[0]))
										.findFirst().get().getName());
	  		ctx.getIOStream().write(event.getFeatures());
	  		return true;
	 	}else{
	  		ctx.getIOStream().writeln(StringConstant.CATEGORY_NOT_FOUND);
	  		return false;
	  	}
	}
}
