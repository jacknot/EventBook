package main.commands.strategy;

public class CommandFactory {
	
	public Command description() {
		return new Command(CommandDescription.DESCRIPTION, new Description());
	}
	
	public Command exit() {
		return new Command(CommandDescription.EXIT, new Exit());
	}
	
	public Command invite() {
		return new Command(CommandDescription.INVITE, new Invite());
	}
	
	public Command login() {
		return new Command(CommandDescription.LOGIN, new Login());
	}
	
	public Command logout() {
		return new Command(CommandDescription.LOGOUT, new Logout());
	}
	
	public Command modify() {
		return new Command(CommandDescription.MODIFY, new Modify());
	}
	
	public Command modifyProfile() {
		return new Command(CommandDescription.MODIFY_PROFILE, new ModifyProfile());
	}
	
	public Command newEvent() {
		return new Command(CommandDescription.NEW_EVENT, new NewEvent());
	}
	
	public Command participate() {
		return new Command(CommandDescription.PARTICIPATE, new Participate());
	}
	
	public Command privateSpaceIn() {
		return new Command(CommandDescription.PRIVATE_SPACE_IN, new PrivateSpaceIn());
	}
	
	public Command privateSpaceOut() {
		return new Command(CommandDescription.PRIVATE_SPACE_OUT, new PrivateSpaceOut());
	}
	
	public Command publish() {
		return new Command(CommandDescription.PUBLISH, new Publish());
	}
	
	public Command registration() {
		return new Command(CommandDescription.REGISTRATION, new Registration());
	}
	
	public Command removeNotifications() {
		return new Command(CommandDescription.REMOVE_NOTIFICATION, new RemoveNotifications());
	}
	
	public Command showCategories() {
		return new Command(CommandDescription.SHOW_CATEGORIES, new ShowCategories());
	}
	
	public Command showCategory() {
		return new Command(CommandDescription.CATEGORY, new ShowCategory());
	}
	
	public Command showNoticeboard() {
		return new Command(CommandDescription.SHOW_NOTICEBOARD, new ShowNoticeboard());
	}
	
	public Command showNotifications() {
		return new Command(CommandDescription.SHOW_NOTIFICATIONS, new ShowNotifications());
	}
	
	public Command showProfile() {
		return new Command(CommandDescription.SHOW_PROFILE, new ShowProfile());
	}
	
	public Command showWorkInProgress() {
		return new Command(CommandDescription.SHOW_WORKINPROGRESS, new ShowWorkInProgress());
	}
	
	public Command unsubscribe() {
		return new Command(CommandDescription.UNSUBSCRIBE, new Unsubscribe());
	}
	
	public Command withdrawProposal() {
		return new Command(CommandDescription.WITHDRAW_PROPOSAL, new WithdrawProposal());
	}
	
}
