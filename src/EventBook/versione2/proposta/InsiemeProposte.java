package EventBook.versione2.proposta;

import java.io.Serializable;
import java.util.HashSet;

import EventBook.versione1.campi.ExpandedHeading;
import EventBook.versione2.fruitore.Notificabile;
import EventBook.versione2.proposta.Stato;

/**
 * Un InsiemeProposte è un oggetto in grado di gestire un certo set di proposte, tutte quante nello stesso stato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class InsiemeProposte extends HashSet<Proposta> implements Serializable{
	//occhio a metodi non voluti/pericolosi
	//definisci interfaccia per limitarli
	/**
	 * 
	 */
	private static final long serialVersionUID = -2744536594935868510L;
	/**
	 * Lo stato che desidero tutte le proste nella lista abbiano
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
	//aggiungere nuove proposte : add(Proposta)
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(Proposta p) {
		if(!contains(p.getValue(ExpandedHeading.TITOLO.getName()))) {
			p.pubblica();
			if(p.sameState(univoco)) {
				super.add(p);
				return true;
			}
		}
		return false;
	}
	//pulire dalle proposte che non piacciono : OK
	/**
	 * Consente di rimuovere tutte le proposte con stato diverso da quello atteso
	 */
	private void clean() {
		this.stream()
				.filter(( p ) -> !p.sameState(univoco))
				.forEach(( p ) -> remove(p));
	}
	//refreshAll : OK
	/**
	 * Effettua un refresh delle proposte nel set
	 */
	public void refresh() {
		this.stream()
				.forEach(( p ) -> p.aggiornaStato());
		clean();
	}
	//iscrivi un Notificabile ad una proposta : OK
	/**
	 * Iscrivi un utente nella proposta di cui si è inserito il titolo
	 * @param title il titolo della proposta a cui aggiungere l'utente
	 * @param user l'utente da aggiungere alla proposta 
	 * @return l'esito dell'iscrizione
	 */
	public boolean iscrivi(String title, Notificabile user) {
		if(contains(title))
			return find(title).iscrivi(user);
		return false;
	}
	//cerca proposta per titolo : OK
	/**
	 * Cerca nel set la proposta avente come titolo quello inserito<br>
	 * Restituisce null se non ce ne sono
	 * @param name il titolo inserito
	 * @return la proposta di cui si è inserito il titolo 
	 */
	private Proposta find(String name) {
		if(contains(name)) 
			return this.stream()
					.filter(( p ) -> p.getValue(ExpandedHeading.TITOLO.getName()).equals(name))
					.findFirst().get();
		return null;
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param name il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public boolean contains(String name) {
		return this.stream()
					.anyMatch(( p ) -> p.getValue(ExpandedHeading.TITOLO.getName()).equals(name));
	}
}