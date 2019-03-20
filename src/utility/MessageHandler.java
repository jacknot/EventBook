package utility;

import java.util.ArrayList;

import users.Message;
import users.User;

public class MessageHandler {

	private static MessageHandler instance;
	
	private MessageHandler() {}
	
	public static MessageHandler getInstance() {
		if(instance == null)
			instance = new MessageHandler();
		return instance;
	}

	/**
	 * Notifica gli utenti che hanno la categoria identificata da categoryName tra le categorie di interesse
	 * che è presente un nuovo evento di interesse 
	 * @param userList lista di utenti a cui inviare il messaggio
	 * @param categoryName nome della categoria
	 */
	public void notifyByInterest(ArrayList<User> userList, String categoryName) {
		for(User user: userList) {
			user.receive(new Message(
					"Messaggio di sistema",
					"E' stato creato evento che ti può interessare",
					"un evento di tipo " + categoryName + "è stato creato"
					));
		}
	}
	
	/**
	 * Manda un invito all'evento agli utenti nella lista userList
	 * @param userList lista di utenti
	 * @param owner Proprietario della proposta
	 */
	public void inviteUsers(ArrayList<User> userList, String owner) {
		for(User user: userList) {
			user.receive(new Message(
					owner,
					"Sei stato invitato ad un evento",
					"Sei stato invitato all'evento tot"
					));
		}
		
	}
}
