package dataTypes;

import java.io.Serializable;

/**
 * Classe che permette di salvare una coppia di valori di tipo uguale o diverso
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 * @param <T1> Tipo del primo attributo
 * @param <T2> Tipo del secondo attributo
 */
public class Pair<T1, T2> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private T1 first;
	private T2 second;
	
	/**
	 * Costruttore
	 */
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Restituisce il primo oggetto della coppia
	 * @return primo oggetto
	 */
	public T1 getFirst() {
		return first;
	}
	
	/**
	 * Imposta il primo oggetto della coppia
	 * @param first nuovo valore
	 */
	public void setFirst(T1 first) {
		this.first = first;
	}
	
	/**
	 * Restituisce il secondo oggetto della coppia
	 * @return secondo oggetto
	 */
	public T2 getSecond() {
		return second;
	}
	
	/**
	 * Imposta il secondo oggetto della coppia
	 * @param second nuovo valore
	 */
	public void setSecond(T2 second) {
		this.second = second;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return first.toString() + " : " + second.toString();
	}
}
