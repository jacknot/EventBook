package EventBook.versione2.proposta;

import java.util.ArrayList;
import EventBook.versione1.Category;
import EventBook.versione1.campi.ExpandedHeading;
import EventBook.versione2.fruitore.Messaggio;
import EventBook.versione2.proposta.Stato;
import EventBook.versione2.fruitore.Notificabile;

/**
 * Una proposta fa riferimento ad un particolare evento e consente di potersi iscrivere ad essa
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Proposta {
	
	/**
	 * La categoria a cui la proposta fa riferimento
	 */
	private Category evento;
	/**
	 * Lo stato attuale della proposta
	 */
	private Stato aState;
	/**
	 * Il proprietario della proposta
	 */
	private Notificabile proprietario;
	/**
	 * Gli iscritti alla proposta
	 */
	private ArrayList<Notificabile> iscritti;
	/**
	 * Costruttore di una proposta
	 * @param c L'evento a cui far� riferimento la proposta
	 * @param u Il proprietario della proposta
	 */
	public Proposta(Category c, Notificabile u) {
		this.evento = c;
		this.proprietario = u;
		this.aState = Stato.INVALIDA;
		this.iscritti = new ArrayList<Notificabile>();
	}
	/**
	 * Fa cambiare stato alla proposta
	 */
	public void aggiornaStato() {
		aState.transiziona(this);
	}
	/**
	 * Imposta un nuovo stato alla proposta
	 * @param nS lo stato da assegnare alla proposta
	 */
	public void setState(Stato nS) {
		aState = nS;
 	}
	/**
	 * Verifica se due proposte sono uguali
	 * @param p la proposta da confrontare
	 * @return True - sono uguali<br>False - sono diverse
	 */
	public boolean equals(Proposta p) {
		return evento.getValue(ExpandedHeading.TITOLO.getName()).equals(p.evento.getValue(ExpandedHeading.TITOLO.getName()));
	}
	/**
	 * Modifica il campo della proposta di cui si � inserito il nome, se esiste
	 * @param name il nome del campo da modificare
	 * @param value il nuovo valore del campo
	 */
	public void cambia(String name, Object value) {
		if(aState.canSet()) {
			evento.setValue(name, value);
			aggiornaStato();
		}
	}
	/**
	 * Restituisce il contenuto del campo di cui si � inserito il nome
	 * @param name il nome del campo 
	 * @return il contenuto del campo
	 */
	public Object getValue(String name) {
		return evento.getValue(name);
	}
	/**
	 * Iscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 */
	public boolean iscrivi(Notificabile user) {
		if(aState.canSubscribe(this)){
			iscritti.add(user);
			aggiornaStato();
			return true;
		}
		return false; 
	}
	/**
	 * Verifica se la proposta � nello stato inserito
	 * @param s lo stato da controllare
	 * @return True - la proposta � nello stato inserito<br>False - la proposta � in uno stato differente
	 */
	public boolean sameState(Stato s) {
		return aState.equals(s);
	}
	/**
	 * Avvisa la proposta che � stata resa pubblica
	 */
	public void pubblica() {
		aState.pubblica(this);
	}
	/**
	 * Invia un messaggio a tutti gli iscritti e al proprietario della risposta
	 * @param msg il messaggio da inviare
	 */
	public void send(Messaggio msg) {
		proprietario.ricevi(msg);
		iscritti.stream()
					.forEach(( i )-> i.ricevi(msg));
	}
	/**
	 * Restituisce il numero di iscritti alla proposta
	 * @return il numero di iscritti alla proposta
	 */
	public int subNumber() {
		return iscritti.size();
	}
	/**
	 * Verifica se la proposta � valida
	 * @return True - la proposta � valida<br>False - la proposta � invalida
	 */
	public boolean isValida() {
		return evento.isValid();
	}
}