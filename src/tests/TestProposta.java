package tests;

import static org.junit.jupiter.api.Assertions.*;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.Proposal;
import users.User;

class TestProposta {
	
	@org.junit.jupiter.api.Test
	void propostaInvalidaTermineIscrizione() {
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), "30/06/2019");
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), "25/06/2019"); //Viene prima di termine iscrizone: Invalida
		event.setValue(FieldHeading.ORA.getName(), "20:00");
		event.setValue(FieldHeading.QUOTA.getName(), "10.00");
		event.setValue(FieldHeading.GENERE.getName(), "M");
		event.setValue(FieldHeading.FASCIA_ETA.getName(), "10-50");
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertFalse(proposal.isValid()); //deve essere invalida
	}
	
	@org.junit.jupiter.api.Test
	void propostaInvalidaObbligatoriNonCompilati() {
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		//event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20); Campo obbligatorio non compilato
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), "21/06/2019");
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), "25/06/2019");
		event.setValue(FieldHeading.ORA.getName(), "20:00");
		event.setValue(FieldHeading.QUOTA.getName(), "10.00");
		event.setValue(FieldHeading.GENERE.getName(), "M");
		event.setValue(FieldHeading.FASCIA_ETA.getName(), "10-50");
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertFalse(proposal.isValid()); //deve essere invalida
	}
	
	@org.junit.jupiter.api.Test
	void propostaValida() { //BOH NON FUNZIONA MA DOVREBBE FUZNIONARE
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), "21/06/2019");
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), "25/06/2019");
		event.setValue(FieldHeading.ORA.getName(), "20:00");
		event.setValue(FieldHeading.QUOTA.getName(), "10.00");
		event.setValue(FieldHeading.GENERE.getName(), "M");
		event.setValue(FieldHeading.FASCIA_ETA.getName(), "10-50");
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertTrue(proposal.isValid()); //deve essere valida
	}

}
