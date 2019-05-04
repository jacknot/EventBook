package dataTypes;

import java.util.ArrayList;
import java.util.stream.Stream;

import categories.CategoryHeading;

/**La classe ha il compito di gestire un elenco contenente nomi di categorie.<br>
 * Nell'elenco ogni nome di cateogoria può comparire al più una volta.
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class CategoriesOfInterest extends ArrayList<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Aggiunge una categoria alla lista di interesse solo se essa coincide con una categoria presente nel sistema 
	 * e non è già presente nell'elenco
	 * @return True se aggiunto correttamente <br> False altrimenti
	 */
	public boolean add(String categoryName) {	
		if(!contains(categoryName) && Stream.of(CategoryHeading.values())
												.anyMatch((ch)->ch.getName().equalsIgnoreCase(categoryName))) {
			return super.add(Stream.of(CategoryHeading.values())
									.filter((ch)->ch.getName().equalsIgnoreCase(categoryName))
									.findFirst().get().getName());
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.stream()
				.forEach((str)->sb.append(str).append("\n"));
		return sb.toString();
	}
}
