package proposals;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import dataTypes.Pair;
import utility.MessageHandler;
import utility.StringConstant;
import fields.FieldHeading;
import users.User;

/**
 * Contiene un set predefiniti di stati in cui la proposta si può trovare
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum State implements Serializable{
	INVALID{
			/* (non-Javadoc)
			 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
			 */
			public boolean transition(Proposal p) {
				if(p.isValid()) {
					p.setState(VALID);
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
	},
	VALID{
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
				p.setState(INVALID);
				return true;
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#pubblica(EventBook.versione2.Proposta)
		 */
		public boolean publish(Proposal p) {
			p.setState(OPEN);
			return true;
		}
	},
	OPEN{
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
				p.setState(CLOSED);
				
				ArrayList<Pair<User, Double>> user_cost = new ArrayList<Pair<User, Double>>();
				p.getUsers().stream()
								.forEach((u)->user_cost.add(new Pair<User, Double>(u, p.additionalCostsOf(u))));
				MessageHandler.getInstance().eventConfirmed(user_cost, 
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
				p.setState(FAILED);
				MessageHandler.getInstance().eventFailed(p.getUsers(), title);
				return true;
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see proposals.State#withdrawal(proposals.Proposal)
		 */
		public boolean withdraw(Proposal p) {
			if(LocalDate.now().compareTo((LocalDate) p.getValue(FieldHeading.TERMINE_RITIRO.getName())) <= 0) {
				p.setState(WITHDRAWN);
				String title = p.getValue(FieldHeading.TITOLO.getName())== null? StringConstant.UNKNOWN_TITLE:
					(String)p.getValue(FieldHeading.TITOLO.getName());
				MessageHandler.getInstance().eventWithdrawn(p.getUsers(), title);
				return true;
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see proposals.State#invite(proposals.Proposal, java.util.ArrayList)
		 */
		public boolean invite(Proposal p, int id, ArrayList<User> invitedU) {
			MessageHandler.getInstance().inviteUsers(invitedU, p.getOwner().getName(), id);
			return true;
		}
	},
	CLOSED{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transition(Proposal p) {
			LocalDate tDate = LocalDate.now();
			Object tmp = p.getValue(FieldHeading.DATAFINE.getName());
			if(tmp == null) {
				LocalDate date = (LocalDate) p.getValue(FieldHeading.DATA.getName());
				if(tDate.compareTo(date.plusDays(1)) >= 0) {
					p.setState(ENDED);
					return true;
				}
				return false;
			}else {
				LocalDate endDate = (LocalDate)tmp;
				if(tDate.compareTo(endDate.plusDays(1)) >= 0) {
					p.setState(ENDED);
					return true;
				}
				return false;
			}
		}		
	},
	WITHDRAWN{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transition(Proposal p) {
			return false;
		}		
	},
	ENDED{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transition(Proposal p) {
			return false;
		}
	},
	FAILED{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transition(Proposal p) {
			return false;
		}
	};
	
	/**
	 * Modifica lo stato della proposta in modo da poterla rendere adatta al pubblico
	 * @param p la proposta a cui fare cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public boolean publish(Proposal p) {
		return false;
	}
	/**
	 * Porta la proposta p in nuovo stato
	 * @param p la proposta a cui far cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public abstract boolean transition(Proposal p);
	/**
	 * Verifica se lo stato attuale consente alla proposta di cambiare
	 * @return True - lo consente<br>False - non lo consente
	 */
	public boolean canSet() {
		return false;
	}
	
	/**
	 * Verifica se la proposta è nello stato di potersi iscrivere
	 * @param p la proposta inserita
	 * @return True - ci si può iscrivere alla proposta<br>False - non ci si può iscrivere alla proposta
	 */
	public boolean canSignUp(Proposal p) {
		return false;
	}
	
	/**
	 * Modifica lo stato della proposta in modo da ritirarla
	 * @param p la proposta da ritirare
	 * @return True - se è avvenuto il ritiro della proposta<br>False - la proposta non è stata ritirata
	 */
	public boolean withdraw(Proposal p) {
		return false;
	}
	
	/**
	 * Invita un insieme di utenti alla proposta
	 * @param p la proposta a cui invitare gli utenti
	 * @param id l'identificativo della proposta
	 * @param invitedU gli utenti da invitare
	 * @return True - se gli utenti vengono invitati con successo<br>False - se gli utenti non vengono invitati
	 */
	public boolean invite(Proposal p, int id, ArrayList<User> invitedU) {
		return false;
	}
}
