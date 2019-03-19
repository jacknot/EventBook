package proposals;

import java.io.Serializable;
import java.time.LocalDate;

import fields.FieldHeading;
import users.Message;

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
			LocalDate lastSigningDay = (LocalDate) p.getValue(FieldHeading.TERMINE_RITIRO.getName());
			String title = p.getValue(FieldHeading.TITOLO.getName())== null? UNKNOWN_TITLE:
															(String)p.getValue(FieldHeading.TITOLO.getName());
			int tol = p.getValue(FieldHeading.TOLL_PARTECIPANTI.getName())== null? 0:
				(Integer)p.getValue(FieldHeading.TOLL_PARTECIPANTI.getName());
			int max = (Integer) p.getValue(FieldHeading.NUMPARTECIPANTI.getName());
			//todayDate <= lastSubDate && subs == full
			if((todayDate.compareTo(lastSubDate) == 0 && p.subNumber() - max <= tol) || 
					(todayDate.compareTo(lastSubDate) < 0 && todayDate.compareTo(lastSigningDay) >= 0) && p.subNumber() - max == tol) {
				p.setState(CLOSED);
				p.send(new Message(	//messaggio che avvisa che la proposta è chiusa
						title+": "+LocalDate.now(),
						CONFIRMOBJ,															
						String.format(CONFIRMFORMAT, title,
													p.getValue(FieldHeading.DATA.getName()),
													p.getValue(FieldHeading.ORA.getName()),
													p.getValue(FieldHeading.LUOGO.getName()),
													p.getValue(FieldHeading.QUOTA.getName()))							
						));
				return true;
			//todayDate >= lastSubDate && subs < full
			}else if(todayDate.compareTo(lastSubDate) >= 0 &&
					p.subNumber() < Integer.class.cast(p.getValue(FieldHeading.NUMPARTECIPANTI.getName()))
					) {
				p.setState(FAILED);
				
				p.send(new Message(	//messaggio che avvisa che la proposta è fallita
						title+": "+LocalDate.now(),	
						FAILUREOBJ,
						String.format(FAILUREFORMAT, title)
						));
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
				String title = p.getValue(FieldHeading.TITOLO.getName())== null? UNKNOWN_TITLE:
					(String)p.getValue(FieldHeading.TITOLO.getName());
				p.send(new Message(	//messaggio che avvisa che la proposta è stata ritirata
						title+": "+ LocalDate.now(),
						WITHDRAWNOBJ,															
						String.format(WITHDRAWNFORMAT, title)							
						));
				return true;
			}
			return false;
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
				LocalDate date = LocalDate.class.cast(p.getValue(FieldHeading.DATA.getName()));
				if(tDate.compareTo(date) > 0) {
					p.setState(ENDED);
					return true;
				}
			}
			LocalDate endDate = LocalDate.class.cast(tmp);
			if(tDate.compareTo(endDate) > 0) {
				p.setState(ENDED);
				return true;
			}
			return false;
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
	
	private static final String UNKNOWN_TITLE = "(titolo mancante)";
	private static final String CONFIRMOBJ = "Conferma evento";
	private static final String FAILUREOBJ = "Fallimento evento";
	private static final String WITHDRAWNOBJ = "Ritiro evento";
	//data ora luogo importo
	private static final String CONFIRMFORMAT = "Siamo lieti di confermare che l'evento %s si terrà il giorno %s alle %s in %s."
													+ "\nSi ricorda di portare %s€ per l'organizzazione";
	private static final String FAILUREFORMAT = "Siamo spiacenti di informarla che l'evento %s non ha raggiunto il numero minimo di iscritti."
													+ "\nL'evento è quindi annullato.";
	private static final String WITHDRAWNFORMAT = "Siamo spiacenti di informarla che l'evento %s è stato ritirato dal proprietario."
			+ "\nL'evento è quindi annullato.";
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
}
