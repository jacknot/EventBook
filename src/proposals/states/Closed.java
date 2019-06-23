package proposals.states;

import java.io.Serializable;
import java.time.LocalDate;

import fields.FieldHeading;
import proposals.Proposal;

public class Closed implements State, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see proposals.states.State#transition(proposals.Proposal)
	 */
	public boolean transition(Proposal p) {
		LocalDate tDate = LocalDate.now();
		Object tmp = p.getValue(FieldHeading.DATAFINE.getName());
		if(tmp == null) {
			LocalDate date = (LocalDate) p.getValue(FieldHeading.DATA.getName());
			if(tDate.compareTo(date.plusDays(1)) >= 0) {
				p.setState(new Ended());
				return true;
			}
			return false;
		}else {
			LocalDate endDate = (LocalDate)tmp;
			if(tDate.compareTo(endDate.plusDays(1)) >= 0) {
				p.setState(new Ended());
				return true;
			}
			return false;
		}
	}

	@Override
	public int getID() {
		return 0;
	}	
}
