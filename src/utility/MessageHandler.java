package utility;

import java.time.LocalDate;
import java.util.ArrayList;

import dataTypes.Pair;
import users.Message;
import users.User;

/**
 * Classe con il compito di gestire l'invio di messaggi a gruppi di utenti.<br>
 * Implementa il design pattern Singleton
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class MessageHandler {

	/**
	 * L'istanza del gestore di messaggi
	 */
	private static MessageHandler instance;
	
	/**
	 * Costruttore, privato per implementare Singleton
	 */
	private MessageHandler() {}
	
	/**
	 * Metodo con il compito di restituire un'istanza del gestore di messaggi
	 * @return l'istanza del gestore di messaggi
	 */
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
		userList.stream().forEach((u)->	u.receive(
				new Message(
					"Messaggio di sistema",
					"Nuovo evento di tuo interesse",
					"E' stato creato un nuovo evento di tipo " + categoryName + " .\nConsulta la bacheca per ulteriori informazioni "
					))
				);
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
	
	/**
	 * Avvisa tutti gli iscritti all'evento (compreso il proprietario) che l'evento è confermato e si svolgerà secondo i dettagli indicati
	 * @param userList lista di utenti
	 * @param title titolo della proposta
	 * @param data data di svolgimento
	 * @param ora ora di svolgimento
	 * @param luogo luogo di svolgimento
	 * @param quota quota da portare per partecipare
	 */
	public void eventConfirmed(ArrayList<Pair<User, Double>> userList, Object title, Object data, Object ora, Object luogo, Object quota) {
		userList.stream()
				.forEach((p)->
					p.getFirst().receive( new Message(
									title+": "+LocalDate.now(),
									StringConstant.CONFIRMOBJ,
									String.format(StringConstant.CONFIRMFORMAT, title,
											data,
											ora,
											luogo,
											quota) + (p.getSecond() == 0?"":String.format(StringConstant.OPTIONAL_AMOUNT, p.getSecond() + (Double)quota))
							))
					);	
	}
	
	/**
	 * Avvisa tutti gli iscritti all'evento (compreso il proprietario) che l'evento è fallito e pertanto non si svolgerà
	 * @param userList lista di utenti
	 * @param title titolo della proposta
	 */
	public void eventFailed(ArrayList<User> userList, Object title) {
		userList.stream().forEach((u)->	u.receive(
				new Message(
					title+": "+LocalDate.now(),
					StringConstant.FAILUREOBJ,
					String.format(StringConstant.FAILUREFORMAT, title)
					))
				);	
	}
	
	/**
	 * Avvisa tutti gli iscritti all'evento (compreso il proprietario) che l'evento è stato ritirato dal proprietario e pertanto non si svolgerà
	 * @param userList lista di utenti
	 * @param title titolo della proposta
	 */
	public void eventWithdrawn(ArrayList<User> userList, Object title) {
		userList.stream().forEach((u)->	u.receive(
				new Message(
					title+": "+LocalDate.now(),
					StringConstant.WITHDRAWNOBJ,
					String.format(StringConstant.WITHDRAWNFORMAT, title)
					))
				);	
	}
	
}
