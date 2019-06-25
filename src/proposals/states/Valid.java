package proposals.states;

import java.io.Serializable;

import proposals.Proposal;

/**
 * Rappresenta lo stato di una proposta valida
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Valid implements State, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	/*
	 * (non-Javadoc)
	 * @see proposals.states.State#getID()
	 */
	@Override
	public int getID() {
		return 5;
	}	
}
