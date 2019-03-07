package EventBook.versione2;

import java.util.ArrayList;
import java.util.Scanner;

import EventBook.campi.*;
import EventBook.categoria.*;
import EventBook.proposta.InsiemeProposte;
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
			if(session.modificaProposta(id, campo.getName(), obj))
				System.out.println("Modifica avvenuta con successo");
			else
				System.out.println("Modifica fallita");
		}),
		CREAZIONE_EVENTO("crea", "Crea un nuovo evento", ()->{
			boolean validData = false;
			//crea tipo evento
			FieldSet fields = FieldSetFactory.getInstance().getContenitore("Partita di Calcio");
			for(int i=0; i< fields.size(); i++) {
				do {
					Field<?> campo = fields.get(i);
					System.out.print("Inserisci "+ campo.getName());
					if(!campo.isBinding())
						System.out.print(" [Facoltativo]");
					System.out.print(" : ");
					String dato = in.nextLine();
	
					if(dato.equals("") && !campo.isBinding()) {
						validData = true;
					} else {
						validData = campo.getClassType().isValidType(dato);
						if(validData) {
							campo.setValue(dato);
							System.out.println(String.format("Il campo %s è di tipo %s, valore: %s", campo.getName(), campo.getValue().getClass(), campo.getValue().toString()));
						}
						else System.out.println("Il dato inserito non è nel formato corretto, riprovare!");
					}
				}while(!validData);
			}
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
		
		private String nome;
		private String descrizione;
		private Runnable esecuzione;
		
		private Comando(String comando, String descrizione, Runnable esecuzione) {
			this.nome = comando;
			this.descrizione = descrizione;
			this.esecuzione = esecuzione;
		}
	
		public String getNome() {
			return nome;
		}
	
		public String getDescrizione() {
			return descrizione;
		}
	
		public void run() {
			esecuzione.run();
		}
		
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
		add(Comando.CATEGORIA);
		add(Comando.DESCRIZIONE);
		add(Comando.REGISTRA);
		add(Comando.LOGIN);
	}
	
	public void log() {
		add(Comando.CREAZIONE_EVENTO);
		add(Comando.MOSTRA_PROPOSTE);
		add(Comando.PUBBLICA);
		add(Comando.LOGOUT);
		remove(Comando.REGISTRA);
		remove(Comando.LOGIN);	
	}
	
	public void logout() {
		remove(Comando.CREAZIONE_EVENTO);
		removeMsg(Comando.MOSTRA_PROPOSTE);
		remove(Comando.PUBBLICA);
		remove(Comando.LOGOUT);
		add(Comando.REGISTRA);
		add(Comando.LOGIN);
	}
	
	public boolean contains(String key) {
		if(key.equals("help")) return true;
		for(Comando comando: this) {
			if(comando.equalsName(key))
				return true;
		}
		return false;
	}
	
	public void run(String nomeComando) {
		if(nomeComando.equals("help"))
			System.out.println(toString());
		for(Comando comando : this) {
			if(comando.equalsName(nomeComando)) {
				comando.run();
				break;
			}				
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("I comandi a tua disposizione:");
		for(Comando comando : this) {
			sb.append(String.format(FORMAT_TOSTRING, comando.getNome(), comando.getDescrizione()));
		}
		return sb.toString();
	}
}