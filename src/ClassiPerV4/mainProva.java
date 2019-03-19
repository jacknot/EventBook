package ClassiPerV4;

import dataTypes.Interval;
import fields.FieldHeading;
import users.User;

public class mainProva {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Profilo profile = new Profilo();
		profile.setValue(FieldHeading.NOMIGNOLO.getName(), "LMae");
		profile.setValue(FieldHeading.FASCIA_ETA_UTENTE.getName(), new Interval(11,50));
		profile.setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), "categoria A");
		User user = new User(profile);
		System.out.println(user.toString());
		System.out.println(user.showProfile());
	}

}
