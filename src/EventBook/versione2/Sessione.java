package EventBook.versione2;

import java.util.ArrayList;

public class Sessione {

	private String proprietario;
	private ArrayList<Proposta> insiemeProposte;
	
	public Sessione(String _proprietario) {
		proprietario = _proprietario;
		insiemeProposte = new ArrayList<Proposta>();
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	public ArrayList<Proposta> getInsiemeProposte() {
		return insiemeProposte;
	}

	public void setInsiemeProposte(ArrayList<Proposta> insiemeProposte) {
		this.insiemeProposte = insiemeProposte;
	}
	
	public void aggiungiProposta(Proposta proposta) {
		insiemeProposte.add(proposta);
	}
	
	public void modificaProposta(Proposta propostaSelezionata, String nome, String valore) {
		for(Proposta proposta: insiemeProposte) {
			if(proposta.equals(propostaSelezionata))
				proposta.cambia(nome, valore);
		}
	}
	
	public void pubblica(Proposta proposta) {
		//
	}
	
}
