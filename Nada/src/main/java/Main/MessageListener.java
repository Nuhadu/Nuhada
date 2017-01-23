package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import channelinstance.*;
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

	public boolean greated = false;

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
				channel.sendMessage(Sentences.messageFor(dest, messages.get(dest))).queue();
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
				String str = gameConducted(author, msg, channel);
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
				if(ask.mode == 2){
					String str = "";
					ArrayList<User> users = channelInstances.get(channel).users;
					if(!users.contains(author)){
						users.add(author);
						str = "J'ai ajouté " + Sentences.getSurname(author, false) +". Quelqu'un d'autre ou go?";
					} else
						str = "Tu as déjà été ajouté " + Sentences.getSurname(author, false) + ".";
					channel.sendMessage(str).queue();
					return true;
				}
		}
		return false;
	}
	
	private boolean answerAsk(User author, Message msg, TextChannel channel){
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		if (askings.get(author) != null) {
			String str = asking(askings.get(author), msg, channel);
			if (str != "") {
				channel.sendMessage(str).queue();
				return  true;
			}
		}
		return false;
	}
	
	private String answer(User author, Message message, TextChannel channel) {				
		String answer = Sentences.getAnswerCommand(message, author, channelInstances.get(channel));
		if(!answer.equals("")) return answer;
		if( answer.equals(""))
			return "Oups, pas de réponses";
		return answer;		
	}
	
	
	
	private String asking(Asking ask, Message message, TextChannel channel) {
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		HashMap<User, Absence> absences = channelInstances.get(channel).absences;
		ArrayList<User> users = channelInstances.get(channel).users;
		String msg = message.getContent().toUpperCase();
		String answer = "";

		switch (ask.mode) {
		case 0: {// Open
			
		}
			break;
		case 1: { // Interpelation
			if (msg.contains("TU TOURNES")) {
				answer = "Mooo! *Tourne sur elle même en faisant la moue*";
				Affinity.changeAfinity(-2, ask.author.getId());
			} else {
				askings.remove(ask.author);
				return answer(ask.author, message, channel);
			}
		}
			break;

		case 2: {// Partie de dé
			if (ask.var.equals("")) {// qui sont les joueurs
				if (msg.equals("GO")) {
					if (users.size() < 1)
						return "Pas assez de joueurs, qui d'autre?";
					else {
						channelInstances.get(channel).jeu = new DesFurieux(users);
						answer = "Je lance le jeu!\n" + gameConducted(ask.author, message, channel);
					}
				} else if(msg.equals("MOI")){
					answer += "J'ai les joueurs:";
					for (User user : users)
						answer += " " + Sentences.getSurname(user, false);
					return answer += "\n Go?";
				} else {
					if (message.getMentionedUsers().size() < 1)
						answer = "J'ai demandé des joueurs, pas des fantômes.\n";
					else {
						for (User user : message.getMentionedUsers())
							if (!users.contains(user)) {
								users.add(user);
							}
					}
					answer += "J'ai les joueurs:";
					for (User user : users)
						answer += " " + Sentences.getSurname(user, false);
					return answer += "\n Go?";
				}
			}
		}
			break;

		case 3: {// Absences1
			if (ask.var.equals("")) {
				ask.var = "SECOND";
				if (msg.contains("NON")) {
					return "Dis moi au moins quand tu reviens! >_<*";
				} else {
					absences.get(ask.author).changeReason(message.getContent());
					return "Et tu reviens quand?";
				}
			} else if (ask.var.equals("SECOND")) {
				if (msg.contains("JAMAIS")) {
					ask.var = "THIRD_BAD";
					return "Ben tant mieux! D'abord !";
				} else {
					ask.var = "THIRD";
					absences.get(ask.author).changeRetour(message.getContent());
					return "Ca marche! En route pour l'aventure!";
				}
			} else if (ask.var.contains("THIRD")) {
				absences.remove(ask.author);
				if (ask.var.contains("BAD"))
					answer = "*Tire une balle dans le coeur de " + Sentences.getSurname(ask.author, false)
							+ "* \n Fallait t'en tenir à ta parole.";
				else {
					answer = "Bon retour " + Sentences.getSurname(ask.author, true) + "!";
					answer += "\n" + answer(ask.author, message, channel);
				}
			}
		}
			break;
		case 4: {// questions
			if (ask.var.equals("CAVA")) {
				answer = "C'est pas comme si ça m'intéressait de toute façon.";
			} else if (ask.var.equals("COMBIEN")) {
				if (msg.equals("OUI")) {
					if (Bank.retire(1, ask.author.getId())) {
						Affinity.changeAfinity(1, ask.author.getId());
						answer = "Héhé, je te le revaudrai!";
					} else
						answer = "Haha, t'es trop pauvre pour ça!";
				} else {
					answer = "Méchant :/";
				}

			} else if (ask.var.equals("FENARO")) {
				if (msg.contains("OUI")) {
					answer = "Il faudra me montrer ça!";
				} else if (msg.contains("NON")) {
					answer = "Dommage, tu aurais pu être revendue un bon prix avec ça.";
				} else {
					answer = "Haha :D";
				}
				ask.mode = 1;
				ask.var = "";
				return answer + "\n Sinon tu voulais quelque chose?";
			}
		}
			break;
		default:
		}

		askings.remove(ask.author);
		return answer;
	}

	private String gameConducted(User author, Message message, TextChannel channel) {
		Jeux jeu = channelInstances.get(channel).jeu;
		String msg = message.getContent().toUpperCase();
		String answer = "";

		if (!jeu.initialized()) {
			return jeu.initialize();
		} else if (!jeu.isStarted()) {
			if (msg.equals("PRÊT")) {
				boolean go = jeu.userPrepare(author);
				if (go) {
					return jeu.play();
				} else {
					return "Il manque" + jeu.userEnAttente();
				}
			} else if (msg.equals("RÈGLES")) {
				return jeu.regle();
			}
		} else if (jeu.seekCommand()) {
			return jeu.receive(message.getContent(), author);
		} else if (jeu.ended())
			channelInstances.get(channel).jeu = null;

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
		if (emote(msg))
			return true;
		if (msg.contains("ARGENT"))
			return true;
		if (msg.contains("REINE"))
			return true;

		return false;
	}

	private boolean emote(String msg) {
		boolean bool = false;

		Random rand = new Random();
		boolean chance = (rand.nextInt(100) > 50);
		if (msg.contains("T.T"))
			bool = true;

		return bool && chance;
	}
	


}