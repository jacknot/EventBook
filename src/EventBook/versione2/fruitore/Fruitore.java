package EventBook.versione2.fruitore;

import java.io.Serializable;

import EventBook.versione2.Notificabile;

public class Fruitore implements Serializable, Notificabile{

	private String nome;

	public Fruitore(String _nome) {
		nome = _nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void ricevi(Messaggio messaggio) {
		// TODO Auto-generated method stub
		
	}
}
