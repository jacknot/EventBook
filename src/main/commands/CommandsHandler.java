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
	private static final String TOSTRING_FORMAT = "\n\t%-30s%-70s%-25s";
	/**
	 * Espressione regolare per l'estrazione del comando
	 */
	private static final String REGEX_COMMAND = "^[a-z][A-Za-z]+( )?";
	/**
	 * Istanza per implementare il design pattern Singleton
	 */
	private static CommandsHandler instance;
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
	private CommandsHandler(InOutStream IOStream) {
		cList = new ArrayList<Commands>();
		this.context = new Context(IOStream);
		cList.add(Commands.EXIT);
		cList.add(Commands.REGISTRATION);
		cList.add(Commands.LOGIN);
	}
	
	/**
	 * Restituisce una istanza della classe
	 * @param IOStream lo stream di Input Output su cui si vuole operare
	 * @return l'istanza della classe
	 */
	public static CommandsHandler getInstance(InOutStream IOStream) {
		if(instance == null)
			instance = new CommandsHandler(IOStream);
		return instance;
	}
	/**
	 * Operazioni sulla lista di comandi a seguito di un logIn
	 */
	private void logIn() {
		cList.add(Commands.SHOW_CATEGORIES);
		cList.add(Commands.CATEGORY);
		cList.add(Commands.DESCRIPTION);
		cList.add(Commands.LOGOUT);
		cList.add(Commands.MODIFY);
		cList.add(Commands.NEW_EVENT);
		cList.add(Commands.SHOW_WORKINPROGRESS);
		cList.add(Commands.SHOW_NOTICEBOARD);
		cList.add(Commands.PUBLISH);
		cList.add(Commands.PARTICIPATE);
		cList.add(Commands.UNSUBSCRIBE); //Comando aggiunto nella V3
		cList.add(Commands.WITHDRAW_PROPOSAL);
		cList.add(Commands.INVITE);
		cList.add(Commands.PRIVATE_SPACE_IN); //Accesso al private space
		cList.remove(Commands.REGISTRATION);
		cList.remove(Commands.LOGIN);	
	}
	/**
	 * Operazioni sulla lista di comandi a seguito di un logOut
	 */
	private void logOut() {
		cList.add(Commands.REGISTRATION);
		cList.add(Commands.LOGIN);
		cList.remove(Commands.SHOW_CATEGORIES);
		cList.remove(Commands.CATEGORY);
		cList.remove(Commands.DESCRIPTION);
		cList.remove(Commands.LOGOUT);
		cList.remove(Commands.MODIFY);
		cList.remove(Commands.NEW_EVENT);
		cList.remove(Commands.SHOW_WORKINPROGRESS);
		cList.remove(Commands.SHOW_NOTICEBOARD);
		cList.remove(Commands.PUBLISH);
		cList.remove(Commands.PARTICIPATE);
		cList.remove(Commands.UNSUBSCRIBE);
		cList.remove(Commands.WITHDRAW_PROPOSAL);
		cList.remove(Commands.INVITE);
		cList.remove(Commands.PRIVATE_SPACE_IN); //Accesso al private space
	}
	
	/**
	 * Operazioni sulla lista di comandi a seguito della richiesta di accesso allo spazio personale
	 */
	private void privateSpaceIn() {
		cList.add(Commands.SHOW_NOTIFICATIONS);
		cList.add(Commands.REMOVE_NOTIFICATION);
		cList.add(Commands.PRIVATE_SPACE_OUT); //Uscita dallo spazio personale
		cList.add(Commands.SHOW_PROFILE);
		cList.add(Commands.MODIFY_PROFILE);
		cList.remove(Commands.SHOW_CATEGORIES);
		cList.remove(Commands.CATEGORY);
		cList.remove(Commands.DESCRIPTION);
		cList.remove(Commands.LOGOUT);
		cList.remove(Commands.MODIFY);
		cList.remove(Commands.NEW_EVENT);
		cList.remove(Commands.SHOW_WORKINPROGRESS);
		cList.remove(Commands.SHOW_NOTICEBOARD);
		cList.remove(Commands.PUBLISH);
		cList.remove(Commands.PARTICIPATE);
		cList.remove(Commands.UNSUBSCRIBE);
		cList.remove(Commands.WITHDRAW_PROPOSAL);
		cList.remove(Commands.INVITE);
		cList.remove(Commands.PRIVATE_SPACE_IN); //Accesso allo spazio personale
	}
	/**
	 * Operazioni sulla lista di comandi a seguito della richiesta di uscita dallo spazio personale
	 */
	private void privateSpaceOut() {
		cList.add(Commands.SHOW_CATEGORIES);
		cList.add(Commands.CATEGORY);
		cList.add(Commands.DESCRIPTION);
		cList.add(Commands.LOGOUT);
		cList.add(Commands.MODIFY);
		cList.add(Commands.NEW_EVENT);
		cList.add(Commands.SHOW_WORKINPROGRESS);
		cList.add(Commands.SHOW_NOTICEBOARD);
		cList.add(Commands.PUBLISH);
		cList.add(Commands.PARTICIPATE);
		cList.add(Commands.UNSUBSCRIBE); //Comando aggiunto nella V3
		cList.add(Commands.WITHDRAW_PROPOSAL);
		cList.add(Commands.INVITE);
		cList.add(Commands.PRIVATE_SPACE_IN); //Accesso al private space
		cList.remove(Commands.SHOW_NOTIFICATIONS);
		cList.remove(Commands.REMOVE_NOTIFICATION);
		cList.remove(Commands.SHOW_PROFILE);
		cList.remove(Commands.MODIFY_PROFILE);
		cList.remove(Commands.PRIVATE_SPACE_OUT); //Accesso al private space
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
					logIn();
				else if(Commands.LOGOUT.hasName(command))
					logOut();
				else if(Commands.PRIVATE_SPACE_IN.hasName(command))
					privateSpaceIn();
				else if(Commands.PRIVATE_SPACE_OUT.hasName(command))
					privateSpaceOut();	
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