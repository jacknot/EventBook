package EventBook.versione2;

import java.util.*;
import java.util.stream.*;

import EventBook.campi.*;
import EventBook.categoria.*;
import EventBook.proposta.*;
import EventBook.versione2.Main.Comando;

/**
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	private static final String BACHECAFILE = "resource/bacheca.ser";
	private static final String REGISTRAZIONIFILE = "resource/registrazioni.ser";

	private static final String COMANDO_LOGIN = "login";
	private static final String COMANDO_LOGOUT = "logout";

	private static final String MESSAGGIO_BENVENUTO = "Welcome to EventBook";
	private static final String ATTESA_COMANDO = "> ";
	private static final String MESSAGGIO_USCITA = "Bye Bye";
	
	private static final String ERRORE_COMANDO_NONRICONOSCIUTO = "Il comando inserito non è stato riconosciuto ('help' per i comandi a disposizione)";

	private static Scanner in;
	
	private static Sessione session;
	private static Database db;
	private static InsiemeProposte bacheca;
	private static boolean exit;
	
	public static void main(String[] args) {
		exit = false;
		ListaComandi protocollo = new ListaComandi();
		
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
			System.out.println(MESSAGGIO_USCITA);	
			in.close();
			save();
		}));
		
		in = new Scanner(System.in);
		
		//benvenuto
		System.out.println(MESSAGGIO_BENVENUTO);
		String comando;
		load();
		
		//duty cycle
		do {
			System.out.print(ATTESA_COMANDO);
			//aspetto input
			comando = in.nextLine();
			// elaborazione comando		
			if(protocollo.contains(comando)) {
				protocollo.run(comando);
				if(comando.equals(COMANDO_LOGIN) && session != null)
					protocollo.log();
				else if(comando.equals(COMANDO_LOGOUT) && session == null)
					protocollo.logout();
			}				
			else
				System.out.println(ERRORE_COMANDO_NONRICONOSCIUTO);
		}while(!exit);
		//uscita
		
	}
	/**
	 * Carica il database e la bacheca
	 */
	private static void load() {
		System.out.println("Caricamento database ...");
		db = (Database)new FileHandler().load(REGISTRAZIONIFILE);
		if(db == null) {
			db = new Database();
			System.out.println("Caricato nuovo database");
			}
		System.out.println("Caricamento bacheca ...");
		bacheca = (InsiemeProposte)new FileHandler().load(BACHECAFILE);
		if(bacheca == null) {
			bacheca = InsiemeProposte.newBacheca();
			System.out.println("Caricata nuova bacheca");
			}
	}
	/**
	 * Salva la bacheca e il database
	 */
	private static void save() {
		System.out.println("Salvataggio bacheca ...");
		System.out.println(new FileHandler().save(BACHECAFILE, bacheca));
		System.out.println("Salvataggio database ...");
		System.out.println(new FileHandler().save(REGISTRAZIONIFILE, db));
	}

	protected enum Comando {
		
		EXIT("exit", "Esci dal programma",()->System.exit(0)),
		CATEGORIA("categoria", "Mostra la categoria disponibile", ()->{
			Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO);
			System.out.println(p.getDescription());
		}),
		DESCRIZIONE("descrizione", "Mostra le caratteristiche della categoria disponibile", ()->{
			Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO);
			System.out.println(p.getFeatures());
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
			}
			else {
				System.out.println("Utente non registrato");
			}
		}),
		LOGOUT("logout", "Per uscire", ()->session = null),
		MODIFICA("modifica","Modifica il campo di una proposta",()->{
			//inserisci id proposta
			boolean annulla = false;
			boolean valido = false;
			int id = -1;
			do {
				try {
				System.out.print("Inserisci l'identificatore : ");
				id = in.nextInt();
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
				System.out.println("Inserisci un " + campo.getType().getSimpleName()+" : ");
				obj = campo.getClassType().parse(in.nextLine());
				if(obj != null)
					valido = true;
				else
					System.out.println("Il valore inserito non è corretto");
			}while(!valido);
			//conferma modifica
			valido = false;
			do {
				System.out.println("Sei sicuro di voler modificare ?");
				System.out.println("Proposta :" + id + ", Campo :" + campo.getName() + ", nuovo valore: " + obj.toString());
				System.out.print("[y/n]> ");
				if(!in.nextLine().equalsIgnoreCase("y"))
					annulla = true;
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
							System.out.print("Inserisci un valore per il campo: ");
							String value = in.nextLine();
							if((value == "") && !fd.isBinding())
								valid = true;
							else if(fd.getClassType().isValidType(value)) {
								valid = true;
								if(evento.setValue(fd.getName(), fd.getClassType().parse(value)))
									System.out.println("Dato inserito correttamente");
								else
									System.out.println("Il dato non è stato inserito correttamente");
							}
							if(!valid)
								System.out.println("Il dato inserito non è valido, riprova");
						}while(!valid);
					});
			if(session.aggiungiProposta(new Proposta(evento, session.getProprietario())))
				System.out.println("La proposta è stata aggiunta alla proposte in lavorazione");
			else
				System.out.println("La proposta non è stata aggiunta");
		}),
		MOSTRA_IN_LAVORAZIONE("mostraInLavorazione", "Visualizza le tue proposte", ()->System.out.print(session.showInProgress())),
		MOSTRA_NOTIFICHE("mostraNotifiche","Mostra le tue notifiche", ()->System.out.println(session.showNotification())),
		RIMUOVI_NOTIFICA("rimuoviNotifica","Rimuovi la notifica inserendo il loro identificativo",()->{
			boolean valido = false;
			do {
				try {
					System.out.print("Inserisci l'id da eliminare: ");
					int i = in.nextInt();
					valido = true;
					if(!session.getProprietario().removeMsg(i))
						System.out.println("La rimozione non è andata a fine");
				}catch(Exception e) {
					System.out.println("Dato invalido, inserisci un numero");
				}
			}while(!valido);
		}),
		MOSTRA_BACHECA("mostraBacheca","Mostra tutte le proposte in bacheca",()->{
			System.out.println("Le proposte in bacheca:\n" + bacheca.showContent());
		}),
		//da controllare
		PUBBLICA("pubblica", "Pubblica un evento creato", ()->{
			boolean valido = false;
			do {
				try {
				System.out.print("Inserisci l'identificatore : ");
				int id = in.nextInt();
				valido = true;
				bacheca.add(session.getProposta(id));
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
				int id = in.nextInt();
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

class ListaComandi extends ArrayList<Comando>{
	
	private static final String FORMAT_TOSTRING = "\n\t%-20s%s";
	
	public ListaComandi() {
		super();
		add(Comando.EXIT);
		add(Comando.REGISTRA);
		add(Comando.LOGIN);
	}
	
	public void log() {
		add(Comando.CATEGORIA);
		add(Comando.DESCRIZIONE);
		add(Comando.LOGOUT);
		add(Comando.MODIFICA);
		add(Comando.CREAZIONE_EVENTO);
		add(Comando.MOSTRA_IN_LAVORAZIONE);
		add(Comando.MOSTRA_NOTIFICHE);
		add(Comando.RIMUOVI_NOTIFICA);
		add(Comando.MOSTRA_BACHECA);
		add(Comando.PUBBLICA);
		add(Comando.PARTECIPA);
		remove(Comando.REGISTRA);
		remove(Comando.LOGIN);	
	}
	
	public void logout() {
		add(Comando.REGISTRA);
		add(Comando.LOGIN);
		remove(Comando.CATEGORIA);
		remove(Comando.DESCRIZIONE);
		remove(Comando.LOGOUT);
		remove(Comando.MODIFICA);
		remove(Comando.CREAZIONE_EVENTO);
		remove(Comando.MOSTRA_IN_LAVORAZIONE);
		remove(Comando.MOSTRA_NOTIFICHE);
		remove(Comando.RIMUOVI_NOTIFICA);
		remove(Comando.MOSTRA_BACHECA);
		remove(Comando.PUBBLICA);
		remove(Comando.PARTECIPA);
	}
	
	/**
	 *	Controlla se è presente un comando con il nome inserito 
	 * @param key il nome del comando di cui si vuole verificare la presenza
	 * @return True - è presente un comando con il nome inserito<br>False - non è presente un comando con il nome inserito
	 */
	public boolean contains(String key) {
		if(key.equals("help")) return true;
		return this.stream()
				.anyMatch((c)->c.equalsName(key));
	}
	
	/**
	 * Esegue il comando di cui si è inserito il nome, se presente
	 * @param nomeComando il nome del comando da eseguire
	 */
	public void run(String nomeComando) {
		if(nomeComando.equals("help"))
			System.out.println(toString());
		if(contains(nomeComando))
			this.stream()
				.filter((c)->c.equalsName(nomeComando))
				.findFirst().get().run();
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("I comandi a tua disposizione:");
		this.stream()
			.forEachOrdered((c)->sb.append(String.format(FORMAT_TOSTRING, c.getNome(), c.getDescrizione())));
		return sb.toString();
	}
}