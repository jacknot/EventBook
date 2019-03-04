package EventBook.versione2;

import java.util.ArrayList;

import EventBook.versione2.proposta.Proposta;

/**Classe che consente di tenere in meomoria le proposte di un fruitore non ancora pubblicate, in modo da consentirne una modifica futura<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Sessione {
	/**
	 * Il nome del fruitore proprietario della sessione
	 */
	private String proprietario;
	
	/**
	 * Elenco di proposte che il proprietario ha in sospeso nella sessione corrente
	 */
	private ArrayList<Proposta> insiemeProposte;
	
	/**
	 * Costruttore
	 * @param _proprietario Il nome del fruitore proprietario della sessione
	 */
	public Sessione(String _proprietario) {
		proprietario = _proprietario;
		insiemeProposte = new ArrayList<Proposta>();
	}

	/**
	 * Restituisce il proprietario della sessione
	 * @return Nome del fruitore proprietario
	 */
	public String getProprietario() {
		return proprietario;
	}

	/**
	 * Restituisce l'elenco delle proposte del fruitore
	 * @return Elenco di proposte
	 */
	public ArrayList<Proposta> getInsiemeProposte() {
		return insiemeProposte;
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
	
	/**
	 * Pubblica la proposta in bacheca
	 * @param proposta Proposta da pubblicare
	 */
	public void pubblica(Proposta proposta) {
		GestoreProposte.getInstance().add(proposta);
	}
	
}
