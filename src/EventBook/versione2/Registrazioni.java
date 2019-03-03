package EventBook.versione2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import EventBook.versione2.fruitore.Fruitore;
import EventBook.versione2.fruitore.Messaggio;

/**Classe che contiene la lista di tutti i Fruitori registrati al programma.<br>
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public class Registrazioni implements Serializable{

	private static final String NOMEFILE = "Registrazioni.ser";
	
	/**
	 * Lista contenente i fruitori registrati
	 */
	private ArrayList<Fruitore> fruitori;
	/**
	 * Istanza necessaria per poter implementare il Design Pattern Singleton
	 */
	private static Registrazioni instance;
	
	/**
	 * Costruttore
	 */
	private Registrazioni() {
		fruitori = new ArrayList<Fruitore>();
	}
	
	/**Metodo per ottenere l'istanza della classe<br>
	 * Necessaria per il Design Pattern Singleton
	 * @return l'istanza della classe
	 */
	public static Registrazioni getInstance() {
		if(instance == null)
			instance = new Registrazioni();
		return instance;
	
	}
	
	/**
	 * Controlla se il fruitore Ë registrato
	 * @param name Il nome del fruitore da cercare
	 * @return True - se esiste un fruitore con tale nome <br>False - se il fruitore non esiste
	 */
	public boolean contains(String nome) {
		for(Fruitore fruitore: fruitori) {
			if(fruitore.getNome().equals(nome))
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
			if(fruitore.getNome().equals(nome))
				fruitore.ricevi(messaggio);
		}
	}
	
	/**
	 * Salva l'oggetto Registrazioni su un file per uso futuro
	 * @throws IOException errore di I/O
	 */
	public void save() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(NOMEFILE);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(instance);
		out.close();
		fileOut.close();
	}
	
	/**
	 * Carica da file l'oggetto Registrazioni e lo assegna alla variabile instance
	 * @throws ClassNotFoundException La classe dell'oggetto serializzato non pu√≤ essere trovata
	 * @throws IOException errore di I/O
	 */
	public void load() throws ClassNotFoundException, IOException {
		FileInputStream fileIn = new FileInputStream(NOMEFILE);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        instance = (Registrazioni) in.readObject();
        in.close();
        fileIn.close();
	}
	
}
