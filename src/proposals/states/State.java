package proposals.states;

import java.util.ArrayList;

import proposals.Proposal;
import users.User;

public interface State {
	/**
	 * Modifica lo stato della proposta in modo da poterla rendere adatta al pubblico
	 * @param p la proposta a cui fare cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public default boolean publish(Proposal p) {
		return false;
	}
	/**
	 * Porta la proposta p in nuovo stato
	 * @param p la proposta a cui far cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public boolean transition(Proposal p);
	/**
	 * Verifica se lo stato attuale consente alla proposta di cambiare
	 * @return True - lo consente<br>False - non lo consente
	 */
	public default boolean canSet() {
		return false;
	}
	
	/**
	 * Verifica se la proposta è nello stato di potersi iscrivere
	 * @param p la proposta inserita
	 * @return True - ci si può iscrivere alla proposta<br>False - non ci si può iscrivere alla proposta
	 */
	public default boolean canSignUp(Proposal p) {
		return false;
	}
	
	/**
	 * Modifica lo stato della proposta in modo da ritirarla
	 * @param p la proposta da ritirare
	 * @return True - se è avvenuto il ritiro della proposta<br>False - la proposta non è stata ritirata
	 */
	public default boolean withdraw(Proposal p) {
		return false;
	}
	
	/**
	 * Invita un insieme di utenti alla proposta
	 * @param p la proposta a cui invitare gli utenti
	 * @param id l'identificativo della proposta
	 * @param invitedU gli utenti da invitare
	 * @return True - se gli utenti vengono invitati con successo<br>False - se gli utenti non vengono invitati
	 */
	public default boolean invite(Proposal p, int id, ArrayList<User> invitedU) {
		return false;
	}
	
	/**
	 * Verifica che lo stato sia uguale allo stato passato per parametro
	 * @param otherState Statto da confrontare
	 * @return True - se uguali <br> False - altrimenti
	 */
	public default boolean equals(State otherState) {
		return this.getID() == otherState.getID();
	}
	
	/**
	 * Restituisce l'identificatore associato allo Stato
	 * @return identificatore numerico
	 */
	public int getID();
}
