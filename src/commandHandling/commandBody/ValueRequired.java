package commandHandling.commandBody;

import commandHandling.Context;
import fields.FieldHeading;
import utility.StringConstant;

/**
 * 
 * Questa interfaccia viene implementata dai comandi che richiedono l'inserimento di un valore
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public interface ValueRequired {
	/**
	 * Richiede all'utente di inserire un valore e ne verifica la validità in base al campo
	 * @param ctx il contesto su cui si deve operare
	 * @param field campo su cui verificare la validità del dato
	 * @param message richiesta all'utente di inserire il dato
	 * @return Oggetto correttamente elaborato in base al campo
	 */
	public default Object acceptValue(Context ctx, FieldHeading field, String message) {
		boolean valid = false;
		Object obj = null;
		do {
			String value = ctx.getIOStream().read("\t" + message);
			if(!field.isBinding() && !field.isOptional() && value.isEmpty())
				valid = true;
			ctx.getIOStream().write(StringConstant.NEW_LINE);
			if(field.getClassType().isValidType(value)) {
				obj = field.getClassType().parse(value);
				valid = true;
			}
			if(!valid)
				ctx.getIOStream().writeln("\tIl valore inserito non è corretto.\n\tInserisca qualcosa del tipo: " + field.getClassType().getSyntax());
		}while(!valid);
		return obj;
	}
}
