package main.commands;

import java.util.ArrayList;

/**
 * Contenitore di comandi utilizzati da un utente in una sessione
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class CommandsHistory {
	
	/**
	 * Contiene la storia dei comandi
	 */
	private ArrayList<String> history;
	private int index;
	
	public CommandsHistory() {
		history = new ArrayList<String>();
		index = 0;
	}
	
	/**
	 * Aggiunge il comando alla storia dei comandi utilizzati
	 * @param command Nome del comando usato dall'utente
	 */
	public void add(String command) {
		if(history.add(command));
			index = size() - 1; 
	}
	
	/**
	 * Restituisce il comando precedente
	 * @return Comando precedente
	 */
	public String previousCommand() {
		String previous = history.get(index);
		index--;
		index = Math.max(0, index);
		return previous;
	}
	
	/**
	 * Restituisce il comando successivo
	 * @return Comando successivo
	 */
	public String nextCommand() {
		String next = history.get(index);
		index++;
		index = Math.min(size()-1, index);
		return next;
	}
	
	/*
	 * Restituisce la dimensione della storia dei comandi
	 */
	private int size() {
		return history.size();
	}
	
}
