package Main;

import java.util.ArrayList;
import java.util.HashMap;

import channelinstance.*;
import channelinstance.Asking.AskType;
import data.*;
import jeux.*;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	public HashMap<TextChannel, ChannelInstance> channelInstances;

	public MessageListener() {
		super();
		channelInstances = new HashMap<>();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		Message msg = event.getMessage();
		TextChannel channel = event.getTextChannel();

		if (channelInstances.get(channel) == null)
			channelInstances.put(channel, new ChannelInstance());

		toLog(author, msg, channel);
		
		if (!event.getAuthor().isBot()) {
			//MESSAGE FOR YOU?
			message(author, channel);
			boolean hasBeenProcessed = false;
			
			if (event.isFromType(ChannelType.PRIVATE))	
				hasBeenProcessed = test(author, msg, channel);
			//ARE WE IN GAME?
			if(!hasBeenProcessed)
				hasBeenProcessed = gameProcess(author, msg, channel);		
			//WANT TO JOIN GAME?
			if(!hasBeenProcessed)
				hasBeenProcessed = wantToJoin(author, msg, channel);
			//ANSWERING NADA?
			if(!hasBeenProcessed)
				hasBeenProcessed = answerAsk(author, msg, channel);		
			//GIVING COMMAND?
			if (concerned(msg, author, channel) && !hasBeenProcessed)			
				event.getChannel().sendMessage(answer(author, msg, channel)).queue();
		}
	}
	
	private void toLog(User author, Message msg, TextChannel channel){
		if (channel.getType() == ChannelType.PRIVATE) {
			System.out.printf("[PM] %s: %s\n", author.getName(), msg.getContent());			
		} else {
			System.out.printf("[%s][%s] %s: %s\n", channel.getGuild().getName(), channel.getName(),
					author.getName(), msg.getContent());
		}
	}
	
	private void message(User dest, TextChannel channel){
		HashMap<User, MessageForYou> messages = channelInstances.get(channel).messages;
		if (!messages.isEmpty())
			if (messages.get(dest) != null){
				channel.sendMessage(Answers.messageFor(dest, messages.get(dest))).queue();
				messages.remove(dest);
			}
	}
	
	private boolean test(User author, Message msg, TextChannel channel){
		if(msg.getContent().toUpperCase().equals("TEST"))
		{
			channel.sendMessage(test(msg, author)).queue();
			return true;
		}
		return false;
	}
	
	private boolean gameProcess(final User author, final Message msg, final TextChannel channel){
		Jeux jeu = channelInstances.get(channel).jeu;
		if (jeu != null) 
			if (jeu.isInGame(author) && jeu.concerned(msg.getContent(), author)) {
				String str = Answers.gameConducted(author, msg.getContent(), channelInstances.get(channel));
				if (!str.equals("")) {					
					channel.sendMessage(str).queue();
					return true;
				}
			}
		return false;
	}
	
	private boolean wantToJoin(User author, Message msg, TextChannel channel){		
		if( msg.getContent().toUpperCase().contains("MOI") ) {
			for(Asking ask : channelInstances.get(channel).askings.values())
				if(ask.mode == AskType.JEU){
					String str = "";
					ArrayList<User> users = channelInstances.get(channel).users;
					if(!users.contains(author)){
						users.add(author);
						str = "J'ai ajouté " + Answers.getSurname(author, false) +". Quelqu'un d'autre ou go?";
					} else
						str = "Tu as déjà été ajouté " + Answers.getSurname(author, false) + ".";
					channel.sendMessage(str).queue();
					return true;
				}
		}
		return false;
	}
	
	private boolean answerAsk(User author, Message msg, TextChannel channel){
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		if (askings.get(author) != null) {
			String str = Answers.asking(askings.get(author), msg, channelInstances.get(channel));
			if (str != "") {
				channel.sendMessage(str).queue();
				return  true;
			}
		}
		return false;
	}
	
	private String answer(User author, Message message, TextChannel channel) {				
		String answer = Answers.getAnswerCommand(message, author, channelInstances.get(channel));
		if(!answer.equals("")) return answer;
		if( answer.equals(""))
			return "Oups, pas de réponses";
		return answer;		
	}
	

	

	
	private String test(Message message, User author){		
		return "Aucun test n'est en cours";
	}
	
	
	//AUTRES

	private boolean concerned(Message message, User author, TextChannel channel) {
		if (concerned(message))
			return true;
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		if (askings.get(author) != null)
			return true;		
		return false;
	}

	private boolean concerned(Message message) {
		String msg = message.getContent().toUpperCase();
		if ( Command.isCommand( msg, channelInstances.get(message.getChannel()) ) )
			return true;
		if (msg.contains("ARGENT"))
			return true;
		if (msg.contains("REINE"))
			return true;

		return false;
	}

}