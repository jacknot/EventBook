package EventBook.versione2.fruitore;

import java.io.Serializable;

public class Fruitore extends Notificabile implements Serializable{

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
