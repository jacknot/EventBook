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
					"Nuovo evento di tuo interesse",
					"E' stato creato un nuovo evento di tipo " + categoryName + " .\nConsulta la bacheca per ulteriori informazioni "
					));
		}
	}
	
	/**
	 * Manda un invito all'evento agli utenti nella lista userList
	 * @param userList lista di utenti
	 * @param owner Proprietario della proposta
	 * @param id l'identificativo della proposta a cui ti sei stato invitato
	 */
	public void inviteUsers(ArrayList<User> userList, String owner, int id) {
		userList.stream().forEach((u)->	u.receive(
				new Message(
					owner,
					"Invito",
					"Sei stato invitato all'evento con identificativo " + id + " .\nConsulta la bacheca per più informazioni"
					))
				);

		
	}
}
