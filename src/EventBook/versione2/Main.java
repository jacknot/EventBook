package EventBook.versione2;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

import EventBook.versione1.*;
import EventBook.versione1.campi.ExpandedHeading;

/**
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {

	private static final String FILEBACHECA = "resource/bacheca.ser";
	private static final String REGISTRAZIONIFILE = "resource/registrazioni.ser";

	private static final String COMANDI_DISPONIBILI = "I comandi a tua disposizione:"
														+ "\n\thelp\t\tComunica i comandi a disposizione"
														+ "\n\tcategoria\tMostra la categoria disponibile"
														+ "\n\tdesc\t\tMostra le caratteristiche della categoria disponibile"
														+ "\n\texit\t\tEsce dal programma"
														+ "\n\tregistra\tRegistra un fruitore"
														+ "\n\tlogin\tAccedi";
															//...
	
	private static final String COMANDO_HELP = "help";
	private static final String COMANDO_DESCRIZIONE = "desc";
	private static final String COMANDO_CATEGORIA = "categoria";
	private static final String COMANDO_USCITA = "exit";
	private static final String COMANDO_REGISTRA = "registra";
	private static final String COMANDO_LOGIN = "login";
	
	//Comandi da loggato
	private static final String COMANDO_CREAZIONE_EVENTO = "crea";
	private static final String COMANDO_MOSTRA_PROPOSTE = "visualizza";
	private static final String COMANDO_PUBBLICA = "pubblica";
	
	private static final String MESSAGGIO_BENVENUTO = "Welcome to EventBook";
	private static final String ATTESA_COMANDO = "> ";
	private static final String MESSAGGIO_USCITA = "Bye Bye";
	

	private static final String ERRORE_COMANDO_NONRICONOSCIUTO = "Il comando inserito non � stato riconosciuto";

	private static Scanner in;
	
	private static Sessione session;
	
	public static void main(String[] args) {
		
		//condizione d'uscita
		boolean exit = false;
		
		HashMap<String, Runnable> protocollo = initCommand();
		
		//chiusura + terminazione anomala -> save
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
			save();
		}));
		
		//definizione scanner
		in = new Scanner(System.in);
		
		//benvenuto
		System.out.println(MESSAGGIO_BENVENUTO);
		
		String comando;
		
		//carica risorse
		GestoreProposte.getInstance().setFile(FILEBACHECA);
		Registrazioni.getInstance().setFile(REGISTRAZIONIFILE);
		load();
		
		//duty cycle
		do {
			System.out.print(ATTESA_COMANDO);
			//aspetto input
			comando = in.nextLine();
			// elaborazione comando
			if(comando.equals(COMANDO_USCITA))
				//voglio uscire
				exit = true;
			else if(protocollo.containsKey(comando))
				//esecuzione comando
				protocollo.get(comando).run();
			else
				//comando non riconosciuto
				System.out.println(ERRORE_COMANDO_NONRICONOSCIUTO);
		}while(!exit);
		//uscita
		
		System.out.println(MESSAGGIO_USCITA);	
		
		//chiusura risorse
		in.close();
	}
	private static void load() {
		GestoreProposte.getInstance().load();
		Registrazioni.getInstance().load();

	}
	private static void save() {
		System.out.println(GestoreProposte.getInstance().save());
		System.out.println(Registrazioni.getInstance().save());
	}
	private static HashMap<String, Runnable> initCommand() {
		//protocollo(modo per riconoscere i comandi)
		HashMap <String,Runnable> protocollo = new HashMap<>();
		
		//inizializzazione protocollo(ci inserisco i comandi)
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
				Registrazioni.getInstance().registra(nome);
		});
		protocollo.put(COMANDO_LOGIN, ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			if(Registrazioni.getInstance().contains(nome)) {
				session = new Sessione(nome);
			}
			else {
				System.out.println("Utente non registrato");
			}
		});
		protocollo.put(COMANDO_CREAZIONE_EVENTO, ()->{
			for(int i=0; i<ExpandedHeading.values().length; i++) {
				System.out.print("inserisci "+ ExpandedHeading.values()[i].getName() + ": ");
				String dato = in.nextLine();
				//ExpandedHeading.values()[i].parse(dato);
			}
		});
		protocollo.put(COMANDO_MOSTRA_PROPOSTE, ()->{
			System.out.print(session.toString());
		});
		protocollo.put(COMANDO_PUBBLICA, ()->{
			System.out.print("Inserisci il nome: ");
			String nome = in.nextLine();
			GestoreProposte.getInstance().add(session.getProposta(nome)); //O con nome o indice
		});
		// fine inizializzazione protocollo
		return protocollo;
	}
}