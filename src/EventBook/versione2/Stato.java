package EventBook.versione2;

/**
 * Interfaccia per definire il comportamento dei vari stati in cui si può trovare una proposta.<br>
 * Implementa il Design Pattern State.
 * @author Matteo
 *
 */
public interface Stato {
	/**
	 * Ogni stato esegue un'operazione di aggiornamento sulla proposta fornita in ingresso
	 * @param p La proposta sulla quale si vuole operare
	 */
	public void update(Proposta p);
}
