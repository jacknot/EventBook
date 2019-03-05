package EventBook.versione2;

import java.time.LocalDate;
import java.time.LocalTime;

public enum ClassType {

	STRING(String.class, ".+"),
	INTEGER(Integer.class, "\\d+"),
	REAL(Integer.class, "\\d+\\.\\d{2}"),
	DATA(LocalDate.class, "[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}"), //approssimativa
	ORA(LocalTime.class, "[0-2][0-9]:[0-5][0-9]"); //aprossimativa
	
	private Class<?> type;
	private String regex;
	
	private ClassType(Class<?> type, String regex) {
		this.type = type;
		this.regex = regex;
	}
	
	public boolean isValidType(String obj) {
		boolean castValido = true;
		try {
			type.cast(obj);
		} catch(ClassCastException e) {
			castValido = false;
		}
		return castValido && obj.matches(regex);
	}
}
