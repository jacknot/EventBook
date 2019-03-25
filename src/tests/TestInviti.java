package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import proposals.Proposal;
import proposals.ProposalHandler;
import users.Database;
import users.User;
import utility.MessageHandler;

class TestInviti {

	@org.junit.jupiter.api.Test
	void notificaInteresse() { 
		Database database = new Database(); //creazione database utenti
		database.register("pinco");
		database.register("Mario"); //registrati nel database
		database.getUser("pinco").setValue(FieldHeading.CATEGORIE_INTERESSE.getName(), FieldHeading.CATEGORIE_INTERESSE.getClassType().parse("Partita di Calcio")); //Interessato a calcio
		
		ProposalHandler noticeBoard = new ProposalHandler(); //creazione bacheca
		//Creazione nuova categoria
		Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), FieldHeading.TERMINEISCRIZIONE.getClassType().parse("21/06/2019"));
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), FieldHeading.DATA.getClassType().parse("25/06/2019"));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		Proposal proposal = new Proposal(event, database.getUser("Mario"));
		
		noticeBoard.add(proposal); //Proposta aggiunta in bacheca
		ArrayList<User> receivers = database.searchBy(proposal.getCategoryName()); //Lista di utenti interessati in base alla categoria
		receivers.remove(database.getUser("Mario")); //rimosso il proprietario proposta
		MessageHandler.getInstance().notifyByInterest(receivers, proposal.getCategoryName()); //invio messaggio alla lista di utenti
		assertFalse(database.getUser("pinco").noMessages()); //Pinco ha ricevuto il messaggio
	}
	

}
