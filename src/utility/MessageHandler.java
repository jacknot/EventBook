package utility;

import java.util.ArrayList;

import users.Message;
import users.User;

public class MessageHandler {

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
					"Invito da " + owner,
					"Sei stato invitato ad un evento",
					"Sei stato invitato all'evento tot"
					));
		}
		
	}
}
