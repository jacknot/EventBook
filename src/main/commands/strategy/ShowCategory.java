package main.commands.strategy;

import java.util.stream.Stream;

import categories.EventHeading;
import main.commands.Context;
import utility.StringConstant;

public class ShowCategory implements CommandInterface{

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context ctx) {
		if(args.length == 0){
		 	ctx.getIOStream().writeln(StringConstant.SPECIFY_CATEGORY_NAME);
		  	return false;
		}else if(Stream.of(EventHeading.values())
		  					.anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
		  	ctx.getIOStream().write(Stream.of(EventHeading.values())
		  								.filter((fh)->fh.getName().equalsIgnoreCase(args[0]))
		  								.findFirst().get().toString());
		  	return true;
		 }else{
		  	ctx.getIOStream().writeln(StringConstant.CATEGORY_NOT_FOUND);
		  	return false;
		  }
	}

}
