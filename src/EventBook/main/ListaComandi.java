package EventBook.main;

import java.util.ArrayList;

import EventBook.main.Main.Comando;

/**
 * Contenitore in grado di gestire una lista di comandi e di poter fare operazioni su di essi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
class ListaComandi extends ArrayList<Comando>{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Il formato con cui vengono stampati i comandi
	 */
	private static final String FORMAT_TOSTRING = "\n\t%-20s%s";
	
	/**
	 * Costruttore
	 */
	public ListaComandi() {
		super();
		add(Comando.EXIT);
		add(Comando.REGISTRA);
		add(Comando.LOGIN);
	}
	
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logIn
	 */
	public void logIn() {
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
	/**
	 * Operazioni sulla lista di comandi di rimozione/aggiunta comandi a seguito di un logOut
	 */
	public void logOut() {
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
		else if(contains(nomeComando))
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