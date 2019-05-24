package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe con il compito di gestire caricamenti e salvataggi da un file di oggetti generici
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class FileHandler{
	
	private static final long EMPTY_FILE = 0;

	/**
	 * Carica un oggetto salvato in un file
	 * @param path il path del file da cui caricare un oggetto
	 * @return l'oggetto contenuto nel file, null in caso di problemi
	 */
	public Object load(String path) {
		File f = new File(path);
		if(f.exists() && f.canRead() && !isEmpty(f)) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))){
				return in.readObject();
			}catch(IOException e) {
				e.printStackTrace();
				return null;
			}catch(ClassNotFoundException e) {
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
		if(!f.exists()) {
			if(f.getParentFile() != null)
				f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f, false))){
			if(f.exists() && f.canWrite()) {
				out.writeObject(obj);
				return true;
			}
		}catch(IOException e) {
			return false;
		}
		return false;
	}
	
	/**
	 * Controlla se il file inserito come parametro è vuoto
	 * @param f il file da controllare
	 * @return True - il file è vuoto<br>False - il file non è vuoto
	 */
	private boolean isEmpty(File f) {
		return f.length() == EMPTY_FILE;
	}
}
