package EventBook.versione1.campi;

import EventBook.versione2.ClassType;

/**Classe con il compito di contenere una descrizione corredata da informazioni relative alla sua obbligatorietà
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */
public enum ExpandedHeading{
	
	TITOLO("Titolo", "Il titolo dell'evento", false, ClassType.STRING),
	NUMEROPARTECIPANTI("Numero di Partecipanti", "Numero di persone da coinvolgere nell'evento", true, ClassType.INTEGER),
	TERMINEISCRIZIONE("Termine ultimo iscrizione", "Ultimo giorno utile per iscriversi all'evento", true, ClassType.DATA),
	LUOGO("Luogo", "Indirizzo del luogo che ospiterà l'evento oppure, se l'evento è itinerante, il luogo di ritrovo dei partecipanti", true, ClassType.STRING),
	DATA("Data", "Data in cui l'evento proposto deve svolgersi o, nel caso l'evento non termini nello stesso giorno in cui ha inizio, la data dell'inizio dell'evento", true, ClassType.DATA),
	ORA("Ora", "Ora in cui i partecipanti dovranno trovarsi nel luogo \"Luogo\" in data \"Data\" per dare inizio all'evento", true, ClassType.ORA),
	DURATA("Durata", "Durata in termini di numero (approssimativo) di ore e minuti, per gli eventi che si esauriscono in un sol giorno, o in termini di numero esatto di giorni, per gli eventi che occupano più giorni	consecutivi", false, ClassType.INTEGER),
	QUOTAINDIVIDUALE("Quota individuale", "Spesa (o una stima della stessa) che ogni partecipante all'iniziativa dovrà sostenere (Può anche essere nulla)", true, ClassType.REAL),
	COMPRESONELLAQUOTA("Compreso nella quota", "Tutte le voci di spesa comprese nell'ammontare indicato nella \"Quota individuale\"", false,  ClassType.STRING),
	DATACONCLUSIVA("Data conclusiva", "Data di conclusione dell'evento", false,  ClassType.DATA),
	ORACONCLUSIVA("Ora conclusiva", "Ora di conclusione dell'evento", false,  ClassType.ORA),
	NOTE("Note", "Informazioni aggiuntive ", false, ClassType.STRING),
	
	//Campi FootballMatch
	GENERE("Genere", "Genere (maschile o femminile) dei giocatori", true, ClassType.STRING),
	FASCIADIETA("Fascia di età", "Estremo inferiore e superiore per l'età dei giocatori", true, ClassType.INTERVAL);

	/**
	 * Contiene il nome
	 */
	private String name;
	/**
	 * Contiene la descrizione
	 */
	private String description;
	/**
	 * Contiene informazioni sull'obbligatorietà
	 */
	private boolean binding;
	/**
	 * Contiene informazioni sul tipo del campo
	 */
	private ClassType type;
	/**
	 * Formattazione per la visualizzazione testuale dell'oggetto
	 */
	private final static String STRINGA_TO_STRING = "Nome: %s%nDescrizione: %s%nObbligatorio: %s%n";
	
	/**Restituisce il nome
	 * @return Il nome
	 */
	public String getName() {
		return name;
	}
	
	/**Restituisce la descrizione
	 * @return La descrizione
	 */
	public String getDescription() {
		return description;
	}
	
	/**Controlla se è obbligatorio
	 * @return True - è obbligatorio<br> False - non è obbligatorio
	 */
	public boolean isBinding() {
		return binding;
	}
	
	/**Costruttore
	 * @param nome Il nome
	 * @param descrizione La descrizione
	 * @param obbligatorio Se obbligatorio
	 */
	private ExpandedHeading(String nome, String descrizione, boolean obbligatorio, ClassType type) {
		this.name = nome;
		this.description = descrizione;
		this.binding = obbligatorio;
		this.type = type;
	}
	/**
	 * Restituisce il tipo del campo
	 * @return il tipo del campo
	 */
	public ClassType getClassType() {
		return type;
	}
	
	public Class<?> getType() {
		return type.getType();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return String.format(STRINGA_TO_STRING, name, description, binding);
	}
	
}