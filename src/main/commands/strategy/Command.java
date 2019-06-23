package main.commands.strategy;

import main.commands.Context;

public class Command {

	private CommandDescription description;
	private CommandInterface algorithm;
	
	
	public Command(CommandDescription description, CommandInterface algorithm) {
		this.description = description;
		this.algorithm = algorithm;
	}

	public String getName() {
		return description.getName();
	}
	
	public String getDescription() {
		return description.getDescription();
	}
	
	public String getSyntax() {
		return description.getSyntax();
	}
	
	public boolean run(String[] args, Context ctx) {
		return algorithm.run(args, ctx);
	}
	

	/**
	 * Controlla se il comando ha il nome inserito
	 * @param comando il presunto nome del comando
	 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
	 */
	public boolean hasName(String comando) {
		return description.hasName(comando);
	}
	
}
