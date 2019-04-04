package main.commands;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interfaccia con il compito di definire la metodologia con la quale il sistema si approccia con l'esterno
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface InOutStream extends Closeable{

	/**
	 * Legge una stringa dallo stream in ingresso
	 * @param ciò che si deve leggere
	 * @return la stringa letta dallo stream in ingresso
	 */
	public String read(String str);
	
	/**
	 * Inserisce una stringa nello stream di uscita
	 * @param str la stringa da inserire nello stream in input
	 */
	public void write(String str);
	
	/**
	 * Inserisce una stringa nello stream di uscita sottoforma di linea
	 * @param str la stringa da inserire nello stream in uscita
	 */
	public void writeln(String str);

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	public void close() throws IOException;
}
