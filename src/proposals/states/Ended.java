package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Ended implements State, Serializable{

	/* (non-Javadoc)
	 * @see proposals.states.State#transition(proposals.Proposal)
	 */
	public boolean transition(Proposal p) {
		return false;
	}
}
