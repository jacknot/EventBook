package progetto.versione1;

import java.time.*;
import java.util.ArrayList;

/**Definisce una struttura dati con il compito di gestire di pi� campi
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class ContenitoreCampi extends ArrayList<Campo<?>>{
	private static final long serialVersionUID = 1L;	//soppressione warning
	private static final String PARTITADICALCIO = "Partita di Calcio";
	private static final String SEPARATORE = "*******************************%n";

	/**Restituisce un campo il cui nome corrisponde a quello inserito
	 * @param name Il nome del campo che si vuole selezionare
	 * @return Il campo con il nome inserito, null altrimenti
	 */
	public Campo<?> getCampo(String name) {
		Campo<?> ris = null;
		ris = this.stream()
				.filter((c)->c.getNome().equals(name))
				.findFirst()
				.get();
		return ris;
	}
	
	/**Metodo statico che consente di generare una collezione di campi prestabilita
	 * @return il contenitore contenente i campi prestabiliti
	 */
	public static ContenitoreCampi generaCampiComuni() {
		ContenitoreCampi contenitore = new ContenitoreCampi();
		contenitore.add(new Campo <String>(IntestazioneEspansa.TITOLO));
		contenitore.add(new Campo <Integer>(IntestazioneEspansa.NUMEROPARTECIPANTI));
		contenitore.add(new Campo <LocalDate>(IntestazioneEspansa.TERMINEISCRIZIONE));
		contenitore.add(new Campo <String>(IntestazioneEspansa.LUOGO));
		contenitore.add(new Campo <LocalDate>(IntestazioneEspansa.DATA));
		contenitore.add(new Campo <LocalTime>(IntestazioneEspansa.ORA));
		contenitore.add(new Campo <Integer>(IntestazioneEspansa.DURATA));
		contenitore.add(new Campo <Double>(IntestazioneEspansa.QUOTAINDIVIDUALE));
		contenitore.add(new Campo <String>(IntestazioneEspansa.COMPRESONELLAQUOTA));
		contenitore.add(new Campo <LocalDate>(IntestazioneEspansa.DATACONCLUSIVA));
		contenitore.add(new Campo <LocalTime>(IntestazioneEspansa.ORACONCLUSIVA));
		contenitore.add(new Campo <String>(IntestazioneEspansa.NOTE));
		return contenitore;
	}
	
	/**Metodo statico che consente di generare una collezione di campi prestabilita a seconda del parametro in ingresso
	 * @param categoria Il parametro per scegliere quale contenitore generare
	 * @return Il contenitore contenente i campi prestabiliti
	 */
	public static ContenitoreCampi generaCampiSpecifici(String categoria) {

		ContenitoreCampi contenitore = new ContenitoreCampi();
		switch(categoria) {
			case PARTITADICALCIO:
					contenitore.add(new Campo <String>(IntestazioneEspansa.GENERE));
					contenitore.add(new Campo <FasciaEta>(IntestazioneEspansa.FASCIADIETA));
					break;
			default:
					break;
		}
		return contenitore;
	}
	
	/**Metodo che, data una collezione in ingresso, restituisce la descrizione del suo contenuto in forma testuale
	 * Non visualizza il valore attuale del contenuto
	 * @param contenitore Il contenitore di cui si vuole visualizzare il contenuto
	 * @return La stringa testuale che descrive la struttura del contenuto
	 */
	public static String visualizzaStruttura(ContenitoreCampi contenitore) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<contenitore.size(); i++) {
			sb.append(contenitore.get(i).visualizzaCaratteristiche());
			sb.append(String.format(SEPARATORE));
		}
		return sb.toString();
	}
	
	
}
