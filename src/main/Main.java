package main;

import java.io.IOException;

import command.*;
import command.InOutAdapter.SimpleStreamAdapter;

/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {

	private static CommandHandler handler;
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}));
		System.out.println(StringConstant.WELCOME);
		handler = CommandHandler.getInstance(ssa);
		do {
			String command = ssa.read(StringConstant.NEW_LINE);
			handler.run(command.trim());
		}while(true);
	}	
}
