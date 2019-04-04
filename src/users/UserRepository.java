package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import users.User;
import users.Message;


/**
 * Classe che gestisce l'insieme di tutti gli utentu registrati al sistema.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class UserRepository implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Lista contenente i Fruitori registrati al programma
	 */
	private ArrayList<User> users;
	/**
	 * Costruttore
	 */
	public UserRepository() {
		this.users = new ArrayList<User>();
	}
	/**
	 * Restituisce l'utente di cui si è passato il nome come parametro, se presente nella lista
	 * @param name il nome dell'utente
	 * @return l'utente di cui si è inserito il nome, null altrimenti
	 */
	public User getUser(String name) {
		if(contains(name))
			return users.stream()
						.filter((f)->f.getName().equals(name))
						.findFirst().get();
		return null;
	}
	/**
	 * Controlla se l'utente è registrato
	 * @param name Il nome dell'utente da cercare
	 * @return True - se esiste un utente con tale nome <br>False - se l'utente non esiste
	 */
	public boolean contains(String name) {
		for(User fruitore: users) {
			if(fruitore.getName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Registra un utente
	 * @param name Nome dell'utente da registrare
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
	 * Permette all'utente specificato di ricevere il messaggio inviato come parametro
	 * @param name nome dell'utente
	 * @param message messaggio da inviare all'utente
	 */
	@Deprecated
	public void receive(String name, Message message) {
		for(User user: users) {
			if(user.getName().equals(name))
				user.receive(message);
		}
	}
	
	/**
	 * Restitusice la lista di utenti a cui interessa la categoria il cui nome è passato come parametro
	 * @param categoryName il nome della categoria
	 * @return la lista contenente tutti gli utenti interessati dalla categoria specificata
	 */
	public ArrayList<User> searchBy(String categoryName){
		return users.stream()
						.filter((u)->u.containsCategory(categoryName))
						.collect(Collectors.toCollection(ArrayList::new));
	}
}
