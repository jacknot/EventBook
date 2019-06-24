package main.commands.strategy;
/**
 * Classe enum rappresentante la descrizione di un comando disponibile all'utente
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum CommandDescription{
	
	EXIT("exit", "Esci dal programma","exit"),
	SHOW_CATEGORIES("mostraCategorie", "Mostra le categorie disponibili", "mostraCategorie"),
	CATEGORY("descrizione", "Mostra la categoria specificata", "descrizione [categoryName]"),
	DESCRIPTION("caratteristiche", "Mostra le caratteristiche della categoria specificata", "caratteristiche [categoryName]"),
	REGISTRATION("registra", "Registra un fruitore", "registra [name]"),
	LOGIN("login", "Accedi", "login [name]"),
	LOGOUT("logout", "Per eseguire il logout","logout"),
	MODIFY("modifica","Modifica il campo di una proposta", "modifica [id]"),
	NEW_EVENT("crea", "Crea un nuovo evento", "crea [categoryName]"),
	SHOW_WORKINPROGRESS("mostraInLavorazione", "Visualizza le sue proposte in lavorazione","mostraInLavorazione"),
	SHOW_NOTIFICATIONS("mostraNotifiche","Mostra le tue notifiche","mostraNotifiche"),
	REMOVE_NOTIFICATION("rimuoviNotifica","Rimuove la notifica usando il suo identificativo", "rimuoviNotifica [id]"),
	SHOW_NOTICEBOARD("mostraBacheca","Mostra tutte le proposte in bacheca","mostraBacheca"),
	PUBLISH("pubblica", "Pubblica un evento creato", "pubblica [id]"),
	PARTICIPATE("partecipa","Partecipa ad una proposta in bacheca", "partecipa [id]"),
	UNSUBSCRIBE("disiscrivi", "Cancella l'iscrizione ad una proposta aperta","disiscrivi"),
	MODIFY_PROFILE("modificaProfilo", "Modifica le caratteristiche del tuo profilo","modificaProfilo"),
	WITHDRAW_PROPOSAL("ritira", "Ritira una proposta in bacheca","ritira[id]"),
	PRIVATE_SPACE_IN("spazioPersonale", "Accedi allo spazio personale", "spazioPersonale"),
	PRIVATE_SPACE_OUT("home", "Esce dallo spazio personale", "home"),
	INVITE("invita", "Invita utenti ad una proposta","invita [id]"),
	SHOW_PROFILE("mostraProfilo", "Mostra il profilo dell'utente","mostraProfilo");	
	
	
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
	 * Costruttore
	 * @param name il nome del comando
	 * @param description la descrizione del comando
	 * @param syntax la sintassi del comando
	 * @param runnable ciò che il comando deve fare
	 */
	private CommandDescription(String name, String description, String syntax) {
		this.name = name;
		this.description = description;
		this.syntax = syntax == "" ? "" : String.format("Sintassi: %s", syntax);
	}

	/**
	 * Restituisce il nome del comando
	 * @return il nome del comando
	 */
	public String getName() {
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
	 * Restituisce la sintassi del comando
	 * @return sintassi del comando
	 */
	public String getSyntax() {
		return syntax;
	}
	

	/**
	 * Controlla se il comando ha il nome inserito
	 * @param comando il presunto nome del comando
	 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
	 */
	public boolean hasName(String comando) {
		return this.name.equals(comando);
	}

	
}
