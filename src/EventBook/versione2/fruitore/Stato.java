package EventBook.versione2.fruitore;

/**
 * Contiene un set predefiniti di stati in cui la proposta si può trovare
 * @author Matteo
 *
 */
public enum Stato {
	INVALIDA,
	VALIDA,
	APERTA,
	CHIUSA,
	CONCLUSA,
	FALLITA;
	
	private Stato() {}
	
	public void pubblica() {}
	public void avvisa() {}
	public void transiziona() {}
	public boolean canSet() {
		return true;
	}
}
