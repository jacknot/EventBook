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
public class GestoreProposte{
	/**
	 * Directory ed estensione del file in cui vengono salvate le risorse in modo consistente
	 */
	private static final String NAMEFORMAT = "resource/%s.txt";
	/**
	 * Il nome di default del file in cui sono inserite le proposte
	 */
	private static final String DEFAULTFILE = "gestoreProposte";
	/**
	 * Il nome del file in cui sono salvate le risorse
	 */
	private File f;
	
	/**
	 * Stream per scrivere sul file
	 */
	private ObjectOutputStream out;
	/**
	 * Stream per leggere dal file
	 */
	private ObjectInputStream in;
	/**
	 * Il set che deve contenere le istanze delle proposte
	 */
	private InsiemeProposte set;
	
	/**
	 * Costruttore
	 */
	public GestoreProposte() {
		this.f = new File(String.format(NAMEFORMAT, DEFAULTFILE));
		set = new InsiemeProposte(Stato.APERTA);
	}
	/**
	 * Costruttore
	 * @param fileName il nome del file in cui destinare le informazioni
	 */
	public GestoreProposte(String fileName) {
		this.f = new File(String.format(NAMEFORMAT, fileName));
		set = new InsiemeProposte(Stato.APERTA);
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
	 * Iscrivi un utente nella proposta di cui si è inserito il titolo
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
	 * Carica il contenuto di sessioni precedenti
	 */
	public void load() {
		if(f.exists() && f.canRead()){
			try {
				in = new ObjectInputStream(new FileInputStream(f));
				set = (InsiemeProposte) in.readObject();
				in.close();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Carica il contenuto nel file di destinazione
	 */
	public void save() {
		try {
			if(!f.exists() || !f.canWrite())
				f.createNewFile();
			out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(set);
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Imposta il nome del file in cui salvare le risorse
	 * @param nFileName il nome del file in cui salvare le risorse
	 * @return l'esito dell'operazione
	 */
	public boolean setFile(String nFileName) {
		return this.f.renameTo(new File(String.format(NAMEFORMAT, DEFAULTFILE)));
	}
	/**
	 * Restituisce il nome del file in cui sono salvate in modo permanente le informazioni
	 * @return il nome del file
	 */ 
	public String getFileName() {
		try {
			return f.getCanonicalPath();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return String.format(NAMEFORMAT, DEFAULTFILE);
	}
}