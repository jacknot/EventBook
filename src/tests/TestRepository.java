package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import categories.FootballMatch;
import fields.FieldHeading;
import proposals.Proposal;
import proposals.State;
import users.User;
import users.UserRepository;
import utility.FileHandler;

class TestRepository {

	@Test
	void testCollegamentoOggettiSalvati() {
		User user = new User("Mario");
		UserRepository ur = new UserRepository();
		ur.register("Mario");
		Proposal proposal = new Proposal(new FootballMatch());
		proposal.setState(State.OPEN);
		proposal.setValue(FieldHeading.NUMPARTECIPANTI.getName(), 30);
		proposal.signUp(user, null);
		FileHandler file = new FileHandler();
		file.save("resources/provaRepository.ser", ur); //Salvataggio Database
		file.save("resources/provaProposal.ser", proposal); //Salvataggio proposta
		UserRepository loadedUR = (UserRepository) file.load("provaRepository.ser");
		Proposal loadedProposal = (Proposal) file.load("provaProposal.ser");
		System.out.println(proposal.getUsers().get(0));
		System.out.println(loadedProposal.getUsers().get(0));
	
		assertEquals(proposal.getUsers().get(0), loadedProposal.getUsers().get(0));
	}

}
