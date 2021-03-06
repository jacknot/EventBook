package categories;

import fields.FieldHeading;

/**
 * Classe con il compito di gestire informazioni legate ad un Concerto Live
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Concert extends Event{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore
	 */
	public Concert() {
		super();
		super.setHeading(EventHeading.CONCERT);
		addSpecificFields();
	}
	/* (non-Javadoc)
	 * @see categories.Category#reset()
	 */
	public Event reset() {
		super.resetSet();
		addSpecificFields();
		super.reset();
		return this;
	}
	/*
	 * (non-Javadoc)
	 * @see categories.Category#addSpecificFields()
	 */
	@Override
	public void addSpecificFields() {
		super.addField(FieldHeading.BACKSTAGE_PASS);
		super.addField(FieldHeading.MEET_AND_GREET);
		super.addField(FieldHeading.MERCHANDISE);
	}
}
