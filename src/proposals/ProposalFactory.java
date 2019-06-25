package proposals;

import categories.Category;

/**
 * Classe con il compito di fornire una nuova istanza di una proposta
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class ProposalFactory {
	/**
	 * Fornisce una nuova nuova istanza di una proposta
	 * @param category L'evento a cui farà riferimento la proposta
	 * @return una nuova proposta che fa riferimento all'evento fornito
	 */
	public static ProposalInterface newProposal(Category category) {
		return new Proposal(category);
	}
}
