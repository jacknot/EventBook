package EventBook.versione2;

import java.util.HashMap;
import java.util.Scanner;

import EventBook.versione1.*;

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
														+ "\n\tregistra\tRegistra un fruitore";
	private static final String COMANDO_HELP = "help";
	private static final String COMANDO_DESCRIZIONE = "desc";
	private static final String COMANDO_CATEGORIA = "categoria";
	private static final String COMANDO_USCITA = "exit";
	private static final String COMANDO_REGISTRA = "registra";
	
	private static final String MESSAGGIO_BENVENUTO = "Welcome to EventBook";
	private static final String ATTESA_COMANDO = "> ";
	private static final String MESSAGGIO_USCITA = "Bye Bye";
	
	private static final String ERRORE_COMANDO_NONRICONOSCIUTO = "Il comando inserito non è stato riconosciuto";

	private static Scanner in;
	public static void main(String[] args) {
		
		//condizione d'uscita
		boolean exit = false;
		
		HashMap<String, Runnable> protocollo = initCommand();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { //Intercetta chiusura 
			System.out.println("Salvataggio...");
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
		
		save();
		//chiusura + terminazione anomala -> save
		System.out.println(MESSAGGIO_USCITA);	
		
		//chiusura risorse
		in.close();
	}
	private static void load() {
		GestoreProposte.getInstance().load();
		Registrazioni.getInstance().load();

	}
	private static void save() {
		GestoreProposte.getInstance().save();
		Registrazioni.getInstance().save();
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
			System.out.println(p.toString());
		});
		//comando per registrare un nuovo utente, usa notazione UNIX command line
		protocollo.put(COMANDO_REGISTRA, ()->{
				System.out.print("Inserisci il nome: ");
				String nome = in.nextLine();
				Registrazioni.getInstance().registra(nome);
		});
		// fine inizializzazione protocollo
		return protocollo;
	}
}