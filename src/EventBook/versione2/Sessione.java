package EventBook.versione2;

import java.util.ArrayList;
import java.util.Optional;

import EventBook.versione1.campi.ExpandedHeading;
import EventBook.versione2.fruitore.Fruitore;
import EventBook.versione2.proposta.Proposta;

/**Classe che consente di tenere in meomoria le proposte di un fruitore non ancora pubblicate, in modo da consentirne una modifica futura<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Sessione {
	/**
	 * Il nome del fruitore proprietario della sessione
	 */
	private Fruitore proprietario;
	
	/**
	 * Elenco di proposte che il proprietario ha in sospeso nella sessione corrente
	 */
	private ArrayList<Proposta> insiemeProposte;
	/**
	 * Costruttore
	 * @param _proprietario Il nome del fruitore proprietario della sessione
	 */
	public Sessione(Fruitore _proprietario) {
		proprietario = _proprietario;
		insiemeProposte = new ArrayList<Proposta>();
	}

	/**
	 * Restituisce il proprietario della sessione
	 * @return Nome del fruitore proprietario
	 */
	public Fruitore getProprietario() {
		return proprietario;
	}

	/**
	 * Restituisce la proposta specificata dal nome
	 * @param nome nome della proposta
	 * @return Proposta
	 */
	public Proposta getProposta(String nome) {
		Optional<Proposta> o = insiemeProposte.stream()
							.filter((p)->p.hasTitle(nome))
							.findFirst(); //O while
		if(o.isPresent())
			return o.get();
		return null;
	}
	/**
	 * Aggiunge una proposta all'elenco delle proposte
	 * @param proposta Proposta da aggiungere all'elenco
	 */
	public void aggiungiProposta(Proposta proposta) {
		insiemeProposte.add(proposta);
	}
	/**
	 * Modifica una proposta esistente 
	 * @param propostaSelezionata il titolo della proposta da modificare
	 * @param nome Il nome del campo da modificare
	 * @param valore Nuovo valore da sostituire
	 */
	public void modificaProposta(Proposta propostaSelezionata, String nome, Object valore) {
		for(Proposta proposta: insiemeProposte) {
			if(proposta.equals(propostaSelezionata))
				proposta.cambia(nome, valore);
		}
	}
	/*
	/**
	 * Pubblica la proposta in bacheca
	 * @param proposta Proposta da pubblicare
	 *//*
	public void pubblica(Proposta proposta) {
		GestoreProposte.getInstance().add(proposta);
	}*/
	
	public String showProposals() {
		StringBuilder stringaRitorno = new StringBuilder();
		for(int i=0; i<insiemeProposte.size();i++) {
			stringaRitorno.append(i+1)
						.append(" : ")
						.append(insiemeProposte.get(i).getValue(ExpandedHeading.TITOLO.getName()))
						.append("\n");
		}
		return stringaRitorno.toString();
	}
	
}
