package categories;

import fields.FieldSetFactory;

/**
 * Classe con il compito di gestire informazioni legate ad un Concerto Live
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
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
	/* (non-Javadoc)
	 * @see categories.Category#reset()
	 */
	public Category reset() {
		fields = FieldSetFactory.getInstance().getSet(CategoryHeading.CONCERT.getName());
		super.reset();
		return this;
	}
}
