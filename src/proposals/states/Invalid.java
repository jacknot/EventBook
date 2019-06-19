package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Invalid implements State,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see proposals.states.State#transition(proposals.Proposal)
	 */
	public boolean transition(Proposal p) {
		if(p.isValid()) {
			p.setState(new Valid());
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see proposals.states.State#canSet()
	 */
	public boolean canSet() {
		return true;
	}
}
