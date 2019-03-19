package proposals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

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
	protected Proposal[] clean() {
		ArrayList<Proposal> toClean = new ArrayList<Proposal>();
		this.stream()
			.filter((p)->!p.hasState(state))
			.forEach((p)->toClean.add(p));
		this.removeAll(toClean);
		return (Proposal[]) toClean.toArray();
	}
	/**
	 * Effettua un refresh delle proposte nel set
	 */
	public Proposal[] refresh() {
		this.stream()
				.forEach(( p ) -> p.update());
		return clean();
	}
	/**
	 * Mostra il contenuto dell'insieme di proposte in forma testuale
	 * @return il contenuto dell'insieme in forma testuale
	 */
	public String showContent() {
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, this.size())
					.forEachOrdered((i)->sb.append(String.format(PROPOSAL, i, this.get(i).toString())));
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
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public boolean contains(Proposal p) {
		return this.stream()
					.anyMatch(( sp ) -> sp.equals(p));
	}
	/**
	 * Controlla che la proposta di cui si è inserito l'identificatore faccia parte del set
	 * @param id l'identificatore della proposta da controllare
	 * @return True - il set contiene la proposta<br>False - il set non contiene la proposta
	 */
	public boolean contains(int id) {
		return id >= 0 && id < this.size();
	}
}