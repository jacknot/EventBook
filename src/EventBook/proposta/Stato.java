package EventBook.proposta;

import java.io.Serializable;
import java.time.LocalDate;

import EventBook.campi.ExpandedHeading;
import EventBook.fruitore.Messaggio;

/**
 * Contiene un set predefiniti di stati in cui la proposta si può trovare
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum Stato implements Serializable{
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
			//integer.class.cast  o Integer.parse(p.getvalue) ?
			//è un cast perchè il campo tiene un valore di tipo intero
			return p.subNumber() < Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName())) - 1; 
		}
		/* (non-Javadoc)
		 * @see EventBook.versione2.fruitore.Stato#transiziona(EventBook.versione2.Proposta)
		 */
		public boolean transiziona(Proposta p) {
			LocalDate todayDate = LocalDate.now();
			//data ultima iscrizione
			LocalDate lastSubDate = LocalDate.class.cast(p.getValue(ExpandedHeading.TERMINEISCRIZIONE.getName()));
			//gestione titolo non inserito
			String titolo = p.getValue(ExpandedHeading.TITOLO.getName()) == null?
					UNKNOWN_TITLE:p.getValue(ExpandedHeading.TITOLO.getName()).toString();
			//todayDate <= lastSubDate && subs == full
			if(todayDate.compareTo(lastSubDate) <= 0 &&
					p.subNumber() == Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName()))
					) {
				p.setState(CHIUSA);
				p.send(new Messaggio(	//messaggio che avvisa che la proposta è chiusa
						titolo,
						CONFIRMOBJ,															
						String.format(CONFIRMFORMAT, titolo,
													p.getValue(ExpandedHeading.DATA.getName()),
													p.getValue(ExpandedHeading.ORA.getName()),
													p.getValue(ExpandedHeading.LUOGO.getName()),
													p.getValue(ExpandedHeading.QUOTAINDIVIDUALE.getName()))							
						));
				return true;
			//todayDate >= lastSubDate && subs < full
			}else if(todayDate.compareTo(lastSubDate) >= 0 &&
					p.subNumber() < Integer.class.cast(p.getValue(ExpandedHeading.NUMEROPARTECIPANTI.getName()))
					) {
				p.setState(FALLITA);
				
				p.send(new Messaggio(	//messaggio che avvisa che la proposta è fallita
						titolo,	
						FAILUREOBJ,
						String.format(FAILUREFORMAT, titolo)
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
	
	private static final String UNKNOWN_TITLE = "(titolo mancante)";
	private static final String CONFIRMOBJ = "Conferma evento";
	private static final String FAILUREOBJ = "Fallimento evento";
	//data ora luogo importo
	private static final String CONFIRMFORMAT = "Siamo lieti di confermare che l'evento %s si terrà il giorno %s alle %s in %s."
													+ "\nSi ricorda di portare %s per l'organizzazione";
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
