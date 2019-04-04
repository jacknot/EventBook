package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.OptionsSet;
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
		//TermineIscrizione > data --> invalida
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().minusDays(1)); 
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event);
		assertFalse(proposal.isValid()); //deve essere invalida
		proposal.setOwner(new User("Mario"), proposal.getOptions());
		assertFalse(proposal.isValid() && proposal.hasState(State.INVALID));
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
		proposal.setOwner(new User("Mario"), proposal.getOptions());
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
		proposal.setOwner(new User("Mario"), proposal.getOptions());
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
		proposal.setOwner(new User("Mario"), proposal.getOptions());
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
		proposal.setOwner(owner, proposal.getOptions());
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
		proposal.setOwner(owner, proposal.getOptions());
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
		event.setValue(FieldHeading.MEET_AND_GREET.getName(), 90.00); //Campo opzionale espresso
		event.setValue(FieldHeading.MERCHANDISE.getName(), 20.00); //Campo opzionale espresso
		assertFalse(event.isValid());
		//BACKSTAGE_PASS.value == null -> evento è INVALIDO (devo dare valore a tutti i campi opzionali)
		event.removeOptionalField(FieldHeading.BACKSTAGE_PASS);
		assertTrue(event.isValid());
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getOptions());
		
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		assertTrue(bacheca.add(proposal)); //proposta aggiunta correttamente	
		OptionsSet pref = bacheca.getPreferenze(0);
		
		pref.makeChoice(FieldHeading.MEET_AND_GREET,  true); //accetta la prima
		pref.makeChoice(FieldHeading.MERCHANDISE, false); //rifiuta la seconda
		assertTrue(pref.hasSameChoices(proposal.getOptions()));
		
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
		assertFalse(event.isValid());	//Termine_ritiro < tDate
		event.setValue(FieldHeading.TERMINE_RITIRO.getName(), null);
		assertTrue(event.isValid());
		
		Proposal proposal = new Proposal(event); //creata proposta
		proposal.setOwner(owner, proposal.getOptions());
		
		ProposalHandler bacheca = new ProposalHandler(); //creata bacheca
		assertTrue(bacheca.add(proposal));
		assertTrue(proposal.hasState(State.OPEN));
		proposal.setState(State.VALID);
		proposal.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().minusDays(1));
		proposal.setState(State.OPEN);
		proposal.update();
		
		//la proposta inoltre mantiene il suo stato -> non posso iscrivermi direttamente ad essa
		assertFalse(proposal.signUp(user, proposal.getOptions()));
		bacheca.refresh(); //La bacheca si aggiorna e cambia stato alla proposta -> passa a Fallita
		assertFalse(bacheca.contains(proposal));
		assertTrue(proposal.hasState(State.FAILED));
		//La proposta, avendo superato la data ultima di termine iscrizione, viene rimossa dalla bacheca
		//La bacheca ora è vuota -> non posso iscrivermi
		assertFalse(bacheca.signUp(0, user, proposal.getOptions())); 
	}
	
	@org.junit.jupiter.api.Test
	void ugualianazaPreferenze() { 
		Proposal p = new Proposal(CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName()));
		assertTrue(p.getOptions().hasSameChoices(p.getOptions()));
	}
	
	@org.junit.jupiter.api.Test
	void propostaTermineRitiroAlGiornoAttuale() {
		User owner =  new User("Mario");
		User pinco = new User("pinco");
		
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now());
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		event.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now());
		assertTrue(event.isValid());
		
		Proposal p = new Proposal(event);
		p.setOwner(owner, p.getOptions());
		assertTrue(p.hasState(State.VALID));
		
		ProposalHandler ph = new ProposalHandler();
		assertTrue(ph.add(p));
		assertTrue(ph.contains(p));
		
		assertTrue(ph.signUp(0, pinco, ph.getPreferenze(0)));
		ph.refresh();
		//subNumber == max && tDate == termine_ritiro == termine_iscrizione -> do precedenza al termine_ iscrizione -> proposta è CLOSED
		assertTrue(p.hasState(State.CLOSED));
		assertFalse(ph.contains(0));
		//ho inviato messaggi relativi alla conferma dell'evento
		assertFalse(pinco.noMessages());
	}
	
	@org.junit.jupiter.api.Test
	void propostaConNPartecipantiA1() {
		User owner =  new User("Mario");
		User pinco = new User("pinco");
		
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 1);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now());
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		assertFalse(event.isValid());
		
		Proposal p = new Proposal(event);
		p.setOwner(owner, p.getOptions());
		assertTrue(p.hasState(State.INVALID));
		assertFalse(p.signUp(pinco, p.getOptions()));
		
		ProposalHandler ph = new ProposalHandler();
		assertFalse(ph.add(p));
		assertFalse(ph.contains(p));
	}
}
