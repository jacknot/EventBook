package main.InOutAdapter;

import main.commands.InOutStream;

/**
 * Classe che implementa l'interfaccia InOut utilizzando i più semplici input e output stream
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class SimpleStreamAdapter implements InOutStream{
	
	/* (non-Javadoc)
	 * @see command.InOutStream#read()
	 */
	@Override
	public String read(String str) {
		write(str);
		return System.console().readLine();
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
	
}
