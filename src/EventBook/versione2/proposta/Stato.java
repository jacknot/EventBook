package EventBook.versione2.proposta;

import java.time.LocalDate;
import EventBook.versione1.campi.ExpandedHeading;
import EventBook.versione2.fruitore.Messaggio;
import EventBook.versione2.proposta.Proposta;

/**
 * Contiene un set predefiniti di stati in cui la proposta si può trovare
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum Stato{
	
	INVALIDA{
			/* (non-Javadoc)
			 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
			 */
			public boolean transiziona(Proposta p) {
				if(p.isValida()) {
					p.setState(VALIDA);
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
	VALIDA{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#canSet()
		 */
		public boolean canSet() {
			return true;
		}
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			if(!p.isValida()) {
				p.setState(INVALIDA);
				return true;
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#pubblica(EventBook.versione2.Proposta)
		 */
		public boolean pubblica(Proposta p) {
			p.setState(APERTA);
			return true;
		}
	},
	APERTA{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#canSubscribe(EventBook.versione2.Proposta)
		 */
		public boolean canSubscribe(Proposta p) {
			return p.subNumber() < Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName())) - 1;
		}
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			LocalDate tDate = LocalDate.now();
			//data ultima iscrizione
			LocalDate lastSubDate = LocalDate.class.cast(p.getValue(ExpandedHeading.TERMINEISCRIZIONE.getName()));
			//todayDate <= lastSubDate && subs == full
			if(tDate.compareTo(lastSubDate) <= 0 & 
					p.subNumber() == Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName()))
					) {
				p.setState(CHIUSA);
				p.send(new Messaggio(	//messaggio che avvisa che la proposta è chiusa
						String.class.cast(p.getValue(ExpandedHeading.TITOLO.getName())),
						CONFIRMOBJ,															
						String.format(CONFIRMFORMAT, String.class.cast(p.getValue(ExpandedHeading.TITOLO.getName())),
													String.class.cast(p.getValue(ExpandedHeading.DATA.getName())),
													String.class.cast(p.getValue(ExpandedHeading.ORA.getName())),
													String.class.cast(p.getValue(ExpandedHeading.LUOGO.getName())),
													String.class.cast(p.getValue(ExpandedHeading.QUOTAINDIVIDUALE.getName())))									
						));
				return true;
			//todayDate == lastSubDate && subs < full
			}else if(tDate.compareTo(lastSubDate) == 0 &
					p.subNumber() < Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName()))
					) {
				p.setState(FALLITA);
				p.send(new Messaggio(	//messaggio che avvisa che la proposta è fallita
						String.class.cast(p.getValue(ExpandedHeading.TITOLO.getName())),	
						FAILUREOBJ,
						String.format(FAILUREFORMAT, String.class.cast(p.getValue(ExpandedHeading.TITOLO.getName())))
						));
				return true;
			}
			return false;
		}
	},
	CHIUSA{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			LocalDate tDate = LocalDate.now();
			Object tmp = p.getValue(ExpandedHeading.DATACONCLUSIVA.getName());
			if(tmp == null) {
				LocalDate date = LocalDate.class.cast(p.getValue(ExpandedHeading.DATA.getName()));
				if(tDate.compareTo(date) > 0) {
					p.setState(CONCLUSA);
					return true;
				}
			}
			LocalDate endDate = LocalDate.class.cast(tmp);
			if(tDate.compareTo(endDate) > 0) {
				p.setState(CONCLUSA);
				return true;
			}
			return false;
		}		
	},
	CONCLUSA{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			return false;
		}		
	},
	FALLITA{
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			return false;
		}
	};
	
	private static final String CONFIRMOBJ = "Conferma evento";
	private static final String FAILUREOBJ = "Fallimento evento";
	//data ora luogo importo
	private static final String CONFIRMFORMAT = "Siamo lieti di confermare che l'evento %s si terrà il giorno %s alle %s in %s."
													+ "\nSi ricorda di portare %s € per l'orgazzazione";
	private static final String FAILUREFORMAT = "Siamo spiacenti di informarla che l'evento %s non ha raggiunto il numero minimo di iscritti."
													+ "\nL'evento è quindi annullato.";
	/**
	 * Modifica lo stato della proposta in modo da poterla rendere adatta al pubblico
	 * @param p la proposta a cui fare cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public boolean pubblica(Proposta p) {
		return false;
	}
	/**
	 * Porta la proposta p in nuovo stato
	 * @param p la proposta a cui far cambiare stato
	 * @return True - è stato cambiato stato con successo<br>False - non è stato cambiato stato alla proposta
	 */
	public abstract boolean transiziona(Proposta p);
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
	public boolean canSubscribe(Proposta p) {
		return false;
	}
}
