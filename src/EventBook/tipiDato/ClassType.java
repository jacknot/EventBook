package EventBook.tipiDato;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import EventBook.versione2.Parser;


/**
 * Contiene dei tipi e una logica per poter analizzare una stringa ed estrarre un valore del tipo correlato
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum ClassType {

	STRING(String.class, "\\D+", (str) -> str),
	INTEGER(Integer.class, "\\d+", (i) -> Integer.parseInt(i)),
	REAL(Double.class, "\\d+\\.\\d{2}", (real) -> Double.parseDouble(real)),
	DATA(LocalDate.class, "(0[1-9]|[1-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/(2[0-9]{3})", (data) ->{
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}),
	ORA(LocalTime.class, "(0[1-9]|1[0-9]|2[0-3]):([0-5][0-9])", (ora) -> {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(ora, formatter);
	}),
	INTERVAL(Interval.class, "\\d{1,2}-\\d{1,2}", (interval) ->{
		StringTokenizer tokenizer = new StringTokenizer(interval, "-");
		return new Interval(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));
	}),
	GENDER(Gender.class, "[m|f]" ,(gender)->{
		return new Gender(gender.equals("f"));
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
	 * Restituisce l'espressione regolare legata al dato
	 * @return l'espressione regolare legata al dato
	 */
	public String getRegex(){
		return regex;
	}
	/**
	 * Traduce una stringa nel tipo voluto
	 * @param value la stringa da tradurre
	 * @return l'oggetto generato dalla stringa tradotta, null se la stringa non è corretta
	 */
	public Object parse(String value){
		if(isValidType(value))
			return parser.parse(value);
		return null;
	}
	/**
	 * Restituisce il tipo di dato
	 * @return il tipo di dato
	 */
	public Class<?> getType(){
		return type;
	}
}