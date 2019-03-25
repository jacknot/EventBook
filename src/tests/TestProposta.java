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
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("30/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019")); //Viene prima di termine iscrizone: Invalida
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertFalse(proposal.isValid()); //deve essere invalida
	}
	
	@org.junit.jupiter.api.Test
	void propostaInvalidaObbligatoriNonCompilati() {
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), null); // Campo obbligatorio non compilato
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(),  FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(),  FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(),  FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(),  FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(),  FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse( "10-50"));
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertFalse(proposal.isValid()); //deve essere invalida
	}
	
	@org.junit.jupiter.api.Test
	void propostaValida() { 
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event, new User("Mario"));
		assertTrue(proposal.isValid()); //deve essere valida
	}

}
