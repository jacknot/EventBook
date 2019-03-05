package EventBook.versione2;

import java.io.*;

import EventBook.versione2.fruitore.Notificabile;
import EventBook.versione2.proposta.InsiemeProposte;
import EventBook.versione2.proposta.Proposta;
import EventBook.versione2.proposta.Stato;

/**
 * Ha il compito di tenere un set persistente di proposte visibili a tutti gli utenti<br>
 * Implementa il Design Pattern Singleton
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class GestoreProposte implements Serializable{
	/**
	 * Contiene il file in cui sono presenti le informazioni del gestore di proposte
	 */
	private File f;

	/**
	 * Il set che deve contenere le istanze delle proposte
	 */ 
	private InsiemeProposte set;
	/**
	 * Istanza per implementare il Design Pattern Singleton
	 */
	private static GestoreProposte instance;

	/**
	 * Costruttore
	 * @param fileName il nome del file in cui destinare le informazioni
	 */
	private GestoreProposte() {
		set = new InsiemeProposte(Stato.APERTA);
	}
	/**
	 * Restituisce un'istanza della classe
	 * @return l'istanza della classe
	 */
	public static GestoreProposte getInstance() {
		if(instance == null)
			instance = new GestoreProposte();
		return instance;
	}
	
	/**
	 * Aggiunge una proposta al set
	 * @param p la proposta da aggiungere
	 * @return l'esito dell'operazione
	 */
	public boolean add(Proposta p) {
		return set.add(p);
	}
	/**
	 * Effettua un refresh sul set
	 */
	public void refresh() {
		set.refresh();
	}
	/**
	 * Iscrivi un utente nella proposta di cui si � inserito il titolo
	 * @param title il titolo della proposta a cui aggiungere l'utente
	 * @param user l'utente da aggiungere alla proposta 
	 * @return l'esito dell'iscrizione
	 */
	public boolean iscrivi(String title, Notificabile user) {
		return set.iscrivi(title, user);
	}
	/**
	 * Controlla che il set contenga almeno una proposta con il titolo inserito
	 * @param name il titolo della proposta
	 * @return True - contiene almeno una proposta con quel titolo<br>False - non ci sono proposte con quel titolo
	 */
	public boolean contains(String title) {
		return set.contains(title);
	}
	/**
	 * Carica il contenuto di sessioni precedenti<br>
	 * Richiede che sia stato impostato un file da cui poter prelevare le informazioni
	 * @return l'esito dell'operazione
	 */
	public boolean load(){
		ObjectInputStream in = null;
		if( f != null && f.exists() && f.canRead()) {
			try {
				in = new ObjectInputStream(new FileInputStream(f));
				instance = (GestoreProposte) in.readObject();
				in.close();
				return true;
			}catch(IOException e) {
				//send eccezione a gestore
				e.printStackTrace();
				return false;
			}catch(ClassNotFoundException e) {
				//send eccezione a gestore
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	/**
	 * Carica il contenuto nel file di destinazione<br>
	 * Necessita che sia stato precedentemente inserito un file su cui operare
	 * @return l'esito dell'operazione
	 */
	public boolean save() {
		ObjectOutputStream out = null;
		if(f == null)
			return false;
		try {
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			if(f.exists() && f.canWrite()) {
				out = new ObjectOutputStream(new FileOutputStream(f, false));
				out.writeObject(instance);
				out.close();
				return true;
			}
		}catch(IOException e) {
			e.printStackTrace();
			return false;
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
	/**
	 * Restituisce il nome del file in cui sono salvate in modo permanente le informazioni
	 * @return il nome del file
	 */ 
	public String getFileName() {
			return f.getPath();
	}
}