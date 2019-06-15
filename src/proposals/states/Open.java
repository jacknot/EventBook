package proposals.states;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import dataTypes.Pair;
import fields.FieldHeading;
import proposals.Proposal;
import users.User;
import utility.MessageHandler;
import utility.StringConstant;

public class Open implements State, Serializable{
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#canSubscribe(EventBook.versione2.Proposta)
	 */
	public boolean canSignUp(Proposal p) {
		int tol = p.getValue(FieldHeading.TOLL_PARTECIPANTI.getName())== null? 0:
			(Integer)p.getValue(FieldHeading.TOLL_PARTECIPANTI.getName());
		return p.subNumber() - (Integer)p.getValue(FieldHeading.NUMPARTECIPANTI.getName())	<  tol; 
	}
	/* (non-Javadoc)
	 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
	 */
	public boolean transition(Proposal p) {
		LocalDate todayDate = LocalDate.now();
		LocalDate lastSubDate = (LocalDate) p.getValue(FieldHeading.TERMINEISCRIZIONE.getName());
		LocalDate lastWithdrawalDay = (LocalDate) p.getValue(FieldHeading.TERMINE_RITIRO.getName());
		String title = p.getValue(FieldHeading.TITOLO.getName())== null? StringConstant.UNKNOWN_TITLE:
														(String)p.getValue(FieldHeading.TITOLO.getName());
		int tol = (Integer)p.getValue(FieldHeading.TOLL_PARTECIPANTI.getName());
		int max = (Integer) p.getValue(FieldHeading.NUMPARTECIPANTI.getName());
		
		if((todayDate.compareTo(lastSubDate) >= 0 && p.subNumber() - max <= tol && 0 <= p.subNumber() - max) || 
				(todayDate.compareTo(lastSubDate) < 0 && todayDate.compareTo(lastWithdrawalDay) > 0 && p.subNumber() - max == tol)) {
			p.setState(new Closed());
			
			ArrayList<Pair<User, Double>> user_cost = new ArrayList<Pair<User, Double>>();
			p.getUsers().stream()
							.forEach((u)->user_cost.add(new Pair<User, Double>(u, p.additionalCostsOf(u))));
			new MessageHandler().eventConfirmed(user_cost, 
														title, 
														p.getValue(FieldHeading.DATA.getName()),
														p.getValue(FieldHeading.ORA.getName()),
														p.getValue(FieldHeading.LUOGO.getName()),
														p.getValue(FieldHeading.QUOTA.getName())
										);
			return true;
		//todayDate >= lastSubDate && subs < full
		}else if(todayDate.compareTo(lastSubDate) >= 0 
				&& p.subNumber() < (Integer)p.getValue(FieldHeading.NUMPARTECIPANTI.getName())) {
			p.setState(new Failed());
			new MessageHandler().eventFailed(p.getUsers(), title);
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see proposals.State#withdrawal(proposals.Proposal)
	 */
	public boolean withdraw(Proposal p) {
		if(LocalDate.now().compareTo((LocalDate) p.getValue(FieldHeading.TERMINE_RITIRO.getName())) <= 0) {
			p.setState(new Withdrawn());
			String title = p.getValue(FieldHeading.TITOLO.getName())== null? StringConstant.UNKNOWN_TITLE:
				(String)p.getValue(FieldHeading.TITOLO.getName());
			new MessageHandler().eventWithdrawn(p.getUsers(), title);
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see proposals.State#invite(proposals.Proposal, java.util.ArrayList)
	 */
	public boolean invite(Proposal p, int id, ArrayList<User> invitedU) {
		new MessageHandler().inviteUsers(invitedU, p.getOwner().getName(), id);
		return true;
	}
}
