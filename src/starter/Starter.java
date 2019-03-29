package starter;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Starter {

	private static final String FILEJAR = "EventBook.jar";
	
	public static void main(String[] args) {
		
		File files = new File("file.txt");
		
		if(new File(FILEJAR).exists()) {
			try {
				String str = String.format("java -jar %s", FILEJAR);
				Runtime.getRuntime().exec(str);
				JOptionPane.showMessageDialog(new JFrame(), str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Programma non trovato");
			System.exit(1);
		}

	}

}
