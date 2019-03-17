package main;


import java.util.*;
import java.util.stream.*;

import categories.*;
import fields.*;
import proposals.*;
import users.Database;
import users.User;
import utility.*;

/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	private static final String NOTICEBOARD = "resource/bacheca.ser";
	private static final String DATABASE = "resource/registrazioni.ser";
	
	private static final String NEW_LINE = "\n";
	private static final String WELCOME = "Welcome to EventBook";
	private static final String WAITING = "> ";
	private static final String EXITMSG = "Bye Bye";
	
	private static final String ERROR_UNKNOWN_COMMAND = "Il comando inserito non è stato riconosciuto ('help' per i comandi a disposizione)";
	
	private static final String SAVE_COMPLETED = "completato";
	private static final String SAVE_FAILED = "fallito";
	
	private static final String INSERT_IDENTIFIER = "Inserisci l'identificatore : ";
	private static final String INSERT_NUMBER = "Inserisci un numero";

	private static Timer refreshTimer;
	private static Scanner in;
	private static CommandList protocol;
	private static Session session;
	private static Database database;
	private static NoticeBoard noticeBoard;
	
	private static final long DELAY = 3600000;//60MIN
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		protocol = new CommandList();
		
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
			System.out.println(EXITMSG);	
			in.close();
			refreshTimer.cancel();
			save();
		}));
		
		in = new Scanner(System.in);
		String command;
		load();
		
		refreshTimer = new Timer("RefreshNoticeBoard");
		refreshTimer.schedule(new TimerTask() {
			public void run() {
				noticeBoard.refresh();
			}
		}, DELAY, DELAY);
		
		System.out.println(NEW_LINE + WELCOME);
		do {
			System.out.print(NEW_LINE + WAITING);
			command = in.nextLine().trim();		
			System.out.print(NEW_LINE);
			if(protocol.contains(command))
				protocol.run(command);
			else
				System.out.println(ERROR_UNKNOWN_COMMAND);
		}while(true);		
	}
	/**
	 * Carica il database e la bacheca
	 */
	private static void load() {
		System.out.println("Caricamento database ...");
		database = (Database)new FileHandler().load(DATABASE);
		if(database == null) {
			database = new Database();
			System.out.println("Caricato nuovo database");
			}
		System.out.println("Caricamento bacheca ...");
		noticeBoard = (NoticeBoard)new FileHandler().load(NOTICEBOARD);
		if(noticeBoard == null) {
			noticeBoard = NoticeBoard.newNoticeBoard();
			System.out.println("Caricata nuova bacheca");
			}
		System.out.println("Fine caricamento");
		
	}
	/**
	 * Salva la bacheca e il database
	 */
	private static void save() {
		System.out.print("Salvataggio bacheca... ");
		System.out.println((new FileHandler().save(NOTICEBOARD, noticeBoard))? SAVE_COMPLETED : SAVE_FAILED);
		System.out.print("Salvataggio database... ");
		System.out.println((new FileHandler().save(DATABASE, database))? SAVE_COMPLETED : SAVE_FAILED);
	}
	/**
	 * Effettua operazioni sul protocollo a seguito del log in dell'utente
	 */
	private static void logIn() {
		protocol.logIn();
	}
	/**
	 * Effettua operazioni sul protocollo a seguito del log out dell'utente
	 */
	private static void logOut() {
		protocol.logOut();
	}
	
	private static Object acceptValue(FieldHeading field, String message) { //NUOVO MODO
		boolean valid = false;
		Object obj = null;
		do {
			System.out.print(message);
			String value = in.nextLine();
			if(!field.isBinding() && value.isEmpty())
				valid = true;
			System.out.print(NEW_LINE);
			if(field.getClassType().isValidType(value)) {
				obj = field.getClassType().parse(value);
				valid = true;
			}
			if(!valid)
				System.out.println("\tIl valore inserito non è corretto.\n\tInserisci qualcosa del tipo: " + field.getClassType().getSyntax());
		}while(!valid);
		return obj;
	}
	
	/**
	 * Enumerazione contente i vari comandi disponibili all'utente, comprese le loro funzionalità
	 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
	 *
	 */
	protected enum Command {
		
		EXIT("exit", "Esci dal programma",(args)->System.exit(0)),
		CATEGORY("categoria", "Mostra la categoria disponibile", (args)->{
			Category p = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
			System.out.print(p.getDescription());
		}),
		DESCRIPTION("descrizione", "Mostra le caratteristiche della categoria disponibile", (args)->{
			Category p = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
			System.out.print(p.getFeatures());
		}),
		REGISTRATION("registra", "Registra un fruitore", (args)->{
			String name = "";
			if(args.length == 1) {
				name = args[0];
			} else {
				System.out.print("Inserisci il nome: ");
				name = in.nextLine();
			}
			if(database.register(name))
				System.out.println("L'utente è stato registrato con successo");
			else
				System.out.println("L'utente è già esistente");
		}),
		LOGIN("login", "Accedi", (args)->{
			String name = "";
			if(args.length == 1) {
				name = args[0];
			} else {
				System.out.print("Inserisci il nome: ");
				name = in.nextLine();
			}
			if(database.contains(name)) {
				session = new Session(database.getUser(name));
				logIn();
				System.out.println("Loggato come: " + name);
			}
			else {
				System.out.println("Utente non registrato");
			}
		}),
		LOGOUT("logout", "Per uscire", (args)->{
			session = null;
			logOut();
			System.out.println("Logout eseguito");
			}),
		MODIFY("modifica","Modifica il campo di una proposta",(args)->{
			boolean abort = false;
			//inserisci id proposta
			boolean valid = false;
			int id = -1;
			try {
				System.out.print(INSERT_IDENTIFIER);
				id = Integer.parseInt(in.nextLine());
				if(!session.contains(id)) {
					abort = true;
				}
			}catch(Exception e) {
				System.out.println(INSERT_NUMBER);
				abort = true;
			}
			//inserisci nome del campo da modificare
			FieldHeading field = FieldHeading.TITOLO;
			if(!abort) {
				System.out.print("Inserisci il nome del campo che vuoi modificare : ");
				String newField = in.nextLine();
				if(Stream.of(FieldHeading.values()).anyMatch((fh)->fh.getName().equals(newField)))
					field = Stream.of(FieldHeading.values())
							.filter((fh)->fh.getName().equals(newField))
							.findAny()
							.get();
				else {
					System.out.println("Il nome inserito non appartiene ad un campo");
					abort = true;
				}
			}
			//inserisci valore del campo da modificare
			Object obj = null;
			if(!abort) {
				obj = acceptValue(field, String.format("\tInserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
				//conferma modifica
				valid = false;
				do {
					System.out.println("Sei sicuro di voler modificare ?");
					System.out.println("Proposta :" + id + ", Campo :" + field.getName() + ", nuovo valore: " + obj.toString());
					System.out.print("[y/n]> ");
					String confirm = in.nextLine();
					if(confirm.equalsIgnoreCase("n")) {
						abort = true;
						valid = true;
					}else if(confirm.equalsIgnoreCase("y")) {
						valid = true;
					}
				}while(!valid);
			}
			//modifica effetiva
			if(!abort && session.modifyProposal(id, field.getName(), obj))
				System.out.println("Modifica avvenuta con successo");
			else
				System.out.println("Modifica fallita");
		}),
		NEW_EVENT("crea", "Crea un nuovo evento", (args)->{
			Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
			Stream.of(FieldHeading.values())
					.filter(( fd )->event.containsField(fd.getName()))
					.forEachOrdered(( fd )->{				
						System.out.println(fd.toString());
						Object obj = acceptValue(fd, "\tInserisci un valore per il campo: ");
						if(event.setValue(fd.getName(), obj))
							System.out.println("\tDato inserito correttamente\n");
						else
							System.out.println("\tIl dato non è stato inserito correttamente\n");
					});
			if(session.addProposal(new Proposal(event, session.getOwner())))
				System.out.println("La proposta è stata aggiunta alla proposte in lavorazione");
			else
				System.out.println("La proposta non è stata aggiunta");
		}),
		SHOW_WORKINPROGRESS("mostraInLavorazione", "Visualizza le tue proposte", (args)->{
			String proposals = session.showInProgress();
			if(proposals.equals(""))
				System.out.print("Nessuna proposta in lavorazione!\n");
			else {
				System.out.print("Le proposte in lavorazione:\n" + session.showInProgress());			
			}
		}),
		SHOW_NOTIFICATIONS("mostraNotifiche","Mostra le tue notifiche", (args)->System.out.println(session.showNotification())),
		REMOVE_NOTIFICATION("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo",(args)->{
			boolean valid = false;
			do {
				try {
					System.out.print("Inserisci l'id da eliminare: ");
					int i = Integer.parseInt(in.nextLine());
					valid = true;
					if(!session.getOwner().removeMsg(i))
						System.out.println("La rimozione non è andata a buon fine");
				}catch(Exception e) {
					System.out.println("Dato invalido, inserisci un numero");
				}
			}while(!valid);
		}),
		SHOW_NOTICEBOARD("mostraBacheca","Mostra tutte le proposte in bacheca",(args)->{
			noticeBoard.refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente vedrà la bacheca aggiornata
			String content = noticeBoard.showContent();
			if(content.equals(""))
				System.out.print("Nessuna proposta in bacheca!\n");
			else {
				System.out.print("Le proposte in bacheca:\n" + noticeBoard.showContent());		
			}
		
		}),
		PUBLISH("pubblica", "Pubblica un evento creato", (args)->{
			boolean valid = false;
			do {
				try {
					System.out.print(INSERT_IDENTIFIER);
					int id = Integer.parseInt(in.nextLine());
					valid = true;
					if(session.contains(id)) {
						if(noticeBoard.add(session.getProposal(id)))
							System.out.println("Proposta aggiunta con successo");
						else
							System.out.println("La proposta inserita non è valida");
					}else
						System.out.println("La proposta inserita non esiste");
				}catch(Exception e) {
					System.out.println(INSERT_NUMBER);
				}
			}while(!valid);
		}),
		PARTICIPATE("partecipa","Partecipa ad una proposta in bacheca",(args)->{
			boolean valid = false;
			do {
				try {
					System.out.print(INSERT_IDENTIFIER);
					int id = Integer.parseInt(in.nextLine());
					valid = true;
					if(!noticeBoard.signUp(id, session.getOwner()))
						System.out.println("L'iscrizione non è andata a buon fine");
					else
						System.out.println("L'iscrizione è andata a buon fine");
				}catch(Exception e) {
					System.out.println(INSERT_NUMBER);
				}
			}while(!valid);
		}),
		UNSUBSCRIBE("disiscrivi", "Cancella l'iscrizione ad una proposta aperta",(args)->{
			User actualUser = session.getOwner();
			System.out.println(noticeBoard.showUserSubscription(actualUser));
			boolean valid = false;
			do {
				try {
					System.out.print(INSERT_IDENTIFIER);
					int id = Integer.parseInt(in.nextLine());
					valid = true;
					if(noticeBoard.isSignedUp(id, actualUser)) {
						if(noticeBoard.unsubscribe(id , actualUser))
							System.out.println("La disiscrizione è andata a buon fine");
						else 
							System.out.println("La disiscrizione NON è andata a buon fine");
					}
					else
						System.out.println("Non sei iscritto a questa proposta");
				}catch(Exception e) {
					System.out.println(INSERT_NUMBER);
				}
			}while(!valid);

		}),
		WITHDRAW_PROPOSAL("ritira", "Ritira una proposta in bacheca", (args)->{
			boolean valid = false;
			do {
				try {
					System.out.print(INSERT_IDENTIFIER);
					int id = Integer.parseInt(in.nextLine());
					valid = true;
					if(noticeBoard.withdraw(id, session.getOwner()))
						System.out.println("La proposta è stata ritirata con successo");
					else
						System.out.println("La proposta non è stata ritirata");
				}catch(Exception e) {
					System.out.println(INSERT_NUMBER);
				}
			}while(!valid);
		});
		/**
		 * Il nome del comando
		 */
		private String name;
		/**
		 * La descrizione del comando
		 */
		private String description;
		/**
		 * L'azione che il comando deve compiere
		 */
		private Shell runnable;
		
		/**
		 * Costruttore
		 * @param comand il nome del comando
		 * @param description la descrizione del comando
		 * @param runnable ciò che il comando deve fare
		 */
		private Command(String comand, String description, Shell runnable) {
			this.name = comand;
			this.description = description;
			this.runnable = runnable;
		}
	
		/**
		 * Restituisce il nome del comando
		 * @return il nome del comando
		 */
		public String getNome() {
			return name;
		}
	
		/**
		 * Restituisce la descrizione del comando
		 * @return restituisce la descrizione del comando
		 */
		public String getDescription() {
			return description;
		}
	
		/**
		 * Esegue il comando
		 */
		public void run(String[] args) {
			runnable.run(args);
		}
		
		/**
		 * Controlla se il comando ha il nome inserito
		 * @param comando il nome presunto del comando
		 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
		 */
		public boolean hasName(String comando) {
			return this.name.equals(comando);
		}
	}	

}