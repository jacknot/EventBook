package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

public class Withdrawn implements State, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see proposals.states.State#transition(proposals.Proposal)
	 */
	public boolean transition(Proposal p) {
		return false;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see proposals.states.State#getID()
	 */
	@Override
	public int getID() {
		return 6;
	}	
}
