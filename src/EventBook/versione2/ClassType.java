package EventBook.versione2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import EventBook.campi.Interval;


/**
 * Contiene dei tipi e una logica per poter analizzare una stringa ed estrarre un valore del tipo correlato
 * @author Matteo
 *
 */
public enum ClassType {

	STRING(String.class, "\\D+", (str) -> str),
	INTEGER(Integer.class, "\\d+", (i) -> Integer.parseInt(i)),
	REAL(Double.class, "\\d+\\.\\d{2}", (real) -> Double.parseDouble(real)),
	DATA(LocalDate.class, "(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/(2[0-9]{3})", (data) ->{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(data, formatter);
	}),
	ORA(LocalTime.class, "(0[1-9]|1[0-9]|2[0-3]):([0-5][0-9])", (ora) -> {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(ora, formatter);
	}),
	INTERVAL(Interval.class, "\\d{1,2}-\\d{1,2}", (interval) ->{
		StringTokenizer tokenizer = new StringTokenizer(interval, "-");
		return new Interval(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));
	});
	
	/**
	 * Tipo di ritorno
	 */
	private Class<?> type;
	/**
	 * L'espressione regolare con il compito di definire le regole tramite le quali analizzare la stringa
	 */
	private String regex;
	/**
	 * La logica per estrarre il tipo di dato corretto
	 */
	private Parser<?> parser;
	
	/**
	 * Costruttore
	 * @param type il tipo di dato
	 * @param regex l'espressione regolare legata al tipo
	 * @param parser la logica di estrazione da una stringa
	 */
	private ClassType(Class<?> type, String regex, Parser<?> parser) {
		this.type = type;
		this.regex = regex;
		this.parser = parser;
	}
	/**
	 * Controlla se la stringa è compatibile con l'espressione regolare inserita
	 * @param obj la stringa da controllare
	 * @return True - la stringa è compatibile<br>False - la stringa non è compatibile
	 */
	public boolean isValidType(String obj) {
		return obj.matches(regex);
	}
	/**
	 * Restituisce la logica di estrazione del dato
	 * @return la logica di estrazione del dato
	 */
	public Parser<?> getParser(){
		return parser;
	}
	
	/**
	 * Restituisce il tipo di dato
	 * @return il tipo di dato
	 */
	public Class<?> getType(){
		return type;
	}
}
