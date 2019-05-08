package main.commands;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.StringConstant;

/**
 * Classe in grado di gestire un insieme di comandi e di poter fare operazioni su di essi.<br>
 * La classe implementa il design pattern Singleton
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class CommandsHandler implements Closeable{
	/**
	 * Il formato con cui vengono stampati i comandi
	 */
	private static final String TOSTRING_FORMAT = "\n\t%-30s%-65s%-25s";
	/**
	 * Espressione regolare per l'estrazione del comando
	 */
	private static final String REGEX_COMMAND = "^[a-z][A-Za-z]+( )?";

	/**
	 * Contiene i comandi
	 */
	private ArrayList<Commands> cList;
	/**
	 * Il contesto sul quale devono operare i comandi
	 */
	private Context context;
	/**
	 * Costruttore
	 */
	public CommandsHandler(InOutStream IOStream) {
		cList = new ArrayList<Commands>();
		this.context = new Context(IOStream);
		cList = CommandsState.BASE.getCommandsList();
	}

	
	/**Restituisce il comando (se presente) che inizia con la stringa passata per parametro
	 * @param initial Stringa iniziale di un comando
	 * @return Nome del comando se trovato <br> la stringa iniziale altrimenti
	 */
	public String hint(String initial) {
		for(Commands command: cList) {
			if(command.getName().startsWith(initial))
				return command.getName() + " ";
		}
		return initial;
	}	
	
	/**
	 * Estrae dall'input dell'utente il comando, separandolo dagli eventuali parametri
	 * @param input input dell'utente
	 * @return l'effettivo comando
	 */
	public String getCommand(String input) {
		Matcher matcher = Pattern.compile(REGEX_COMMAND).matcher(input);
		if(matcher.find())
			return matcher.group().trim();
		return StringConstant.EMPTY_STRING;
	}
	
	/**
	 * Controlla se è presente un comando con il nome inserito 
	 * @param key il nome del comando di cui si vuole verificare la presenza
	 * @return True - è presente un comando con il nome inserito<br>False - non è presente un comando con il nome inserito
	 */
	public boolean contains(String key) {
		String command = getCommand(key);
		if(command != null && command.equals("help")) 
			return true;
		return cList.stream()
				.anyMatch((c)->c.hasName(command));
	}

	/**
	 * Esegue il comando di cui si è inserito il nome, se presente
	 * @param input input dell'utente, contenente il comando e gli eventuali parametri
	 */
	public void run(String input) {
		String command = getCommand(input);
		String parameters = input.replaceAll(command, "").trim();
		String[] args = new String[0];
		if(!parameters.equals("")) {
			args = new String[1];
			args[0] = parameters;
		}
		if(command.equals("help"))
			context.getIOStream().writeln(toString());
		else if(!contains(command)) 
			context.getIOStream().writeln(StringConstant.ERROR_UNKNOWN_COMMAND);
		else if(cList.stream()
			.filter((c)->c.hasName(command))
			.findFirst().get()
			.run(context, args)) {
				if(Commands.LOGIN.hasName(command))
					cList = CommandsState.LOGIN.getCommandsList();
				else if(Commands.LOGOUT.hasName(command))
					cList = CommandsState.BASE.getCommandsList();
				else if(Commands.PRIVATE_SPACE_IN.hasName(command))
					cList = CommandsState.PRIVATESPACE.getCommandsList();
				else if(Commands.PRIVATE_SPACE_OUT.hasName(command))
					cList = CommandsState.LOGIN.getCommandsList();
		}
		context.getIOStream().write(StringConstant.NEW_LINE + StringConstant.WAITING);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("I comandi a tua disposizione:");
		cList.stream()
			.forEachOrdered((c)->sb.append(String.format(TOSTRING_FORMAT, c.getName(), c.getDescription(), c.getSyntax())));
		return sb.toString();
	}

	/**
	 * Chiude tutte le risorse usate
	 * @throws IOException in caso di errori nella chiusura
	 */
	@Override
	public void close() throws IOException {
		context.close();
	}
}