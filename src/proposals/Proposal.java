package proposals;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import categories.Category;
import utility.StringConstant;
import dataTypes.Pair;
import fields.FieldHeading;
import users.Message;
import users.Subscriber;
import users.User;

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
	private Subscriber owner;
	/**
	 * Gli iscritti alla proposta
	 */
	private ArrayList<Subscriber> subscribers;
	/**
	 * Contiene informazioni legate al passaggio di stato della proposta
	 */
	private ArrayList<Pair<State, LocalDate>> statePassages;
	
	/**
	 * Costruttore di una proposta
	 * @param event L'evento a cui farà riferimento la proposta
	 * @param owner Il proprietario della proposta
	 */
	public Proposal(Category event) {
		this.event = event;
		this.owner = null;
		this.subscribers = new ArrayList<Subscriber>();
		this.aState = State.INVALID;
		statePassages = new ArrayList<Pair<State, LocalDate>>();
		update();
		statePassages.add(new Pair<>(aState, LocalDate.now()));
	}
	
	/**
	 * Imposta il proprietario della proposta
	 * @param nOwner il nuovo proprietario della proposta
	 * @param pref le preferenze espresse dal proprietario
	 * @return True - l'utente inserito è stato impostato come proprietario<br>False - l'utente inserito non è stato inserito come proprietario
	 */
	public boolean setOwner(User nOwner, Preferences pref) {
		if(getPreferenze().sameChoices(pref) && this.owner == null) {
			this.owner = new Subscriber(nOwner, pref);
			this.update();
			return true;
		}
		return false;
	}
	/**
	 * Controlla se la proposta ha un proprietario
	 * @return True - la proposta ha un proprietario<br>False - la proposta non ha un proprietario
	 */
	public boolean hasOwner() {
		return (owner == null)?false:true;
	}
	/**
	 * Fa cambiare stato alla proposta
	 */
	public void update() {
		if(aState.transition(this))
			statePassages.add(new Pair<>(aState, LocalDate.now()));
	}
	/**
	 * Imposta un nuovo stato alla proposta
	 * @param nS lo stato da assegnare alla proposta
	 */
	public void setState(State nS) {
		aState = nS;
		statePassages.add(new Pair<>(aState, LocalDate.now()));
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
	public boolean signUp(User user, Preferences preferenza) {
		if(aState.canSignUp(this)){
			if(!isSignedUp(user) && getPreferenze().sameChoices(preferenza)) {
				Subscriber sub = new Subscriber(user, preferenza);
				subscribers.add(sub);
				update();
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * Disiscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 * @return True - l'utente è stato correttamente disiscritto dalla proposta<br>False - l'utente non è stato disiscritto dalla proposta
	 */
	public boolean unsubscribe(User user) {
		if(aState.canSignUp(this)){
			if(!isOwner(user) && isSignedUp(user)) {
				subscribers.remove(subscribers.stream()
												.filter((s) -> s.getUser().equals(user))
												.findFirst().get());
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
	 * Comunica che la proposta sta per essere ritirata.
	 * @return True - la proposta è pronta ad essere ritirata<br>False - la proposta non può essere ritirata
	 */
	public boolean withdraw() {
		return aState.withdraw(this);
	}
	/**
	 * Invia un messaggio a tutti gli iscritti e al proprietario della risposta
	 * @param msg il messaggio da inviare
	 */
	public void send(Message msg) {
		//ARRAY TEMPORANEO
		ArrayList <Subscriber> receivers = new ArrayList <Subscriber>();
		receivers.add(owner);
		receivers.addAll(subscribers);
		receivers.stream()
					.forEach(( s )-> {
							Message m = msg;
							if(msg.getObject().equals(StringConstant.CONFIRMOBJ)) {
								Double sum = Stream.of(s.getPreferenze().getChoices())
													.filter((fh)-> s.getPreferenze().getPreferenza(fh))
													.map((fh)->(Double) getValue(fh.getName()))
													.mapToDouble(Double::doubleValue)
													.sum();
								m.setObject(m.getObject() + String.format("\nValutate le sue scelte relative alle voci opzionali "
																				+ "si ricorda di portare un totale di %s€.",
																				(sum + (Double) getValue(FieldHeading.QUOTA.getName())))
										);
							}
							s.receive(m);
						});
	}
	/**
	 * Restituisce il numero di iscritti alla proposta
	 * @return il numero di iscritti alla proposta
	 */
	public int subNumber() {
		return subscribers.size() + (hasOwner()?1:0);
	}
	/**
	 * Verifica se la proposta è valida
	 * @return True - la proposta è valida<br>False - la proposta è invalida
	 */
	public boolean isValid() {
		return hasOwner() && event.isValid();
	}
	
	/**
	 * Verifica se l'utente è il proprietario della proposta
	 * @param user utente
	 * @return True se proprietario<br> False altrimenti
	 */
	public boolean isOwner(User user) {
		return owner.getUser().equals(user);
	}
	
	/**
	 * Verifica se l'utente è iscritto alla proposta
	 * @param user utente
	 * @return True se iscritto<br> False altrimenti
	 */
	public boolean isSignedUp(User user) {
		return isOwner(user) || subscribers.stream()
											.map((s) -> s.getUser())
											.anyMatch(( u )->u.equals(user));
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Propositore : " + owner + "\n" + event.toString()
					+ "\tIscritti: " + subNumber()
					+ "\n\t" +subscribers.toString() + "\n";
	}
	
	/**
	 * Controlla se la proposta è legata al tipo di evento di cui si è inserito il nome della categoria
	 * @param name il nome della categoria di evento
	 * @return True - la proposta è relativa alla categoria<br>False - la proposta non è legata alla categoria
	 */
	public boolean isCategory(String name) {
		return event.hasName(name);
	}

	/**
	 * Restituisce la lista di iscritti all proposta
	 * @return La lista di utenti iscritti all proposta
	 */
	public ArrayList<User> getSubscribers(){
		//return subscribers;
		return subscribers.stream()
							.map((s) -> s.getUser())
							.collect(Collectors.toCollection(ArrayList :: new));
	}
	
	/**
	 * Restituisce il nome della categoria a cui la proposta fa riferimento
	 * @return il nome della categoria a cui la proposta fa riferimento
	 */
	public String getCategoryName() {
		return event.getName();
	}
	/**
	 * Restituisce l'insieme delle varie opzioni disponibili sulla proposta
	 * @return il set delle varie opzioni
	 */
	public Preferences getPreferenze() {
		return event.getPreferenze();
	}
}
