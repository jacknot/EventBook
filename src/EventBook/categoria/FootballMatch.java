package EventBook.categoria;

import EventBook.campi.FieldSetFactory;

/**Classe con il compito di istanziare eventi legati ad una partita di calcio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class FootballMatch extends Category{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore
	 */
	public FootballMatch() {
		super();
		heading = Heading.PARTITADICALCIO;
		fields = FieldSetFactory.getInstance()
											.getContenitore(Heading.PARTITADICALCIO.getName());
	}
}
