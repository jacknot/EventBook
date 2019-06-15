package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Valid implements State, Serializable{
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#canSet()
	 */
	public boolean canSet() {
		return true;
	}
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
	 */
	public boolean transition(Proposal p) {
		if(!p.isValid()) {
			p.setState(new Invalid());
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#pubblica(EventBook.versione2.Proposta)
	 */
	public boolean publish(Proposal p) {
		p.setState(new Open());
		return true;
	}
}
