package Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import channelinstance.Absence;
import channelinstance.Asking;
import channelinstance.ChannelInstance;
import channelinstance.MessageForYou;
import data.Affinity;
import data.Bank;
import data.SurnameStorage;
import jeux.DesFurieux;
import jeux.Jeux;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	final static String[] greeting_soir = { "Bonsoir #NAME!", "Bienvenue #NAME.", "On attendait plus que toi!",
			"Oh non pas lui.." };
	final static String[] greeting_jour = { "Bonjour #NAME!", "Alors #NAME, on est matinal?", "*baille* 'Jour..",
			"'Jour #NAME!" };
	final static String[] greeting_nope = { "Salut #NAME!", "Heureux d'être accueilli par une magnifique pirate?",
			"#NAME, tu tombes à pic, y a le pont à brosser!", "*sourire accueillant*" };
	final static String[] answer_interpel = { "Mui?", "C'est moi.", "Héhé, en quoi puis-je t'aider?",
			"Si c'est pour de l'argent, j'en ai que pour moi!" };
	final static String[] asked_inexist = { "C'est indiscret #NAME.", "Eh bien.. mettons que j'ai une réponse!", ":3",
			"Des fois, je me demande si Miss Fortune me cherche aussi",
			"Demande à Jack Sombrebarbe. Il sait pas tout mais il te tuera peut-être pas.",
			"C'est pas vrai, c'est pas moi, t'as pas de preuve! Tu voulais quoi déjà?" };
	final static String[] greeting_not_jour = { "On vient tout juste de s'lever #NAME?",
			"Hum.. Dois y avoir du décalage horaire avec Aranor.." };
	final static String[] greeting_not_soir = { "Il n'est pas si tard que ça! Flemmard!",
			"C'est déjà le matin tu sais?", "That's no moon... That is the sun boy!" };

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
					String str = "Hey " + getSurname(author, true) + "! "
							+ getSurname(messages.get(author).author, false) + " te dit : "
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
							str = "Je t'ajoute " + getSurname(author, false) +".";
						} else
							str = "Tu as déjà été ajouté " + getSurname(author, false) + ".";
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
				msg = getRdmSentence(greeting_not_soir, author);
				break;
			case 1:
				msg = getRdmSentence(greeting_not_jour, author);
			}

			msg += " M'enfin bienvenue à bord!";
		} else {
			switch (heure) {
			case -1:
				msg = getRdmSentence(greeting_soir, author);
				break;
			case 0:
				msg = getRdmSentence(greeting_nope, author);
				break;
			case 1:
				msg = getRdmSentence(greeting_jour, author);
			}
		}

		return msg;
	}

	private String answer(User author, Message message, TextChannel channel) {
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		HashMap<User, Absence> absences = channelInstances.get(channel).absences;
		HashMap<User, MessageForYou> messages = channelInstances.get(channel).messages;
		String msg = message.getContent().toUpperCase();
		String answer = "Hum..";
		Mode mode = new Mode(0);
		Random rand = new Random();

		// BONJOUR
		if (msg.contains("DIS À")) {
			if (message.getMentionedUsers().size() != 1) {
				return "Il m'faut un unique destinataire. J'suis pas un pigeon voyageur magique";
			}

			User dest = message.getMentionedUsers().get(0);
			String sentence = searchSentenceAfter(message.getContent(), "À", dest);
			messages.put(dest, new MessageForYou(author, sentence));
			return "C'est noté, je lui dirai.";
		} else if (greeting(message.getContent().toUpperCase(), mode)) {
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
		} // Nada
		else if (interpelationOnly(msg)) {
			if (rand.nextInt(100) > 20) {
				askings.put(author, new Asking(author, 1));
				return getRdmSentence(answer_interpel, author);
			} else {
				if (author.getName().toUpperCase().equals("FENARO07")) {
					askings.put(author, new Asking(author, 4, "FENARO"));
					return "On m'a dit que tu avais une arme à feu entre les jambes. C'est vrai?";
				}
				answer = "Non. *Tire la langue*";
			}

		} // Quelqu'un?
		else if (msg.contains("IL N'Y A PERSONNE?")) {
			if (absences.isEmpty()) {
				answer = "Il y a moi!";
			} else {
				answer = "Pour l'instant que moi mais";
				for (Absence abs : absences.values()) {
					answer += "\n" + abs;
				}
				answer += " \nVoilà, ça fera " + 30 * absences.size() + " quals.";
			}
		} // T.T
		else if (msg.contains("T.T")) {
			if (rand.nextInt(100) > 75) {
				Bank.retire(-1, author.getId());
				answer = "*Prends pitié de " + getSurname(author, true) + " et lui donne 1 qual*";
			} else
				answer = "*pat pat " + getSurname(author, false) + "*";
		} // Tu tournes
		else if (msg.contains("TU TOURNES")) {
			answer = "Cette fois, c'est pas pour moi! ;)";
		} // Jeu
		else if (msg.contains("PARTIE DE DÉS")) {
			askings.put(author, new Asking(author, 2));
			return "C'est partis pour les dés de la mort! Qui joue?";
		} // Absence
		else if (msg.contains("JE M'ABSENTE")) {
			if (askings.get(author) != null)
				if (askings.get(author).var.equals("THIRD"))
					return "Mais c'était inutile de revenir X)";
			askings.put(author, new Asking(author, 3));
			absences.put(author, new Absence(author));
			return "Oh :/ pourquoi?";
		} else if (msg.contains("NOUVEAU SURNOM POUR")) {
			if (message.getMentionedUsers().size() != 1) {
				if (msg.contains("NOUVEAU SURNOM POUR ALL")) {
					String surnom = searchSentenceAfter(message.getContent(), "ALL", null);
					SurnameStorage.addSurname(surnom, "ALL");
				} else if (message.getMentionedUsers().size() > 1)
					return "Hé oh! Un à la fois! J'suis pas un perroquet!";
				else
					return "C'est bien beau tout ça, mais je sais pas pour qui c'est.";
			} else {
				User dest = message.getMentionedUsers().get(0);
				String surnom = searchSentenceAfter(message.getContent(), "POUR", dest);
				SurnameStorage.addSurname(surnom, dest.getId());
			}
			return "Oki doki, ça marche !";
		} else if (msg.endsWith("?")) {
			// question thibault
			if (msg.contains("ÇA VA?") || msg.contains("CA VA ?")) {
				askings.put(author, new Asking(author, 4, "CAVA"));
				return "Ça va et toi?";
			}
			if (msg.contains("COMBIEN J'AI?") || msg.equals("COMBIEN J'AI ?")) {
				
				answer = "Tu as " + Bank.getSolde(author.getId()) + " quals.";
				if (rand.nextInt(100) > 75){
					answer += " quals. Tu m'en donnes un?";
					askings.put(author, new Asking(author, 4, "COMBIEN"));
				}
				return answer;
			}
			if(msg.contains("COMMANDES?") || msg.contains("COMMANDES ?")){
				answer = "Je ne suis pas un robot! Mais tu peux essayer de me dire quand tu t'absentes, ou de me demander si quelqu'un est là, ou de dire à quelqu'un quelque chose, ou de me donner des idées de surnoms ou regarder plus haut ce que les autres ont fait :P";
				return answer;
			}

			if (author.getName().toUpperCase().equals("THIBAULT")) {
				answer = "Désolé mon petit chou, mais je n'ai pas encore de réponses :/";
			} else
				answer = getRdmSentence(asked_inexist, author);
		} else if (msg.equals("RIEN"))
			answer = "Ben m'embête pas alors!";
		 else
			answer = "Hum, c'est intéressant..";

		return answer;
	}

	private String asking(Asking ask, Message message, TextChannel channel) {
		HashMap<User, Asking> askings = channelInstances.get(channel).askings;
		HashMap<User, Absence> absences = channelInstances.get(channel).absences;
		ArrayList<User> users = channelInstances.get(channel).users;
		String msg = message.getContent().toUpperCase();
		String answer = "";

		switch (ask.mode) {
		case 0: {// Quel Nuha?
			if ((msg.contains("TOI") || msg.contains("PIRATE")) && !msg.contains("PAS TOI")) {
				ask.mode = 1;
				return getRdmSentence(answer_interpel, message.getAuthor());
			} else {
				answer = "Ca ne me concerne pas alors.";
			}

		}
			break;
		case 1: { // Interpelation
			if (msg.contains("TU TOURNES")) {
				answer = "Mooo! *Tourne sur elle même en faisant la moue*";
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
						answer += " " + getSurname(user, false);
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
						answer += " " + getSurname(user, false);
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
					answer = "*Tire une balle dans le coeur de " + getSurname(ask.author, false)
							+ "* \n Fallait t'en tenir à ta parole.";
				else {
					answer = "Bon retour " + getSurname(ask.author, true) + "!";
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

	private String getSurname(User author, boolean canAll) {
		Random rand = new Random();
		String str = "";
		switch (rand.nextInt(4)) {
		case 0:
			str = author.getName();
			break;
		case 1:
		case 2:
			if (canAll)
				str = SurnameStorage.getSurname("ALL");
			break;
		default:
			str = SurnameStorage.getSurname(author.getId());
		}
		if (str.equals(""))
			return author.getName();
		return str;
	}

	private String getRdmSentence(String[] sentences, User destinataire) {
		Random rand = new Random();
		return replaceTag(sentences[rand.nextInt(sentences.length)], destinataire);
	}

	private String replaceTag(String str, User destinataire) {
		return str.replace("#NAME", getSurname(destinataire, true));
	}

	private String searchSentenceAfter(String message, String lastWord, User tag) {
		StringTokenizer st = new StringTokenizer(message);
		String token = "";
		// on avance jusqu'au dernier mot
		while (!token.toUpperCase().equals(lastWord) && st.hasMoreTokens()) {
			token = st.nextToken();
		}

		// on passe le nom s'il y en a un
		if (tag != null) {
			StringTokenizer st2 = new StringTokenizer(tag.getName());
			for (int i = 0; i < st2.countTokens() && st.hasMoreTokens(); i++) {
				st.nextToken();
			}
		}

		// on rassemble le reste.
		token = "";
		while (st.hasMoreTokens()) {
			token += st.nextToken() + " ";
		}

		return token;
	}

}
