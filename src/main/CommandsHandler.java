package main;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import fields.FieldSetFactory;
import proposals.Preferenze;
import proposals.Proposal;
import proposals.ProposalHandler;
import users.User;
import users.UserDatabase;
import utility.FileHandler;
import utility.MessageHandler;
import utility.Session;

class CommandsHandler {
	
	private static final String NOTICEBOARD = "resource/bacheca.ser";
	private static final String DATABASE = "resource/registrazioni.ser";
	
	private static final String NEW_LINE = "\n";
	private static final String EXITMSG = "Bye Bye";
	private static final String WAITING = "> ";
	
	private static final String ERROR_UNKNOWN_COMMAND = "Il comando inserito non è stato riconosciuto ('help' per i comandi a disposizione)";
	
	private static final String SAVE_COMPLETED = "completato";
	private static final String SAVE_FAILED = "fallito";
	
	private static final String INSERT_IDENTIFIER = "Inserisci l'identificatore : ";
	private static final String INSERT_NUMBER = "Inserisci un numero";

	private static Timer refreshTimer;
	private static CommandsList protocol;
	private static Session session;
	private static UserDatabase database;
	private static ProposalHandler noticeBoard;
	
	private static final long DELAY = 3600000;//60MIN

	private static CommandsHandler instance;
	private static InOutStream stream;
	
	private CommandsHandler() {}
	
	protected static CommandsHandler getInstance() {
		if(instance == null)
			instance = new CommandsHandler();
		return instance;
	}
	
	/**
	 * Carica il database e la bacheca
	 */
	protected void load() {
		
		protocol = new CommandsList();
		
		//chiusura + terminazione anomala -> save
				Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 		
					stream.close();
					refreshTimer.cancel();
					save();
					stream.writeln(EXITMSG);	
				}));

		
		refreshTimer = new Timer("RefreshNoticeBoard");
		refreshTimer.schedule(new TimerTask() {
			public void run() {
				noticeBoard.refresh();
			}
		}, DELAY, DELAY);
		
		stream.writeln("Caricamento database ...");
		database = (UserDatabase)new FileHandler().load(DATABASE);
		if(database == null) {
			database = new UserDatabase();
			stream.writeln("Caricato nuovo database");
			}
		stream.writeln("Caricamento bacheca ...");
		noticeBoard = (ProposalHandler)new FileHandler().load(NOTICEBOARD);
		if(noticeBoard == null) {
			noticeBoard = new ProposalHandler();
			stream.writeln("Caricata nuova bacheca");
			}
		stream.writeln("Fine caricamento");
		stream.writeln("Pronto");
		stream.write(NEW_LINE + WAITING);
	}
	/**
	 * Salva la bacheca e il database
	 */
	protected void save() {
		stream.write("Salvataggio bacheca... ");
		stream.writeln((new FileHandler().save(NOTICEBOARD, noticeBoard))? SAVE_COMPLETED : SAVE_FAILED);
		stream.write("Salvataggio database... ");
		stream.writeln((new FileHandler().save(DATABASE, database))? SAVE_COMPLETED : SAVE_FAILED);
	}
	
	protected void run(String command){
		if(protocol.contains(command))
			protocol.run(command);
		else
			stream.writeln(ERROR_UNKNOWN_COMMAND);
		stream.write(NEW_LINE + WAITING);
	}
	
	protected void setStream(InOutStream _stream) {
		stream = _stream;
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
			stream.writeln("Le categorie disponibili: ");
			Stream.of(CategoryHeading.values()).forEach((ch)->stream.writeln("\t" + ch.getName()));
			return true;
		}),
		//syntax : categoria [category]
		CATEGORY("categoria", "Mostra la categoria disponibile\tSintassi: categoria [name]", (args)->{
			if(args.length == 0){
			 	stream.writeln("Inserisci il nome di una categoria");
			  	return false;
			}else if(Stream.of(CategoryHeading.values())
			  					.anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
			  	stream.write(Stream.of(CategoryHeading.values())
			  								.filter((fh)->fh.getName().equalsIgnoreCase(args[0]))
			  								.findFirst().get().toString());
			  	return true;
			 }else{
			  	stream.writeln("Il nome inserito non appartiene ad una categoria");
			  	return false;
			  }
		}),
		//syntax : descrizione [category]
		DESCRIPTION("descrizione", "Mostra le caratteristiche della categoria disponibile\tSintassi: descrizione [name]", (args)->{
			if(args.length == 0){
		 		stream.writeln("Inserisci il nome di una categoria");
		  		return false;
		  	}else if(Stream.of(CategoryHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
		  		stream.write(FieldSetFactory.getInstance().getSet(args[0]).getFeatures());
		  		return true;
		 	}else{
		  		stream.writeln("Il nome inserito non appartiene ad una categoria");
		  		return false;
		  	}
		}),
		//syntax : registra [name]
		REGISTRATION("registra", "Registra un fruitore\tSintassi: registra [name]", (args)->{
			if(args.length == 0){
		 		stream.writeln("Inserisci il nomignolo dell'utente da registrare");
		  		return false;
		  	}else if(args.length > 0 && !database.contains(args[0])){
		  		database.register(args[0]);
				stream.writeln("L'utente è stato registrato con successo");
				stream.writeln("Compilare, se si vuole, il proprio Profilo personale:\n");
				User user = database.getUser(args[0]);
				FieldHeading[] fields = user.getEditableFields();
				Stream.of(fields)
						.forEach((fh)->{
							stream.writeln(fh.toString());
							Object obj = acceptValue(fh, "Inserisci un valore per il campo: ");
							if(user.setValue(fh.getName(), obj))
								stream.writeln("\tDato inserito correttamente\n");
							else
								stream.writeln("\tIl dato non è stato inserito correttamente\n");
						});
				return true;
		 	}else{
		  		stream.writeln("Il nome inserito è già esistente");
		  		return false;
		  	}
		}),
		//syntax : login [name]
		LOGIN("login", "Accedi\tSintassi: login [name]", (args)->{
			if(args.length == 0){
		 		stream.writeln("Inserisci il nomignolo di un utente contenuto nel database");
		  		return false;
		  	}else if(args.length > 0 && database.contains(args[0])){
		  		session = new Session(database.getUser(args[0]));
		  		stream.writeln("Loggato come: " + args[0]);
		  		return true;
		 	}else{
		  		stream.writeln("Utente non registrato");
		  		return false;
		  	}
		}),
		LOGOUT("logout", "Per uscire", (args)->{
			session = null;
			stream.writeln("Logout eseguito");
			return true;
			}),
		//syntax : modifica [id]
		MODIFY("modifica","Modifica il campo di una proposta\tSintassi: modifica [id]",(args)->{
			if(args.length == 0) {
				stream.write("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				stream.write("Inserisci un solo parametro");
				return false;	
			}else {
				//inserisci id proposta
				boolean valid = false;
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
					if(!session.contains(id)) {
						stream.writeln("Identificatore non esistente");
						return false;
					}
				}catch(NumberFormatException e) {
					stream.writeln(INSERT_NUMBER);
					return false;
				}
				//inserisci nome del campo da modificare
				FieldHeading field = FieldHeading.TITOLO;
				stream.write("Inserisci il nome del campo che vuoi modificare : ");
				String newField = stream.read();
				if(Stream.of(FieldHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
					field = Stream.of(FieldHeading.values())
							.filter((fh)->fh.getName().equalsIgnoreCase(newField))
							.findAny()
							.get();
				else {
					stream.writeln("Il nome inserito non appartiene ad un campo");
					return false;
				}
				//inserisci valore del campo da modificare
				Object obj = null;
				obj = acceptValue(field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
				//conferma modifica
				valid = false;
				do {
					stream.writeln("Sei sicuro di voler modificare ?");
					String newValue = "";
					if(obj!=null)
						newValue = obj.toString();
					stream.writeln("Proposta :" + id + ", Campo :" + field.getName() + ", nuovo valore: " + newValue);
					stream.write("[y/n]> ");
					String confirm = stream.read();
					if(confirm.equalsIgnoreCase("n")) {
						valid = true;
						stream.writeln("La modifica è stata annullata");
						return false;
					}else if(confirm.equalsIgnoreCase("y"))
						valid = true;
				}while(!valid);
				//modifica effetiva
				if(session.modifyProposal(id, field.getName(), obj)) {
					stream.writeln("Modifica avvenuta con successo");
					return true;
				}else {
					stream.writeln("Modifica fallita");
					return false;
				}
			}
		}),
		NEW_EVENT("crea", "Crea un nuovo evento\tSintassi: crea [categoryName]", (args)->{
			if(args.length == 0) {
				stream.write("Inserisci il nome di una categoria");
				return false;
			}
			String categoryName = args[0];
			if(!Stream.of(CategoryHeading.values()).anyMatch((ch)->ch.getName().equalsIgnoreCase(categoryName))) {
				stream.writeln("Categoria non esistente");
				return false;
			}
			Category event = CategoryCache.getInstance()
											.getCategory(Stream.of(CategoryHeading.values())
																.filter((ch)->ch.getName().equalsIgnoreCase(categoryName))
																.findFirst().get().getName());
			//campi facoltativi/obbligatori
			Stream.of(FieldHeading.values())
					.filter(( fd )->event.containsField(fd.getName()))
					.filter(( fd )->!fd.isOptional())
					.forEachOrdered(( fd )->{	
						stream.writeln(fd.toString());
						Object obj = acceptValue(fd, "Inserisci un valore per il campo: ");
						if(event.setValue(fd.getName(), obj))
							stream.writeln("\tDato inserito correttamente\n");
						else
							stream.writeln("\tIl dato non è stato inserito correttamente\n");
					});
			//campi opzionali
			Stream.of(FieldHeading.values())
					.filter(( fd )->event.containsField(fd.getName()))
					.filter(( fd )->fd.isOptional())
					.forEachOrdered(( fd )->{
						stream.writeln(fd.toString());
						stream.write("Vuoi inserire questo campo opzionale?[y|n]>");
						boolean valid = false;
						boolean keepField = false;
						do {
							String confirm = stream.read();
							if(confirm.equalsIgnoreCase("y")) {
								valid = true;
								keepField = true;
							}else if(confirm.equalsIgnoreCase("n")) 
								valid = true;
							else
								stream.writeln("Valore inserito errato: inserisci 'y' o 'n'");
						}while(!valid);
						if(!keepField) {
							stream.writeln("Il campo opzionale " + fd.getName() + " non verrà inserito nella categoria");
							event.removeOptionalField(fd);
						}else {
							stream.writeln("Il campo opzionale " + fd.getName() + " verrà inserito nella categoria");
							Object obj = acceptValue(fd, "Inserisci un valore per il campo: ");
							if(event.setValue(fd.getName(), obj))
								stream.writeln("\tDato inserito correttamente\n");
							else
								stream.writeln("\tIl dato non è stato inserito correttamente\n");
						}
					});
			//iscrizione proprietario
			Proposal p = new Proposal(event);
			Preferenze pref = p.getPreferenze();
			Stream.of(pref.getChoices())
					.forEach((fh)->{
						stream.writeln(fh.toString());
						stream.write("Vuoi usufruirne?[y|n]>");
						boolean confirm = false;
						boolean valid = false;
						do {
							String ok = stream.read();
							if(ok.equalsIgnoreCase("y")) {
								confirm = true;
								valid = true;
							}else if(ok.equalsIgnoreCase("n"))
								valid = true;
							else
								stream.writeln("Il valore inserito non è corretto: inserisci 'y' o 'n'");
						}while(!valid);
						pref.impostaPreferenza(fh, confirm);
					});
			p.setOwner(session.getOwner(), pref);
			if(session.addProposal(p)) {
				stream.writeln("La proposta è stata aggiunta alla proposte in lavorazione");
				return true;
			}else {
				stream.writeln("La proposta non è stata aggiunta");
				return false;
			}
		}),
		SHOW_WORKINPROGRESS("mostraInLavorazione", "Visualizza le tue proposte", (args)->{
			String proposals = session.showInProgress();
			if(proposals.equals(""))
				stream.write("Nessuna proposta in lavorazione!\n");
			else 
				stream.write("Le proposte in lavorazione:\n" + session.showInProgress());
			return true;
		}),
		SHOW_NOTIFICATIONS("mostraNotifiche","Mostra le tue notifiche", (args)->{
			if(session.noMessages()) 
				stream.writeln("Nessun messaggio.");
			else
				stream.writeln(session.showNotification());
			return true;
		}),
		//syntax : rimuoviNotifica [id]
		REMOVE_NOTIFICATION("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo\tSintassi: rimuoviNotifica [id]",(args)->{
			if(args.length == 0) {
				stream.write("Inserisci un parametro");
				return false;
			}else if(args.length > 1) { 
				stream.write("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					stream.writeln("Dato invalido, inserisci un numero");
					return false;
				}
				if(!session.getOwner().removeMsg(id)) {
					stream.writeln("La rimozione non è andata a buon fine");
					return false;
				}else {
					stream.writeln("Rimossa correttamente");
					return true;
				}
			}
		}),
		SHOW_NOTICEBOARD("mostraBacheca","Mostra tutte le proposte in bacheca",(args)->{
			noticeBoard.refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente vedrà la bacheca aggiornata
			String content = noticeBoard.showContent();
			if(content.equals(""))
				stream.write("Nessuna proposta in bacheca!\n");
			else 
				stream.write("Le proposte in bacheca:\n" + content);
			return true;
		}),
		//syntax : pubblica [id]
		PUBLISH("pubblica", "Pubblica un evento creato\tSintassi: pubblica [id]", (args)->{
			if(args.length == 0) {
				stream.write("Inserisci un parametro");
				return false;
			}else if(args.length > 1){
				stream.write("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					stream.writeln(INSERT_NUMBER);
					return false;
				}
				if(session.contains(id)) {
					if(noticeBoard.add(session.getProposal(id))) {
						String categoryName = session.getProposal(id).getCategoryName();
						session.removeProposal(id);
						stream.writeln("Proposta aggiunta con successo");
						ArrayList<User> receivers = database.searchBy(categoryName);
						receivers.remove(session.getOwner());
						MessageHandler.getInstance().notifyByInterest(receivers, categoryName);
						return true;
					}else {
						stream.writeln("La proposta inserita non è valida");
						return false;
					}
				}else {
					stream.writeln("La proposta inserita non esiste");
					return false;
				}
			}
		}),
		// syntax : partecipa [id]
		PARTICIPATE("partecipa","Partecipa ad una proposta in bacheca\tSintassi: partecipa [id]",(args)->{
			if(args.length == 0) {
				stream.write("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				stream.write("Inserisci un solo parametro");
				return false;
			}else { 
				int id = -1;
				try {
					id = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					stream.writeln(INSERT_NUMBER);
					return false;
				}
				Preferenze pref = noticeBoard.getPreferenze(id);
				Stream.of(pref.getChoices())
						.forEach((fh)->{
							stream.writeln(fh.toString());
							stream.write("Vuoi usufruirne?[y|n]>");
							boolean confirm = false;
							boolean valid = false;
							do {
								String ok = stream.read();
								if(ok.equalsIgnoreCase("y")) {
									confirm = true;
									valid = true;
								}else if(ok.equalsIgnoreCase("n"))
									valid = true;
								else
									stream.writeln("Il valore inserito non è corretto: inserisci 'y' o 'n'");
							}while(!valid);
							pref.impostaPreferenza(fh, confirm);
						});
				if(!noticeBoard.signUp(id, session.getOwner(), pref)) {
					stream.writeln("L'iscrizione non è andata a buon fine");
					return false;
				}else {
					stream.writeln("L'iscrizione è andata a buon fine");
					return true;
				}
			}
		}),
		UNSUBSCRIBE("disiscrivi", "Cancella l'iscrizione ad una proposta aperta",(args)->{
			User actualUser = session.getOwner();
			stream.writeln(noticeBoard.showUserSubscription(actualUser));
			int id = -1;
			try {
				stream.write(INSERT_IDENTIFIER);
				id = Integer.parseInt(stream.read());
			}catch(NumberFormatException e) {
				stream.writeln(INSERT_NUMBER);
				return false;
			}
			if(noticeBoard.isSignedUp(id, actualUser)) {
				if(noticeBoard.unsubscribe(id , actualUser)) {
					stream.writeln("La disiscrizione è andata a buon fine");
					return true;
				}else {
					stream.writeln("La disiscrizione NON è andata a buon fine");
					return false;
				}
			}else {
				stream.writeln("Non sei iscritto a questa proposta");
				return false;
			}
		}),
		MODIFY_PROFILE("modificaProfilo", "Modifica le caratteristiche del tuo profilo",(args)->{
			FieldHeading[] fields = session.getOwner().getEditableFields();
			FieldHeading field = FieldHeading.TITOLO;
			stream.write("Inserisci il nome del campo che vuoi modificare : ");
			String newField = stream.read();
			if(Stream.of(fields).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
					field = Stream.of(fields)
									.filter((fh)->fh.getName().equalsIgnoreCase(newField))
									.findAny()
									.get();
			else {
				stream.writeln("Il nome inserito non appartiene ad un campo modificabile");
				return false;
			}
			if(field.getName().equals(FieldHeading.CATEGORIE_INTERESSE.getName())) {
				boolean add = true;
				stream.write("Inserisci modalità di modifica: \"a\" aggiungi - \"r\" togli> ");
				String confirm = stream.read();
				if(confirm.equalsIgnoreCase("r"))
					add = false;
				else if(!confirm.equalsIgnoreCase("a")){
					stream.writeln("Errore");
					return false;
				}
				stream.write("Inserisci il nome della categoria da " + (add? "aggiungere" : "rimuovere") + "> ");
				String categoryName = stream.read();
				if(Stream.of(CategoryHeading.values()).anyMatch((fh) -> fh.getName().equalsIgnoreCase(categoryName))) {
					String cat = Stream.of(CategoryHeading.values()).filter((fh) -> fh.getName().equalsIgnoreCase(categoryName)).findFirst().get().getName();
					if(session.getOwner().modifyCategory(cat, add)) {
						stream.writeln("Categoria modificata con successo");
						return true;
					}else {
						stream.writeln("La modifica non è andata a buon fine");
						return false;
					}
				}else {
					stream.writeln("Il nome inserito non appartiene ad una categoria");
					return false;
				}
			}else {
				//inserisci valore del campo da modificare
				Object obj = null;
				obj = acceptValue(field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
				if(session.getOwner().setValue(field.getName(), obj)) {
					stream.writeln("Modifica avvenuta con successo");
					return true;
				}else {
					stream.writeln("Modifica fallita");
					return false;
				}
			}

		}),
		//syntax : ritira [id]
		WITHDRAW_PROPOSAL("ritira", "Ritira una proposta in bacheca\tSintassi: ritira [id]", (args)->{
			if(args.length == 0) {
				stream.write("Inserisci un parametro");
				return false;
			}else if(args.length > 1) {
				stream.write("Inserisci un solo parametro");
				return false;
			}else {
				int id = -1;
				try {
					stream.write(INSERT_IDENTIFIER);
					id = Integer.parseInt(stream.read());
				}catch(NumberFormatException e) {
					stream.writeln(INSERT_NUMBER);
					return false;
				}
				if(noticeBoard.withdraw(id, session.getOwner())) {
					stream.writeln("La proposta è stata ritirata con successo");
					return true;
				}else {
					stream.writeln("La proposta non è stata ritirata");
					return false;
				}
			}
		}),
		PRIVATE_SPACE_IN("spazioPersonale", "Accedi allo spazio personale", (args)->{
			noticeBoard.refresh();
			stream.writeln("Accesso completato allo spazio personale ('help' per i comandi)");
			return true;
		}),
		PRIVATE_SPACE_OUT("back", "Esci dal private space", (args)->{
			stream.writeln("Sei uscito dal tuo spazio personale");
			return true;
		}),
		INVITE("invite", "Invita utenti ad una proposta",(args)->{
			int id = -1;
			try {
				stream.write(INSERT_IDENTIFIER);
				id = Integer.parseInt(stream.read());
			}catch(NumberFormatException e) {
				stream.writeln(INSERT_NUMBER);
				return false;
			}
			User owner = session.getOwner();
			if(noticeBoard.isOwner(id, owner)) {
				ArrayList<User> userList = noticeBoard.searchBy(owner, noticeBoard.getCategory(id));
				stream.writeln("Potenziali utenti da invitare: " + userList.toString());
				stream.writeln("Vuoi mandare un invito a tutti?");
				stream.write("[y|n]> ");
				String confirm = stream.read();
				if(confirm.equalsIgnoreCase("y")) {
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else if(confirm.equalsIgnoreCase("n")) {							
					ArrayList<User> receivers = new ArrayList<>();
					userList.stream()
								.forEach(( u )->{
									stream.write("Invitare " + u.getName() + " ? [y|n]> ");
									String answer = stream.read();
									if(answer.equalsIgnoreCase("y")) {
										receivers.add(u);
										stream.writeln("L'utente verrà notificato");
									}else if(answer.equalsIgnoreCase("n"))
										stream.writeln(u.getName() + " non verrà invitato ");
									else
										stream.writeln("Inserito valore non valido. L'utente non verrà notificato");
								});
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else {
					stream.writeln("L'invio non verrà effettuato, non è stato inserito una conferma corretta");
					return false;
				}
			}else {
				stream.writeln("La proposta non è di tua proprietà");
				return false;
			}
		}),
		SHOW_PROFILE("mostraProfilo", "Mostra il profilo dell'utente",(args)->{
			stream.write(session.getOwner().showProfile());
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
				stream.write("\t" + message);
				String value = stream.read();
				if(!field.isBinding() && !field.isOptional() && value.isEmpty())
					valid = true;
				stream.write(NEW_LINE);
				if(field.getClassType().isValidType(value)) {
					obj = field.getClassType().parse(value);
					valid = true;
				}
				if(!valid)
					stream.writeln("\tIl valore inserito non è corretto.\n\tInserisci qualcosa del tipo: " + field.getClassType().getSyntax());
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
