package EventBook.versione2.main;

import java.util.*;
import java.util.stream.*;

import EventBook.campi.*;
import EventBook.categoria.*;
import EventBook.proposta.*;
import EventBook.versione2.Database;
import EventBook.versione2.FileHandler;
import EventBook.versione2.Sessione;

/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	private static final String BACHECA = "resource/bacheca.ser";
	private static final String DATABASE = "resource/registrazioni.ser";
	
	private static final String NEW_LINE = "\n";
	private static final String MESSAGGIO_BENVENUTO = "Welcome to EventBook";
	private static final String ATTESA_COMANDO = "> ";
	private static final String MESSAGGIO_USCITA = "Bye Bye";
	
	private static final String ERRORE_SCONOSCIUTO = "Il comando inserito non è stato riconosciuto ('help' per i comandi a disposizione)";

	private static Timer refreshTimer;
	private static Scanner in;
	private static ListaComandi protocollo;
	private static Sessione session;
	private static Database db;
	private static InsiemeProposte bacheca;
	
	private static final long DELAY = 20000;//300000;//5MIN
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		protocollo = new ListaComandi();
		
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
			System.out.println(MESSAGGIO_USCITA);	
			in.close();
			refreshTimer.cancel();
			save();
		}));
		
		in = new Scanner(System.in);
		String comando;
		load();
		
		refreshTimer = new Timer("RefreshBacheca");
		refreshTimer.schedule(new TimerTask() {
			public void run() {
				bacheca.refresh();
				//System.out.println("(Refreshed)");
			}
		}, DELAY, DELAY);
		
		System.out.println(NEW_LINE + MESSAGGIO_BENVENUTO);
		do {
			System.out.print(NEW_LINE + ATTESA_COMANDO);
			comando = in.nextLine().trim();		
			System.out.print(NEW_LINE);
			if(protocollo.contains(comando))
				protocollo.run(comando);
			else
				System.out.println(ERRORE_SCONOSCIUTO);
		}while(true);		
	}
	/**
	 * Carica il database e la bacheca
	 */
	private static void load() {
		System.out.println("Caricamento database ...");
		db = (Database)new FileHandler().load(DATABASE);
		if(db == null) {
			db = new Database();
			System.out.println("Caricato nuovo database");
			}
		System.out.println("Caricamento bacheca ...");
		bacheca = (InsiemeProposte)new FileHandler().load(BACHECA);
		if(bacheca == null) {
			bacheca = InsiemeProposte.newBacheca();
			System.out.println("Caricata nuova bacheca");
			}
		System.out.println("Fine caricamento");
	}
	/**
	 * Salva la bacheca e il database
	 */
	private static void save() {
		System.out.println("Salvataggio bacheca ...");
		System.out.println(new FileHandler().save(BACHECA, bacheca));
		System.out.println("Salvataggio database ...");
		System.out.println(new FileHandler().save(DATABASE, db));
	}
	/**
	 * Effettua operazioni sul protocollo a seguito del log in dell'utente
	 */
	private static void logIn() {
		protocollo.logIn();
	}
	/**
	 * Effettua operazioni sul protocollo a seguito del log out dell'utente
	 */
	private static void logOut() {
		protocollo.logOut();
	}
	
	/**
	 * Enumerazione contente i vari comandi disponibili all'utente, comprese le loro funzionalità
	 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
	 *
	 */
	protected enum Comando {
		
		EXIT("exit", "Esci dal programma",()->System.exit(0)),
		CATEGORIA("categoria", "Mostra la categoria disponibile", ()->{
			Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO);
			System.out.print(p.getDescription());
		}),
		DESCRIZIONE("descrizione", "Mostra le caratteristiche della categoria disponibile", ()->{
			Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO);
			System.out.print(p.getFeatures());
		}),
		REGISTRA("registra", "Registra un fruitore", ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			if(db.registra(nome))
				System.out.println("L'utente è stato registrato con successo");
			else
				System.out.println("L'utente non è stato registrato");
		}),
		LOGIN("login", "Accedi", ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			if(db.contains(nome)) {
				session = new Sessione(db.getFruitore(nome));
				logIn();
				System.out.println("Loggato come: " + nome);
			}
			else {
				System.out.println("Utente non registrato");
			}
		}),
		LOGOUT("logout", "Per uscire", ()->{
			session = null;
			logOut();
			System.out.println("Logout eseguito");
			}),
		MODIFICA("modifica","Modifica il campo di una proposta",()->{
			boolean annulla = false;
			//inserisci id proposta
			boolean valido = false;
			int id = -1;
			do {
				try {
				System.out.print("Inserisci l'identificatore : ");
				id = Integer.parseInt(in.nextLine());
				valido = true;
				bacheca.add(session.getProposta(id));
				}catch(Exception e) {
					System.out.println("Inserisci un numero");
				}
			}while(!valido);
			//inserisci nome del campo da modificare
			valido = false;
			ExpandedHeading campo = ExpandedHeading.TITOLO;
			do {
				System.out.print("Inserisci il nome del campo che vuoi modificare : ");
				String nCampo = in.nextLine();
				try {
					campo = ExpandedHeading.valueOf(nCampo.toUpperCase());
					valido = true;
				}catch(IllegalArgumentException e) {
					System.out.println("Il nome inserito non appartiene ad un campo");
				}
			}while(!valido);
			//inserisci valore del campo da modificare
			valido = false;
			Object obj;
			do {
				System.out.print("Inserisci il nuovo valore (" + campo.getType().getSimpleName()+") : ");
				obj = campo.getClassType().parse(in.nextLine());
				if(obj != null)
					valido = true;
				else
					System.out.println("Il valore inserito non è corretto.\nInserisci qualcosa del tipo: " + campo.getClassType().getSyntax());
			}while(!valido);
			//conferma modifica
			valido = false;
			do {
				System.out.println("Sei sicuro di voler modificare ?");
				System.out.println("Proposta :" + id + ", Campo :" + campo.getName() + ", nuovo valore: " + obj.toString());
				System.out.print("[y/n]> ");
				String conferma = in.nextLine();
				if(conferma.equalsIgnoreCase("n")) {
					annulla = true;
					valido = true;
				}else if(conferma.equalsIgnoreCase("y")) {
					valido = true;
				}
			}while(!valido);
			//modifica effetiva
			if(!annulla && session.modificaProposta(id, campo.getName(), obj))
				System.out.println("Modifica avvenuta con successo");
			else
				System.out.println("Modifica fallita");
		}),
		CREAZIONE_EVENTO("crea", "Crea un nuovo evento", ()->{
			Category evento = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO);
			Stream.of(ExpandedHeading.values())
					.filter(( fd )->evento.containsField(fd.getName()))
					.forEachOrdered(( fd )->{
						boolean valid = false;
						System.out.println(fd.toString());
						do {
							System.out.print("\tInserisci un valore per il campo: ");
							String value = in.nextLine();
							if(!fd.isBinding() && value.isEmpty()) {
								valid = true;
								System.out.print(NEW_LINE); //Lascio uno spazio per non stampare tutto attaccato
							}
							if(fd.getClassType().isValidType(value)) {
								valid = true;
								if(evento.setValue(fd.getName(), fd.getClassType().parse(value)))
									System.out.println("\tDato inserito correttamente\n");
								else
									System.out.println("\tIl dato non è stato inserito correttamente\n");
							}
							if(!valid)
								System.out.println(NEW_LINE + "\tIl dato inserito non è valido.\n\tInserisci qualcosa di tipo "
														+ fd.getClassType().getSyntax() +"\n");
						}while(!valid);
					});
			if(session.aggiungiProposta(new Proposta(evento, session.getProprietario())))
				System.out.println("La proposta è stata aggiunta alla proposte in lavorazione");
			else
				System.out.println("La proposta non è stata aggiunta");
		}),
		MOSTRA_IN_LAVORAZIONE("mostraInLavorazione", "Visualizza le tue proposte", ()->{
			String proposte = session.showInProgress();
			if(proposte.equals(""))
				System.out.print("Nessuna proposta in lavorazione!");
			else System.out.print(session.showInProgress());			
		}),
		MOSTRA_NOTIFICHE("mostraNotifiche","Mostra le tue notifiche", ()->System.out.println(session.showNotification())),
		RIMUOVI_NOTIFICA("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo",()->{
			boolean valido = false;
			do {
				try {
					System.out.print("Inserisci l'id da eliminare: ");
					int i = Integer.parseInt(in.nextLine());
					valido = true;
					if(!session.getProprietario().removeMsg(i))
						System.out.println("La rimozione non è andata a buon fine");
				}catch(Exception e) {
					System.out.println("Dato invalido, inserisci un numero");
				}
			}while(!valido);
		}),
		MOSTRA_BACHECA("mostraBacheca","Mostra tutte le proposte in bacheca",()->{
			bacheca.refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente vedrà la bacheca aggiornata
			System.out.print("Le proposte in bacheca:\n" + bacheca.showContent());
		}),
		PUBBLICA("pubblica", "Pubblica un evento creato", ()->{
			boolean valido = false;
			do {
				try {
					System.out.print("Inserisci l'identificatore : ");
					int id = Integer.parseInt(in.nextLine());
					valido = true;
					if(session.contains(id)) {
						bacheca.add(session.getProposta(id));
						System.out.println("Proposta aggiunta con successo");
					}else
						System.out.println("La proposta inserita non è valida");
				}catch(Exception e) {
					System.out.println("Inserisci un numero");
				}
			}while(!valido);
		}),
		PARTECIPA("partecipa","Partecipa ad una proposta in bacheca",()->{
			boolean valido = false;
			do {
				try {
				System.out.print("Inserisci l'identificatore : ");
				int id = Integer.parseInt(in.nextLine());
				valido = true;
				if(!bacheca.iscrivi(id, session.getProprietario()))
					System.out.println("L'iscrizione non è andata a buon fine");
				}catch(Exception e) {
					System.out.println("Inserisci un numero");
				}
			}while(!valido);
		});
		
		/**
		 * Il nome del comando
		 */
		private String nome;
		/**
		 * La descrizione del comando
		 */
		private String descrizione;
		/**
		 * L'azione che il comando deve compiere
		 */
		private Runnable esecuzione;
		
		/**
		 * Costruttore
		 * @param comando il nome del comando
		 * @param descrizione la descrizione del comando
		 * @param esecuzione ciò che il comando deve fare
		 */
		private Comando(String comando, String descrizione, Runnable esecuzione) {
			this.nome = comando;
			this.descrizione = descrizione;
			this.esecuzione = esecuzione;
		}
	
		/**
		 * Restituisce il nome del comando
		 * @return il nome del comando
		 */
		public String getNome() {
			return nome;
		}
	
		/**
		 * Restituisce la descrizione del comando
		 * @return
		 */
		public String getDescrizione() {
			return descrizione;
		}
	
		/**
		 * Esegue il comando
		 */
		public void run() {
			esecuzione.run();
		}
		
		/**
		 * Controlla se il comando ha il nome inserito
		 * @param comando il nome presunto del comando
		 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
		 */
		public boolean equalsName(String comando) {
			return this.nome.equals(comando);
		}
	}	

}