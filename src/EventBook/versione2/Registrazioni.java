package EventBook.versione2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import EventBook.versione2.fruitore.Fruitore;
import EventBook.versione2.fruitore.Messaggio;

public class Registrazioni implements Serializable{

	private static final String NOMEFILE = "Registrazioni.ser";
	
	private ArrayList<Fruitore> fruitori;
	private static Registrazioni instance;
	
	private Registrazioni() {
		fruitori = new ArrayList<Fruitore>();
	}
	
	public static Registrazioni getInstance() {
		if(instance == null)
			instance = new Registrazioni();
		return instance;
	
	}
	
	public boolean contains(String nome) {
		for(Fruitore fruitore: fruitori) {
			if(fruitore.getNome().equals(nome))
				return true;
		}
		return false;
	}
	
	public void registra(String nome) {
		fruitori.add(new Fruitore(nome));
	}
	
	public void ricevi(String nome, Messaggio messaggio) {
		for(Fruitore fruitore: fruitori) {
			if(fruitore.getNome().equals(nome))
				fruitore.ricevi(messaggio);
		}
	}
	
	public void save() throws IOException {
		FileOutputStream fileOut = new FileOutputStream(NOMEFILE);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(instance);
		out.close();
		fileOut.close();
	}
	
	public void load() throws ClassNotFoundException, IOException {
		FileInputStream fileIn = new FileInputStream(NOMEFILE);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        instance = (Registrazioni) in.readObject();
        in.close();
        fileIn.close();
	}

	public ArrayList<Fruitore> getFruitori() {
		return fruitori;
	}

	public void setFruitori(ArrayList<Fruitore> fruitori) {
		this.fruitori = fruitori;
	}
}
