package general;

import channel.CommandManager;
import channel.UserInstance;
import command.CreateSessionCommand;
import command.JoinSessionCommand;
import command.SearchSessionCommand;
import net.dv8tion.jda.core.entities.User;

public class FateCommandManager extends CommandManager {

	private Session session;
	private UserInstance userInstance;
	
	/**
	 * Constructor to keep access to the session
	 * @param session
	 */
	public FateCommandManager( User user) {
		super(  user.getPrivateChannel() );
		this.userInstance = UserInstance.getUserInstance(user);
		sendMessage( new SearchSessionCommand().process( userInstance, null));
	}
	
	public boolean setSession( Session session ) {
		if( Session.joinSession(userInstance.user, session.getName()) ) {
			this.session = session;
			refreshCommands();
			return true;
		}
		sendMessage("Une erreur est survenue avec cette session.");
		return false;
	}
	
	
	//Implemented methods
	@Override
	public void refreshCommands () {
		if( session == null) {
			commands.put(SearchSessionCommand.ID, new SearchSessionCommand());
			commands.put(JoinSessionCommand.ID, new JoinSessionCommand(this));
			commands.put(CreateSessionCommand.ID, new CreateSessionCommand() );
		} else {
			commands.remove(SearchSessionCommand.ID);
			commands.remove(JoinSessionCommand.ID);
			commands.remove(CreateSessionCommand.ID);
		}
	}

	@Override
	public void refreshCommands(UserInstance ui) {
		ui.privateManager.refreshCommands();
	}

	
	@Override
	public void sendMessageTo(String str, User user){
		sendPrivateMessage(str, user);
	}
	
	@Override
	public void sendMessage(String str) {
		sendPrivateMessage(str, userInstance.user);
	}
	

}
