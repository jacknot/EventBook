package utility;

import java.util.ArrayList;

import users.Message;
import users.User;

public class MessageHandler {

	private static MessageHandler instance;
	
	private MessageHandler() {
		
	}
	
	public static MessageHandler getInstance() {
		if(instance == null)
			instance = new MessageHandler();
		return instance;
	}

	
	public void notifyByInterest(ArrayList<User> userList, String categoryName) {
		for(User user: userList) {
			user.receive(new Message(
					"Messaggio di sistema",
					"E' stato creato evento che ti può interessare",
					"un evento di tipo " + categoryName + "+ stato creato"
					));
		}
	}
	
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
