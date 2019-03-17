package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

import users.User;

/**
 * Un InsiemeProposte è un oggetto in grado di gestire un certo set di proposte, tutte quante nello stesso stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class NoticeBoard extends ArrayList<Proposal> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744536594935868510L;
	/**
	 * Formattazione per la visualizzazione testuale della singola proposta
	 */
	private static final String PROPOSAL = "\n%d : %s";
	
	/**
	 * Lo stato che desidero tutte le proposte nella lista abbiano
	 */
	private final State state;
	
	/**
	 * Costruttore
	 * @param nState lo stato in cui devono essere tutte le proposte inserite
	 */
	public NoticeBoard(State nState) {
		super();
		this.state = nState;
	}
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public synchronized boolean add(Proposal p) {
		if(!contains(p)) {
			p.publish();
			if(p.hasState(state)) {
				return super.add(p);
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
			if(this.get(id).isOwner(user)) {
				outcome = this.get(id).withdraw();
				clean();
			}
		}
		return outcome;
	}
	/**
	 * Consente di rimuovere tutte le proposte con stato diverso da quello atteso
	 */
	private  void clean() {
		ArrayList<Proposal> toClean = new ArrayList<Proposal>();
		this.stream()
			.filter((p)->!p.hasState(state))
			.forEach((p)->toClean.add(p));
		removeAll(toClean);
	}
	/**
	 * Effettua un refresh delle proposte nel set
	 */
	public synchronized void refresh() {
		this.stream()
				.forEach(( p ) -> p.update());
		clean();
	}
	/**
	 * Iscrivi un utente nella proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da aggiungere alla proposta 
	 * @return l'esito dell'iscrizione
	 */
	public synchronized boolean signUp(int id, User user) {
		if(contains(id))
			return get(id).signUp(user);
		return false;
	}
	
	/**
	 * Discrivi un utente dalla proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da disiscrivere alla proposta 
	 * @return l'esito della disiscrizione
	 */
	public synchronized boolean unsubscribe(int id, User user) {
		if(contains(id))
			return get(id).unsubscribe(user);
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
			return get(id).isSignedUp(user);
		return false;
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public synchronized boolean contains(Proposal p) {
		return this.stream()
					.anyMatch(( sp ) -> sp.equals(p));
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public synchronized boolean contains(int id) {
		return id >= 0 && id < size();
	}
	/**
	 * Mostra il contenuto dell'insieme di proposte in forma testuale
	 * @return il contenuto dell'insieme in forma testuale
	 */
	public synchronized String showContent() {
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, size())
					.forEachOrdered((i)->sb.append(String.format(PROPOSAL, i, get(i).toString())));
		return sb.toString();
	}
	
	/**
	 * Ritorna una stringa contenente tutte le proposte a cui è iscritto l'utente passato come parametro
	 * @param user utente
	 * @return strigna di proposte a cui l'utente è iscritto
	 */
	public synchronized String showUserSubscription(User user) {
		StringBuilder userSubscription = new StringBuilder("Proposte a cui sei iscritto:");
		for(int i=0; i<this.size(); i++) {
			if(this.get(i).isSignedUp(user)) {
				userSubscription.append(String.format("\n%d : %s", i, this.get(i).toString()));
			}
		}
		return userSubscription.toString();
	}
	
	/**
	 * Genera una nuova istanza di un insieme di proposte
	 * @return una nuova bacheca
	 */
	//da inserire potenzialmente in una Factory
	public static NoticeBoard newNoticeBoard() {
		return new NoticeBoard(State.OPEN);
	}
}