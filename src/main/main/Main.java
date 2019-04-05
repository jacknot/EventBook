package main.main;

import java.io.IOException;

import main.command.InOutAdapter.SimpleStreamAdapter;
import main.commands.*;
import utility.StringConstant;

/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {

	private static CommandsHandler handler;
	
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		SimpleStreamAdapter ssa = new SimpleStreamAdapter();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 		
			try {
				handler.close();
				ssa.writeln(StringConstant.EXITMSG);
				ssa.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
		System.out.println(StringConstant.WELCOME);
		handler = CommandsHandler.getInstance(ssa);
		do {
			String command = ssa.read(StringConstant.EMPTY_STRING);
			handler.run(command.trim());
		}while(true);
	}	
}
