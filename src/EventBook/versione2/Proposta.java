package EventBook.versione2;

import EventBook.versione1.*;
import EventBook.versione2.stati.*;
import EventBook.versione2.fruitore.*;

/**
 * 
 * @author Matteo
 *
 */
public class Proposta {
	/**
	 * Contiene un evento
	 */
	private Category evento;
	/**
	 * Lo stato in cui si trova la proposta
	 */
	private Stato stato;
	
	/**
	 * Costruttore di una proposta
	 * @param c l'evento alla quale fa riferimento
	 */
	public Proposta(Category c) {
		evento = c;
		stato = new Invalido();
	}
	/**
	 * Restituisce lo stato della proposta
	 * @return Lo stato attuale della proposta
	 */
	public Stato getState() {
		return stato;
	}
	/**
	 * Imposta lo stato della proposta con un nuovo stato
	 * @param nS	Il nuovo stato da far assumere dalla proposta
	 */
	public void setState(Stato nS) {
		stato = nS;
	}
	
	public void send(Messaggio msg) {}
	public void cambia(String nome, String valore) {
		// TODO Auto-generated method stub
		
	}
}
