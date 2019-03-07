package EventBook.versione2;

import java.io.Serializable;
import java.util.ArrayList;

import EventBook.fruitore.Fruitore;
import EventBook.fruitore.Messaggio;


/**Classe che contiene la lista di tutti i Fruitori registrati al programma.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Database implements Serializable{

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
	//estendere rilasciando una Sessione con già incorporato il fruitore
	public Fruitore getFruitore(String name) {
		if(contains(name))
			return fruitori.stream().filter((f)->f.getName().equals(name)).findFirst().get();
		return null;
	}
	/**
	 * Controlla se il fruitore � registrato
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
	 */
	public void registra(String nome) {
		fruitori.add(new Fruitore(nome));
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
