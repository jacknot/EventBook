package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.Proposal;
import proposals.ProposalHandler;
import proposals.State;
import users.UserDatabase;
import users.Message;
import users.User;
import utility.MessageHandler;

class TestInviti {

	@org.junit.jupiter.api.Test
	void notificaInteresse() { 
		UserDatabase database = new UserDatabase(); //creazione database utenti
		database.register("pinco");
		database.register("Mario"); //registrati nel database
		database.getUser("pinco").setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), 
											FieldHeading.CATEGORIE_INTERESSE.getClassType().parse("Partita di Calcio")); //Interessato a calcio
		
		ProposalHandler noticeBoard = new ProposalHandler(); //creazione bacheca
		//Creazione nuova categoria
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event);
		proposal.setOwner(database.getUser("Mario"), proposal.getPreferenze());
		
		noticeBoard.add(proposal); //Proposta aggiunta in bacheca
		ArrayList<User> receivers = database.searchBy(proposal.getCategoryName()); //Lista di utenti interessati in base alla categoria
		receivers.remove(database.getUser("Mario")); //rimosso il proprietario proposta
		MessageHandler.getInstance().notifyByInterest(receivers, proposal.getCategoryName()); //invio messaggio alla lista di utenti
		assertFalse(database.getUser("pinco").noMessages()); //Pinco ha ricevuto il messaggio
	}
	
	@org.junit.jupiter.api.Test
	void invitaTutti() {
//		idea:
//			1. creo proposta
//			2. aggiungo proposta in bacheca
//			3. iscrivo gente
//			4. raggiungo il limite
//			5. farla passare APERTA - > CHIUSA
//			6. creo nuova proposta
//			7. invito gente
//			8. controllo che la gente sia stata corretamente invitata
		
		UserDatabase db = new UserDatabase();
		ProposalHandler ph = new ProposalHandler();
		db.register("mario");
		db.register("carlo");
		assertTrue(db.contains("mario"));
		assertTrue(db.contains("carlo"));
		assertFalse(db.contains("pluto"));
		
		//creo proposta
		Category c1 = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		c1.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		c1.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		c1.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		c1.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		c1.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		c1.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		c1.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		c1.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		c1.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(1));
		assertTrue(c1.isValid());
		
		Proposal p1 = new Proposal(c1);
		p1.setOwner(db.getUser("mario"), p1.getPreferenze());
		
		assertTrue(p1.hasState(State.VALID));
		//aggiungo la proposta al gestore
		assertTrue(ph.add(p1));
		assertTrue(p1.hasState(State.OPEN));
		assertTrue(ph.contains(p1));
		assertTrue(ph.isSignedUp(0, db.getUser("mario")));
		//ho iscritto gente fino a far riempire la proposta (APERTA -> CHIUSA)
		ph.signUp(0, db.getUser("carlo"), ph.getPreferenze(0));
		assertTrue(p1.hasState(State.CLOSED));
		assertFalse(ph.contains(p1));
		
		//creo nuova proposta
		Category c2 = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		c2.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 2);
		c2.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now().plusDays(1));
		c2.setValue(FieldHeading.LUOGO.getName(), "Lograto");
		c2.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(2));
		c2.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		c2.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		c2.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		c2.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		c2.setValue(FieldHeading.TERMINE_RITIRO.getName(), LocalDate.now().minusDays(1));
		assertTrue(c2.isValid());
		
		Proposal p2 = new Proposal(c2);
		p2.setOwner(db.getUser("mario"), p2.getPreferenze());
		assertTrue(p2.hasState(State.VALID));
		
		//aggiungo la proposta al gestore
		assertTrue(ph.add(p2));
		assertTrue(p2.hasState(State.OPEN));
		assertTrue(ph.contains(p2));
		assertTrue(ph.isSignedUp(0, db.getUser("mario")));
		
		//invito gente (carlo)
		ArrayList<User> receivers = ph.searchBy(db.getUser("mario"), p2.getCategoryName());
		assertTrue(receivers.contains(db.getUser("carlo")));
		receivers.stream().forEach((u)->u.receive(new Message("prova","prova","Invito te!")));
		assertFalse(db.getUser("carlo").noMessages());
		System.out.println(db.getUser("carlo").showNotifications());
	}

}
