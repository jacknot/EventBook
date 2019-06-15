package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Valid implements State, Serializable{

	/* (non-Javadoc)
	 * @see proposals.states.State#canSet()
	 */
	public boolean canSet() {
		return true;
	}

	/* (non-Javadoc)
	 * @see proposals.states.State#transition(proposals.Proposal)
	 */
	public boolean transition(Proposal p) {
		if(!p.isValid()) {
			p.setState(new Invalid());
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see proposals.states.State#publish(proposals.Proposal)
	 */
	public boolean publish(Proposal p) {
		p.setState(new Open());
		return true;
	}
}
