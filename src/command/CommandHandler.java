package command;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contenitore in grado di gestire una lista di comandi e di poter fare operazioni su di essi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class CommandHandler implements Closeable{
	/**
	 * Il formato con cui vengono stampati i comandi
	 */
	private static final String TOSTRING_FORMAT = "\n\t%-20s%20s";
	/**
	 * Espressione regolare per l'estrazione del comando
	 */
	private static final String REGEX_COMMAND = "^[a-z][A-Za-z]+( )?";
	/**
	 * Istanza per implementare il design pattern Singleton
	 */
	private static CommandHandler instance;
	/**
	 * Contiene i comandi
	 */
	private ArrayList<Command> cList;
	/**
	 * Il contesto sul quale devono operare i comandi
	 */
	private Context context;
	/**
	 * Costruttore
	 */
	private CommandHandler(InOutStream IOStream) {
		cList = new ArrayList<Command>();
		
		this.context = new Context(IOStream);
		
		cList.add(Command.EXIT);
		cList.add(Command.REGISTRATION);
		cList.add(Command.LOGIN);
	}
	
	public static CommandHandler getInstance(InOutStream IOStream) {
		if(instance == null)
			instance = new CommandHandler(IOStream);
		return instance;
	}
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logIn
	 */
	private void logIn() {
		cList.add(Command.SHOW_CATEGORIES);
		cList.add(Command.CATEGORY);
		cList.add(Command.DESCRIPTION);
		cList.add(Command.LOGOUT);
		cList.add(Command.MODIFY);
		cList.add(Command.NEW_EVENT);
		cList.add(Command.SHOW_WORKINPROGRESS);
		cList.add(Command.SHOW_NOTICEBOARD);
		cList.add(Command.PUBLISH);
		cList.add(Command.PARTICIPATE);
		cList.add(Command.UNSUBSCRIBE); //Comando aggiunto nella V3
		cList.add(Command.WITHDRAW_PROPOSAL);
		cList.add(Command.INVITE);
		cList.add(Command.PRIVATE_SPACE_IN); //Accesso al private space
		cList.remove(Command.REGISTRATION);
		cList.remove(Command.LOGIN);	
	}
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logOut
	 */
	private void logOut() {
		cList.add(Command.REGISTRATION);
		cList.add(Command.LOGIN);
		cList.remove(Command.SHOW_CATEGORIES);
		cList.remove(Command.CATEGORY);
		cList.remove(Command.DESCRIPTION);
		cList.remove(Command.LOGOUT);
		cList.remove(Command.MODIFY);
		cList.remove(Command.NEW_EVENT);
		cList.remove(Command.SHOW_WORKINPROGRESS);
		cList.remove(Command.SHOW_NOTICEBOARD);
		cList.remove(Command.PUBLISH);
		cList.remove(Command.PARTICIPATE);
		cList.remove(Command.UNSUBSCRIBE);
		cList.remove(Command.WITHDRAW_PROPOSAL);
		cList.remove(Command.INVITE);
		cList.remove(Command.PRIVATE_SPACE_IN); //Accesso al private space
	}
	
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un accesso al private space
	 */
	private void privateSpaceIn() {
		cList.add(Command.SHOW_NOTIFICATIONS);
		cList.add(Command.REMOVE_NOTIFICATION);
		cList.add(Command.PRIVATE_SPACE_OUT); //Uscita dal private space
		cList.add(Command.SHOW_PROFILE);
		cList.add(Command.MODIFY_PROFILE);
		cList.remove(Command.CATEGORY);
		cList.remove(Command.DESCRIPTION);
		cList.remove(Command.LOGOUT);
		cList.remove(Command.MODIFY);
		cList.remove(Command.NEW_EVENT);
		cList.remove(Command.SHOW_WORKINPROGRESS);
		cList.remove(Command.SHOW_NOTICEBOARD);
		cList.remove(Command.PUBLISH);
		cList.remove(Command.PARTICIPATE);
		cList.remove(Command.UNSUBSCRIBE); //Comando aggiunto nella V3
		cList.remove(Command.WITHDRAW_PROPOSAL);
		cList.remove(Command.INVITE);
		cList.remove(Command.PRIVATE_SPACE_IN); //Accesso al private space
	}
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un uscita dal private space
	 */
	private void privateSpaceOut() {
		cList.add(Command.CATEGORY);
		cList.add(Command.DESCRIPTION);
		cList.add(Command.LOGOUT);
		cList.add(Command.MODIFY);
		cList.add(Command.NEW_EVENT);
		cList.add(Command.SHOW_WORKINPROGRESS);
		cList.add(Command.SHOW_NOTICEBOARD);
		cList.add(Command.PUBLISH);
		cList.add(Command.PARTICIPATE);
		cList.add(Command.UNSUBSCRIBE); //Comando aggiunto nella V3
		cList.add(Command.WITHDRAW_PROPOSAL);
		cList.add(Command.INVITE);
		cList.add(Command.PRIVATE_SPACE_IN); //Accesso al private space
		cList.remove(Command.SHOW_NOTIFICATIONS);
		cList.remove(Command.REMOVE_NOTIFICATION);
		cList.remove(Command.SHOW_PROFILE);
		cList.remove(Command.MODIFY_PROFILE);
		cList.remove(Command.PRIVATE_SPACE_OUT); //Accesso al private space
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
		return "";
	}
	
	/**
	 *	Controlla se è presente un comando con il nome inserito 
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
		else if(contains(command)) {
			if(cList.stream()
				.filter((c)->c.hasName(command))
				.findFirst().get().run(context, args)) {
					if(Command.LOGIN.hasName(command))
						logIn();
					else if(Command.LOGOUT.hasName(command))
						logOut();
					else if(Command.PRIVATE_SPACE_IN.hasName(command))
						privateSpaceIn();
					else if(Command.PRIVATE_SPACE_OUT.hasName(command))
						privateSpaceOut();
			}
		}else 
			context.getIOStream().writeln(StringConstant.ERROR_UNKNOWN_COMMAND);
		context.getIOStream().write(StringConstant.NEW_LINE + StringConstant.WAITING);
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("I comandi a tua disposizione:");
		cList.stream()
			.forEachOrdered((c)->sb.append(String.format(TOSTRING_FORMAT, c.getNome(), c.getDescription())));
		return sb.toString();
	}

	/**
	 * Chiude tutte le risorse usate
	 * @throws IOException in caso di errori nella chiusura
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		context.close();
	}
}