package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.Proposal;
import proposals.ProposalHandler;
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
		//event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20); // Campo obbligatorio non compilato
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
	
	@org.junit.jupiter.api.Test
	void propostaValidaPubblicata() { 
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));	//categoria valida generata
		Proposal proposal = new Proposal(event, new User("Mario")); //creata proposta
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal);
		assertTrue(bacheca.contains(proposal)); //proposta aggiunta correttamente
	}
	
	@org.junit.jupiter.api.Test
	void propostaValidaRitirata() { 
		User owner =  new User("Mario");
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50")); //categoria valida generata (con un termine di ritiro corretto)
		Proposal proposal = new Proposal(event, owner); //creata proposta
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal); //proposta aggiunta correttamente
		bacheca.withdraw(0, owner); //rimossa proposta
		assertFalse(bacheca.contains(proposal));	//la bacheca non contiene più la proposta
	}
	
	
	@org.junit.jupiter.api.Test
	void iscrizionePropostaValida() { 
		User owner =  new User("Mario");
		User user =  new User("Pinco");
		
		//Data termine iscrizione precedente di un mese rispetto al tempo attuale
		int mesePrima = LocalDate.now().getMonthValue()  - 1;
		if(mesePrima == 0)
			mesePrima = 12; // da gennaio a dicembre
		int meseDopo = LocalDate.now().getMonthValue() % 12 + 1;
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		
		Proposal proposal = new Proposal(event, owner); //creata proposta
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal); //proposta aggiunta correttamente		
		assertTrue(bacheca.signUp(0, user)); //Impossibile registrarsi oltre il termine iscrizione
	}

	@org.junit.jupiter.api.Test
	void impossibileIscriversiDopoTermineIscrizione() { 
		User owner =  new User("Mario");
		User user =  new User("Pinco");
		
		//Data termine iscrizione precedente di un mese rispetto al tempo attuale
		int mesePrima = LocalDate.now().getMonthValue()  - 1;
		if(mesePrima == 0)
			mesePrima = 12; // da gennaio a dicembre
		int meseDopo = LocalDate.now().getMonthValue() % 12 + 1;
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/02/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		event.setValue(FieldHeading.TERMINE_RITIRO.getName(), FieldHeading.TERMINE_RITIRO.getClassType().parse("21/02/2019"));
		
		Proposal proposal = new Proposal(event, owner); //creata proposta
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal); //proposta aggiunta correttamente	
		bacheca.refresh(); //La bacheca si aggiorna e cambia stato alla proposta -> passa a Fallita
		assertFalse(bacheca.signUp(0, user)); //Impossibile registrarsi oltre il termine iscrizione
	}
	
}
