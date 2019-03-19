package proposals;

import java.util.stream.IntStream;

import users.User;

public class ProposalHandler {
	/**
	 * Formattazione per la visualizzazione testuale della singola proposta
	 */
	private static final String PROPOSAL = "\n%d : %s";
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
		if(contains(id))
			return bacheca.get(id).isSignedUp(user);
		return false;
	}
	/**
	 * Ritorna una stringa contenente tutte le proposte a cui è iscritto l'utente passato come parametro
	 * @param user utente
	 * @return strigna di proposte a cui l'utente è iscritto
	 */
	public synchronized String showUserSubscription(User user) {
		StringBuilder sb = new StringBuilder("Proposte a cui sei iscritto:");
		IntStream.range(0, bacheca.size())
					.filter((i)->bacheca.get(i).isSignedUp(user))
					.forEach((i)->sb.append(String.format(PROPOSAL, i, bacheca.get(i).toString())));
		return sb.toString();
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public synchronized boolean contains(Proposal p) {
		return bacheca.stream()
					.anyMatch(( sp ) -> sp.equals(p));
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public synchronized boolean contains(int id) {
		return id >= 0 && id < bacheca.size();
	}
}
