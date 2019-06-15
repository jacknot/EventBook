package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Invalid implements State,Serializable{
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
	 */
	public boolean transition(Proposal p) {
		if(p.isValid()) {
			p.setState(new Valid());
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#canSet()
	 */
	public boolean canSet() {
		return true;
	}
}
