package EventBook.versione1;


/**Classe con il compito di istanziare eventi legati ad una partita di calcio
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class PartitaDiCalcio extends Category{
	/**
	 * Costruttore
	 */
	public PartitaDiCalcio() {
		super();
		heading = Heading.PARTITADICALCIO;
		fields = FieldSetFactory.getInstance()
											.getContenitore(Heading.PARTITADICALCIO.getName());
	}
}
