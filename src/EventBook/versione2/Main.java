package EventBook.versione2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import EventBook.versione1.*;

/**
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class Main {
	
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

	public static void main(String[] args) {
		
		//condizione d'uscita
		boolean exit = false;
		
		//inizializzazione cache categorie
		CategoryCache cache = CategoryCache.getInstance();
		
		//protocollo(modo per riconoscere i comandi)
		HashMap <String,Runnable> protocollo = new HashMap<>();
		
		//inizializzazione protocollo(ci inserisco i comandi)
		//comando help
		protocollo.put(COMANDO_HELP,()->{
			System.out.println(COMANDI_DISPONIBILI);
		});
		
		//comando categoria ( visualizza la categoria)
		protocollo.put(COMANDO_CATEGORIA,()->{
			Category p = cache.getCategory(Heading.PARTITADICALCIO.getName());
			System.out.println(p.getDescription());
		});
		
		//comando caratteristiche ( visualizza le caratteristiche della categoria
		protocollo.put(COMANDO_DESCRIZIONE,()->{
			Category p = cache.getCategory(Heading.PARTITADICALCIO.getName());
			System.out.println(p.toString());
		});
		//comando per registrare un nuovo utente, usa notazione UNIX command line
		protocollo.put(COMANDO_REGISTRA, ()->{
			try {
				System.out.print("Inserisci il nome: ");
				Scanner in = new Scanner(System.in);
				String nome = in.nextLine();
				Registrazioni.getInstance().registra(nome);
				Registrazioni.getInstance().save();
				
			} catch (IOException e) {
				System.err.println("Impossibile salvare i dati di registrazione!");
			}
		});
		// fine inizializzazione protocollo
		
		//definizione scanner
		Scanner in = new Scanner(System.in);
		
		//benvenuto
		System.out.println(MESSAGGIO_BENVENUTO);
		String comando;
		//carica risorse
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
		
		try {
			Registrazioni.getInstance().load();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Registrazioni.getInstance().contains("salva"));
		System.out.println(Registrazioni.getInstance().contains("pinco"));
		//uscita
		//chiusura + terminazione anomala -> save
		System.out.println(MESSAGGIO_USCITA);	
		//chiusura risorse
		in.close();
	}
}