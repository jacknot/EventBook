package dataTypes;

import java.io.Serializable;

/**
 * La classe ha il compito di gestire un intervallo.<br>
 * Gli estremi dell'intervallo sono due dati numerici interi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */
public class Interval implements Serializable{

	//Attributi
	
	private static final long serialVersionUID = 1L;
	/**
	 * Estremo inferiore dell'intervallo
	 */
	private int infExt;
	/**
	 * Estremo superiore dell'intervallo
	 */
	private int supExt;
	
	/**Costruttore 
	 * @param iE Il dato che rappresenta l'estremo inferiore dell'intervallo
	 * @param sE Il dato che rappresenta l'estremo superiore dell'intervallo
	 */
	public Interval(int iE, int sE) {
		if(iE > sE) {
			this.infExt = sE;
			this.supExt = iE;
		}else {
			this.infExt = iE;
			this.supExt = sE;
		}	
	}
	//this.estremoSuperiore >= this.estremoInferiore
	
	//GETTERS E SETTERS
	/**
	 * @return Il valore attuale dell'estremo inferiore
	 */
	public int getInferiorExt() {
		return infExt;
	}
	/**Assegna un nuovo valore all'estremo inferiore
	 * @param infE Il nuovo estremo inferiore
	 */
	public void setInferiorExt(int infE) {
		this.infExt = infE;
	}
	/**
	 * @return Il valore attuale dell'estremo superiore
	 */
	public int getSuperiorExt() {
		return supExt;
	}
	/**Assegna un nuovo valore all'estremo superiore
	 * @param supE Il nuovo estremo superiore
	 */
	public void setSuperiorExt(int supE) {
		this.supExt = supE;
	}
	//METODI
	/**Restituisce l'intervallo (gap) fra i due dati numerici
	 * @return L'intervallo fra i due dati
	 */
	//estremoSuperiore >= estremoInferiore
	public int getGap() {
		return supExt - infExt;
	}
	//TOSTRING
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return infExt + " - " + supExt;
	}
}
