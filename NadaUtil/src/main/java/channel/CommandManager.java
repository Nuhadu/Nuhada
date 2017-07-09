package channel;

import java.util.Collection;
import java.util.HashMap;

import commands.Command;
import commands.DisplayCommandsCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class CommandManager {
	
	private final static String NOT_A_COMMAND_AT_ALL = "Cette command n'existe pas, ou n'est pas valide ici.";
	protected HashMap<String, Command> commands;
	private MessageChannel channel;
	
	public CommandManager( MessageChannel channel ){
		//to avoid the overwrite of an already exsiting command who could have internal values;
		commands = new HashMap<String, Command>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Command put(String str, Command c) {
				if( keySet().contains(str) )
					return get(str);
				else
					return super.put(str, c);
			}
		};
		
		commands.put(DisplayCommandsCommand.ID, new DisplayCommandsCommand(this));
		refreshCommands();
		this.channel = channel;
	}
	
	protected Collection<Command> getCommands(){
		return commands.values();
	}
	
	private Command identifyCommand( String str){
		for( Command c : getCommands() ) {
			if( c.isThisCommand( str) )
				return c;
		}
		return null;		
	}
	
	public void process(Message message, UserInstance ui) {
		if( ui.ask != null )
			asking(message, ui);
		else
			answer(message, ui);
	}
	
	private void answer( Message message, UserInstance instance){
		Command command = identifyCommand(message.getContent());
		if(command == null)
			sendMessageTo(NOT_A_COMMAND_AT_ALL, message.getAuthor());
		else{
			sendMessageTo(command.process(instance, message), message.getAuthor());
		}
		instance.lastCommand = command;
	}
	
	public String listCommand(){
		String str ="";
		for(Command command : getCommands()){
			if(!str.equals(""))
				str+=", ";
			str+= command.getName();
		}
		return str;
	}
		
	private void asking(Message message, UserInstance instance){
		sendMessageTo(instance.lastCommand.processAsk(instance, message, instance.ask), message.getAuthor());
	}
	
	/**
	 * Send a message in channel and which user it is adressed to
	 * @param str
	 * @param user
	 */
	public void sendMessageTo(String str, User user){
		if(str.equals(""))
			return;
		channel.sendMessage(user.getName() +"> " +str).queue();
	}
	
	/**
	 * Send a private message to the user
	 * @param str
	 * @param user
	 */
	public void sendPrivateMessage(String str, User user ) {
		user.getPrivateChannel().sendMessage(str).queue();
	}
	
	/**
	 * Send a message to channel
	 */
	public void sendMessage(String str) {
		channel.sendMessage(str).queue();
	}
	
	

	//To Implement
	/**
	 * This method should refresh the list of accessible commands
	 */
	public abstract void refreshCommands();
	
	/**
	 * This method should refresh the list of accessible commands for the given user.
	 */
	public abstract void refreshCommands(UserInstance ui);
	
	
}
