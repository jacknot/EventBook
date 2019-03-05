package EventBook.versione2.fruitore;

import java.io.Serializable;
import java.util.ArrayList;

/**La classe PrivateSpace ha il compito di fornire una struttura adatta a contenere e gestire un insieme di messaggi.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class SpazioPersonale implements Serializable{
	
	private ArrayList <Messaggio> messageList;
	
	private final static String FORMAT_TO_STRING ="Spazio personale : %n%n";
	
	/**
	 * Costruttore per la classe PrivateSpace
	 */
	public SpazioPersonale() {
		messageList = new ArrayList();
	}
	
	/**
	 * Restituisce la lista dei messaggi
	 * @return La lista dei messaggi
	 */
	public ArrayList <Messaggio> getMessageList() {
		return messageList;
	}
	
	/**
	 * Permette di aggiungere un nuovo messaggio alla lista
	 * @param newMessage Il nuovo messaggio da aggiungere
	 */
	public void add(Messaggio newMessage) {
		messageList.add(newMessage);
	}
	
	/**
	 * Permette di rimuovere un messaggio presente nella lista
	 * @param title Nome del messaggio da rimuovere
	 */
	public void remove(String title) {
		messageList.stream()
					.filter(( m )->m.getObject().equalsIgnoreCase(title))
					.forEach(( m )->messageList.remove(m));
	}

	public String toString() {
		StringBuffer temp = new StringBuffer("Spazio personale: %n");
		for(int i=0; i<messageList.size(); i++) {
			temp.append(messageList.get(i));
		}
		return String.format(temp.toString());
	}
}
