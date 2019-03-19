package ClassiPerV4;

import java.util.ArrayList;

import proposals.Proposal;
import proposals.State;
import users.User;

/**
 * Uno Storico è un oggetto in grado di gestire un insieme di Proposte.
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Storico {
	/**
	 * ArrayList contenente le proposte concluse
	 */
	ArrayList <Proposal> proposteConcluse; //Vedere se mettere anche le chiuse
	/**
	 * ArrayList contenente le proposte fallite
	 */
	ArrayList <Proposal> proposteFallite;
	/**
	 * ArrayList contenente le proposte ritirate
	 */
	ArrayList <Proposal> proposteRitirate;
	/**
	 * Costruttore
	 */
	public Storico() {
		proposteConcluse = new ArrayList <Proposal>();
		proposteFallite = new ArrayList <Proposal>();
		proposteRitirate = new ArrayList <Proposal>();
	}
	/**
	 * Aggiunge la proposta passata in ingresso al corretto ArrayList
	 * @param proposal Proposta passata in ingresso
	 * @return True - la proposta è stata aggiunta correttamente ad un ArrayList<br>False - Altrimenti
	 */
	public boolean addProposal(Proposal proposal) {
		if(proposal.hasState(State.ENDED)) {
			return proposteConcluse.add(proposal);
		}
		/*else if(proposal.hasState(State.FALLITA???????????)) {
			return proposteFallite.add(proposal);
		}*/
		else if(proposal.hasState(State.WITHDRAWN)) {
			return proposteRitirate.add(proposal);
		}
		else {
			return false;
		}
	}
	
	public ArrayList<User> ricercaUtenti(User owner, String nomeCategoria){
		//INVIA MESSAGGI AI CONOSCENTI
		return null;
	}
}
