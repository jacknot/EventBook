package dataTypes;

import java.util.ArrayList;
import java.util.stream.Stream;

import categories.CategoryHeading;

public class CategoriesInterest extends ArrayList<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Aggiunge una categoria alla lista di interesse solo se non è già presente e coincide con una categoria
	 * presente nel sistema
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
