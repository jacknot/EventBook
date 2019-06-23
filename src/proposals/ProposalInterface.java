package proposals;

import java.util.ArrayList;
import java.util.stream.Stream;

import categories.Category;
import users.Subscriber;
import users.User;
import proposals.states.State;

/**
 * Interfaccia che definisce il comportamento delle Proposte. 
 * Segue il principio DIP (Dependency Inversion Principle).
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface ProposalInterface {
	/**
	 * Restituisce l'insieme delle varie opzioni disponibili sulla proposta
	 * @return il set delle varie opzioni
	 */
	public OptionsSet getOptions();
	/**
	 * Imposta il proprietario della proposta
	 * @param nOwner il nuovo proprietario della proposta
	 * @param pref le preferenze espresse dal proprietario
	 * @return True - l'utente inserito è stato impostato come proprietario<br>False - l'utente inserito non è stato inserito come proprietario
	 */
	public boolean setOwner(User nOwner, OptionsSet pref);	
	/**
	 * Verifica se la proposta è nello stato inserito
	 * @param s lo stato da controllare
	 * @return True - la proposta è nello stato inserito<br>False - la proposta è in uno stato differente
	 */
	public boolean hasState(State s);
	/**
	 * Fa cambiare stato alla proposta
	 */
	public void update() ;
	/**
	 * Verifica se l'utente è iscritto alla proposta
	 * @param user utente
	 * @return True se iscritto<br> False altrimenti
	 */
	public boolean isSignedUp(User user);
	/**
	 * Restituisce il nome della categoria a cui la proposta fa riferimento
	 * @return il nome della categoria a cui la proposta fa riferimento
	 */
	public String getCategoryName();
	/**
	 * Modifica il campo della proposta di cui si � inserito il nome, se esiste
	 * @param name il nome del campo da modificare
	 * @param value il nuovo valore del campo
	 * @return l'esito della modifica
	 */
	public boolean setValue(String name, Object value);
	/**
	 * Avvisa la proposta che è stata resa pubblica
	 */
	public void publish();
	/**
	 * Verifica se l'utente è il proprietario della proposta
	 * @param user utente
	 * @return True se proprietario<br> False altrimenti
	 */
	public boolean isOwner(User user);
	/**
	 * Comunica che la proposta sta per essere ritirata.
	 * @return True - la proposta è pronta ad essere ritirata<br>False - la proposta non può essere ritirata
	 */
	public boolean withdraw();
	/**
	 * Iscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 * @param choices le scelte effettuate dall'utente
	 * @return True - l'utente è stato correttamente iscritto alla proposta<br>False - l'utente non è stato iscritto alla proposta
	 */
	public boolean signUp(User user, OptionsSet choices);
	/**
	 * Disiscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 * @return True - l'utente è stato correttamente disiscritto dalla proposta<br>False - l'utente non è stato disiscritto dalla proposta
	 */
	public boolean unsubscribe(User user);
	/**
	 * Controlla se la proposta è al completo 
	 * @return True - la proposta è al completo<br>False - la proposta non è al completo
	 */
	public boolean isFull();
	/**
	 * Restituisce la lista di iscritti all proposta, compreso il creatore della proposta
	 * @return La lista di utenti iscritti all proposta
	 */
	public ArrayList<User> getUsers();
	/**
	 * Controlla se la proposta è legata al tipo di evento di cui si è inserito il nome della categoria
	 * @param name il nome della categoria di evento
	 * @return True - la proposta è relativa alla categoria<br>False - la proposta non è legata alla categoria
	 */
	public boolean hasCategory(String name);
	/**
	 * Invita un gruppo di utenti alla proposta
	 * @param id l'identificativo della proposta
	 * @param invitedUsers gli utenti da invitare
	 * @return True - se gli inviti vengono inviati correttamente<br>False - se gli inviti non vengono inviati
	 */
	public boolean invite(int id, ArrayList<User> invitedUsers);
	/**
	 * Restituisce il contenuto del campo di cui si è inserito il nome
	 * @param name il nome del campo 
	 * @return il contenuto del campo
	 */
	public Object getValue(String name);
	/**
	 * Restituisce lo stream di tutti gli iscritti, propositore compreso
	 * @return lo stream di tutti gli iscritti
	 */
	public Stream<Subscriber> getSubscribers();
	
	/**
	 * Restituisce il proprietario della proposta
	 * @return il proprietario della proposta
	 */
	public User getOwner();
	
	/**
	 * Restituisce la categoria della proposta
	 * @return categoria della proposta
	 */
	public Category getCategory();
	
	
	public default boolean equals(ProposalInterface pi) {
		return (this.getOwner().equals(pi.getOwner()) && this.getCategory().equals(pi.getCategory()));
	}
}
