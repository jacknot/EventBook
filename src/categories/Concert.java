package categories;

import fields.Field;
import fields.FieldHeading;
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
		addSpecificFields();
	}
	/* (non-Javadoc)
	 * @see categories.Category#reset()
	 */
	public Category reset() {
		fields = new FieldSetFactory().commonSet();
		fields.add(new Field<>(FieldHeading.BACKSTAGE_PASS));
		fields.add(new Field <>(FieldHeading.MEET_AND_GREET));
		fields.add(new Field <>(FieldHeading.MERCHANDISE));
		super.reset();
		return this;
	}
	/*
	 * (non-Javadoc)
	 * @see categories.Category#addSpecificFields()
	 */
	@Override
	public void addSpecificFields() {
		fields.add(new Field<>(FieldHeading.BACKSTAGE_PASS));
		fields.add(new Field <>(FieldHeading.MEET_AND_GREET));
		fields.add(new Field <>(FieldHeading.MERCHANDISE));
	}
}
