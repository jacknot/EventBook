package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

import users.User;

public class ProposalHandler implements Serializable{

	/**
	 * 
	 */
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
	public synchronized boolean add(Proposal p) {
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
	public synchronized boolean withdraw(int id, User user) {
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
	 * @return l'esito dell'iscrizione
	 */
	public synchronized boolean signUp(int id, User user) {
		if(bacheca.contains(id))
			return bacheca.get(id).signUp(user);
		return false;
	}
	/**
	 * Discrivi un utente dalla proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da disiscrivere alla proposta 
	 * @return l'esito della disiscrizione
	 */
	public synchronized boolean unsubscribe(int id, User user) {
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
	public synchronized boolean isSignedUp(int id, User user) {
		return bacheca.isSignedUp(id,user);

	}
	/**
	 * Ritorna una stringa contenente tutte le proposte a cui è iscritto l'utente passato come parametro
	 * @param user utente
	 * @return strigna di proposte a cui l'utente è iscritto
	 */
	public synchronized String showUserSubscription(User user) {
		return bacheca.showUserSubscription(user);
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public synchronized boolean contains(Proposal p) {
		return bacheca.contains(p);
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public synchronized boolean contains(int id) {
		return bacheca.contains(id);
	}
	/**
	 * Effettua un update su tutto il set
	 */
	public synchronized void refresh() {
		//ritirate fallite concluse non vanno da nessuna parte
		//chiuse vanno in concluse
		//aperte -> fallite/ritirate/chiuse
		bacheca.refresh().stream()
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
	public synchronized String showContent() {
		return bacheca.showContent();
	}
	
	/**
	 * Verifica che l'utente passato per parametro sia il proprietario della proposta identificata dall'id
	 * @return True se proprietario <br> False altrimenti
	 */
	public synchronized boolean isOwner(int id, User user) {
		return bacheca.get(id).isOwner(user);
	}
	
	/**
	 * Restituisce una lista di utenti papabili per un invito in base a quelli che hanno partecipate a proposte
	 * presentate dall'utente inserito
	 * @param owner utente proprietario di proposte
	 * @param categoryName nome della Categoria
	 * @return lista contente utenti da invitare
	 */
	public synchronized ArrayList<User> searchBy(User owner, String categoryName){
		ArrayList<User> userList = new ArrayList<User>();
		/*Stream.concat(proposteConcluse.stream(), proposteChiuse.stream())
			.filter((p)->p.isOwner(user))
			.filter((p)->p.isCategory(categoryName))
			.map((p) -> p.getSubscribers())
			.forEach((l) -> l.stream()
								.forEach((u) -> {
									if(!userList.contains(u))
										userList.add(u);
				}));*/
		ArrayList<Proposal> p = new ArrayList<Proposal>();
		
		p.addAll(proposteConcluse);
		p.addAll(proposteChiuse);
		
		for(Proposal prop : p) {
			if(prop.isOwner(owner) && prop.isCategory(categoryName)) {
				userList.addAll(prop.getSubscribers());
			}
		}
		
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
}
