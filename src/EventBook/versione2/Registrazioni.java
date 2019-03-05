package EventBook.versione2;

import java.io.File;
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


	/**
	 * Lista contenente i fruitori registrati
	 */
	private ArrayList<Fruitore> fruitori;
	/**
	 * Istanza necessaria per poter implementare il Design Pattern Singleton
	 */
	private static Registrazioni instance;
	/**
	 * Il file dell'oggetto
	 */
	private File f;
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
	
	/**
	 * Salva l'oggetto Registrazioni su un file per uso futuro
	 * @return l'esito dell'operazione
	 */
	public boolean save() { //Eccezioni nel main...
	  ObjectOutputStream out = null;
		if(f == null)
			return false;
		try {
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			if(f.exists() & f.canWrite()) {
				
				out = new ObjectOutputStream(new FileOutputStream(f, false));
				out.writeObject(instance);
				out.close();
				return true;
			}
		}catch(IOException e) {
			return false;
		}
		return false;
	}

	
	/**
	 * Carica da file l'oggetto Registrazioni e lo assegna alla variabile instance
	 * @return l'esito dell'operazione
	 */
	public boolean load(){
		if( f != null & f.exists() & f.canRead()) {
			try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));) {
				instance = (Registrazioni) in.readObject();
				return true;
			}catch(IOException e) {
				//send eccezione a gestore
				return false;
			}catch(ClassNotFoundException e) {
				//send eccezione a gestore
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Imposta il nome del file in cui salvare le risorse
	 * @param nFileName il nome del file in cui salvare le risorse
	 */
	public void setFile(String nFileName) {
		f = new File(nFileName);
	}
}
