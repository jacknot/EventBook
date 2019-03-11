package EventBook.fruitore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**La classe PrivateSpace ha il compito di fornire una struttura adatta a contenere e gestire un insieme di messaggi.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class SpazioPersonale implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Contiene i messaggi
	 */
	private ArrayList <Messaggio> messageList;
	
	private final static String FORMAT_TO_STRING ="Spazio personale : %n%n";
	
	/**
	 * Costruttore per la classe PrivateSpace
	 */
	public SpazioPersonale() {
		messageList = new ArrayList<Messaggio>();
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
	 * @param index identificatore del messaggio da rimuovere
	 * @return l'esito della rimozione
	 */
	public boolean remove(int index) {
		if(index < messageList.size()) {
			messageList.remove(index);
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer temp = new StringBuffer(FORMAT_TO_STRING);
		IntStream.range(0, messageList.size())
					.forEachOrdered((i)->temp.append(i + " : " + messageList.get(i).toString() + "\n"));
		return String.format(temp.toString());
	}
}
