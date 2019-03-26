package categories;

import fields.FieldSetFactory;

public class Concert extends Category{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore
	 */
	public Concert() {
		super();
		heading = CategoryHeading.CONCERT;
		fields = FieldSetFactory.getInstance().getSet(CategoryHeading.CONCERT.getName());
	}
}
