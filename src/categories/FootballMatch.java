package categories;

import fields.FieldHeading;

/**Classe con il compito di istanziare eventi legati ad una partita di calcio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class FootballMatch extends Event{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore
	 */
	public FootballMatch() {
		super();
		super.setHeading(EventHeading.FOOTBALLMATCH);
		addSpecificFields();
	}

	/*
	 * (non-Javadoc)
	 * @see categories.Category#addSpecificFields()
	 */
	@Override
	public void addSpecificFields() {
		super.addField(FieldHeading.GENERE);
		super.addField(FieldHeading.FASCIA_ETA);
	}
}
