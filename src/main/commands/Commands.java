package main.commands;

import java.util.ArrayList;
import java.util.stream.Stream;

import categories.Category;
import categories.CategoryCache;
import categories.CategoryHeading;
import fields.FieldHeading;
import fields.FieldSetFactory;
import proposals.Preferences;
import proposals.Proposal;
import users.User;
import utility.MessageHandler;
import utility.StringConstant;
/**
 * Enumerazione contente i vari comandi disponibili all'utente, comprese le loro funzionalità
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum Commands {

		EXIT("exit", "Esci dal programma","exit",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			System.exit(0);
			return true;
		}),
		SHOW_CATEGORIES("mostraCategorie", "Mostra le categorie disponibili", "mostraCategorie", (ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.getIOStream().writeln("Le categorie disponibili: ");
			Stream.of(CategoryHeading.values()).forEach((ch)->ctx.getIOStream().writeln("\t" + ch.getName()));
			return true;
		}),
		CATEGORY("descrizione", "Mostra la categoria specificata", "descrizione [categoryName]", (ctx, args)->{
			if(args.length == 0){
			 	ctx.getIOStream().writeln(StringConstant.SPECIFY_CATEGORY_NAME);
			  	return false;
			}else if(Stream.of(CategoryHeading.values())
			  					.anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
			  	ctx.getIOStream().write(Stream.of(CategoryHeading.values())
			  								.filter((fh)->fh.getName().equalsIgnoreCase(args[0]))
			  								.findFirst().get().toString());
			  	return true;
			 }else{
			  	ctx.getIOStream().writeln(StringConstant.CATEGORY_NOT_FOUND);
			  	return false;
			  }
		}),
		DESCRIPTION("caratteristiche", "Mostra le caratteristiche della categoria specificata", "caratteristiche [categoryName]", (ctx, args)->{
			if(args.length == 0){
		 		ctx.getIOStream().writeln(StringConstant.SPECIFY_CATEGORY_NAME);
		  		return false;
		  	}else if(Stream.of(CategoryHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(args[0]))){
		  		ctx.getIOStream().write(FieldSetFactory.getInstance().getSet(args[0]).getFeatures());
		  		return true;
		 	}else{
		  		ctx.getIOStream().writeln(StringConstant.CATEGORY_NOT_FOUND);
		  		return false;
		  	}
		}),
		REGISTRATION("registra", "Registra un fruitore", "registra [name]", (ctx, args)->{
			if(args.length == 0){
		 		ctx.getIOStream().writeln("Inserisci il nomignolo dell'utente da registrare");
		  		return false;
		  	}else if(!ctx.getDatabase().contains(args[0])){
		  		ctx.getDatabase().register(args[0]);
				ctx.getIOStream().writeln("L'utente è stato registrato con successo");
				ctx.getIOStream().writeln("Compilare, se si vuole, il proprio Profilo personale:\n");
				User user = ctx.getDatabase().getUser(args[0]);
				FieldHeading[] fields = user.getEditableFields();
				Stream.of(fields)
						.forEach((fh)->{
							ctx.getIOStream().writeln(fh.toString());
							Object obj = acceptValue(ctx, fh, "Inserisci un valore per il campo: ");
							if(user.setValue(fh.getName(), obj))
								ctx.getIOStream().writeln("\tDato inserito correttamente\n");
							else
								ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
						});
				return true;
		 	}else{
		  		ctx.getIOStream().writeln("Il nome inserito è già esistente");
		  		return false;
		  	}
		}),
		//syntax : login [name]
		LOGIN("login", "Accedi", "login [name]", (ctx, args)->{
			if(args.length == 0){
		 		ctx.getIOStream().writeln("Inserisci il nomignolo di un utente contenuto nel database");
		  		return false;
		  	}else if(ctx.getDatabase().contains(args[0])){
		  		ctx.newSession(args[0]);
		  		ctx.getIOStream().writeln("Loggato come: " + args[0]);
		  		return true;
		 	}else{
		  		ctx.getIOStream().writeln("Utente non registrato");
		  		return false;
		  	}
		}),
		LOGOUT("logout", "Per uscire","logout", (ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.resetSession();
			ctx.getIOStream().writeln("Logout eseguito");
			return true;
			}),
		//syntax : modifica [id]
		MODIFY("modifica","Modifica il campo di una proposta", "modifica [id]",(ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			//inserisci id proposta
			boolean valid = false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
				if(!ctx.getSession().contains(id)) {
					ctx.getIOStream().writeln("Nessuna proposta in lavorazione con questo id");
					return false;
				}
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			//inserisci nome del campo da modificare
			FieldHeading field = FieldHeading.TITOLO;
			String newField = ctx.getIOStream().read("Inserisci il nome del campo che vuoi modificare : ");
			if(Stream.of(FieldHeading.values()).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
				field = Stream.of(FieldHeading.values())
						.filter((fh)->fh.getName().equalsIgnoreCase(newField))
						.findAny()
						.get();
			else {
				ctx.getIOStream().writeln("Il nome inserito non appartiene ad un campo");
				return false;
			}
			//inserisci valore del campo da modificare
			Object obj = null;
			obj = acceptValue(ctx, field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
			//conferma modifica
			valid = false;
			do {
				ctx.getIOStream().writeln("Sei sicuro di voler modificare ?");
				String newValue = "";
				if(obj!=null)
					newValue = obj.toString();
				String confirm = ctx.getIOStream()
										.read("Proposta :" + id + ", Campo :" + field.getName() + ", nuovo valore: " + newValue 
												+ " [y/n]");
				if(confirm.equalsIgnoreCase("n")) {
					valid = true;
					ctx.getIOStream().writeln("La modifica è stata annullata");
					return false;
				}else if(confirm.equalsIgnoreCase("y"))
					valid = true;
			}while(!valid);
			//modifica effetiva
			if(ctx.getSession().modifyProposal(id, field.getName(), obj)) {
				ctx.getIOStream().writeln("Modifica avvenuta con successo");
				return true;
			}else {
				ctx.getIOStream().writeln("Modifica fallita");
				return false;
			}
		}),
		NEW_EVENT("crea", "Crea un nuovo evento", "crea [categoryName]", (ctx, args)->{
			if(args.length == 0) {
				ctx.getIOStream().writeln(StringConstant.SPECIFY_CATEGORY_NAME);
				return false;
			}
			String categoryName = args[0];
			if(!Stream.of(CategoryHeading.values()).anyMatch((ch)->ch.getName().equalsIgnoreCase(categoryName))) {
				ctx.getIOStream().writeln("Categoria non esistente");
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
						ctx.getIOStream().writeln(fd.toString());
						Object obj = acceptValue(ctx, fd, "Inserisci un valore per il campo: ");
						if(event.setValue(fd.getName(), obj))
							ctx.getIOStream().writeln("\tDato inserito correttamente\n");
						else
							ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
					});
			//campi opzionali
			Stream.of(FieldHeading.values())
					.filter(( fd )->event.containsField(fd.getName()))
					.filter(( fd )->fd.isOptional())
					.forEachOrdered(( fd )->{
						boolean valid = false;
						boolean keepField = false;
						do {
							String confirm = ctx.getIOStream().read(fd.toString() + "\nVuoi inserire questo campo opzionale? [y|n]");
							if(confirm.equalsIgnoreCase("y")) {
								valid = true;
								keepField = true;
							}else if(confirm.equalsIgnoreCase("n")) 
								valid = true;
							else
								ctx.getIOStream().writeln("Valore inserito errato: inserisci 'y' o 'n'");
						}while(!valid);
						if(!keepField) {
							ctx.getIOStream().writeln("Il campo opzionale " + fd.getName() + " non verrà inserito nella categoria");
							event.removeOptionalField(fd);
						}else {
							ctx.getIOStream().writeln("Il campo opzionale " + fd.getName() + " verrà inserito nella categoria");
							Object obj = acceptValue(ctx, fd, "Inserisci un valore per il campo: ");
							if(event.setValue(fd.getName(), obj))
								ctx.getIOStream().writeln("\tDato inserito correttamente\n");
							else
								ctx.getIOStream().writeln("\tIl dato non è stato inserito correttamente\n");
						}
					});
			//iscrizione proprietario
			Proposal p = new Proposal(event);
			Preferences pref = p.getPreferenze();
			pref = setPreference(pref, ctx);
			p.setOwner(ctx.getSession().getOwner(), pref);
			if(ctx.getSession().addProposal(p)) {
				ctx.getIOStream().writeln("La proposta è stata aggiunta alla proposte in lavorazione");
				return true;
			}else {
				ctx.getIOStream().writeln("La proposta non è stata aggiunta");
				return false;
			}
		}),
		SHOW_WORKINPROGRESS("mostraInLavorazione", "Visualizza le tue proposte","mostraInLavorazione", (ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			String proposals = ctx.getSession().showInProgress();
			if(proposals.equals(""))
				ctx.getIOStream().write("Nessuna proposta in lavorazione!\n");
			else 
				ctx.getIOStream().write("Le proposte in lavorazione:\n" + ctx.getSession().showInProgress());
			return true;
		}),
		SHOW_NOTIFICATIONS("mostraNotifiche","Mostra le tue notifiche","mostraNotifiche", (ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			if(ctx.getSession().noMessages()) 
				ctx.getIOStream().writeln("Nessun messaggio.");
			else
				ctx.getIOStream().writeln(ctx.getSession().showNotification());
			return true;
		}),
		//syntax : rimuoviNotifica [id]
		REMOVE_NOTIFICATION("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo", "rimuoviNotifica [id]",(ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			if(!ctx.getSession().getOwner().removeMsg(id)) {
				ctx.getIOStream().writeln("La rimozione non è andata a buon fine");
				return false;
			}else {
				ctx.getIOStream().writeln("Rimossa correttamente");
				return true;
			}
		}),
		SHOW_NOTICEBOARD("mostraBacheca","Mostra tutte le proposte in bacheca","mostraBacheca",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.getProposalHandler().refresh(); //refresh forzato quando viene richiesta la bacheca, sicuramente utente vedrà la bacheca aggiornata
			String content = ctx.getProposalHandler().showContent();
			if(content.equals(""))
				ctx.getIOStream().write("Nessuna proposta in bacheca!\n");
			else 
				ctx.getIOStream().write("Le proposte in bacheca:\n" + content);
			return true;
		}),
		//syntax : pubblica [id]
		PUBLISH("pubblica", "Pubblica un evento creato", "pubblica [id]", (ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			if(ctx.getSession().contains(id)) {
				if(ctx.getProposalHandler().add(ctx.getSession().getProposal(id))) {
					String categoryName = ctx.getSession().getProposal(id).getCategoryName();
					ctx.getSession().removeProposal(id);
					ctx.getIOStream().writeln("Proposta aggiunta con successo");
					ArrayList<User> receivers = ctx.getDatabase().searchBy(categoryName);
					receivers.remove(ctx.getSession().getOwner());
					MessageHandler.getInstance().notifyByInterest(receivers, categoryName);
					return true;
				}else {
					ctx.getIOStream().writeln("La proposta inserita non è valida");
					return false;
				}
			}else {
				ctx.getIOStream().writeln("La proposta inserita non esiste");
				return false;
			}		
		}),
		// syntax : partecipa [id]
		PARTICIPATE("partecipa","Partecipa ad una proposta in bacheca", "partecipa [id]",(ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			if(!ctx.getProposalHandler().isOwner(id, ctx.getSession().getOwner())) {
				Preferences pref = ctx.getProposalHandler().getPreferenze(id);
				if(pref!=null) {
					FieldHeading[] optionalFields = pref.getChoices();
					if(optionalFields.length != 0) {
						ctx.getIOStream().writeln("Per completare l'iscrizione all'evento compilare i seguenti campi:");
						pref = setPreference(pref, ctx);
						ctx.getIOStream().writeln(StringConstant.EMPTY_STRING);
					}
					if(!ctx.getProposalHandler().signUp(id, ctx.getSession().getOwner(), pref)) {
						ctx.getIOStream().writeln("L'iscrizione non è andata a buon fine");
						return false;
					}else {
						ctx.getIOStream().writeln("L'iscrizione è andata a buon fine");
						return true;
					}
				} else {
					ctx.getIOStream().writeln("Proposta non trovata");
					return false;
				}	
			} else {
				ctx.getIOStream().writeln("Sei il proprietario della proposta, sei automaticamente iscritto");
				return false;
			}
		}),
		UNSUBSCRIBE("disiscrivi", "Cancella l'iscrizione ad una proposta aperta","disiscrivi",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			User actualUser = ctx.getSession().getOwner();
			if(ctx.getProposalHandler().countUserSubscription(actualUser) == 0) {
				ctx.getIOStream().writeln("Non sei iscritto a nessuna proposta");
				return false;
			} else {
				ctx.getIOStream().writeln(ctx.getProposalHandler().showUserSubscription(actualUser));
				int id = -1;
				try {
					id = Integer.parseInt(ctx.getIOStream().read(StringConstant.INSERT_IDENTIFIER));
				}catch(NumberFormatException e) {
					ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
					return false;
				}
				if(ctx.getProposalHandler().isSignedUp(id, actualUser)) {
					if(ctx.getProposalHandler().unsubscribe(id , actualUser)) {
						ctx.getIOStream().writeln("La disiscrizione è andata a buon fine");
						return true;
					}else {
						ctx.getIOStream().writeln("La disiscrizione NON è andata a buon fine");
						return false;
					}
				}else {
					ctx.getIOStream().writeln("Non sei iscritto a questa proposta");
					return false;
				}
			}
		}),
		MODIFY_PROFILE("modificaProfilo", "Modifica le caratteristiche del tuo profilo","modificaProfilo",(ctx, args)->{
				if(!checkNoParameter(ctx, args))
					return false;
				FieldHeading[] fields = ctx.getSession().getOwner().getEditableFields();
				FieldHeading field = FieldHeading.TITOLO;
				String newField = ctx.getIOStream().read("Inserisci il nome del campo che vuoi modificare : ");
				if(Stream.of(fields).anyMatch((fh)->fh.getName().equalsIgnoreCase(newField)))
						field = Stream.of(fields)
										.filter((fh)->fh.getName().equalsIgnoreCase(newField))
										.findAny()
										.get();
				else {
					ctx.getIOStream().writeln("Il nome inserito non appartiene ad un campo modificabile");
					return false;
				}
				if(field.getName().equals(FieldHeading.CATEGORIE_INTERESSE.getName())) {
					boolean add = true;
					String confirm = ctx.getIOStream().read("Inserisci modalità di modifica: \"a\" aggiungi - \"r\" togli");
					if(confirm.equalsIgnoreCase("r"))
						add = false;
					else if(!confirm.equalsIgnoreCase("a")){
						ctx.getIOStream().writeln("Errore");
						return false;
					}
					String categoryName = ctx.getIOStream().read("Inserisci il nome della categoria da " + (add? "aggiungere" : "rimuovere") + "> ");
					if(Stream.of(CategoryHeading.values()).anyMatch((fh) -> fh.getName().equalsIgnoreCase(categoryName))) {
						String cat = Stream.of(CategoryHeading.values()).filter((fh) -> fh.getName().equalsIgnoreCase(categoryName)).findFirst().get().getName();
						if(ctx.getSession().getOwner().modifyCategory(cat, add)) {
							ctx.getIOStream().writeln("Categoria modificata con successo");
							return true;
						}else {
							ctx.getIOStream().writeln("La modifica non è andata a buon fine");
							return false;
						}
					}else {
						ctx.getIOStream().writeln("Il nome inserito non appartiene ad una categoria");
						return false;
					}
				}else {
					//inserisci valore del campo da modificare
					Object obj = null;
					obj = acceptValue(ctx, field, String.format("Inserisci il nuovo valore (%s) : ", field.getType().getSimpleName()));
					if(ctx.getSession().getOwner().setValue(field.getName(), obj)) {
						ctx.getIOStream().writeln("Modifica avvenuta con successo");
						return true;
					}else {
						ctx.getIOStream().writeln("Modifica fallita");
						return false;
					}
				}
		}),
		WITHDRAW_PROPOSAL("ritira", "Ritira una proposta in bacheca","ritira[id]", (ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			User owner = ctx.getSession().getOwner();
			if(ctx.getProposalHandler().isOwner(id, owner)) {
				if(ctx.getProposalHandler().withdraw(id, owner)) {
					ctx.getIOStream().writeln("La proposta è stata ritirata con successo");
					return true;
				}else {
					ctx.getIOStream().writeln("La proposta non è stata ritirata");
					return false;
				}	
			}else {
				ctx.getIOStream().writeln("La proposta non è di tua proprietà");
				return false;
			}
		}),
		PRIVATE_SPACE_IN("spazioPersonale", "Accedi allo spazio personale", "spazioPersonale",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.getProposalHandler().refresh();
			ctx.getIOStream().writeln("Accesso completato allo spazio personale ('help' per i comandi)");
			return true;
		}),
		PRIVATE_SPACE_OUT("home", "Esci dal private space", "home",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.getIOStream().writeln("Sei uscito dal tuo spazio personale");
			return true;
		}),
		INVITE("invita", "Invita utenti ad una proposta","invita [id]",(ctx, args)->{
			if(!checkOneParameter(ctx, args))
				return false;
			int id = -1;
			try {
				id = Integer.parseInt(args[0]);
			}catch(NumberFormatException e) {
				ctx.getIOStream().writeln(StringConstant.INSERT_NUMBER);
				return false;
			}
			User owner = ctx.getSession().getOwner();
			if(ctx.getProposalHandler().isOwner(id, owner)) {
				ArrayList<User> userList = ctx.getProposalHandler().searchBy(owner, ctx.getProposalHandler().getCategory(id));
				if(userList.isEmpty()) {
					ctx.getIOStream().writeln("Nessun utente trovato da invitare per questa proposta");
					return false;
				}
				ctx.getIOStream().writeln("Potenziali utenti da invitare: " + userList.toString());
				String confirm = ctx.getIOStream().read("Vuoi mandare un invito a tutti?" + "\n[y|n]> ");
				if(confirm.equalsIgnoreCase("y")) {
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else if(confirm.equalsIgnoreCase("n")) {							
					ArrayList<User> receivers = new ArrayList<>();
					userList.stream()
								.forEach(( u )->{
									String answer = ctx.getIOStream().read("Invitare " + u.getName() + " ? [y|n]> ");
									if(answer.equalsIgnoreCase("y")) {
										receivers.add(u);
										ctx.getIOStream().writeln("L'utente verrà notificato");
									}else if(answer.equalsIgnoreCase("n"))
										ctx.getIOStream().writeln(u.getName() + " non verrà invitato ");
									else
										ctx.getIOStream().writeln("Inserito valore non valido. L'utente non verrà notificato");
								});
					MessageHandler.getInstance().inviteUsers(userList, owner.getName(), id);
					return true;
				}else {
					ctx.getIOStream().writeln("L'invio non verrà effettuato, non è stato inserito una conferma corretta");
					return false;
				}
			}else {
				ctx.getIOStream().writeln("La proposta non è di tua proprietà");
				return false;
			}
		}),
		SHOW_PROFILE("mostraProfilo", "Mostra il profilo dell'utente","mostraProfilo",(ctx, args)->{
			if(!checkNoParameter(ctx, args))
				return false;
			ctx.getIOStream().write(ctx.getSession().getOwner().showProfile());
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
		 * La sintassi per l'esecuzione del comando
		 */
		private String syntax;
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
		private Commands(String comand, String description, String syntax, Shell runnable) {
			this.name = comand;
			this.description = description;
			this.syntax = syntax == "" ? "" : String.format("Sintassi: %s", syntax);
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
		 * @return la descrizione del comando
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * Restituisce la sintassi per l'esecuzione del comando
		 * @return sintassi del comando
		 */
		public String getSyntax() {
			return syntax;
		}
	
		/**
		 * Esegue il comando
		 * @param ctx il contesto sul quale deve operare il comando
		 * @param args gli argomenti del comando
		 * @return l'esito del comando
		 */
		public boolean run(Context ctx, String[] args) {
			return runnable.run(ctx, args);
		}
		
		/**
		 * Richiede all'utente di inserire un valore e ne verifica la validità in base al campo
		 * @param field campo su cui verificare la validità del dato
		 * @param message richiesta all'utente di inserire il dato
		 * @return Oggetto correttamente elaborato in base al campo
		 */
		private static Object acceptValue(Context ctx, FieldHeading field, String message) {
			boolean valid = false;
			Object obj = null;
			do {
				String value = ctx.getIOStream().read("\t" + message);
				if(!field.isBinding() && !field.isOptional() && value.isEmpty())
					valid = true;
				ctx.getIOStream().write(StringConstant.NEW_LINE);
				if(field.getClassType().isValidType(value)) {
					obj = field.getClassType().parse(value);
					valid = true;
				}
				if(!valid)
					ctx.getIOStream().writeln("\tIl valore inserito non è corretto.\n\tInserisci qualcosa del tipo: " + field.getClassType().getSyntax());
			}while(!valid);
			return obj;
		}
		
		private static Preferences setPreference(Preferences pref, Context ctx) {
			Stream.of(pref.getChoices())
			.forEach((fh)->{
				boolean confirm = false;
				boolean valid = false;
				do {
					String ok = ctx.getIOStream().read(String.format("\n%s\n\tVuoi usufruirne? [y|n] > ", fh.toString()));
					if(ok.equalsIgnoreCase("y")) {
						confirm = true;
						valid = true;
					}else if(ok.equalsIgnoreCase("n"))
						valid = true;
					else
						ctx.getIOStream().writeln("Il valore inserito non è corretto: inserisci 'y' o 'n'");
				}while(!valid);
				pref.impostaPreferenza(fh, confirm);
			});
			return pref;
		}
		
		private static boolean checkOneParameter(Context ctx, String args[]) {
			if(args.length == 0) {
				ctx.getIOStream().writeln("Inserisci un parametro");
				return false;
			} else if(args.length > 1) {
				ctx.getIOStream().writeln("Inserisci un solo parametro");
				return false;
			}
			return true;
		}
		
		private static boolean checkNoParameter(Context ctx, String args[]) {
			if(args.length != 0) {
				ctx.getIOStream().writeln(StringConstant.TOO_PARAMETERS);
				return false;
			}
			return true;
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
