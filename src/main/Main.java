package main;


import java.util.*;

/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	
	private static final String NEW_LINE = "\n";
	private static final String WELCOME = "Welcome to EventBook";
	private static final String WAITING = "> ";


	private static CommandsHandler handler;
	
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		System.out.println(WELCOME + NEW_LINE);
		
		handler = CommandsHandler.getInstance();
		handler.load();
		
		Scanner in = new Scanner(System.in);
		do {
			String command = in.nextLine();
			handler.run(command.trim());
		}while(true);
	}

	
}