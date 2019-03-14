package proposals;

import java.io.Serializable;
import java.util.ArrayList;

import categories.Category;
import users.Message;
import users.Notifiable;

/**
 * Una proposta fa riferimento ad un particolare evento e consente di potersi iscrivere ad essa
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Proposal implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * La categoria a cui la proposta fa riferimento
	 */
	private Category event;
	/**
	 * Lo stato attuale della proposta
	 */
	private State aState;
	/**
	 * Il proprietario della proposta
	 */
	private Notifiable owner;
	/**
	 * Gli iscritti alla proposta
	 */
	private ArrayList<Notifiable> subscribers;
	/**
	 * Costruttore di una proposta
	 * @param event L'evento a cui farà riferimento la proposta
	 * @param owner Il proprietario della proposta
	 */
	public Proposal(Category event, Notifiable owner) {
		this.event = event;
		this.owner = owner;
		this.subscribers = new ArrayList<Notifiable>();
		this.aState = State.INVALID;
		//gestisce il caso in cui l'evento di riferimento sia già valido
		update();
	}
	/**
	 * Fa cambiare stato alla proposta
	 */
	public void update() {
		aState.transition(this);
	}
	/**
	 * Imposta un nuovo stato alla proposta
	 * @param nS lo stato da assegnare alla proposta
	 */
	public void setState(State nS) {
		aState = nS;
 	}
	/**
	 * Verifica se la proposta è uguale a quella inserita
	 * @param p la proposta con cui fare il confronto
	 * @return True - sono uguali<br>False - sono diverse
	 */
	public boolean equals (Proposal p) {
		return (this.owner.equals(p.owner) && this.event.equals(p.event));
	}
	/**
	 * Modifica il campo della proposta di cui si � inserito il nome, se esiste
	 * @param name il nome del campo da modificare
	 * @param value il nuovo valore del campo
	 * @return l'esito della modifica
	 */
	public boolean modify(String name, Object value) {
		if(aState.canSet()) {
			boolean outcome = event.setValue(name, value);
			update();
			return outcome;
		}
		return false;
	}
	/**
	 * Restituisce il contenuto del campo di cui si è inserito il nome
	 * @param name il nome del campo 
	 * @return il contenuto del campo
	 */
	public Object getValue(String name) {
		return event.getValue(name);
	}
	/**
	 * Iscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 * @return True - l'utente è stato correttamente iscritto alla proposta<br>False - l'utente non è stato iscritto alla proposta
	 */
	public boolean signUp(Notifiable user) {
		if(aState.canSignUp(this)){
			if(!owner.equals(user) && !subscribers.contains(user)) {
				subscribers.add(user);
				update();
				return true;
			}
		}
		return false; 
	}
	/**
	 * Verifica se la proposta è nello stato inserito
	 * @param s lo stato da controllare
	 * @return True - la proposta è nello stato inserito<br>False - la proposta è in uno stato differente
	 */
	public boolean hasState(State s) {
		return aState.equals(s);
	}
	/**
	 * Avvisa la proposta che è stata resa pubblica
	 */
	public void publish() {
		aState.publish(this);
	}
	/**
	 * Invia un messaggio a tutti gli iscritti e al proprietario della risposta
	 * @param msg il messaggio da inviare
	 */
	public void send(Message msg) {
		owner.receive(msg);
		subscribers.stream()
					.forEach(( i )-> i.receive(msg));
	}
	/**
	 * Restituisce il numero di iscritti alla proposta
	 * @return il numero di iscritti alla proposta
	 */
	public int subNumber() {
		return subscribers.size() + 1;
	}
	/**
	 * Verifica se la proposta è valida
	 * @return True - la proposta è valida<br>False - la proposta è invalida
	 */
	public boolean isValid() {
		return event.isValid();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Propositore : " + owner + "\n" + event.toString();
	}
}
