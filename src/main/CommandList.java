package main;

import java.util.ArrayList;

import main.Main.Command;

/**
 * Contenitore in grado di gestire una lista di comandi e di poter fare operazioni su di essi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
class CommandList extends ArrayList<Command>{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Il formato con cui vengono stampati i comandi
	 */
	private static final String TOSTRING_FORMAT = "\n\t%-20s%s";
	
	/**
	 * Costruttore
	 */
	public CommandList() {
		super();
		add(Command.EXIT);
		add(Command.REGISTRATION);
		add(Command.LOGIN);
	}
	
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logIn
	 */
	public void logIn() {
		add(Command.CATEGORY);
		add(Command.DESCRIPTION);
		add(Command.LOGOUT);
		add(Command.MODIFY);
		add(Command.NEW_EVENT);
		add(Command.SHOW_WORKINPROGRESS);
		add(Command.SHOW_NOTIFICATIONS);
		add(Command.REMOVE_NOTIFICATION);
		add(Command.SHOW_NOTICEBOARD);
		add(Command.PUBLISH);
		add(Command.PARTICIPATE);
		remove(Command.REGISTRATION);
		remove(Command.LOGIN);	
	}
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logOut
	 */
	public void logOut() {
		add(Command.REGISTRATION);
		add(Command.LOGIN);
		remove(Command.CATEGORY);
		remove(Command.DESCRIPTION);
		remove(Command.LOGOUT);
		remove(Command.MODIFY);
		remove(Command.NEW_EVENT);
		remove(Command.SHOW_WORKINPROGRESS);
		remove(Command.SHOW_NOTIFICATIONS);
		remove(Command.REMOVE_NOTIFICATION);
		remove(Command.SHOW_NOTICEBOARD);
		remove(Command.PUBLISH);
		remove(Command.PARTICIPATE);
	}
	
	/**
	 *	Controlla se è presente un comando con il nome inserito 
	 * @param key il nome del comando di cui si vuole verificare la presenza
	 * @return True - è presente un comando con il nome inserito<br>False - non è presente un comando con il nome inserito
	 */
	public boolean contains(String key) {
		if(key.equals("help")) return true;
		return this.stream()
				.anyMatch((c)->c.hasName(key));
	}
	
	/**
	 * Esegue il comando di cui si è inserito il nome, se presente
	 * @param command il nome del comando da eseguire
	 */
	public void run(String command) {
		if(command.equals("help"))
			System.out.println(toString());
		else if(contains(command))
			this.stream()
				.filter((c)->c.hasName(command))
				.findFirst().get().run();
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("I comandi a tua disposizione:");
		this.stream()
			.forEachOrdered((c)->sb.append(String.format(TOSTRING_FORMAT, c.getNome(), c.getDescription())));
		return sb.toString();
	}
}