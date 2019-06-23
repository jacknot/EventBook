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
import proposals.states.Invalid;
import proposals.states.State;

/**
 * Una proposta fa riferimento ad un particolare evento e consente di potersi iscrivere ad essa
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Proposal implements ProposalInterface,Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * La categoria a cui la proposta fa riferimento
	 */
	private Category category;
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
	public Proposal(Category category) {
		this.category = category;
		this.owner = null;
		this.subscribers = new ArrayList<Subscriber>();
		this.aState = new Invalid();
		statePassages = new ArrayList<Pair<State, LocalDate>>();
		invitations = new ArrayList<Pair<User, LocalDate>>();
		update();
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#setOwner(users.User, proposals.OptionsSet)
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

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#update()
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
	public boolean equals(Proposal p) {
		return (this.owner.equals(p.owner) && this.category.equals(p.category));
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#setValue(java.lang.String, java.lang.Object)
	 */
	public boolean setValue(String name, Object value) {
		if(aState.canSet()) {
			boolean outcome = category.setValue(name, value);
			update();
			return outcome;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#getValue(java.lang.String)
	 */
	public Object getValue(String name) {
		return category.getValue(name);
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#signUp(users.User, proposals.OptionsSet)
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

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#unsubscribe(users.User)
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

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#hasState(proposals.State)
	 */
	public boolean hasState(State s) {
		return aState.equals(s);
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#publish()
	 */
	public void publish() {
		aState.publish(this);
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#withdraw()
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
				&& category.isValid();
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#isOwner(users.User)
	 */
	public boolean isOwner(User user) {
		return this.owner.getUser().equals(user);
	}
	
	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#isSignedUp(users.User)
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
		return category.toString() + "\n" + "Proposto da : " + owner.getName() + "\n"
					+ "\tIscritti: " + subNumber()
					+ "\n\t" + getSubscribers().collect(Collectors.toCollection(ArrayList::new)).toString();
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#hasCategory(java.lang.String)
	 */
	public boolean hasCategory(String name) {
		return category.hasName(name);
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#getSubscribers()
	 */
	public Stream<Subscriber> getSubscribers(){
		return Stream.concat(Stream.of(owner), subscribers.stream());
	}

	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#getUsers()
	 */
	public ArrayList<User> getUsers(){
		return getSubscribers()
					.map((s) -> s.getUser())
					.collect(Collectors.toCollection(ArrayList :: new));
	}
	
	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#getCategoryName()
	 */
	public String getCategoryName() {
		return category.getName();
	}
	
	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#getOptions()
	 */
	public OptionsSet getOptions() {
		return category.getOptions();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proposals.ProposalInterface#getOwner()
	 */
	public User getOwner() {
		return owner.getUser();
	}
	
	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#isFull()
	 */
	public boolean isFull() {
		return subscribers.size() == ((Integer)this.getValue(FieldHeading.NUMPARTECIPANTI.getName())) 
							+ ((Integer)this.getValue(FieldHeading.TOLL_PARTECIPANTI.getName()));
	}
	
	/* (non-Javadoc)
	 * @see proposals.ProposalInterface#invite(int, java.util.ArrayList)
	 */
	public boolean invite(int id, ArrayList<User> invitedUsers) {
		if(aState.invite(this, id, invitedUsers)) {
			invitedUsers.stream()
							.forEach((u)->invitations.add(new Pair<User, LocalDate>(u, LocalDate.now())));
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proposals.ProposalInterface#getCategory()
	 */
	public Category getCategory() {
		return category;
	}
}
