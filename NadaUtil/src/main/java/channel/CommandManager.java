package channel;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public abstract class CommandManager {
	
	private final static String NOT_A_COMMAND_AT_ALL = "Cette command n'existe pas.";
	private final static String NOT_A_COMMAND = "Cette command n'est pas valide dans cette section.";
	
	public CommandManager(){
	}
	
	protected List<Command> getCommands(){
		return new ArrayList<Command>();
	}
	
	protected boolean isCommand(Command command){
		
		
		return getCommands().contains(command);
	}
	
	protected abstract String answer(Command command, Message message, UserInstance instance);
	
	public void answer( Message message, UserInstance instance){
		Command command = Command.getCommand(message.getContent());
		if(command == null)
			sendMessage(NOT_A_COMMAND_AT_ALL, message.getAuthor());
		else if(!isCommand(command))
			sendMessage(NOT_A_COMMAND, message.getAuthor());
		else{
			switch(command){
			case COMMANDES:
				sendMessage(listCommand(), message.getAuthor());
				break;
			case QUITTER:
				sendMessage(quitter(message.getAuthor()), message.getAuthor());
				break;
			default:
				sendMessage(answer(command, message, instance), message.getAuthor());
				instance.lastCommand = command;
			}
		}
	}
	
	private String listCommand(){
		String str ="";
		for(Command command : getCommands()){
			if(!str.equals(""))
				str+=", ";
			str+= command.str;
		}
		return str;
	}
	
	protected abstract String asking(Message message, Asking ask, UserInstance instance);
	
	public void asking(Message message, UserInstance instance){
		sendMessage(asking(message, instance.ask, instance), message.getAuthor());
	}
	
	public void sendMessage(String str, User user){
		if(str.equals(""))
			return;
		user.getPrivateChannel().sendMessage(str).queue();
	}
	
	protected String quitter(User user){
	//TODO
	return "Bye";
	}
	
}
