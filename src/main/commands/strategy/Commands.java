package main.commands.strategy;

import main.commands.Context;
/**
 * Classe astratta rappresentante un comando disponibile all'utente compreso di funzionalità
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public abstract class Commands {
	
	/**
	 * Il nome del comando
	 */
	private String name;
	/**
	 * La descrizione del comando
	 */
	private String description;
	/**
	 * La sintassi per l'esecuzione del comando
	 */
	private String syntax;
	
	/**
	 * Costruttore
	 * @param name il nome del comando
	 * @param description la descrizione del comando
	 * @param syntax la sintassi del comando
	 * @param runnable ciò che il comando deve fare
	 */
	public Commands(String name, String description, String syntax) {
		this.name = name;
		this.description = description;
		this.syntax = syntax == "" ? "" : String.format("Sintassi: %s", syntax);
	}
	
	/**
	 * Esegue il comando
	 * @param ctx il contesto sul quale deve lavorare il comando
	 * @param args gli argomenti del comando
	 * @return l'esito del comando
	 */
	public abstract boolean run(String[] args, Context ctx);

	/**
	 * Restituisce il nome del comando
	 * @return il nome del comando
	 */
	public String getName() {
		return name;
	}

	/**
	 * Restituisce la descrizione del comando
	 * @return la descrizione del comando
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Restituisce la sintassi del comando
	 * @return sintassi del comando
	 */
	public String getSyntax() {
		return syntax;
	}

	
}
