package EventBook.versione2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import EventBook.versione1.campi.Field;
import EventBook.versione1.campi.Interval;

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
	
	private Class<?> type;
	private String regex;
	private Parser<?> parser;
	
	private ClassType(Class<?> type, String regex, Parser<?> parser) {
		this.type = type;
		this.regex = regex;
		this.parser = parser;
	}
	
	
	public boolean isValidType(String obj) {
		return obj.matches(regex);
	}
	
	public void parse(Field<?> campo, String value){ //eventualmente field da spostare in Parser, devo per forza assegnare qua il valore perchè non si sa che tipo deve restituire questo metodo
		campo.setValue(parser.parser(value));
		//return campo;
	}
	
	public Class<?> getType(){
		return type;
	}
}
