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
				userSubscription.append(String.format(PROPOSAL, i, this.get(i).toString()));
			}
		}
		return userSubscription.toString();
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