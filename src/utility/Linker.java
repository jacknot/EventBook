package utility;

import proposals.Proposal;
import proposals.ProposalHandler;
import users.UserRepository;

public class Linker {

	
	private static Linker instance;
	
	private Linker() {}
	
	public static Linker getInstance() {
		if(instance==null)
			instance = new Linker();
		return instance;
	}
	
	public void link(ProposalHandler proposalHandler, UserRepository database) {
		for(Proposal proposal : proposalHandler.getAll()) {		
				proposal.getSubscribers().forEach((sub)->sub.setUser(database.getUser(sub.getName())));
			}	
	}
}
