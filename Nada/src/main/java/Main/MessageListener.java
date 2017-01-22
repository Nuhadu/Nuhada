package Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

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

		if (event.isFromType(ChannelType.PRIVATE)) {
			System.out.printf("[PM] %s: %s\n", author.getName(), msg.getContent());
			if(msg.getContent().toUpperCase().equals("TEST"))
			{
				event.getChannel().sendMessage(test(msg, author)).queue();
			}
		} else {
			System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), channel.getName(),
					event.getMember().getEffectiveName(), msg.getContent());
		}

		boolean hasBeenProcessed = false;

		if (!event.getAuthor().isBot()) {
			HashMap<User, Asking> askings = channelInstances.get(channel).askings;
			HashMap<User, MessageForYou> messages = channelInstances.get(channel).messages;
			Jeux jeu = channelInstances.get(channel).jeu;
			
			if (!messages.isEmpty()) {
				if (messages.get(author) != null) {
					String str = "Hey " + Sentences.getSurname(author, true) + "! "
							+ Sentences.getSurname(messages.get(author).author, false) + " te dit : "
							+ messages.get(author).message;
					event.getChannel().sendMessage(str).queue();
					messages.remove(author);
				}
			}
			if (jeu != null) {
				if (jeu.isInGame(author) && jeu.concerned(msg.getContent(), author)) {
					String str = gameConducted(author, msg, channel);
					if (!str.equals("")) {
						hasBeenProcessed = true;
						event.getChannel().sendMessage(str).queue();
					}
				}
			}
			
			if( msg.getContent().toUpperCase().contains("MOI") && !hasBeenProcessed) {
				for(Asking ask : askings.values())
					if(ask.mode == 2){
						String str = "";
						if(channelInstances.get(channel).users == null)
							channelInstances.get(channel).users = new ArrayList<User>();
						ArrayList<User> users = channelInstances.get(channel).users;
						if(!users.contains(author)){
							users.add(author);
							str = "Je t'ajoute " + Sentences.getSurname(author, false) +".";
						} else
							str = "Tu as déjà été ajouté " + Sentences.getSurname(author, false) + ".";
						event.getChannel().sendMessage(str).queue();
					}
			}

			if (askings.get(author) != null && !hasBeenProcessed) {
				String str = asking(askings.get(author), msg, channel);
				if (str != "") {
					hasBeenProcessed = true;
					event.getChannel().sendMessage(str).queue();
				}
			}
			
			if (concerned(msg, author, channel) && !hasBeenProcessed)			
				event.getChannel().sendMessage(answer(author, msg, channel)).queue();
		}
	}

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
		if (interpelationOnly(msg))
			return true;
		if (interpelation(msg))
			return true;
		if (emote(msg))
			return true;
		if (msg.contains("ARGENT"))
			return true;
		if (msg.contains("REINE"))
			return true;
		if (msg.contains("TU TOURNES"))
			return true;
		if (msg.contains("IL N'Y A PERSONNE?"))
			return true;		
		if (greeting(msg, new Mode(0)) && !greated)
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

	private boolean interpelation(String msg) {
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg);
		while (tk.hasMoreTokens()) {
			token = tk.nextToken();
			if (token.equals("NADA") || token.equals("NADA,"))
				return true;
			if (token.equals("NUHADA")|| token.equals("NUHADA,"))
				return true;
			if (token.equals("NUHA")|| token.equals("NUHA,"))
				return true;

		}
		return false;
	}

	private boolean interpelationOnly(String msg) {
		if (msg.equals("NADA"))
			return true;
		if (msg.equals("NUHADA"))
			return true;

		if (msg.equals("NADA!"))
			return true;
		if (msg.equals("NUHADA!"))
			return true;

		if (msg.equals("NADA?"))
			return true;
		if (msg.equals("NUHADA?"))
			return true;

		if (msg.equals("NADA ?"))
			return true;
		if (msg.equals("NUHADA ?"))
			return true;

		if (msg.equals("NADA !"))
			return true;
		if (msg.equals("NUHADA !"))
			return true;
		
		if (msg.equals("NADA,"))
			return true;
		if (msg.equals("NUHADA,"))
			return true;
		
		return false;
	}

	private boolean greeting(String msg, Mode mode) {
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg);
		while (tk.hasMoreTokens()) {
			token = tk.nextToken();
			if (token.equals("BONJOUR")) {
				mode.i = 1;
				return true;
			}
			if (token.equals("BJR")) {
				mode.i = 1;
				return true;
			}
			if (token.equals("SALUT")) {
				mode.i = 0;
				return true;
			}
			if (token.equals("YO")) {
				mode.i = 0;
				return true;
			}
			if (token.equals("BONSOIR")) {
				mode.i = -1;
				return true;
			}
			if (token.equals("SOIR") && !msg.contains("CE SOIR") && !msg.contains("UN SOIR")) {
				mode.i = -1;
				return true;
			}
			if (token.equals("'SOIR")) {
				mode.i = -1;
				return true;
			}
			if (token.equals("HELLO")) {
				mode.i = 0;
				return true;
			}
			if (token.equals("OHAYO")) {
				mode.i = 1;
				return true;
			}
			if (token.equals("JOUR") && !msg.contains("CE JOUR") && !msg.contains("UN JOUR")) {
				mode.i = 1;
				return true;
			}
			if (token.equals("'JOUR")) {
				mode.i = 1;
				return true;
			}
			if (token.equals("KIKOU")) {
				mode.i = 0;
				return true;
			}
			if (token.equals("YOSHA")) {
				mode.i = 0;
				return true;
			}
		}
		return false;
	}

	private String greetingBack(User author, Mode mode) {
		String msg = "Bonjour";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		int heure = Integer.decode(sdf.format(cal.getTime()));

		if (heure < 11)
			heure = 1;
		else if (heure > 17)
			heure = -1;
		else
			heure = 0;
		if (mode.i != 0 && heure != mode.i) {
			switch (mode.i) {
			case -1:
				msg = Sentences.getRdmSentence(Sentences.greeting_not_soir, author);
				break;
			case 1:
				msg = Sentences.getRdmSentence(Sentences.greeting_not_jour, author);
			}

			msg += " M'enfin bienvenue à bord!";
		} else {
			switch (heure) {
			case -1:
				msg = Sentences.getRdmSentence(Sentences.greeting_soir, author);
				break;
			case 0:
				msg = Sentences.getRdmSentence(Sentences.greeting_nope, author);
				break;
			case 1:
				msg = Sentences.getRdmSentence(Sentences.greeting_jour, author);
			}
		}

		return msg;
	}

	private String answer(User author, Message message, TextChannel channel) {
		
		String msg = message.getContent().toUpperCase();
		Random rand = new Random();
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		if (interpelationOnly(msg)) {
			if (rand.nextInt(100) > 20) {
				askings.put(author, new Asking(author, 1));
				return Sentences.getRdmSentence(Sentences.answer_interpel, author);
			} else {
				if (author.getName().toUpperCase().equals("FENARO07")) {
					askings.put(author, new Asking(author, 4, "FENARO"));
					return "On m'a dit que tu avais une arme à feu entre les jambes. C'est vrai?";
				}
				return "Non. *Tire la langue*";
			}

		} 
		
		Mode mode = new Mode(0);
		if (greeting(message.getContent().toUpperCase(), mode)) {
			String answer = "";			
			answer = greetingBack(author, mode);
			greated = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(3600000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					greated = false;
				}
			}).start();
			return answer;
		}
			
		String answer = Sentences.getAnswerCommand(message, author, channelInstances.get(channel));
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
				if (users == null) {
					channelInstances.get(channel).users = new ArrayList<>();
					users = channelInstances.get(channel).users;
				}
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
	


}