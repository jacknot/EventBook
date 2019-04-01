package main.command.InOutAdapter;

import java.io.IOException;
import java.util.Scanner;

import main.commands.InOutStream;

/**
 * Classe che implemeneta l'interfaccia InOut con lo scopo di fare da ponte fra la console e gli utilizzatori dell'interfaccia
 * @author Matteo
 *
 */
public class SimpleStreamAdapter implements InOutStream{
	
	/**
	 * Lo scanner in ingresso
	 */
	private Scanner in;
	
	/**
	 * Costruttore
	 */
	public SimpleStreamAdapter() {
		in = new Scanner(System.in);
	}
	
	/* (non-Javadoc)
	 * @see command.InOutStream#read()
	 */
	@Override
	public String read(String str) {
		write(str);
		return in.nextLine();
	}

	/* (non-Javadoc)
	 * @see command.InOutStream#write(java.lang.String)
	 */
	@Override
	public void write(String str) {
		System.out.print(str);	
	}

	/* (non-Javadoc)
	 * @see command.InOutStream#writeln(java.lang.String)
	 */
	@Override
	public void writeln(String str) {
		System.out.println(str);	
	}

	/* (non-Javadoc)
	 * @see command.InOutStream#close()
	 */
	@Override
	public void close() throws IOException{
		in.close();
	}
	
}
