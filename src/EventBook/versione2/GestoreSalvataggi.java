package EventBook.versione2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GestoreSalvataggi <T>{

		public boolean save(File f, T instance) { //Eccezioni nel main...
			  ObjectOutputStream out = null;
				if(f == null) {
					System.out.println(f + " nullo: " + f==null);
				
					return false; }
				try {
					if(!f.exists()) {
						
						f.getParentFile().mkdirs();
						f.createNewFile();
					}
					if(f.exists() && f.canWrite()) {
						
						out = new ObjectOutputStream(new FileOutputStream(f, false));
						out.writeObject(instance);
						out.close();
						return true;
					}
				}catch(IOException e) {
					System.out.println(f + " exc: " + f.exists());
					return false;
				}
				System.out.println(f + " niente: " + f.exists());
				return false;
			}
		
		public T load(File f){
			if( f != null && f.exists() && f.canRead() && !(f.length() == 0)) {
				try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));) {
					return (T) in.readObject();
				}catch(IOException e) {
					//send eccezione a gestore
					return null;
				}catch(ClassNotFoundException e) {
					//send eccezione a gestore
					return null;
				}
			}
			return null;
		}
}
