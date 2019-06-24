package commandHandling;

import commandHandling.commandBody.*;

/**
 * Classe con il compito di fornire una nuova istanza di un comando
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public class CommandFactory {
	
	/**
	 * Crea il comando per la descrizione di una categoria
	 * @return comando description
	 */
	public Command description() {
		return new Command(CommandDescription.DESCRIPTION, new Description());
	}
	
	/**
	 * Crea il comando per l'uscita dal programma
	 * @return comando exit
	 */
	public Command exit() {
		return new Command(CommandDescription.EXIT, new Exit());
	}
	
	/**
	 * Crea il comando per l'invito di utenti
	 * @return comando invite
	 */
	public Command invite() {
		return new Command(CommandDescription.INVITE, new Invite());
	}
	
	/**
	 * Crea il comando per l'accesso al proprio profilo
	 * @return comando login
	 */
	public Command login() {
		return new Command(CommandDescription.LOGIN, new Login());
	}
	
	/**
	 * Crea il comando per la disconnessione dal proprio profilo
	 * @return comando logout
	 */
	public Command logout() {
		return new Command(CommandDescription.LOGOUT, new Logout());
	}
	
	/**
	 * Crea il comando per la modificare i campi di una proposta
	 * @return comando modify
	 */
	public Command modify() {
		return new Command(CommandDescription.MODIFY, new Modify());
	}
	
	/**
	 * Crea il comando per la modificare i campi del proprio profilo
	 * @return comando modifyProfile
	 */
	public Command modifyProfile() {
		return new Command(CommandDescription.MODIFY_PROFILE, new ModifyProfile());
	}
	
	/**
	 * Crea il comando per creare un nuovo evento
	 * @return comando newEvent
	 */
	public Command newEvent() {
		return new Command(CommandDescription.NEW_EVENT, new NewEvent());
	}
	
	/**
	 * Crea il comando per partecipare a una proposta in bacheca
	 * @return comando participate
	 */
	public Command participate() {
		return new Command(CommandDescription.PARTICIPATE, new Participate());
	}
	
	/**
	 * Crea il comando per accedere al proprio spazio personale
	 * @return comando privateSpaceIn
	 */
	public Command privateSpaceIn() {
		return new Command(CommandDescription.PRIVATE_SPACE_IN, new PrivateSpaceIn());
	}
	
	/**
	 * Crea il comando per uscire dal proprio spazio personale
	 * @return comando privateSpaceOut
	 */
	public Command privateSpaceOut() {
		return new Command(CommandDescription.PRIVATE_SPACE_OUT, new PrivateSpaceOut());
	}
	
	/**
	 * Crea il comando per pubblicare una proposta in bacheca
	 * @return comando publish
	 */
	public Command publish() {
		return new Command(CommandDescription.PUBLISH, new Publish());
	}
	
	/**
	 * Crea il comando per registrare un profilo
	 * @return comando registration
	 */
	public Command registration() {
		return new Command(CommandDescription.REGISTRATION, new Registration());
	}
	
	/**
	 * Crea il comando per rimuovere le notifiche dallo spazio personale
	 * @return comando removeNotifications
	 */
	public Command removeNotifications() {
		return new Command(CommandDescription.REMOVE_NOTIFICATION, new RemoveNotifications());
	}
	
	/**
	 * Crea il comando per mostrare tutte le categorie del sistema
	 * @return comando showCategories
	 */
	public Command showCategories() {
		return new Command(CommandDescription.SHOW_CATEGORIES, new ShowCategories());
	}
	
	/**
	 * Crea il comando per mostrare le caratteristiche di una Categoria
	 * @return comando category
	 */
	public Command showCategory() {
		return new Command(CommandDescription.CATEGORY, new ShowCategory());
	}
	
	/**
	 * Crea il comando per mostrare la bacheca
	 * @return comando showNoticeboard
	 */
	public Command showNoticeboard() {
		return new Command(CommandDescription.SHOW_NOTICEBOARD, new ShowNoticeboard());
	}
	
	/**
	 * Crea il comando per mostrare le notifiche del proprio spazio personale
	 * @return comando showNotifications
	 */
	public Command showNotifications() {
		return new Command(CommandDescription.SHOW_NOTIFICATIONS, new ShowNotifications());
	}
	
	/**
	 * Crea il comando per mostrare il proprio profilo personale
	 * @return comando showProfile
	 */
	public Command showProfile() {
		return new Command(CommandDescription.SHOW_PROFILE, new ShowProfile());
	}
	
	/**
	 * Crea il comando per mostrare le proprie proposte in lavorazione
	 * @return comando showWorkInProgress
	 */
	public Command showWorkInProgress() {
		return new Command(CommandDescription.SHOW_WORKINPROGRESS, new ShowWorkInProgress());
	}
	
	/**
	 * Crea il comando per disiscriversi da una proposta
	 * @return comando unsubscribe
	 */
	public Command unsubscribe() {
		return new Command(CommandDescription.UNSUBSCRIBE, new Unsubscribe());
	}
	
	/**
	 * Crea il comando per ritirare una proposta dalla bacheca
	 * @return comando withdrawProposal
	 */
	public Command withdrawProposal() {
		return new Command(CommandDescription.WITHDRAW_PROPOSAL, new WithdrawProposal());
	}
	
}
