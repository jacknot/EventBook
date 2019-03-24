package tests;

import static org.junit.jupiter.api.Assertions.*;

import users.Database;
import users.Message;

class TestUser {

	@org.junit.jupiter.api.Test
	void registrazioneUtenteGiaRegistrato() {
		Database database = new Database();
		database.register("Mario"); //Registrato utente Mario
		assertFalse(database.register("Mario")); // Deve fallire: Mario già registrato		
	}
	
	@org.junit.jupiter.api.Test
	void registrazioneNonRegistrato() {
		Database database = new Database();
		assertTrue(database.register("Mario")); // Deve essere registrato	
	}
	
	@org.junit.jupiter.api.Test
	void ricezioneMessaggio() {
		Database database = new Database();
		database.register("Mario");
		database.receive("Mario", new Message("Mittente", "Oggetto", "Descrizione"));
		assertFalse(database.getUser("Mario").noMessages()); //deve aver ricevuto il messaggio
	}
	
	@org.junit.jupiter.api.Test
	void rimozioneMessaggio() {
		Database database = new Database();
		database.register("Mario");
		database.receive("Mario", new Message("Mittente", "Oggetto", "Descrizione"));
		database.getUser("Mario").removeMsg(0); //rimuove messaggio
		assertTrue(database.getUser("Mario").noMessages()); //messaggio eliminato
	}

	
}
