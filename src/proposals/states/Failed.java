package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Failed implements State, Serializable{
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
	 */
	public boolean transition(Proposal p) {
		return false;
	}
}
