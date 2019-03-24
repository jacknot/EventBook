package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**La classe PrivateSpace ha il compito di fornire una struttura adatta a contenere e gestire un insieme di messaggi.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class PersonalSpace implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Contiene i messaggi
	 */
	private ArrayList <Message> messageList;
	
	/**
	 * Formato in stringa dello spazio personale
	 */
	private final static String TOSTRING_FORMAT ="Spazio personale : %n%n";	
	/**
	 * Formattazione per la visualizzazione testuale dell singolo messaggio
	 */
	private static final String MESSAGE = "\n%d : %s";
	/**
	 * Rappresentazione testuale della classe quand non ci sono messaggi non ci sono messaggi
	 */
	private static final String EMPTY = "Non hai messaggi";
	
	/**
	 * Costruttore per la classe PrivateSpace
	 */
	public PersonalSpace() {
		messageList = new ArrayList<Message>();
	}
	/**
	 * Restituisce la lista dei messaggi
	 * @return La lista dei messaggi
	 */
	public ArrayList <Message> getMessageList() {
		return messageList;
	}
	/**
	 * Permette di aggiungere un nuovo messaggio alla lista
	 * @param newMessage Il nuovo messaggio da aggiungere
	 */
	public void add(Message newMessage) {
		messageList.add(newMessage);
	}
	/**
	 * Permette di rimuovere un messaggio presente nella lista
	 * @param index identificatore del messaggio da rimuovere
	 * @return l'esito della rimozione
	 */
	public boolean remove(int index) {
		if(index >= 0 && index < messageList.size()) {
			messageList.remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica la presenza o meno di messaggi
	 * @return True se nessun messaggio presente <br> False altrimenti
	 */
	public boolean noMessages() {
		return messageList.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer tmp = new StringBuffer(TOSTRING_FORMAT);
		if(messageList.size() == 0)
			tmp.append(EMPTY);
		else
			IntStream.range(0, messageList.size())
						.forEachOrdered((i)->tmp.append(String.format(MESSAGE, i, messageList.get(i).toString())));
		return String.format(tmp.toString());
	}
}
