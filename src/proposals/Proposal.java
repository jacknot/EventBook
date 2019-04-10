package proposals;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import categories.Category;
import dataTypes.Pair;
import fields.FieldHeading;
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
	 * Contiene informazioni su tutti gli utenti invitati a questa proposta
	 */
	private ArrayList<Pair<User, LocalDate>> invitations;
	/**
	 * Costruttore di una proposta
	 * @param event L'evento a cui farà riferimento la proposta
	 */
	public Proposal(Category event) {
		this.event = event;
		this.owner = null;
		this.subscribers = new ArrayList<Subscriber>();
		this.aState = State.INVALID;
		statePassages = new ArrayList<Pair<State, LocalDate>>();
		invitations = new ArrayList<Pair<User, LocalDate>>();
		update();
	}
	
	/**
	 * Imposta il proprietario della proposta
	 * @param nOwner il nuovo proprietario della proposta
	 * @param pref le preferenze espresse dal proprietario
	 * @return True - l'utente inserito è stato impostato come proprietario<br>False - l'utente inserito non è stato inserito come proprietario
	 */
	public boolean setOwner(User nOwner, OptionsSet pref) {
		if(getOptions().hasSameChoices(pref) && this.owner == null) {
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
		State oldState = aState;
		if(aState.transition(this))
			if(!oldState.equals(aState))
				statePassages.add(new Pair<>(aState, LocalDate.now()));
	}
	/**
	 * Imposta un nuovo stato alla proposta
	 * @param nS lo stato da assegnare alla proposta
	 */
	public void setState(State nS) {
		State oldState = aState;
		if(!oldState.equals(nS)) {
			aState = nS;
			statePassages.add(new Pair<>(aState, LocalDate.now()));
		}
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
	public boolean setValue(String name, Object value) {
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
	 * @param choices le scelte effettuate dall'utente
	 * @return True - l'utente è stato correttamente iscritto alla proposta<br>False - l'utente non è stato iscritto alla proposta
	 */
	public boolean signUp(User user, OptionsSet choices) {
		if(aState.canSignUp(this)){
			if(!isSignedUp(user) && getOptions().hasSameChoices(choices)) {
				Subscriber sub = new Subscriber(user, choices);
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
	 * Calcola i costi addizionali che un dato utente deve sostenere
	 * @param u l'utente di cui si vogliono conoscere i costi addizionali
	 * @return i costi addizionali che l'utente inserito deve sostenere
	 */
	public Double additionalCostsOf(User u) {
		if(!isSignedUp(u))
			return 0.00;
		Subscriber s = getSubscribers()
						.filter((sub)->sub.getUser().equals(u))
						.findFirst().get();
		return Stream.of(s.getChoices().getOptions())
						.filter((fh)-> s.getChoices().getChoice(fh))
						.map((fh)->(Double) getValue(fh.getName()))
						.mapToDouble(Double::doubleValue)
						.sum();
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
		return hasOwner() 
				&& event.isValid();
	}
	
	/**
	 * Verifica se l'utente è il proprietario della proposta
	 * @param user utente
	 * @return True se proprietario<br> False altrimenti
	 */
	public boolean isOwner(User user) {
		return this.owner.getUser().equals(user);
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
	public boolean hasCategory(String name) {
		return event.hasName(name);
	}
	/**
	 * Restituisce lo stream di tutti gli iscritti, propositore compreso
	 * @return lo stream di tutti gli iscritti
	 */
	public Stream<Subscriber> getSubscribers(){
		return Stream.concat(subscribers.stream(), Stream.of(owner));
	}

	/**
	 * Restituisce la lista di iscritti all proposta, compreso il creatore della proposta
	 * @return La lista di utenti iscritti all proposta
	 */
	public ArrayList<User> getUsers(){
		return getSubscribers()
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
	public OptionsSet getOptions() {
		return event.getOptions();
	}
	
	/**
	 * Restituisce il proprietario della proposta
	 * @return il proprietario della proposta
	 */
	public User getOwner() {
		return owner.getUser();
	}
	
	/**
	 * Controlla se la proposta è al completo 
	 * @return True - la proposta è al completo<br>False - la proposta non è al completo
	 */
	public boolean isFull() {
		return subscribers.size() == ((Integer)this.getValue(FieldHeading.NUMPARTECIPANTI.getName())) 
							+ ((Integer)this.getValue(FieldHeading.TOLL_PARTECIPANTI.getName()));
	}
	
	/**
	 * Invita un gruppo di utenti alla proposta
	 * @param id l'identificativo della proposta
	 * @param invitedUsers gli utenti da invitare
	 * @return True - se gli inviti vengono inviati correttamente<br>False - se gli inviti non vengono inviati
	 */
	public boolean invite(int id, ArrayList<User> invitedUsers) {
		if(aState.invite(this, id, invitedUsers)) {
			invitedUsers.stream()
							.forEach((u)->invitations.add(new Pair<User, LocalDate>(u, LocalDate.now())));
			return true;
		}
		return false;
	}
}
