package EventBook.proposta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

import EventBook.fruitore.Notificabile;

/**
 * Un InsiemeProposte è un oggetto in grado di gestire un certo set di proposte, tutte quante nello stesso stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class InsiemeProposte extends ArrayList<Proposta> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744536594935868510L;
	/**
	 * Lo stato che desidero tutte le proposte nella lista abbiano
	 */
	private final Stato univoco;
	
	/**
	 * Costruttore
	 * @param nState lo stato in cui devono essere tutte le proposte inserite
	 */
	public InsiemeProposte(Stato nState) {
		super();
		univoco = nState;
	}
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public synchronized boolean add(Proposta p) {
		if(!contains(p)) {
			p.pubblica();
			if(p.sameState(univoco)) {
				return super.add(p);
			}
		}
		return false;
	}
	/**
	 * Consente di rimuovere tutte le proposte con stato diverso da quello atteso
	 */
	private  void clean() {
		ArrayList<Proposta> toClean = new ArrayList<Proposta>();
		this.stream()
			.filter((p)->!p.sameState(univoco))
			.forEach((p)->toClean.add(p));
		removeAll(toClean);
	}
	/**
	 * Effettua un refresh delle proposte nel set
	 */
	public synchronized void refresh() {
		this.stream()
				.forEach(( p ) -> p.aggiornaStato());
		clean();
	}
	/**
	 * Iscrivi un utente nella proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta a cui aggiungere l'utente
	 * @param user l'utente da aggiungere alla proposta 
	 * @return l'esito dell'iscrizione
	 */
	public synchronized boolean iscrivi(int id, Notificabile user) {
		if(id < size())
			return get(id).iscrivi(user);
		return false;
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param p il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public synchronized boolean contains(Proposta p) {
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
					.forEachOrdered((i)->sb.append("\n" + i + " : " + get(i).toString()));
		return sb.toString();
	}
	/**
	 * Genera una nuova istanza di un insieme di proposte
	 * @return una nuova bacheca
	 */
	//da inserire potenzialmente in una Factory
	public static InsiemeProposte newBacheca() {
		return new InsiemeProposte(Stato.APERTA);
	}
}