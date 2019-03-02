package EventBook.versione2;

import java.util.ArrayList;

import EventBook.versione1.campi.ExpandedHeading;
import EventBook.versione2.fruitore.Stato;

/**
 * Un InsiemeProposte è un oggetto in grado di gestire un certo set di proposte, tutte quante nello stesso stato
 * @author Matteo Salvalai[715827]
 *
 */
public class InsiemeProposte extends ArrayList<Proposta>{
	
	private final Stato univoco;
	
	public InsiemeProposte(Stato nState) {
		super();
		univoco = nState;
	}
	//aggiungere nuove proposte : add(Proposta)
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	//precondizione: La proposta sia nello stato corretto quando inserita
	public boolean add(Proposta p) {
		if(!contains(p.getValue(ExpandedHeading.TITOLO.getName()))) {
			super.add(p);
			p.aggiornaStato();
			return true;
		}
		return false;
	}
	//rimuovere proposta : remove(String) : OK
	/**
	 * Rimuove la proposta di cui si è inserito il titolo, se presente
	 * @param title il titolo della proposta da eliminare
	 * @return l'esito della rimozione
	 */
	public boolean remove(String title) {
		return remove(find(title));
	}
	//pulire dalle proposte che non piacciono : OK
	public void clean() {
		this.stream()
				.filter(( p ) -> !p.sameState(univoco))
				.forEach(( p ) -> remove(p));
	}
	//refresh di singola proposta : OK
	/**
	 * Effettua il refresh solo sulla proposta di cui si è inserito il titolo
	 * @param title il titolo della proposta su cui fare refresh
	 */ 
	public void refresh(String title) {
		if(contains(title))
			this.stream()
					.filter(( p ) -> p.getValue(ExpandedHeading.TITOLO.getName()).equals(title))
					.findFirst()
					.get().aggiornaStato();
	}
	//refreshAll : OK
	/**
	 * Effettua un refresh su tutte le proposte
	 */
	public void refreshAll() {
		this.stream()
				.forEach(( p ) -> p.aggiornaStato());
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