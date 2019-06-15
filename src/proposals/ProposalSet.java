package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import users.User;

/**
 * Classe in grado di gestire un certo set di proposte, tutte nello stesso stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class ProposalSet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744536594935868510L;
	/**
	 * Formattazione per la visualizzazione testuale della singola proposta
	 */
	private static final String PROPOSAL = "\n[id : %d] %s\n-";
	/**
	 * Lo stato che desidero tutte le proposte nella lista abbiano
	 */
	private final State state;
	/**
	 * 
	 */
	private ArrayList<Proposal> set;
	/**
	 * Costruttore
	 * @param nState lo stato in cui devono essere tutte le proposte inserite
	 */
	public ProposalSet(State nState) {
		set = new ArrayList<>();
		this.state = nState;
	}
	/**
	 * Consente di rimuovere tutte le proposte con stato diverso da quello atteso
	 */
	public ArrayList<Proposal> clean() {
		ArrayList<Proposal> toClean = new ArrayList<Proposal>();
		this.set.stream()
			.filter((p)->!p.hasState(state))
			.forEach((p)->toClean.add(p));
		this.set.removeAll(toClean);
		return toClean;
	}
	/**
	 * Effettua un refresh delle proposte nel set
	 * @return lista di proposte rimaste in bacheca
	 */
	public ArrayList<Proposal> refresh() {
		this.set.stream()
				.forEach(( p ) -> p.update());
		return clean();
	}
	/**
	 * Mostra il contenuto dell'insieme di proposte in forma testuale
	 * @return il contenuto dell'insieme in forma testuale
	 */
	public String showContent() {
		sort();
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, this.set.size())
					.forEachOrdered((i)->sb.append(String.format(PROPOSAL, i, this.set.get(i).toString())));
		return sb.toString();
	}	 
	/**
	 * Restuisce lo stato che devono avere tutte le proposte
	 * @return stato
	 */
	public State getState() {
		return state;
	}
	/**
	 * Restituisce il contenuto del Set sotto forma di ArrayList
	 * @return Contenuto del Set
	 */
	public ArrayList<Proposal> getSet() {
		return set;
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public boolean contains(Proposal p) {
		return this.set.stream()
					.anyMatch(( sp ) -> sp.equals(p));
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public boolean contains(int id) {
		return id >= 0 && id < this.set.size();
	}
	/**
	 * Ritorna una stringa contenente tutte le proposte a cui è iscritto l'utente passato come parametro
	 * @param user utente
	 * @return strigna di proposte a cui l'utente è iscritto
	 */
	public String showUserSubscription(User user) {
		StringBuilder sb = new StringBuilder("Proposte a cui sei iscritto:");
		IntStream.range(0, this.set.size())
					.filter((i)->this.set.get(i).isSignedUp(user))
					.forEach((i)->sb.append(String.format(PROPOSAL, i, this.set.get(i).toString())));
		return sb.toString();
	}
	
	/**
	 * Ritorna una il numero di proposte a cui l'utente passato per parametro è iscritto
	 * @param user utente
	 * @return numero di iscrizioni dell'utente
	 */
	public int countUserSubscription(User user) {
		return (int) IntStream.range(0, this.set.size())
					.filter((i)->this.set.get(i).isSignedUp(user))
					.count();
	}
	
	/**
	 * Ordina le proposte nel ProposalSet in base alla categoria
	 */
	private void sort() {
		set.sort((p1, p2) -> p1.getCategoryName().compareTo(p2.getCategoryName()));
	}	
	
	/**
	 * Verifica che l'utente passato come parametro sia iscritto alla proposta identificata da id
	 * @param id id della proposta
	 * @param user utente
	 * @return True se iscritto - False se non iscritto
	 */
	public boolean isSignedUp(int id, User user) {
		if(contains(id))
			return this.set.get(id).isSignedUp(user);
		return false;
	}
	
	/**
	 * Aggiunge una proposta al set
	 * @param p la proposta da aggiungere
	 * @return True - se la proposta è stata aggiunta con successo<br>False - se la proposta non è stata aggiunta
	 */
	public boolean add(Proposal p) {
		return this.set.add(p);
	}
	
	/**
	 * Restituisce uno stream sequenziale usando il set come risorsa
	 * @return lo stream sequenziale
	 */
	public Stream<Proposal> stream() {
		return set.stream();
	}
	
	/**
	 * Restituisce la proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta
	 * @return La proposta di cui si è inserito l'identificatore.
	 * 			<br>Restituisce null se l'identificatore non è valido
	 */
	public Proposal get(int id) {
		return this.set.get(id);
	}
	/**
	 * Aggiunge tutte le proposte contenuto nel set inserito
	 * @param ps il set di cui si vogliono inserire le proposte
	 * @return True - se sono state aggiunte con successo<br>False - se non sono state aggiunte
	 */
	public boolean addAll(ProposalSet ps) {
		return this.set.addAll(ps.set);
	}
}