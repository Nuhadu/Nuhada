package data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import Main.Mode;
import channelinstance.Absence;
import channelinstance.Asking;
import channelinstance.Asking.AskType;
import data.Affinity;
import channelinstance.ChannelInstance;
import channelinstance.MessageForYou;
import jeux.DesFurieux;
import jeux.Jeux;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public abstract class Answers {

	
	public static String getAnswerCommand(Message message, User author, ChannelInstance instance){
		String answer = "Hum, c'est intéressant..";
		Mode mode = new Mode(0);
		Command command =  Command.getCommand(message.getContent(), mode);
		if(command == null){			
			return answer;
		}
		
		switch(command){
		case ABSENCE:
			answer = absence(instance, author);
			break;
		case JOUER:
			answer = jouer(instance, author);
			break;
		case MESSAGE:
			answer = messageTo(message.getMentionedUsers(), instance, author, message.getContent());
			break;
		case PERSONNE:
			answer = personne(instance);
			break;
		case QUESTION:
			answer = question(author);
			break;
		case RIEN:
			answer = rien();
			break;
		case SURNOM_ALL:
			answer = surnom(null, message.getContent(), author);
			break;
		case SURNOM:
			answer = surnom(message.getMentionedUsers(), message.getContent(), author);
			break;
		case TOURNES:
			answer = tournes(instance, author);
			break;
		case TRISTE:
			answer = triste(author);
			break;
		case AMOUR:
			answer = amour(author);
			break;
		case ARGENT:
			answer = argent(instance, author);
			break;
		case CAVA:
			answer = cava(instance, author);
			break;
		case COMMANDES:
			answer = commandes();
			break;
		case ALLSURNAME:
			answer = allSurnameOf(message.getMentionedUsers(), author);
			break;
		case BONJOUR:
			answer = greeting(message.getContent(), author, instance, mode);
			break;
		case INTERPEL:
			answer = interpel(message.getContent(), author, instance);
			break;
		}
		
		
		return answer;
	}
	
	public static String asking(Asking ask, Message message, ChannelInstance instance) {
		String answer = "";
		switch(ask.mode){
		case ABSENCE:
			answer = ask_absence(ask,message, instance);
			break;
		case INTERPEL:
			answer = ask_interpel(ask, message, instance);
			break;
		case JEU:
			answer = ask_jeu(ask, message, instance);
			break;
		case QUESTION:
			answer = ask_question(ask, message.getContent(), instance);
			break;
		default:
			break;
		
		}
		
		return answer;
	}
	
	private static String ask_absence(Asking ask, Message message, ChannelInstance instance){
		String msg = message.getContent().toUpperCase();
		if (ask.var.equals("")) {
			ask.var = Sentences.ASK_SECOND;
			if (msg.contains("NON")) {
				return Sentences.ASK_REASON_NO;
			} else {
				instance.absences.get(ask.author).changeReason(message.getContent());
				return Sentences.ASK_REASON_YES;
			}
		} else if (ask.var.equals(Sentences.ASK_SECOND)) {
			if (msg.contains("JAMMAIS")) {
				ask.var = Sentences.ASK_THIRD + Sentences.ASK_BAD;
				return  Sentences.ASK_RETOUR_NO;
			} else {
				ask.var = Sentences.ASK_THIRD;
				instance.absences.get(ask.author).changeRetour(message.getContent());
				return Sentences.ASK_RETOUR_YES;
			}
		} else if (ask.var.contains(Sentences.ASK_THIRD)) {
			instance.absences.remove(ask.author);
			if (ask.var.contains(Sentences.ASK_BAD)){
				return replaceTag(Sentences.ASK_RETOUR_BAD_TAG, ask.author);
			}
			else {
				return replaceTag(Sentences.ASK_RETOUR_TAG, ask.author) + "\n" + getAnswerCommand(message, ask.author, instance);
			}
		}		
		return "";
	}
	
	private static String ask_interpel(Asking ask, Message message, ChannelInstance instance){
		if (Command.getCommand(message.getContent(), new Mode(0)) == Command.TOURNES) {
			instance.askings.remove(ask.author);
			Affinity.changeAfinity(-2, ask.author.getId());
			return Sentences.ASK_INTERPEL_TOURNES;			
		} else {
			instance.askings.remove(ask.author);
			return getAnswerCommand( message,ask.author, instance);
		}
	}
	
	private static String ask_jeu(Asking ask, Message message, ChannelInstance instance){
		String msg = message.getContent().toUpperCase();
		if (ask.var.equals(Sentences.ASK_NONE)) {// qui sont les joueurs
			if (msg.equals("GO")) {
				if (instance.users.size() < 2)
					return Sentences.ASK_JEU_NOT_ENOUGH;
				else {
					instance.jeu = new DesFurieux(instance.users);
					instance.askings.remove(ask.author);
					return Sentences.ASK_JEU_LANCE  + "\n" + gameConducted(ask.author, message.getContent(), instance);
				}
			} else if(msg.equals("ANNULE")){
				instance.askings.remove(ask.author);
				instance.users = null;
				return Sentences.ASK_JEU_ANNULE;
			}
			else if(msg.equals("MOI")){
				String answer = Sentences.ASK_JEU_JOUEURS;
				for (User user : instance.users)
					answer += " " + getSurname(user, false);
				return answer += "\n" + Sentences.ASK_JEU_GO_ANNULE ;
			} else {
				String answer = "";
				if (message.getMentionedUsers().size() < 1)
					answer = Sentences.ASK_JEU_PAS_JOUEUR;
				else {
					for (User user : message.getMentionedUsers())
						if (!instance.users.contains(user)) {
							instance.users.add(user);
						}
				}
				answer +=  Sentences.ASK_JEU_JOUEURS;
				for (User user : instance.users)
					answer += " " + getSurname(user, false);
				return answer += "\n" + Sentences.ASK_JEU_GO_ANNULE;
			}
		}
		return "";
	}
	
	private static String ask_question(Asking ask, String content, ChannelInstance instance){
		String msg = content.toUpperCase();
		if (ask.var.equals("CAVA")) {
			instance.askings.remove(ask.author);
			return Sentences.CAVA_AFFINITY.getFromAffinity(Affinity.getAfinity(ask.author.getId()));
		} else if (ask.var.equals(Sentences.ASK_COMBIEN)) {
			instance.askings.remove(ask.author);
			if (msg.equals("OUI")) {
				if (Bank.retire(1, ask.author.getId())) {
					Affinity.changeAfinity(1, ask.author.getId());
					return Sentences.ASK_QUESTION_MERCI;
				} else
					return Sentences.ASK_QUESTION_MOQUE;
			} else {
				return Sentences.ASK_MECHANT;
			}

		} else if (ask.author.getId().equals("194133620418543616")) {
			String answer = "";
			if (msg.contains("OUI")) {
				 answer = Sentences.ASK_FENARO_OUI;
			} else if (msg.contains("NON")) {
				 answer = Sentences.ASK_FENARO_NON;
			} else {
				 answer = Sentences.ASK_FENARO_OTHER;
			}
			ask.mode = AskType.INTERPEL;
			ask.var = "";
			return answer + "\n" +Sentences.ASK_FENARO_ANYWAY;
		}
		
		return "";
	}	
	
	public static String gameConducted(User author, String message, ChannelInstance instance) {
		Jeux jeu = instance.jeu;
		String msg = message.toUpperCase();
		String answer = "";

		if (!jeu.initialized()) {
			return jeu.initialize();
		} else if (!jeu.isStarted()) {
			if (msg.equals("PRÊT")) {
				boolean go = jeu.userPrepare(author);
				if (go) {
					return jeu.play();
				} else {
					return Sentences.JEU_MANQUE + jeu.userEnAttente();
				}
			} else if (msg.equals("RÈGLES")) {
				return jeu.regle();
			}
		} else if (jeu.seekCommand()) {
			return jeu.receive(message, author);
		} else if (jeu.ended())
			instance.jeu = null;

		return answer;
	}
	
	//CASE ABSENCE
	private static String absence(ChannelInstance instance, User author){
		if (instance.askings.get(author) != null)
			if (instance.askings.get(author).var.equals("THIRD"))
				return Sentences.ABSENCE_INUTILE;
		instance.askings.put(author, new Asking(author, AskType.ABSENCE));
		instance.absences.put(author, new Absence(author));
		return Sentences.ABSENCE;
	}
	
	//CASE JOUER
	private static String jouer(ChannelInstance instance, User author){		
		instance.askings.put(author, new Asking(author, AskType.JEU));
		instance.users = new ArrayList<User>();
		instance.users.add(author);
		return Sentences.DES_FURIEUX;
	}
	
	//CASE MESSAGE
	private static String messageTo(List<User> dests, ChannelInstance instance, User author, String content){
			if (dests.size() != 1)
				return getRdmSentence(Sentences.DEST_TROP, author);			
			User dest = dests.get(0);
			String sentence = searchSentenceAfter(content, "À", dest);
			instance.messages.put(dest, new MessageForYou(author, sentence));
			return Sentences.MESSAGE_OK;
	}
	
	//CASE PERSONNE
	private static String personne( ChannelInstance instance){
		String answer = "";
		if (instance.absences.isEmpty()) {
			answer = Sentences.PERSONNE_NONE;
		} else {
			answer = Sentences.PERSONNE_AFK;
			for (Absence abs : instance.absences.values()) {
				answer += "\n" + abs;
			}
			answer += " \nVoilà, ça fera " + 30 * instance.absences.size() + " quals.";
		}
		return answer;
	}
	
	//CASE QUESTION
	private static String question(User author){
		if(	author.getId().equals("147045778542690304") )
			return Sentences.ASKED_INEXIST_THIB;
		return getRdmSentence(Sentences.ASKED_INEXIST, author);
	}
	
	//CASE SURNOM
	private static String surnom(List<User> dests, String content, User author){
		if(dests == null){//ALL
			String surnom = searchSentenceAfter(content, "ALL", null);
			SurnameStorage.addSurname(surnom, "ALL");
		}
		else{ //User
			if (dests.size() > 1)
				return getRdmSentence(Sentences.DEST_TROP, author);
			else if(dests.size() == 0)
				return Sentences.DEST_NONE;
			
			User dest = dests.get(0);
			String surnom = searchSentenceAfter(content, "POUR", dest);
			SurnameStorage.addSurname(surnom, dest.getId());
		}
		
		return Sentences.SURNOM_OK;
	}
	
	//case TOURNES:	
	private static String tournes(ChannelInstance instance, User author){
		if(instance.askings.get(author).mode == AskType.INTERPEL){
			Affinity.changeAfinity(-2, author.getId());
			return Sentences.TOURNE_OUI;
		}		
		return Sentences.TOURNE_NON;
	}
	
	//case TRISTE:	
	private static String triste(User author){
		Random rand = new Random();
		if (rand.nextInt(100) > 75) {
			Bank.retire(-1, author.getId());
			return replaceTag(Sentences.TRISTE_PITIE_TAG, author);
		} else {			
			Affinity.changeAfinity(2, author.getId());
			return replaceTag(Sentences.TRISTE_PATPAT_TAG, author);
		}
	}
	
	//case AMOUR:
	private static String amour(User author){
		return Sentences.AMOUR_AFFINITY.getFromAffinity(Affinity.getAfinity(author.getId()));
	}
	
	//case ARGENT:	
	private static String argent(ChannelInstance instance, User author){
		String answer = "";
		Random rand = new Random();
		answer = replaceTag(Sentences.ARGENT_COMBIEN_TAG, author);
		if (rand.nextInt(100) > 80){
			answer += Sentences.ARGENT_DONNE;
			instance.askings.put(author, new Asking(author, AskType.QUESTION, "COMBIEN"));
		}
		return answer;
	}
	
	//case CAVA:	
	private static String cava(ChannelInstance instance, User author){
		instance.askings.put(author, new Asking(author, AskType.QUESTION, "CAVA"));
		return Sentences.CAVA ;
	}
	
	//case COMMANDES:
	private static String commandes(){
		return "Je ne suis pas un robot! Mais je vais essayer de faire ça la prochaine fois";
	}
	
	//case RIEN:
	private static String rien(){
		return Sentences.RIEN;
	}
	
	//case TOUT LES SURNOMS DE:
	private static String allSurnameOf(List<User> dests, User author){
		if (dests.size() > 1)
				return getRdmSentence(Sentences.DEST_TROP, author);
		else if (dests.size() == 0)
				return Sentences.DEST_NONE;
		
		User dest = dests.get(0);
		String str = SurnameStorage.getAllSurname(dest.getId());
		if(str.equals(""))
			return Sentences.ALLSURNAME_NONE;
		else
			return Sentences.ALLSURNAME_ILYA + str +".";
		
	}
	
	//case greeting
	private static String greeting(String content, User author, ChannelInstance instance, Mode mode){
		String answer = greetingBack(author, mode);
		instance.greated = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				instance.greated = false;
			}
		}).start();
		return answer;
		
	}
	
	
	//case interpel	
	private static String interpel(String content, User author, ChannelInstance instance){
		Random rand = new Random();
		if (rand.nextInt(100) > 20) {
			instance.askings.put(author, new Asking(author, AskType.INTERPEL));
			return getRdmSentence(Sentences.ANSWER_INTERPEL, author);
		} else {
			if (author.getName().toUpperCase().equals("FENARO07")) {
				instance.askings.put(author, new Asking(author, AskType.QUESTION, "FENARO"));
				return Sentences.INTERPEL_FENARO;
			}
			return Sentences.INTERPEL_NO;
		}
	}
	
	//###METHODS UTILS###
	
	
	
	private static String greetingBack(User author, Mode mode) {
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
				msg = getRdmSentence(Sentences.GREETING_NOT_SOIR, author);
				break;
			case 1:
				msg = getRdmSentence(Sentences.GREETING_NOT_JOUR, author);
			}

			msg += Sentences.GREETINGBACK_MENFIN;
		} else {
			switch (heure) {
			case -1:
				msg = getRdmSentence(Sentences.GREETING_SOIR, author);
				break;
			case 0:
				msg = getRdmSentence(Sentences.GREETING_NOPE, author);
				break;
			case 1:
				msg = getRdmSentence(Sentences.GREETING_JOUR, author);
			}
		}
		return msg;
	}
	
	
	
	public static String getRdmSentence(String[] sentences, User destinataire) {
		Random rand = new Random();
		return replaceTag(sentences[rand.nextInt(sentences.length)], destinataire, true);
	}
	
	private static String replaceTag(final String str, User destinataire) {
		String result = ""+str;
		if(str.contains("#NAME"))
			result =  str.replace("#NAME", getSurname(destinataire, true));
		if(str.contains("#SOLDE"))
			result = result.replace("#SOLDE", "" +Bank.getSolde(destinataire.getId()));
		return result;
	}
	
	private static String replaceTag(final String str, User destinataire, boolean canAll) {
		String result = ""+str;
		if(str.contains("#NAME"))
			result =  str.replace("#NAME", getSurname(destinataire, canAll));
		if(str.contains("#SOLDE"))
			result = result.replace("#SOLDE", "" +Bank.getSolde(destinataire.getId()));
		return result;
	}
	
	public static String getSurname(User author, boolean canAll) {
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
	
	public static String searchSentenceAfter(String message, String lastWord, User tag) {
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
	
	public static String messageFor(User dest, MessageForYou msg){
		return replaceTag(Sentences.MESSAGEFOR_HEY, dest) + replaceTag(msg.toString(),msg.author);
	}
}
