package fields;

import dataTypes.ClassType;

/**Classe con il compito di contenere una descrizione corredata da informazioni relative alla sua obbligatorietà e il suo tipo
 * @author Matteo Salvalai [715827], Lorenzo Maestrini [715780], Jacopo Mora [715149]
 *
 */
public enum FieldHeading{
	TITOLO("Titolo", "Il titolo dell'evento", false, ClassType.STRING),
	NUMPARTECIPANTI("Numero di Partecipanti", "Numero di persone da coinvolgere nell'evento", true, ClassType.INTEGER),
	TERMINEISCRIZIONE("Termine ultimo iscrizione", "Ultimo giorno utile per iscriversi all'evento", true, ClassType.DATA),
	LUOGO("Luogo", "Indirizzo del luogo che ospiterà l'evento oppure, se l'evento è itinerante,"
			+ " il luogo di ritrovo dei partecipanti", true, ClassType.STRING),
	DATA("Data", "Data in cui l'evento proposto deve svolgersi o, nel caso l'evento non termini nello stesso giorno in cui ha inizio,"
			+ " la data dell'inizio dell'evento", true, ClassType.DATA),
	ORA("Ora", "Ora in cui i partecipanti dovranno trovarsi nel luogo \"Luogo\" in data \"Data\" per dare inizio all'evento", true, ClassType.ORA),
	DURATA("Durata", "Durata in termini di numero (approssimativo) di ore e minuti, per gli eventi che si esauriscono in un sol giorno,"
			+ " o in termini di numero esatto di giorni, per gli eventi che occupano più giorni	consecutivi", false, ClassType.ORA),
	QUOTA("Quota individuale", "Spesa (o una stima della stessa) che ogni partecipante all'iniziativa dovrà sostenere "
			+ "(Può anche essere nulla)", true, ClassType.REAL),
	COMPRESO_QUOTA("Compreso nella quota", "Tutte le voci di spesa comprese nell'ammontare indicato nella "
			+ "\"Quota individuale\"", false,  ClassType.STRING),
	DATAFINE("Data conclusiva", "Data di conclusione dell'evento", false,  ClassType.DATA),
	ORAFINE("Ora conclusiva", "Ora di conclusione dell'evento", false,  ClassType.ORA),
	NOTE("Note", "Informazioni aggiuntive", false, ClassType.STRING),
	
	//Nuovi campi aggiunti nella versione 3
	TOLL_PARTECIPANTI("Tolleranza numero partecipanti", "Numero di partecipanti eventualmente accettabili in esubero rispetto al \"Numero di partecipanti\"", false, ClassType.INTEGER),
	TERMINE_RITIRO("Termine ultimo di ritiro iscrizione", " Data entro cui a ogni fruitore che ha aderito all’evento è concesso di cancellare la sua iscrizione e al fruitore che ha proposto l’evento di ritirare la proposta", false, ClassType.DATA),
	//Campi FootballMatch
	GENERE("Genere", "Genere (maschile o femminile) dei giocatori", true, ClassType.GENDER),
	FASCIA_ETA("Fascia di età", "Estremo inferiore e superiore per l'età dei giocatori", true, ClassType.INTERVAL),
	
	//Nuovi campi aggiunti per la versione 4 (Per l'utente)
	NOMIGNOLO("Nomignolo", "Nome di fantasia scelto dal fruitore, che identifica univocamente il fruitore stesso", true, ClassType.STRING),
	FASCIA_ETA_UTENTE("Fascia di età utente", "Estremo inferiore e superiore di un intervallo di età entro cui cade quella del fruitore", false, ClassType.INTERVAL),
	CATEGORIE_INTERESSE("Categorie di interesse","Elenca le categorie di eventi a cui il fruitore è particolarmente interessato", false, ClassType.INTEREST_CATEGORIES);

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
	//private final static String STRINGA_TO_STRING = "Nome: %s%nDescrizione: %s%nObbligatorio: %s";
	private final static String TOSTRING_FORMAT = "Nome: %s%s%nDescrizione: %s";
	
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
	 * @param type il tipo di dato associato alla descrizione
	 */
	private FieldHeading(String nome, String descrizione, boolean obbligatorio, ClassType type) {
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
	
	/**
	 * Restituisce il tipo di dato che l'intestazione può contenere
	 * @return il tipo di dato che può contenere
	 */
	public Class<?> getType() {
		return type.getType();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		String bindingString = "";
		if(!binding)
			bindingString = " [Facoltativo]";
		return String.format(TOSTRING_FORMAT, name, bindingString, description);
	}
	/**
	 * Controlla se le due intestazioni sono uguali
	 * @param eh l'intestazione da confrontare
	 * @return True - sono uguali<br>False - sono diversi
	 */
	public boolean equals(FieldHeading eh) {
		return this.name.equals(eh.name);
	}
	
}