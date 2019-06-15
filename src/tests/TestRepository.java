package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import categories.Category;
import categories.EventCache;
import categories.EventHeading;
import fields.FieldHeading;
import proposals.Proposal;
import proposals.State;
import users.User;
import users.UserRepository;
import utility.FileHandler;

class TestRepository {

	private static User user;
	private static UserRepository ur;
	private static UserRepository loadedUR;
	private static Proposal proposal;
	private static Proposal loadedProposal;
	
	@BeforeAll
	public static void before() {
		user = new User("Mario");
		ur = new UserRepository();
		ur.register("Mario");
		Category event = new EventCache().getCategory(EventHeading.FOOTBALLMATCH.getName());
		event.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 20);
		event.setValue(FieldHeading.TERMINEISCRIZIONE.getName(), LocalDate.now());
		event.setValue(FieldHeading.LUOGO.getName(), "Brescia");
		event.setValue(FieldHeading.DATA.getName(), LocalDate.now().plusDays(1));
		event.setValue(FieldHeading.ORA.getName(), FieldHeading.ORA.getClassType().parse("20:00"));
		event.setValue(FieldHeading.QUOTA.getName(), FieldHeading.QUOTA.getClassType().parse("10.00"));
		event.setValue(FieldHeading.GENERE.getName(), FieldHeading.GENERE.getClassType().parse("M"));
		event.setValue(FieldHeading.FASCIA_ETA.getName(), FieldHeading.FASCIA_ETA.getClassType().parse("10-50"));
		proposal = new Proposal(event);	
		proposal.setOwner(new User("Owner"), proposal.getOptions());
		proposal.setState(State.OPEN);
		proposal.signUp(user, proposal.getOptions());
		FileHandler file = new FileHandler();
		file.save("tests/provaRepository.ser", ur); //Salvataggio Database
		file.save("tests/provaProposal.ser", proposal); //Salvataggio proposta
		loadedUR = (UserRepository) file.load("tests/provaRepository.ser");
		loadedProposal = (Proposal) file.load("tests/provaProposal.ser");
	}
	
	@Test
	void testUtentePropostaUgualeUtentePropostaCaricata() {	
		assertEquals(proposal.getUsers().get(1), loadedProposal.getUsers().get(1));
	}
	
	@Test
	void testUtenteDatabaseUgualeUtenteProposta() {	
		assertEquals(loadedUR.getUser("Mario"), loadedProposal.getUsers().get(1));
	}
	
	@Test
	void testUtenteUgualeUtentePropostaCaricata() {	
		assertEquals(user, loadedProposal.getUsers().get(1));
	}
	
	@Test
	void testUtenteUgualeUtenteDatabaseCaricato() {	
		assertEquals(user, loadedUR.getUser("Mario"));
	}

}
