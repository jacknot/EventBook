package EventBook.versione2;

import java.util.HashMap;
import java.util.Scanner;

import EventBook.versione1.*;
import EventBook.versione1.campi.Field;
import EventBook.versione1.campi.FieldSet;
import EventBook.versione2.proposta.InsiemeProposte;

/**
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	private static final String BACHECAFILE = "resource/bacheca.ser";
	private static final String REGISTRAZIONIFILE = "resource/registrazioni.ser";

	private static final String COMANDI_DISPONIBILI = "I comandi a tua disposizione:"
														+ "\n\thelp\t\tComunica i comandi a disposizione"
														+ "\n\tcategoria\tMostra la categoria disponibile"
														+ "\n\tdescrizione\t\tMostra le caratteristiche della categoria disponibile"
														+ "\n\texit\t\tEsce dal programma"
														+ "\n\tregistra\tRegistra un fruitore"
														+ "\n\tlogin\tAccedi";
	
	private static final String COMANDO_HELP = "help";
	private static final String COMANDO_DESCRIZIONE = "descrizione";
	private static final String COMANDO_CATEGORIA = "categoria";
	private static final String COMANDO_USCITA = "exit";
	private static final String COMANDO_REGISTRA = "registra";
	private static final String COMANDO_LOGIN = "login";
	
	private static final String COMANDO_CREAZIONE_EVENTO = "crea";
	private static final String COMANDO_MOSTRA_PROPOSTE = "visualizza";
	private static final String COMANDO_PUBBLICA = "pubblica";
	
	private static final String MESSAGGIO_BENVENUTO = "Welcome to EventBook";
	private static final String ATTESA_COMANDO = "> ";
	private static final String MESSAGGIO_USCITA = "Bye Bye";

	private static final String ERRORE_COMANDO_NONRICONOSCIUTO = "Il comando inserito non � stato riconosciuto";

	private static Scanner in;
	
	private static Sessione session;
	private static Database db;
	private static InsiemeProposte bacheca;
	private static boolean exit;
	
	public static void main(String[] args) {
		exit = false;
		HashMap<String, Runnable> protocollo = initCommand();
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
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
			if(comando.equals(COMANDO_USCITA))
				exit = true;
			else if(protocollo.containsKey(comando))
				protocollo.get(comando).run();
			else
				System.out.println(ERRORE_COMANDO_NONRICONOSCIUTO);
		}while(!exit);
		//uscita
		System.out.println(MESSAGGIO_USCITA);	
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
	private static HashMap<String, Runnable> initCommand() {
		HashMap <String,Runnable> protocollo = new HashMap<>();
		//comando help
		protocollo.put(COMANDO_HELP,()->{
			System.out.println(COMANDI_DISPONIBILI);
		});
		//comando categoria ( visualizza la categoria)
		protocollo.put(COMANDO_CATEGORIA,()->{
				Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO.getName());
				System.out.println(p.getDescription());
		});
		//comando caratteristiche ( visualizza le caratteristiche della categoria
		protocollo.put(COMANDO_DESCRIZIONE,()->{
			Category p = CategoryCache.getInstance().getCategory(Heading.PARTITADICALCIO.getName());
			System.out.println(p.getFeatures());
		});
		//comando per registrare un nuovo utente, usa notazione UNIX command line
		protocollo.put(COMANDO_REGISTRA, ()->{
				System.out.print("Inserisci il nome: ");
				String nome = in.nextLine();
				db.registra(nome);
		});
		protocollo.put(COMANDO_LOGIN, ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			if(db.contains(nome)) {
				session = new Sessione(db.getFruitore(nome));
			}
			else {
				System.out.println("Utente non registrato");
			}
		});
		protocollo.put(COMANDO_CREAZIONE_EVENTO, ()->{
			boolean validData = false;
			FieldSet fields = FieldSetFactory.getInstance().getContenitore("Partita di Calcio");
			for(int i=0; i< fields.size(); i++) {
				do {
					Field<?> campo = fields.get(i);
					System.out.print("Inserisci "+ campo.getName());
					if(!campo.isBinding())System.out.print(" [Facoltativo]");
					System.out.print(" : ");
					String dato = in.nextLine();

					if(dato.equals("") && !campo.isBinding()) {
						validData = true;
					} else {
						validData = campo.getClassType().isValidType(dato);
						if(validData) {
							campo.getClassType().parse(campo, dato);
							System.out.println(String.format("Il campo %s è di tipo %s", campo.getName(), campo.getValue().getClass()));
						}
						else System.out.println("Il dato inserito non è nel formato corretto, riprovare!");
					}
				}while(!validData);
			}
		});
		protocollo.put(COMANDO_MOSTRA_PROPOSTE, ()->{
			System.out.print(session.showProposals());
		});
		protocollo.put(COMANDO_PUBBLICA, ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			bacheca.add(session.getProposta(nome)); //O con nome o indice
		});
		// fine inizializzazione protocollo
		return protocollo;
	}
}