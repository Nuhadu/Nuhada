package jdraranor.jdrchannel;

import java.util.ArrayList;
import java.util.List;

import jdraranor.JDRAranorEngine;
import jdraranor.UserInstance;
import jdraranor.command.JDRCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class JDRChannel {
	protected TextChannel channel;
	private ArrayList<JDRCommand> commands;
	private final static String NOT_A_COMMAND_AT_ALL = "Cette command n'existe pas.";
	private final static String NOT_A_COMMAND = "Cette command n'est pas valide dans cette section.";
	private TextChannel lastChannel;
	
	public JDRChannel(TextChannel channel, ArrayList<JDRCommand> commands){
		this.channel = channel;
		this.commands = commands;
		commands.add(JDRCommand.QUITTER);
		commands.add(JDRCommand.COMMANDES);
	}
	
	protected List<JDRCommand> getCommands(){
		return commands;
	}
	
	public JDRChannel setLastChannel(TextChannel pChannel){
		lastChannel = pChannel;
		return this;
	}
	
	protected boolean isCommand(JDRCommand command){
		if(JDRCommand.COMMANDES == command)
			return true;
		if(JDRCommand.QUITTER == command)
			return true;
		return getCommands().contains(command);
	}
	
	protected abstract String answer(JDRCommand command, Message message, UserInstance instance);
	
	public void answer( Message message, UserInstance instance){
		JDRCommand command = JDRCommand.getCommand(message.getContent());
		if(command == null)
			sendMessage(NOT_A_COMMAND_AT_ALL);
		else if(!isCommand(command))
			sendMessage(NOT_A_COMMAND);
		else{
			switch(command){
			case COMMANDES:
				sendMessage(listCommand());
				break;
			case QUITTER:
				sendMessage(quitter(message.getAuthor()));
				break;
			default:
				sendMessage(answer(command, message, instance));
				instance.lastCommand = command;
			}
		}
	}
	
	private String listCommand(){
		String str ="";
		for(JDRCommand command : getCommands()){
			if(!str.equals(""))
				str+=", ";
			str+= command.str;
		}
		return str;
	}
	
	protected abstract String asking(Message message, JDRAsking ask, UserInstance instance);
	
	public void asking(Message message, UserInstance instance){
		sendMessage(asking(message, instance.ask, instance));
	}
	
	public void sendMessage(String str){
		if(str.equals(""))
			return;
		channel.sendMessage(str).queue();
	}
	
	protected String quitter(User user){
		if(lastChannel == null){			
			return "Tu es mon prisonnier, muhaha! Et puis, y a rien à l'extérieur. RIEN";
		}
		else{
			JDRAranorEngine.goBackTo(channel, lastChannel, user);
			return "";
		}
	}
	
}
