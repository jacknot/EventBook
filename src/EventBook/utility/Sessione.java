package EventBook.utility;

import java.util.ArrayList;
import java.util.stream.IntStream;

import EventBook.fruitore.Fruitore;
import EventBook.proposta.Proposta;


/**Classe che consente di tenere in meomoria le proposte di un fruitore non ancora pubblicate, in modo da consentirne una modifica futura<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Sessione {
	/**
	 * Il nome del fruitore proprietario della sessione
	 */
	private Fruitore proprietario;
	
	/**
	 * Elenco di proposte che il proprietario ha in sospeso nella sessione corrente
	 */
	private ArrayList<Proposta> insiemeProposte;
	/**
	 * Costruttore
	 * @param _proprietario Il nome del fruitore proprietario della sessione
	 */
	public Sessione(Fruitore _proprietario) {
		proprietario = _proprietario;
		insiemeProposte = new ArrayList<Proposta>();
	}

	/**
	 * Restituisce il proprietario della sessione
	 * @return Nome del fruitore proprietario
	 */
	public Fruitore getProprietario() {
		return proprietario;
	}

	/**
	 * Restituisce la proposta specificata dall'id
	 * @param id l'identificatore della proposta
	 * @return la proposta identificata dall'identificatore, null se non è valida
	 */
	public Proposta getProposta(int id) {
		if(id < insiemeProposte.size()) {
			return insiemeProposte.get(id);
		}
		return null;
	}
	/**
	 * Rimuovi la proposta di cui si inserisce l'identificatore
	 * @param id l'identificatore
	 * @return l'esito della rimozione
	 */
	public boolean removeProposta(int id) {
		if(id < insiemeProposte.size()) {
			insiemeProposte.remove(id);
			return true;
		}
		return false;
	}
	/**
	 * Aggiunge una proposta all'elenco delle proposte
	 * @param proposta Proposta da aggiungere all'elenco
	 * @return L'esito dell'operazione
	 */
	public boolean aggiungiProposta(Proposta proposta) {
		return insiemeProposte.add(proposta);
	}
	/**
	 * Modifica una proposta esistente 
	 * @param id l'identificatore della proposta da modificare
	 * @param campo Il nome del campo da modificare
	 * @param valore Nuovo valore da sostituire
	 * @return l'esito della modifica
	 */
	public boolean modificaProposta(int id, String campo, Object valore) {
		if(id < insiemeProposte.size()) {
			return insiemeProposte.get(id).modifica(campo, valore);
			}
		return false;
	}
	/**
	 * Mostra le proposte prese su cui l'utente sta attualmente lavorando
	 * @return la rappresentazione testuale della proposte prese in carico
	 */
	public String showInProgress() {
		StringBuilder stringaRitorno = new StringBuilder();
		IntStream.range(0, insiemeProposte.size())
					.forEachOrdered((i)->stringaRitorno.append("\n" + i + " : " + insiemeProposte.get(i).toString()));
		return stringaRitorno.toString();
	}
	/**
	 * Mostra le notifiche dell'utente a cui la sessione appartiene
	 * @return L'elenco delle notifiche sottoforma di stringa
	 */
	public String showNotification() {
		return proprietario.getPrivateSpace().toString();
	}
	/**
	 * Controlla se la sessione contiene la proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta
	 * @return True - contiene la proposta<br>False - non contiene la proposta
	 */
	public boolean contains(int id) {
		return id >= 0 && id < insiemeProposte.size();
	}
}
