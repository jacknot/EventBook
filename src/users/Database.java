package users;

import java.io.Serializable;
import java.util.ArrayList;

import users.Fruitore;
import users.Messaggio;


/**Classe che contiene la lista di tutti i Fruitori registrati al programma.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Database implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Lista contenente i fruitori registrati
	 */
	private ArrayList<Fruitore> fruitori;
	/**
	 * Costruttore
	 */
	public Database() {
		fruitori = new ArrayList<Fruitore>();
	}
	/**
	 * Restituisce il fruitore di cui si è inserito il nome, se presente
	 * @param name il nome del fruitore
	 * @return il fruitore di cui si è inserito il nome, null altrimenti
	 */
	public Fruitore getFruitore(String name) {
		if(contains(name))
			return fruitori.stream().filter((f)->f.getName().equals(name)).findFirst().get();
		return null;
	}
	/**
	 * Controlla se il fruitore è registrato
	 * @param name Il nome del fruitore da cercare
	 * @return True - se esiste un fruitore con tale nome <br>False - se il fruitore non esiste
	 */
	public boolean contains(String nome) {
		for(Fruitore fruitore: fruitori) {
			if(fruitore.getName().equals(nome))
				return true;
		}
		return false;
	}
	
	/**
	 * Registra un fruitore nella lista Registrazioni
	 * @param nome Nome del fruitore da registrare
	 * @return True - l'utente è stato registrato con successo<br>False - l'utente non è stato registrato
	 */
	public boolean registra(String nome) {
		if(!contains(nome)) {
			fruitori.add(new Fruitore(nome));
			return true;
		}
		return false;
	}
	
	/**
	 * Permette al fruitore specificato di ricevere il Messaggio inviato come parametro
	 * @param nome nome del fruitore
	 * @param messaggio messaggio da inviare al fruitore
	 */
	public void ricevi(String nome, Messaggio messaggio) {
		for(Fruitore fruitore: fruitori) {
			if(fruitore.getName().equals(nome))
				fruitore.ricevi(messaggio);
		}
	}
}
