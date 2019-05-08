package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.OptionsSet;
import proposals.Proposal;
import proposals.ProposalHandler;
import proposals.State;
import users.UserRepository;
import users.User;
import utility.MessageHandler;

class TestInviti {

	@org.junit.jupiter.api.Test
	void confrontoPreferenze() {
		Proposal p1 = new Proposal(new CategoryCache().getCategory(CategoryHeading.CONCERT.getName()));
		OptionsSet pref = p1.getOptions();
		assertTrue(p1.getOptions().hasSameChoices(pref));
		assertTrue(pref.contains(FieldHeading.BACKSTAGE_PASS));
		assertTrue(pref.makeChoice(FieldHeading.BACKSTAGE_PASS, true));
		assertTrue(p1.getOptions().hasSameChoices(pref));
	}
	
	@org.junit.jupiter.api.Test
	void iscrizioneConOpzioni() {
		//creazione database
		UserRepository database = new UserRepository();
		database.register("pinco");
		database.register("Mario");
		ProposalHandler noticeBoard = new ProposalHandler();
		
		//Creazione nuova categoria
		Category c1 = new CategoryCache().getCategory(CategoryHeading.CONCERT.getName());
		c1.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		c1.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		c1.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		c1.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		c1.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		c1.setValue(FieldHeading.QUOTA.getName(),10.00);
		c1.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		c1.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().plusDays(1));
		c1.setValue(FieldHeading.BACKSTAGE_PASS.getName(), 20.00);
		c1.removeOptionalField(FieldHeading.MEET_AND_GREET);
		c1.removeOptionalField(FieldHeading.MERCHANDISE);
		//non rimuovo campi non opzionali
		assertFalse(c1.removeOptionalField(FieldHeading.FASCIA_ETA));
		assertTrue(c1.containsField(FieldHeading.BACKSTAGE_PASS.getName()));
		assertFalse(c1.containsField(FieldHeading.MEET_AND_GREET.getName()));
		assertFalse(c1.containsField(FieldHeading.MERCHANDISE.getName()));
		
		//proposta aggiunta in bacheca
		Proposal proposal = new Proposal(c1);
		assertTrue(proposal.setOwner(database.getUser("Mario"), proposal.getOptions()));
		assertTrue(proposal.hasState(State.VALID));
		noticeBoard.add(proposal);
		
		//la proposta può chiudersi solo quando ho abbastanza utenti e la data attuale è > data termine ultimo ritiro
		//devo impostare la data termine < data attuale
		proposal.setState(State.VALID);
		assertTrue(c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(1)));
		proposal.setState(State.OPEN);
		assertTrue(((LocalDate)proposal.getValue(FieldHeading.TERMINE_RITIRO.getName())).compareTo(LocalDate.now().minusDays(1)) == 0);
		
		//effettuo iscrizione
		OptionsSet pref = noticeBoard.getPreferenze(0);

		assertFalse(pref.contains(FieldHeading.MEET_AND_GREET));
		assertFalse(pref.contains(FieldHeading.MERCHANDISE));
		assertTrue(pref.contains(FieldHeading.BACKSTAGE_PASS));
		assertTrue(pref.makeChoice(FieldHeading.BACKSTAGE_PASS, true));
		
		//iscrizione alla proposta
		assertTrue(noticeBoard.signUp(0, database.getUser("pinco"), pref));
		
		//check iscrizione -> capienza MAX -> proposta CHIUSA -> eventi legati al passaggio di stato
		assertTrue(proposal.hasState(State.CLOSED));
		assertFalse(database.getUser("Mario").noMessages());
		assertFalse(database.getUser("pinco").noMessages());
		System.out.println("--------------------------------------------------------------------------- \n"
							+ "iscrizioneConOpzioni ->\n"
							+ "Pinco: " + database.getUser("pinco").showNotifications());
		System.out.println("\nMario: " + database.getUser("Mario").showNotifications());
	}
	
	@org.junit.jupiter.api.Test
	void avvisoAgliInteressatiAllaCreazione() { 
		//creazione database utenti
		UserRepository database = new UserRepository();
		database.register("pinco");
		database.register("Mario");
		database.getUser("pinco").setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), 
											FieldHeading.CATEGORIE_INTERESSE.getClassType().parse("Partita di Calcio")); //Interessato a calcio
		
		ProposalHandler noticeBoard = new ProposalHandler(); //creazione bacheca
		//Creazione nuova categoria
		Category event = new CategoryCache().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event);
		proposal.setOwner(database.getUser("Mario"), proposal.getOptions());
		assertTrue(proposal.isValid() && proposal.hasState(State.VALID));
		
		noticeBoard.add(proposal); //Proposta aggiunta in bacheca
		ArrayList<User> receivers = database.searchBy(proposal.getCategoryName()); //Lista di utenti interessati in base alla categoria
		receivers.remove(database.getUser("Mario")); //rimosso il proprietario proposta
		new MessageHandler().notifyByInterest(receivers, proposal.getCategoryName()); //invio messaggio alla lista di utenti
		assertFalse(database.getUser("pinco").noMessages()); //Pinco ha ricevuto il messaggio
	}
	
	@org.junit.jupiter.api.Test
	void invitiAPropostaAPERTA() {
//		idea:
//			1. creo proposta
//			2. aggiungo proposta in bacheca
//			3. iscrivo gente
//			4. raggiungo il limite
//			5. farla passare APERTA - > CHIUSA
//			6. creo nuova proposta
//			7. invito gente
//			8. controllo che la gente sia stata corretamente invitata
		
		UserRepository db = new UserRepository();
		ProposalHandler ph = new ProposalHandler();
		db.register("mario");
		db.register("carlo");
		assertTrue(db.contains("mario"));
		assertTrue(db.contains("carlo"));
		assertFalse(db.contains("pluto"));
		
		//creo proposta
		Category c1 = new CategoryCache().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		c1.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		c1.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		c1.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		c1.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		c1.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		c1.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		c1.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		c1.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(1));
		//la data di ritiro è precedente la data attuale
		assertFalse(c1.isValid());
		c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().plusDays(1));
		assertTrue(c1.isValid());
		
		Proposal p1 = new Proposal(c1);
		p1.setOwner(db.getUser("mario"), p1.getOptions());
		
		assertTrue(p1.hasState(State.VALID));
		//aggiungo la proposta al gestore
		assertTrue(ph.add(p1));
		assertTrue(p1.hasState(State.OPEN));
		assertTrue(ph.contains(p1));
		assertTrue(ph.isSignedUp(0, db.getUser("mario")));
		//la proposta può chiudersi solo quando ho abbastanza utenti e la data attuale è > data termine ultimo ritiro
		//devo impostare la data termine < data attuale
		p1.setState(State.VALID);
		assertTrue(c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(1)));
		p1.setState(State.OPEN);
		assertTrue(((LocalDate)p1.getValue(FieldHeading.TERMINE_RITIRO.getName())).compareTo(LocalDate.now().minusDays(1)) == 0);
		//ho iscritto gente fino a far riempire la proposta (APERTA -> CHIUSA)
		ph.signUp(0, db.getUser("carlo"), ph.getPreferenze(0));
		assertTrue(p1.hasState(State.CLOSED));
		assertFalse(ph.contains(p1));
		
		//creo nuova proposta
		Category c2 = new CategoryCache().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		c2.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		c2.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		c2.setValue(FieldHeading.LUOGO.getName(), "Lograto");
		c2.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		c2.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		c2.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		c2.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		c2.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		c2.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().plusDays(1));
		assertTrue(c2.isValid());
		
		Proposal p2 = new Proposal(c2);
		p2.setOwner(db.getUser("mario"), p2.getOptions());
		assertTrue(p2.hasState(State.VALID));
		
		//aggiungo la proposta al gestore
		assertTrue(ph.add(p2));
		assertTrue(p2.hasState(State.OPEN));
		assertTrue(ph.contains(p2));
		assertTrue(ph.isSignedUp(0, db.getUser("mario")));
		
		//invito gente (carlo)
		ArrayList<User> receivers = ph.searchBy(db.getUser("mario"), p2.getCategoryName());
		assertTrue(receivers.contains(db.getUser("carlo")));
		assertTrue(ph.inviteTo(0, receivers));		
		assertFalse(db.getUser("carlo").noMessages());
		System.out.println("--------------------------------------------------------------------------- \n"
								+ "invitiAPropostaAPERTA ->"
								+ "\ncarlo: " + db.getUser("carlo").showNotifications());
	}

}
