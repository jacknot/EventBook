package dataTypes;

import java.util.ArrayList;

import categories.CategoryHeading;

public class CategoriesInterest extends ArrayList<String>{

	private static final long serialVersionUID = 1L;

	public boolean add(String categoryName) {	
		if(!contains(categoryName)) {
			CategoryHeading[] cats = CategoryHeading.values();
			
			for(int i=0; i< cats.length; i++) {
				if(cats[i].getName().equalsIgnoreCase(categoryName)) {
					add(categoryName);
					return true;
				}
			}
		}
		return false;
	}
}
