package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

/**
 * Rappresenta lo stato di una proposta fallita
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Failed implements State, Serializable{

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
		return 2;
	}	
}
