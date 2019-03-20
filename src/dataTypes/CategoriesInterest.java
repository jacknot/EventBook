package dataTypes;

import java.util.ArrayList;

import categories.CategoryHeading;

public class CategoriesInterest extends ArrayList<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Aggiunge una categoria alla lista di interesse solo se non è già presente e coincide con una categoria
	 * presente nel sistema
	 * @return True se aggiunto correttamente <br> False altrimenti
	 */
	public boolean add(String categoryName) {	
		if(!contains(categoryName)) {
			CategoryHeading[] cats = CategoryHeading.values();
			
			for(int i=0; i < cats.length; i++) {
				if(cats[i].getName().equalsIgnoreCase(categoryName)) {		
					return super.add(categoryName);
				}
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String str : this) {
			sb.append(str).append("\n");
		}
		return sb.toString();
	}
}
