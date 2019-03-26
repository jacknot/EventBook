package fields;

import dataTypes.ClassType;

public class InteractiveField<T> extends Field<T>{

	/**
	 * Contiene il valore del campo
	 */
	private T valueUser;
	
	private ClassType typeUser;
	
	public InteractiveField(FieldHeading head, ClassType type) {
		super(head);
		typeUser = type;
		valueUser = null;
	}
	
	public boolean setValueUser(Object nValue) {
		try {
			this.valueUser = getType().cast(nValue);
			return true;
		}catch(ClassCastException e) {
			return false;
		}
	}

	
	
	
}
