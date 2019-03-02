package EventBook.versione2;

import EventBook.versione1.Category;
import EventBook.versione2.fruitore.Stato;

public class Proposta {
	private Category evento;
	private Stato aState;
	
	public void aggiornaStato() {	}
	public boolean isValida() {
		return aState.isValido();
	}
	public void cambia(String name, Object value) {
		if(aState.canSet()) {
			evento.setValue(name, value);
			aggiornaStato();
		}
	}
	/**
	 * Restituisce il contenuto del campo di cui si è inserito il nome
	 * @param name il nome del campo 
	 * @return il contenuto del campo
	 */
	public Object getValue(String name) {
		return evento.getValue(name);
	}
	/**
	 * Iscrive un fruitore alla proposta 
	 * @param user il fruitore da iscrivere
	 */
	public boolean iscrivi(Notificabile user) {
		return true;
	}
	public boolean sameState(Stato s) {
		return true;
	}
}
