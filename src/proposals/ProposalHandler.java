package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

import users.User;

/**
 * Classe con il compito di gestire un insieme di proposte in base al loro stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class ProposalHandler implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * ArrayList contenente le proposte concluse
	 */
	ProposalSet proposteConcluse;
	/**
	 * ArrayList contenente le proposte fallite
	 */
	ProposalSet proposteFallite;
	/**
	 * ArrayList contenente le proposte ritirate
	 */
	ProposalSet proposteRitirate;
	/**
	 * ArrayList contenente le proposte ritirate
	 */
	ProposalSet proposteChiuse;
	/**
	 * ArrayList contenente le proposte ritirate
	 */
	ProposalSet bacheca;

	/**
	 * Costruttore
	 */
	public ProposalHandler() {
		proposteConcluse = new ProposalSet(State.ENDED);
		proposteFallite = new ProposalSet(State.FAILED);
		proposteRitirate = new ProposalSet(State.WITHDRAWN);
		proposteChiuse = new ProposalSet(State.CLOSED);
		bacheca = new ProposalSet(State.OPEN);
	}
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(Proposal p) {
		if(!bacheca.contains(p)) {
			p.publish();
			if(p.hasState(bacheca.getState())) {
				return bacheca.add(p);
			}
		}
		return false;
	}
	/**
	 * Ritira una proposta dalla bacheca
	 * @param id l'identificatore della proposta da ritirare
	 * @param user l'utente che vuole rimuovere la proposta
	 * @return True - la proposta è stata ritirata<br>False - la proposta non è stata ritirata
	 */
	public boolean withdraw(int id, User user) {
		boolean outcome = false;
		if(contains(id)) {
			if(bacheca.get(id).isOwner(user)) {
				outcome = bacheca.get(id).withdraw();
				bacheca.clean();
			}
		}
		return outcome;
	}
	/**
	 * Iscrivi un utente nella proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da aggiungere alla proposta 
	 * @param preferenze preferenze impostate dall'utente
	 * @return l'esito dell'iscrizione
	 */
	public boolean signUp(int id, User user, OptionsSet preferenze) {
		if(bacheca.contains(id)) 
			if(bacheca.get(id).signUp(user, preferenze)) {
				this.refresh();
				return true;
			}
		return false;
	}
	/**
	 * Discrivi un utente dalla proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da disiscrivere alla proposta 
	 * @return l'esito della disiscrizione
	 */
	public boolean unsubscribe(int id, User user) {
		if(bacheca.contains(id))
			return bacheca.get(id).unsubscribe(user);
		return false;
	}
	/**
	 * Verifica che l'utente passato come parametro sia iscritto alla proposta identificata da id
	 * @param id id della proposta
	 * @param user utente
	 * @return True se iscritto - False se non iscritto
	 */
	public boolean isSignedUp(int id, User user) {
		if(bacheca.contains(id))
			return bacheca.isSignedUp(id,user);
		return false;

	}
	
	/**
	 * Controlla se la proposta di cui si è inseriti l'identificatore è al completo 
	 * @param id l'identificatore
	 * @return True - la proposta è al completo<br>False - la proposta non è al completo
	 */
	public boolean isFull(int id) {
		if(bacheca.contains(id)) 
			return bacheca.get(id).isFull();
		return false;
	}
	
	/**
	 * Ritorna una stringa contenente tutte le proposte a cui è iscritto l'utente passato come parametro
	 * @param user utente
	 * @return stringa di proposte a cui l'utente è iscritto
	 */
	public String showUserSubscription(User user) {
		return bacheca.showUserSubscription(user);
	}
	/**
	 * Ritorna una il numero di proposte a cui l'utente passato per parametro è iscritto
	 * @param user utente
	 * @return numero di iscrizioni dell'utente
	 */
	public int countUserSubscription(User user) {
		return bacheca.countUserSubscription(user);
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public boolean contains(Proposal p) {
		return bacheca.contains(p);
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public boolean contains(int id) {
		return bacheca.contains(id);
	}
	/**
	 * Effettua un update su tutto il set
	 */
	public void refresh() {
		//ritirate fallite concluse non vanno da nessuna parte
		//chiuse vanno in concluse
		//aperte -> fallite/ritirate/chiuse
		bacheca.refresh()
				.stream()
					.forEach(( p )->{
						if(p.hasState(proposteChiuse.getState())) 
							proposteChiuse.add(p);
						else if(p.hasState(proposteFallite.getState()))
							proposteFallite.add(p);
						else if(p.hasState(proposteRitirate.getState()))
							proposteRitirate.add(p);
					});
		proposteChiuse.refresh().stream()
				.forEach(( p )->proposteConcluse.add(p));
	}
	/**
	 * Mostra il contenuto dell'insieme di proposte in forma testuale
	 * @return il contenuto dell'insieme in forma testuale
	 */
	public String showContent() {
		return bacheca.showContent();
	}
	
	/**
	 * Verifica che l'utente passato per parametro sia il proprietario della proposta identificata dall'id
	 * @param id identficatore della proposta
	 * @param user utente
	 * @return True se proprietario <br> False altrimenti
	 */
	public boolean isOwner(int id, User user) {
		if(bacheca.contains(id))
			return bacheca.get(id).isOwner(user);
		return false;
	}
	
	/**
	 * Restituisce una lista di utenti papabili per un invito in base a quelli che hanno partecipate a proposte
	 * presentate dall'utente inserito.<br>
	 * L'utente inserito non fa parte della lista restituita
	 * @param owner utente proprietario di proposte
	 * @param categoryName nome della Categoria
	 * @return lista contente utenti da invitare
	 */
	public ArrayList<User> searchBy(User owner, String categoryName){
		ArrayList<User> userList = new ArrayList<User>();
		Stream.concat(proposteConcluse.stream(), proposteChiuse.stream())
			.filter((p)->p.isOwner(owner))
			.filter((p)->p.hasCategory(categoryName))
			.map((p) -> p.getUsers())
			.forEach((l) -> l.stream()
								.forEach((u) -> {
									if(!userList.contains(u) && !u.equals(owner))
										userList.add(u);
				}));
		return userList;
	}

	/**
	 * Restituisce il nome della categoria della proposta che coincide con id
	 * @param id id della proposta
	 * @return Nome della categoria
	 */
	public String getCategory(int id) {
		if(bacheca.contains(id))
			return bacheca.get(id).getCategoryName();
		return null;
	}
	/**
	 * Restituisce le scelte che sono possibili fare sulle voci opzionali della proposta di cui si è inserito l'identificativo
	 * @param id l'identificativo della proposta interessata
	 * @return l'insieme di scelte che è possibile fare sulla proposta, null se la proposta non esiste
	 */
	public OptionsSet getPreferenze(int id) {
		if(bacheca.contains(id))
			return bacheca.get(id).getOptions();
		return null;
	}
	/**
	 * Invita gli utenti inseriti alla proposta
	 * @param id l'identificatore della proposta a cui invitare gli utenti
	 * @param users gli utenti da invitare alla proposta
	 * @return True - se gli inviti sono stati inviati correttamente<br>False - se gli inviti non sono stati inviati
	 */
	public boolean inviteTo(int id, ArrayList<User> users) {
		if(bacheca.contains(id)) {
			return bacheca.get(id).invite(id, users);
		}
		return false;
	} 
	
	/**
	 * restituisce tutte le proposte contenute nel gestore
	 * @return le proposte contenute nel gestore di proposte
	 */
	public ArrayList<Proposal> getAll() {
		ArrayList<Proposal> all = new ArrayList<Proposal>();
		all.addAll(bacheca);
		all.addAll(proposteChiuse);
		all.addAll(proposteConcluse);
		all.addAll(proposteFallite);
		all.addAll(proposteRitirate);
		return all;
	}
}
