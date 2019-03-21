package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import users.User;
import users.Message;


/**Classe che contiene la lista di tutti i Fruitori registrati al programma.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Database implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Lista contenente i fruitori registrati
	 */
	private ArrayList<User> users;
	/**
	 * Costruttore
	 */
	public Database() {
		this.users = new ArrayList<User>();
	}
	/**
	 * Restituisce il fruitore di cui si è inserito il nome, se presente
	 * @param name il nome del fruitore
	 * @return il fruitore di cui si è inserito il nome, null altrimenti
	 */
	public User getUser(String name) {
		if(contains(name))
			return users.stream()
						.filter((f)->f.getName().equals(name))
						.findFirst().get();
		return null;
	}
	/**
	 * Controlla se il fruitore è registrato
	 * @param name Il nome del fruitore da cercare
	 * @return True - se esiste un fruitore con tale nome <br>False - se il fruitore non esiste
	 */
	public boolean contains(String name) {
		for(User fruitore: users) {
			if(fruitore.getName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Registra un fruitore nella lista Registrazioni
	 * @param name Nome del fruitore da registrare
	 * @return True - l'utente è stato registrato con successo<br>False - l'utente non è stato registrato
	 */
	public boolean register(String name) {
		if(!contains(name)) {
			users.add(new User(name));
			return true;
		}
		return false;
	}
	
	/**
	 * Permette al fruitore specificato di ricevere il Messaggio inviato come parametro
	 * @param name nome del fruitore
	 * @param message messaggio da inviare al fruitore
	 */
	public void receive(String name, Message message) {
		for(User user: users) {
			if(user.getName().equals(name))
				user.receive(message);
		}
	}
	
	/**
	 * Restitusice la lista di utenti a cui interessa la categoria di cui si è inserito il nome
	 * @param categoryName il nome della categoria
	 * @return la lista contenente tutti gli utenti interessati nella categoria inserita
	 */
	public ArrayList<User> searchBy(String categoryName){
		return users.stream()
						.filter((u)->u.containsCategory(categoryName))
						.collect(Collectors.toCollection(ArrayList::new));
	}
}
