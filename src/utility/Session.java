package utility;

import java.util.ArrayList;
import java.util.stream.IntStream;

import proposals.Proposal;
import users.User;


/**Classe che consente di tenere in meomoria le proposte di un fruitore non ancora pubblicate, in modo da consentirne una modifica futura<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Session {
	/**
	 * Il nome del fruitore proprietario della sessione
	 */
	private User owner;
	
	/**
	 * Elenco di proposte che il proprietario ha in sospeso nella sessione corrente
	 */
	private ArrayList<Proposal> proposalsSet;
	/**
	 * Costruttore
	 * @param owner Il nome del fruitore proprietario della sessione
	 */
	public Session(User owner) {
		this.owner = owner;
		proposalsSet = new ArrayList<Proposal>();
	}

	/**
	 * Restituisce il proprietario della sessione
	 * @return Nome del fruitore proprietario
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * Restituisce la proposta specificata dall'id
	 * @param id l'identificatore della proposta
	 * @return la proposta identificata dall'identificatore, null se non è valida
	 */
	public Proposal getProposal(int id) {
		if(id < proposalsSet.size()) {
			return proposalsSet.get(id);
		}
		return null;
	}
	/**
	 * Rimuovi la proposta di cui si inserisce l'identificatore
	 * @param id l'identificatore
	 * @return l'esito della rimozione
	 */
	public boolean removeProposal(int id) {
		if(id < proposalsSet.size()) {
			proposalsSet.remove(id);
			return true;
		}
		return false;
	}
	/**
	 * Aggiunge una proposta all'elenco delle proposte
	 * @param proposal Proposta da aggiungere all'elenco
	 * @return L'esito dell'operazione
	 */
	public boolean addProposal(Proposal proposal) {
		return proposalsSet.add(proposal);
	}
	/**
	 * Modifica una proposta esistente 
	 * @param id l'identificatore della proposta da modificare
	 * @param campo Il nome del campo da modificare
	 * @param valore Nuovo valore da sostituire
	 * @return l'esito della modifica
	 */
	public boolean modifyProposal(int id, String campo, Object valore) {
		if(id < proposalsSet.size()) {
			return proposalsSet.get(id).modify(campo, valore);
			}
		return false;
	}
	/**
	 * Mostra le proposte prese su cui l'utente sta attualmente lavorando
	 * @return la rappresentazione testuale della proposte prese in carico
	 */
	public String showInProgress() {
		StringBuilder stringaRitorno = new StringBuilder();
		IntStream.range(0, proposalsSet.size())
					.forEachOrdered((i)->stringaRitorno.append("\n" + i + " : " + proposalsSet.get(i).toString()));
		return stringaRitorno.toString();
	}
	/**
	 * Mostra le notifiche dell'utente a cui la sessione appartiene
	 * @return L'elenco delle notifiche sottoforma di stringa
	 */
	public String showNotification() {
		return owner.showNotifications();
	}
	
	/**
	 * Controlla se la sessione contiene la proposta di cui si è inserito l'identificatore
	 * @param id l'identificatore della proposta
	 * @return True - contiene la proposta<br>False - non contiene la proposta
	 */
	public boolean contains(int id) {
		return id >= 0 && id < proposalsSet.size();
	}
}
