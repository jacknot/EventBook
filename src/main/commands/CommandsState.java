package main.commands;

import java.util.ArrayList;

/**
 * Classe con il compito di rappresentare i vari stati in cui una lista di comandi si può trovare durante l'esecuzione del programma
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public enum CommandsState implements InitCommandsList{

	BASE(){		
		
		/*
		 * (non-Javadoc)
		 * @see main.commands.InitCommandsList#getCommandsList()
		 */
		@Override
		public ArrayList<Commands> getCommandsList(){
			ArrayList<Commands> commandsList = new ArrayList<Commands>();
			commandsList.add(Commands.EXIT);
			commandsList.add(Commands.REGISTRATION);
			commandsList.add(Commands.LOGIN);
			return commandsList;
		}
	},
	LOGIN(){
		
		/*
		 * (non-Javadoc)
		 * @see main.commands.InitCommandsList#getCommandsList()
		 */
		@Override
		public ArrayList<Commands> getCommandsList(){
			ArrayList<Commands> commandsList = new ArrayList<Commands>();
			commandsList.add(Commands.EXIT);
			commandsList.add(Commands.SHOW_CATEGORIES);
			commandsList.add(Commands.CATEGORY);
			commandsList.add(Commands.DESCRIPTION);
			commandsList.add(Commands.LOGOUT);
			commandsList.add(Commands.MODIFY);
			commandsList.add(Commands.NEW_EVENT);
			commandsList.add(Commands.SHOW_WORKINPROGRESS);
			commandsList.add(Commands.SHOW_NOTICEBOARD);
			commandsList.add(Commands.PUBLISH);
			commandsList.add(Commands.PARTICIPATE);
			commandsList.add(Commands.UNSUBSCRIBE);
			commandsList.add(Commands.WITHDRAW_PROPOSAL);
			commandsList.add(Commands.INVITE);
			commandsList.add(Commands.PRIVATE_SPACE_IN);
			return commandsList;
		}
	},
	PRIVATESPACE(){
		/*
		 * (non-Javadoc)
		 * @see main.commands.InitCommandsList#getCommandsList()
		 */
		@Override
		public ArrayList<Commands> getCommandsList() {
			ArrayList<Commands> commandsList = new ArrayList<Commands>();
			commandsList.add(Commands.EXIT);
			commandsList.add(Commands.SHOW_NOTIFICATIONS);
			commandsList.add(Commands.REMOVE_NOTIFICATION);
			commandsList.add(Commands.PRIVATE_SPACE_OUT); //Uscita dallo spazio personale
			commandsList.add(Commands.SHOW_PROFILE);
			commandsList.add(Commands.MODIFY_PROFILE);
			return commandsList;
		}
		
	};
	

}

/**
 * Interfaccia con il compito di restituire una lista di comandi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
interface InitCommandsList {	
	
	/**
	 * Restituisce una lista contenente comandi
	 * @return Lista di comandi
	 */
	public ArrayList<Commands> getCommandsList();
}
