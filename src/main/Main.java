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
	private static ProposalHandler noticeBoard;
	
	private static final long DELAY = 3600000;//60MIN
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		protocol = new CommandList();
		
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 		
			in.close();
			refreshTimer.cancel();
			save();
			System.out.println(EXITMSG);	
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
		noticeBoard = (ProposalHandler)new FileHandler().load(NOTICEBOARD);
		if(noticeBoard == null) {
			noticeBoard = new ProposalHandler();
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
	 * Enumerazione contente i vari comandi disponibili all'utente, comprese le loro funzionalità
	 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
	 *
	 */
	protected enum Command {
		
		EXIT("exit", "Esci dal programma",(args)->{
			System.exit(0);
			return true;
			}),
		SHOW_CATEGORIES("mostraCategorie", "Mostra le categorie disponibili", (args)->{
			System.out.println("Le categorie disponibili: ");
			Stream.of(CategoryHeading.values()).forEach((ch)->System.out.println("\t" + ch.getName()));
			return true;
		}),
		//syntax : categoria [category]
		CATEGORY("categoria", "Mostra la categoria disponibile\tSintassi: categoria [name]", (args)->{
			if(args.length == 0){
			 	System.out.println("Inserisci il nome di una categoria");
			  	return false;
			}else if(Stream.of(CategoryHeading.values())
			  					.anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
			  	System.out.print(Stream.of(CategoryHeading.values())
			  								.filter((fh)->fh.getName().equalsIgnoreCase(args[0]))
			  								.findFirst().get().toString());
			  	return true;
			 }else{
			  	System.out.println("Il nome inserito non appartiene ad una categoria");
			  	return false;
			  }
		}),
		//syntax : descrizione [category]
		DESCRIPTION("descrizione", "Mostra le caratteristiche della categoria disponibile\tSintassi: descrizione [name]", (args)->{
			if(args.length == 0){
		 		System.out.println("Inserisci il nome di una categoria");
		  		return false;
		  	}else if(Stream.of(CategoryHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
		  		System.out.print(FieldSetFactory.getInstance().getSet(args[0]).getFeatures());
		  		return true;
		 	}else{
		  		System.out.println("Il nome inserito non appartiene ad una categoria");
		  		return false;
		  	}
		}),
		//syntax : registra [name]
		REGISTRATION("registra", "Registra un fruitore\tSintassi: registra [name]", (args)->{
			if(args.length == 0){
		 		System.out.println("Inserisci il nomignolo dell'utente da registrare");
		  		return false;
		  	}else if(args.length > 0 && !database.contains(args[0])){
		  		database.register(args[0]);
				System.out.println("L'utente è stato registrato con successo");
				System.out.println("Compilare, se si vuole, il proprio Profilo personale:\n");
				User user = database.getUser(args[0]);
				FieldHeading[] fields = user.getEditableFields();
				Stream.of(fields)
						.forEach((fh)->{
							System.out.println(fh.toString());
							Object obj = acceptValue(fh, "Inserisci un valore per il campo: ");
							if(user.setValue(fh.getName(), obj))
								System.out.println("\tDato inserito correttamente\n");
							else
								System.out.println("\tIl dato non è stato inserito correttamente\n");
						});
				return true;
		 	}else{
		  		System.out.println("Il nome inserito è già esistente");
		  		return false;
		  	}
		}),
		//syntax : login [name]
		LOGIN("login", "Accedi\tSintassi: login [name]", (args)->{
			if(args.length == 0){
		 		System.out.println("Inserisci il nomignolo di un utente contenuto nel database");
		  		return false;
		  	}else if(args.length > 0 && database.contains(args[0])){
		  		session = new Session(database.getUser(args[0]));
		  		System.out.println("Loggato come: " + args[0]);
		  		return true;
		 	}else{
		  		System.out.println("Utente non registrato");
		  		return false;
		  	}
		}),
		LOGOUT("logout", "Per uscire", (args)->{
			session = null;
			System.out.println("Logout eseguito");
			return true;
			}),
		//syntax : modifica [id]
		MODIFY("modifica","Modifica il campo di una proposta\tSintassi: modifica [id]",(args)->{
			if(args.length == 0) {
				System.out.print("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				System.out.print("Inserisci un solo parametro");
				return false;	
			}else {
				//inserisci id proposta
				boolean valid = false;
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
					if(!session.contains(id)) {
						System.out.println("Identificatore non esistente");
						return false;
					}
				}catch(NumberFormatException e) {
					System.out.println(INSERT_NUMBER);
					return false;
				}
				//inserisci nome del campo da modificare
				FieldHeading field = FieldHeading.TITOLO;
				System.out.print("Inserisci il nome del campo che vuoi modificare : ");
				String newField = in.nextLine();
				if(Stream.of(FieldHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
					field = Stream.of(FieldHeading.values())
							.filter((fh)->fh.getName().equalsIgnoreCase(newField))
							.findAny()
							.get();
				else {
					System.out.println("Il nome inserito non appartiene ad un campo");
					return false;
				}
				//inserisci valore del campo da modificare
				Object obj = null;
				obj = acceptValue(field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
				//conferma modifica
				valid = false;
				do {
					System.out.println("Sei sicuro di voler modificare ?");
					String newValue = "";
					if(obj!=null)
						newValue = obj.toString();
					System.out.println("Proposta :" + id + ", Campo :" + field.getName() + ", nuovo valore: " + newValue);
					System.out.print("[y/n]> ");
					String confirm = in.nextLine();
					if(confirm.equalsIgnoreCase("n")) {
						valid = true;
						System.out.println("La modifica è stata annullata");
						return false;
					}else if(confirm.equalsIgnoreCase("y"))
						valid = true;
				}while(!valid);
				//modifica effetiva
				if(session.modifyProposal(id, field.getName(), obj)) {
					System.out.println("Modifica avvenuta con successo");
					return true;
				}else {
					System.out.println("Modifica fallita");
					return false;
				}
			}
		}),
		NEW_EVENT("crea", "Crea un nuovo evento", (args)->{
			Category event = CategoryCache.getInstance().getCategory(CategoryHeading.FOOTBALLMATCH.getName());
			Stream.of(FieldHeading.values())
					.filter(( fd )->event.containsField(fd.getName()))
					.forEachOrdered(( fd )->{				
						System.out.println(fd.toString());
						Object obj = acceptValue(fd, "Inserisci un valore per il campo: ");
						if(event.setValue(fd.getName(), obj))
							System.out.println("\tDato inserito correttamente\n");
						else
							System.out.println("\tIl dato non è stato inserito correttamente\n");
					});
			if(session.addProposal(new Proposal(event, session.getOwner()))) {
				System.out.println("La proposta è stata aggiunta alla proposte in lavorazione");
				return true;
			}else {
				System.out.println("La proposta non è stata aggiunta");
				return false;
			}
		}),
		SHOW_WORKINPROGRESS("mostraInLavorazione", "Visualizza le tue proposte", (args)->{
			String proposals = session.showInProgress();
			if(proposals.equals(""))
				System.out.print("Nessuna proposta in lavorazione!\n");
			else 
				System.out.print("Le proposte in lavorazione:\n" + session.showInProgress());
			return true;
		}),
		SHOW_NOTIFICATIONS("mostraNotifiche","Mostra le tue notifiche", (args)->{
			if(session.noMessages()) 
				System.out.println("Nessun messaggio.");
			else
				System.out.println(session.showNotification());
			return true;
		}),
		//syntax : rimuoviNotifica [id]
		REMOVE_NOTIFICATION("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo\tSintassi: rimuoviNotifica [id]",(args)->{
			if(args.length == 0) {
				System.out.print("Inserisci un parametro");
				return false;
			}else if(args.length > 1) { 
				System.out.print("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					System.out.println("Dato invalido, inserisci un numero");
					return false;
				}
				if(!session.getOwner().removeMsg(id)) {
					System.out.println("La rimozione non è andata a buon fine");
					return false;
				}else {
					System.out.println("Rimossa correttamente");
					return true;
				}
			}
		}),
		SHOW_NOTICEBOARD("mostraBacheca","Mostra tutte le proposte in bacheca",(args)->{
			noticeBoard.refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente vedrà la bacheca aggiornata
			String content = noticeBoard.showContent();
			if(content.equals(""))
				System.out.print("Nessuna proposta in bacheca!\n");
			else 
				System.out.print("Le proposte in bacheca:\n" + content);
			return true;
		}),
		//syntax : pubblica [id]
		PUBLISH("pubblica", "Pubblica un evento creato\tSintassi: pubblica [id]", (args)->{
			if(args.length == 0) {
				System.out.print("Inserisci un parametro");
				return false;
			}else if(args.length > 1){
				System.out.print("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					System.out.println(INSERT_NUMBER);
					return false;
				}
				if(session.contains(id)) {
					if(noticeBoard.add(session.getProposal(id))) {
						String categoryName = session.getProposal(id).getCategoryName();
						System.out.println("Proposta aggiunta con successo");
						ArrayList<User> receivers = database.searchBy(categoryName);
						receivers.remove(session.getOwner());
						MessageHandler.getInstance().notifyByInterest(receivers, categoryName);
						return true;
					}else {
						System.out.println("La proposta inserita non è valida");
						return false;
					}
				}else {
					System.out.println("La proposta inserita non esiste");
					return false;
				}
			}
		}),
		// syntax : partecipa [id]
		PARTICIPATE("partecipa","Partecipa ad una proposta in bacheca\tSintassi: partecipa [id]",(args)->{
			if(args.length == 0) {
				System.out.print("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				System.out.print("Inserisci un solo parametro");
				return false;
			}else { 
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					System.out.println(INSERT_NUMBER);
					return false;
				}
				if(!noticeBoard.signUp(id, session.getOwner())) {
					System.out.println("L'iscrizione non è andata a buon fine");
					return false;
				}else {
					System.out.println("L'iscrizione è andata a buon fine");
					return true;
				}
			}
		}),
		UNSUBSCRIBE("disiscrivi", "Cancella l'iscrizione ad una proposta aperta",(args)->{
			User actualUser = session.getOwner();
			System.out.println(noticeBoard.showUserSubscription(actualUser));
			int id = -1;
			try {
				System.out.print(INSERT_IDENTIFIER);
				id = Integer.parseInt(in.nextLine());
			}catch(NumberFormatException e) {
				System.out.println(INSERT_NUMBER);
				return false;
			}
			if(noticeBoard.isSignedUp(id, actualUser)) {
				if(noticeBoard.unsubscribe(id , actualUser)) {
					System.out.println("La disiscrizione è andata a buon fine");
					return true;
				}else {
					System.out.println("La disiscrizione NON è andata a buon fine");
					return false;
				}
			}else {
				System.out.println("Non sei iscritto a questa proposta");
				return false;
			}
		}),
		MODIFY_PROFILE("modificaProfilo", "Modifica le caratteristiche del tuo profilo",(args)->{
			FieldHeading[] fields = session.getOwner().getEditableFields();
			FieldHeading field = FieldHeading.TITOLO;
			System.out.print("Inserisci il nome del campo che vuoi modificare : ");
			String newField = in.nextLine();
			if(Stream.of(fields).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
					field = Stream.of(fields)
									.filter((fh)->fh.getName().equalsIgnoreCase(newField))
									.findAny()
									.get();
			else {
				System.out.println("Il nome inserito non appartiene ad un campo modificabile");
				return false;
			}
			if(field.getName().equals(FieldHeading.CATEGORIE_INTERESSE.getName())) {
				boolean add = true;
				System.out.print("Inserisci modalità di modifica: \"a\" aggiungi - \"r\" togli> ");
				String confirm = in.nextLine();
				if(confirm.equalsIgnoreCase("r"))
					add = false;
				else if(!confirm.equalsIgnoreCase("a")){
					System.out.println("Errore");
					return false;
				}
				System.out.print("Inserisci il nome della categoria da " + (add? "aggiungere" : "rimuovere") + "> ");
				String categoryName = in.nextLine();
				if(Stream.of(CategoryHeading.values()).anyMatch((fh) -> fh.getName().equalsIgnoreCase(categoryName))) {
					String cat = Stream.of(CategoryHeading.values()).filter((fh) -> fh.getName().equalsIgnoreCase(categoryName)).findFirst().get().getName();
					if(session.getOwner().modifyCategory(cat, add)) {
						System.out.println("Categoria modificata con successo");
						return true;
					}else {
						System.out.println("La modifica non è andata a buon fine");
						return false;
					}
				}else {
					System.out.println("Il nome inserito non appartiene ad una categoria");
					return false;
				}
			}else {
				//inserisci valore del campo da modificare
				Object obj = null;
				obj = acceptValue(field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
				if(session.getOwner().setValue(field.getName(), obj)) {
					System.out.println("Modifica avvenuta con successo");
					return true;
				}else {
					System.out.println("Modifica fallita");
					return false;
				}
			}

		}),
		//syntax : ritira [id]
		WITHDRAW_PROPOSAL("ritira", "Ritira una proposta in bacheca\tSintassi: ritira [id]", (args)->{
			if(args.length == 0) {
				System.out.print("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				System.out.print("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					System.out.print(INSERT_IDENTIFIER);
					id = Integer.parseInt(in.nextLine());
				}catch(NumberFormatException e) {
					System.out.println(INSERT_NUMBER);
					return false;
				}
				if(noticeBoard.withdraw(id, session.getOwner())) {
					System.out.println("La proposta è stata ritirata con successo");
					return true;
				}else {
					System.out.println("La proposta non è stata ritirata");
					return false;
				}
			}
		}),
		PRIVATE_SPACE_IN("spazioPersonale", "Accedi allo spazio personale", (args)->{
			noticeBoard.refresh();
			System.out.println("Accesso completato allo spazio personale ('help' per i comandi)");
			return true;
		}),
		PRIVATE_SPACE_OUT("back", "Esci dal private space", (args)->{
			System.out.println("Sei uscito dal tuo spazio personale");
			return true;
		}),
		INVITE("invite", "Invita utenti ad una proposta",(args)->{
			int id = -1;
			try {
				System.out.print(INSERT_IDENTIFIER);
				id = Integer.parseInt(in.nextLine());
			}catch(NumberFormatException e) {
				System.out.println(INSERT_NUMBER);
				return false;
			}
			User owner = session.getOwner();
			if(noticeBoard.isOwner(id, owner)) {
				ArrayList<User> userList = noticeBoard.searchBy(owner, noticeBoard.getCategory(id));
				System.out.println("Potenziali utenti da invitare: " + userList.toString());
				System.out.println("Vuoi mandare un invito a tutti?");
				System.out.print("[y|n]> ");
				String confirm = in.nextLine();
				if(confirm.equalsIgnoreCase("y")) {
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else if(confirm.equalsIgnoreCase("n")) {							
					ArrayList<User> receivers = new ArrayList<>();
					userList.stream()
								.forEach(( u )->{
									System.out.print("Invitare " + u.getName() + " ? [y|n]> ");
									String answer = in.nextLine();
									if(answer.equalsIgnoreCase("y")) {
										receivers.add(u);
										System.out.println("L'utente verrà notificato");
									}else if(answer.equalsIgnoreCase("n"))
										System.out.println(u.getName() + " non verrà invitato ");
									else
										System.out.println("Inserito valore non valido. L'utente non verrà notificato");
								});
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else {
					System.out.println("L'invio non verrà effettuato, non è stato inserito una conferma corretta");
					return false;
				}
			}else {
				System.out.println("La proposta non è di tua proprietà");
				return false;
			}
		}),
		SHOW_PROFILE("mostraProfilo", "Mostra il profilo dell'utente",(args)->{
			System.out.print(session.getOwner().showProfile());
			return true;
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
		 * @param args gli argomenti del comando
		 * @return l'esito del comando
		 */
		public boolean run(String[] args) {
			return runnable.run(args);
		}
		
		/**
		 * Richiede all'utente di inserire un valore e ne verifica la validità in base al campo
		 * @param field campo su cui verificare la validità del dato
		 * @param message richiesta all'utente di inserire il dato
		 * @return Oggetto correttamente elaborato in base al campo
		 */
		private static Object acceptValue(FieldHeading field, String message) {
			boolean valid = false;
			Object obj = null;
			do {
				System.out.print("\t" + message);
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
		 * Controlla se il comando ha il nome inserito
		 * @param comando il nome presunto del comando
		 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
		 */
		public boolean hasName(String comando) {
			return this.name.equals(comando);
		}
	}	
}