package EventBook.tipiDato;

/**
 * Classe con il compito di poter istanziare dati relativi al genere di una persona
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Gender {
	
	/**
	 * Il genere 
	 */
	private boolean female;
	
	/**
	 * Costruttore
	 * @param female il genere da inserire
	 */
	public Gender(boolean female) {
		this.female = female;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return female?"Female":"Male";
	}
}
