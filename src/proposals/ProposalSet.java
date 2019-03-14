package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

import users.Notifiable;

/**
 * Un InsiemeProposte è un oggetto in grado di gestire un certo set di proposte, tutte quante nello stesso stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class ProposalSet extends ArrayList<Proposal> implements Serializable{
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
	public ProposalSet(State nState) {
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
	public synchronized boolean signUp(int id, Notifiable user) {
		if(id < size())
			return get(id).signUp(user);
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
	 * Genera una nuova istanza di un insieme di proposte
	 * @return una nuova bacheca
	 */
	//da inserire potenzialmente in una Factory
	public static ProposalSet newNoticeBoard() {
		return new ProposalSet(State.OPEN);
	}
}