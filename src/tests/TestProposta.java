package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.Preferences;
import proposals.Proposal;
import proposals.ProposalHandler;
import proposals.State;
import users.User;

class TestProposta {
	
	@org.junit.jupiter.api.Test
	void propostaInvalidaTermineIscrizione() {
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		//Viene prima di termine iscrizone: Invalida
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().minusDays(1)); 
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event);
		assertFalse(proposal.isValid()); //deve essere invalida
		proposal.setOwner(new User("Mario"), proposal.getPreferenze());
		assertFalse(proposal.isValid());
	}
	
	@org.junit.jupiter.api.Test
	void propostaInvalidaObbligatoriNonCompilati() {
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		//event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20); // Campo obbligatorio non compilato
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(),  LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(),  LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(),  FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(),  FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(),  FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse( "10-50"));
		Proposal proposal = new Proposal(event);
		proposal.setOwner(new User("Mario"), proposal.getPreferenze());
		assertFalse(proposal.isValid()); //deve essere invalida
	}
	
	@org.junit.jupiter.api.Test
	void propostaValida() { 
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event);
		assertFalse(proposal.isValid()); //deve essere valida
		proposal.setOwner(new User("Mario"), proposal.getPreferenze());
		assertTrue(proposal.isValid()); //deve essere valida
	}
	
	@org.junit.jupiter.api.Test
	void propostaValidaPubblicata() { 
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));	//categoria valida generata
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(new User("Mario"), proposal.getPreferenze());
		assertTrue(proposal.isValid()); //deve essere valida
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal);
		assertTrue(bacheca.contains(proposal)); //proposta aggiunta correttamente
	}
	
	@org.junit.jupiter.api.Test
	void propostaValidaRitirata() { 
		User owner =  new User("Mario");
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50")); //categoria valida generata (con un termine di ritiro corretto)
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getPreferenze());
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal); //proposta aggiunta correttamente
		bacheca.withdraw(0, owner); //rimossa proposta
		assertFalse(bacheca.contains(proposal));	//la bacheca non contiene più la proposta
	}
	
	
	@org.junit.jupiter.api.Test
	void iscrizionePropostaValida() { 
		User owner =  new User("Mario");
		User user =  new User("Pinco");

		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getPreferenze());
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		assertTrue(bacheca.add(proposal)); //proposta aggiunta correttamente		
		assertTrue(bacheca.signUp(0, user, bacheca.getPreferenze(0)));
	}
	
	
	@org.junit.jupiter.api.Test
	void iscrizionePropostaValidaConPreferenze() { 
		User owner =  new User("Mario");
		User user =  new User("Pinco");

		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.CONCERT.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.MEET_AND_GREET.getName(), FieldHeading.MEET_AND_GREET.getClassType().parse("90.00")); //Campo opzionale espresso
		event.setValue(FieldHeading.MERCHANDISE.getName(), FieldHeading.MERCHANDISE.getClassType().parse("20.00")); //Campo opzionale espresso
		
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getPreferenze());
	
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		bacheca.add(proposal); //proposta aggiunta correttamente	
		Preferences pref = bacheca.getPreferenze(0);
		
		pref.impostaPreferenza(FieldHeading.MEET_AND_GREET,  true); //accetta la prima
		pref.impostaPreferenza(FieldHeading.MERCHANDISE, false); //rifiuta la seconda
			
		assertTrue(bacheca.signUp(0, user, pref));
	}

	@org.junit.jupiter.api.Test
	void impossibileIscriversiDopoTermineIscrizione() { 
		User owner =  new User("Mario");
		User user =  new User("Pinco");

		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		event.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(3));
		
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getPreferenze());
		
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		assertTrue(bacheca.add(proposal));
		proposal.setState(State.VALID);
		proposal.modify(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().minusDays(1));
		proposal.setState(State.OPEN);
		bacheca.refresh(); //La bacheca si aggiorna e cambia stato alla proposta -> passa a Fallita
		assertFalse(bacheca.contains(proposal));
		assertTrue(proposal.hasState(State.FAILED));
		//La proposta, avendo superato la data ultima di termine iscrizione, viene rimossa dalla bacheca
		//La bacheca ora è vuota -> non posso iscrivermi
		assertFalse(bacheca.signUp(0, user, null)); 
	}
	
}
