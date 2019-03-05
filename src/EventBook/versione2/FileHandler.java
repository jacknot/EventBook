package EventBook.versione2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe con il compito di gestire caricamenti e salvataggi da un file di oggetti generici
 * @author Matteo
 *
 */
public class FileHandler{
	/**
	 * Carica un oggetto salvato in un file
	 * @param path il path del file da cui caricare un oggetto
	 * @return l'oggetto contenuto nel file, null in caso di problemi
	 */
	public Object load(String path) {
		File f = new File(path);
		if(f.exists() && f.canRead() && f.length()!=0) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))){
				return in.readObject();
			}catch(IOException e) {
				//send eccezione a gestore
				e.printStackTrace();
				return null;
			}catch(ClassNotFoundException e) {
				//send eccezione a gestore
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	/**
	 * Salva l'oggetto inserito nel file di cui si è inserito il path
	 * @param path il path del file in cui salvare l'oggetto
	 * @param obj l'oggetto da salvare
	 * @return l'esito dell'operazione
	 */
	public boolean save(String path, Object obj) {
		File f = new File(path);
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f, false))){
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			if(f.exists() && f.canWrite()) {
				out.writeObject(obj);
				return true;
			}
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
