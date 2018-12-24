package progetto.versione1;

/**Classe con il compito di ospitare due dati numerici interi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */

//sdf
public class FasciaEta {

	//ATTRIBUTI
	/**
	 * Il minore fra i due dati
	 */
	private int estremoInferiore;
	/**
	 * Il maggiore fra i due dati
	 */
	private int estremoSuperiore;
	
	//COSTRUTTORE
	/**Costruttore 
	 * @param estremoInferiore Il dato numerico pi� piccolo si vuole assegnare
	 * @param estremoSuperiore Il dato numerico pi� grande si vuole assegnare
	 */
	//estremoSuperiore >= estremoInferiore
	public FasciaEta(int estremoInferiore, int estremoSuperiore) {
		this.estremoInferiore = estremoInferiore;
		this.estremoSuperiore = estremoSuperiore;
	}
	//this.estremoSuperiore >= this.estremoInferiore
	
	//GETTERS E SETTERS
	/**
	 * @return Il valore attuale dell'estremo inferiore
	 */
	public int getEstremoInferiore() {
		return estremoInferiore;
	}
	/**Assegna un nuovo valore all'estremo inferiore
	 * @param estremoInferiore Il nuovo estremo inferiore
	 */
	public void setEstremoInferiore(int estremoInferiore) {
		this.estremoInferiore = estremoInferiore;
	}
	/**
	 * @return Il valore attuale dell'estremo superiore
	 */
	public int getEstremoSuperiore() {
		return estremoSuperiore;
	}
	/**Assegna un nuovo valore all'estremo superiore
	 * @param estremoSuperiore Il nuovo estremo superiore
	 */
	public void setEstremoSuperiore(int estremoSuperiore) {
		this.estremoSuperiore = estremoSuperiore;
	}
	//METODI
	/**Restituisce l'intervallo fra i due dati numerici
	 * @return L'intervallo fra i due dati
	 */
	//estremoSuperiore >= estremoInferiore
	public int getGap() {
		return estremoSuperiore - estremoInferiore;
	}
	//TOSTRING
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return estremoInferiore + " - " + estremoSuperiore;
	}
}
