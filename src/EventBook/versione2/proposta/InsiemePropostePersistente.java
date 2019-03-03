package EventBook.versione2.proposta;

import java.io.*;

/**
 * Un set di proposte in grado di mantenerle in modo persistente
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class InsiemePropostePersistente{
	private InsiemeProposte set;
	private String fileName;
	
	public InsiemePropostePersistente(String filname) {
		//politiche controllo del file
		this.fileName = filname;
	}
	public void save() {
		//controlli sull'integrità del file
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			oos.writeObject(set);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//chiusura stream
	}
	public void load() {
		//controlli integrità del file
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)));
			set = (InsiemeProposte) ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//chiusura integrità del file
	}
}
