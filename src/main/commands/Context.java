package main.commands;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import proposals.ProposalHandler;
import users.UserDatabase;
import utility.FileHandler;
import utility.Session;
import utility.StringConstant;

public class Context implements Closeable{
	
	private static final String NOTICEBOARD = "resource/bacheca.ser";
	private static final String DATABASE = "resource/registrazioni.ser";

	private static final long DELAY = 3600000;//60MIN
	
	private Timer refreshTimer;
	private Session session;
	private UserDatabase database;
	private ProposalHandler noticeBoard;
	private InOutStream inOut;
	
	/**
	 * Costruttore 
	 * @param inOut lo stream di ingresso/uscita
	 */
	public Context(InOutStream inOut) {
		this.inOut = inOut;
		this.session = null;
		//logica di creazione degli argomenti del contesto
		load();
		
		refreshTimer = new Timer("RefreshNoticeBoard");
		refreshTimer.schedule(new TimerTask() {
			public void run() {
				noticeBoard.refresh();
			}
		}, DELAY, DELAY);
	}

	/**
	 * Carica tutte le informazioni importanti legate al contesto
	 */
	private void load() {
		inOut.writeln("Caricamento database ...");
		database = (UserDatabase)new FileHandler().load(DATABASE);
		if(database == null) {
			database = new UserDatabase();
			inOut.writeln("Caricato nuovo database");
			}
		inOut.writeln("Caricamento bacheca ...");
		noticeBoard = (ProposalHandler)new FileHandler().load(NOTICEBOARD);
		if(noticeBoard == null) {
			noticeBoard = new ProposalHandler();
			inOut.writeln("Caricata nuova bacheca");
			}
		inOut.writeln("Fine caricamento");
		inOut.writeln("Pronto");
		inOut.write(StringConstant.NEW_LINE + StringConstant.WAITING);
	}
	
	/**
	 * Salva tutte le informazioni importanti del contesto in modo permanente
	 */
	private void save() {
		inOut.write("Salvataggio bacheca... ");
		inOut.writeln((new FileHandler().save(NOTICEBOARD, noticeBoard))? StringConstant.SAVE_COMPLETED : StringConstant.SAVE_FAILED);
		inOut.write("Salvataggio database... ");
		inOut.writeln((new FileHandler().save(DATABASE, database))? StringConstant.SAVE_COMPLETED : StringConstant.SAVE_FAILED);
	}
	/**
	 * Restituisce lo stream su cui fare operazioni di ingresso/uscita
	 * @return Lo stream di ingresso/uscita
	 */
	public InOutStream getIOStream() {
		return inOut;
	}
	/**
	 * Restituisce l'attuale sessione
	 * @return
	 */
	public Session getSession() {
		return session;
	}
	/**
	 * Inizializza la sessione legandola all'utente di cui si è inserito il nomignolo se questo è registrato nel databse
	 * @param nomignolo
	 * @return True - la sezione è stata inizializzata correttamente<br>False - la sessione non è stata inizializzata correttamente
	 */
	public boolean newSession(String nomignolo) {
		if(database.contains(nomignolo)) {
			session = new Session(database.getUser(nomignolo));
			return true;
		}
		return false;
	}
	/**
	 * Resetta la sessione usata dal contesto
	 */
	public void resetSession() {
		this.session = null;
	}
	/**
	 * Restituisce il database di utenti
	 * @return il database di utenti
	 */
	public UserDatabase getDatabase() {
		return database;
	}
	/**
	 * Restituisce il gestore delle proposte
	 * @return il gestore delle proposte
	 */
	public ProposalHandler getProposalHandler() {
		return noticeBoard;
	}
	/**
	 * Chiude tutte le risorse usate dal contesto.<br>
	 * Non è responsabilità del contesto la chiusura dello stream di ingressi uscita
	 * @throws IOException eventuali eccezioni dovute alla chiusura non riuscita delle risorse
	 */
	@Override
	public void close() throws IOException {
		save();
		refreshTimer.cancel();
	}
}
