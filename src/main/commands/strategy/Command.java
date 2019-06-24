package main.commands.strategy;

import main.commands.Context;

public class Command {

	private CommandDescription description;
	private CommandInterface algorithm;
	
	
	/**
	 * Costruttore
	 * @param description la descrizione del comando
	 * @param algorithm l'algoritmo di esecuzione del comando
	 */
	public Command(CommandDescription description, CommandInterface algorithm) {
		this.description = description;
		this.algorithm = algorithm;
	}

	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.CommandDescription#getName()
	 */
	public String getName() {
		return description.getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.CommandDescription#getDescription()
	 */
	public String getDescription() {
		return description.getDescription();
	}
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.CommandDescription#getSyntax()
	 */
	public String getSyntax() {
		return description.getSyntax();
	}
	
	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.CommandInterface#run(java.lang.String[], main.commands.Context)
	 */
	public boolean run(String[] args, Context ctx) {
		return algorithm.run(args, ctx);
	}	

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.CommandDescription#hasName(java.lang.String)
	 */
	public boolean hasName(String comando) {
		return description.hasName(comando);
	}
	
}
