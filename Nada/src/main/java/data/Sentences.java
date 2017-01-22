package data;

import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import channelinstance.Absence;
import channelinstance.Asking;
import channelinstance.ChannelInstance;
import channelinstance.MessageForYou;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public abstract class Sentences {
	private final static String ABSENCE = "Oh :/ pourquoi?";
	private final static String ABSENCE_INUTILE = "Mais c'était inutile de revenir X)";
	private final static String SURNOM_OK = "Oki doki, ça marche !";
	private final static String DEST_NONE = "C'est bien beau tout ça, mais je sais pas pour qui c'est.";
	private final static String DES_FURIEUX = "C'est partis pour les dés de la mort! Qui joue?";
	private final static String MESSAGE_OK = "C'est noté, je lui dirai.";
	private final static String PERSONNE_NONE = "Il y a moi!";
	private final static String PERSONNE_AFK =  "Pour l'instant que moi mais";
	private final static String TOURNE_OUI = "Mooo! *Tourne sur elle même en faisant la moue*";
	private final static String TOURNE_NON ="Cette fois, c'est pas pour moi! ;)";
	private final static String TRISTE_PITIE_TAG = "*Prends pitié de #NAME et lui donne 1 qual*";
	private final static String TRISTE_PATPAT_TAG = "*pat pat #NAME.*";
	private final static String ARGENT_COMBIEN_TAG = "Tu as #SOLDE quals.";
	private final static String ARGENT_DONNE = " Tu m'en donnes un?";
	private final static String CAVA = "Ça va et toi?";
	private final static String RIEN = "Ben m'embête pas alors!";
	private final static String ALLSURNAME_NONE = "Il n'en a pas encore et c'est bien dommage!";
	private final static String ALLSURNAME_ILYA = "Alors il y a: ";
	
	public final static String[] DEST_TROP =  {"Il m'faut un unique destinataire. J'suis pas un pigeon voyageur magique", "Hé oh! Un à la fois! J'suis pas un perroquet!"};
	public final static String[] greeting_soir = { "Bonsoir #NAME!", "Bienvenue #NAME.", "On attendait plus que toi!","Oh non pas lui.." };
	public final static String[] greeting_jour = { "Bonjour #NAME!", "Alors #NAME, on est matinal?", "*baille* 'Jour..","'Jour #NAME!" };
	public final static String[] greeting_nope = { "Salut #NAME!", "Heureux d'être accueilli par une magnifique pirate?","#NAME, tu tombes à pic, y a le pont à brosser!", "*sourire accueillant*" };
	public final static String[] answer_interpel = { "Mui?", "C'est moi.", "Héhé, en quoi puis-je t'aider?","Si c'est pour de l'argent, j'en ai que pour moi!" };
	public final static String[] asked_inexist = { "C'est indiscret #NAME.", "Eh bien.. mettons que j'ai une réponse!", ":3","Des fois, je me demande si Miss Fortune me cherche aussi","Demande à Jack Sombrebarbe. Il sait pas tout mais il te tuera peut-être pas.","C'est pas vrai, c'est pas moi, t'as pas de preuve! Tu voulais quoi déjà?" };
	public final static String[] greeting_not_jour = { "On vient tout juste de s'lever #NAME?","Hum.. Dois y avoir du décalage horaire avec Aranor.." };
	public final static String[] greeting_not_soir = { "Il n'est pas si tard que ça! Flemmard!","C'est déjà le matin tu sais?", "That's no moon... That is the sun boy!" };
	
	public final static AffinitySelector amour = new AffinitySelector( new String[]{"Haha!", "Aimer? On a pris la peine de t'apprendre ce verbre?", "Hum.. offre moi des quals, on en reparlera", "Pas encore", "Peut-être", "J't'aime bien", "Je t'adore!", "Faut-il être aveugle pour en douter? Crétin"});
	
	
	public static String getAnswerCommand(Message message, User author, ChannelInstance instance){
		String answer = "Hum, c'est intéressant..";
		Command command =  Command.getCommand(message.getContent());
		if(command == null)
			return answer;
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
		}
		
		return answer;
	}
	
	//CASE ABSENCE
	private static String absence(ChannelInstance instance, User author){
		if (instance.askings.get(author) != null)
			if (instance.askings.get(author).var.equals("THIRD"))
				return ABSENCE_INUTILE;
		instance.askings.put(author, new Asking(author, 3));
		instance.absences.put(author, new Absence(author));
		return ABSENCE;
	}
	
	//CASE JOUER
	private static String jouer(ChannelInstance instance, User author){		
		instance.askings.put(author, new Asking(author, 2));
		return DES_FURIEUX;
	}
	
	//CASE MESSAGE
	private static String messageTo(List<User> dests, ChannelInstance instance, User author, String content){
			if (dests.size() != 1)
				return getRdmSentence(DEST_TROP, author);			
			User dest = dests.get(0);
			String sentence = searchSentenceAfter(content, "À", dest);
			instance.messages.put(dest, new MessageForYou(author, sentence));
			return MESSAGE_OK;
	}
	
	//CASE PERSONNE
	private static String personne( ChannelInstance instance){
		String answer = "";
		if (instance.absences.isEmpty()) {
			answer = PERSONNE_NONE;
		} else {
			answer = PERSONNE_AFK;
			for (Absence abs : instance.absences.values()) {
				answer += "\n" + abs;
			}
			answer += " \nVoilà, ça fera " + 30 * instance.absences.size() + " quals.";
		}
		return answer;
	}
	
	//CASE QUESTION
	private static String question(User author){
		return getRdmSentence(Sentences.asked_inexist, author);
	}
	
	//CASE SURNOM
	private static String surnom(List<User> dests, String content, User author){
		if(dests == null){//ALL
			String surnom = searchSentenceAfter(content, "ALL", null);
			SurnameStorage.addSurname(surnom, "ALL");
		}
		else{ //User
			if (dests.size() > 1)
				return getRdmSentence(DEST_TROP, author);
			else if(dests.size() == 0)
				return DEST_NONE;
			
			User dest = dests.get(0);
			String surnom = searchSentenceAfter(content, "POUR", dest);
			SurnameStorage.addSurname(surnom, dest.getId());
		}
		
		return SURNOM_OK;
	}
	
	//case TOURNES:	
	private static String tournes(ChannelInstance instance, User author){
		if(instance.askings.get(author).mode == 1){
			Affinity.changeAfinity(-2, author.getId());
			return TOURNE_OUI;
		}		
		return TOURNE_NON;
	}
	
	//case TRISTE:	
	private static String triste(User author){
		Random rand = new Random();
		if (rand.nextInt(100) > 75) {
			Bank.retire(-1, author.getId());
			return replaceTag(TRISTE_PITIE_TAG, author);
		} else {			
			Affinity.changeAfinity(2, author.getId());
			return replaceTag(TRISTE_PATPAT_TAG, author);
		}
	}
	
	//case AMOUR:
	private static String amour(User author){
		return Sentences.amour.getFromAffinity(Affinity.getAfinity(author.getId()));
	}
	
	//case ARGENT:	
	private static String argent(ChannelInstance instance, User author){
		String answer = "";
		Random rand = new Random();
		answer = replaceTag(ARGENT_COMBIEN_TAG, author);
		if (rand.nextInt(100) > 80){
			answer += ARGENT_DONNE;
			instance.askings.put(author, new Asking(author, 4, "COMBIEN"));
		}
		return answer;
	}
	
	//case CAVA:	
	private static String cava(ChannelInstance instance, User author){
		instance.askings.put(author, new Asking(author, 4, "CAVA"));
		return CAVA ;
	}
	
	//case COMMANDES:
	private static String commandes(){
		return "Je ne suis pas un robot! Mais tu peux essayer de me dire quand tu t'absentes, ou de me demander si quelqu'un est là, ou de dire à quelqu'un quelque chose, ou de me donner des idées de surnoms ou regarder plus haut ce que les autres ont fait :P";
	}
	
	//case RIEN:
	private static String rien(){
		return RIEN;
	}
	
	//case TOUT LES SURNOMS DE:
	private static String allSurnameOf(List<User> dests, User author){
		if (dests.size() > 1)
				return getRdmSentence(DEST_TROP, author);
		else if (dests.size() == 0)
				return DEST_NONE;
		
		User dest = dests.get(0);
		String str = SurnameStorage.getAllSurname(dest.getId());
		if(str.equals(""))
			return ALLSURNAME_NONE;
		else
			return ALLSURNAME_ILYA + str +".";
		
	}
	
	
	
	//###METHODS UTILS###
		
	public static String getRdmSentence(String[] sentences, User destinataire) {
		Random rand = new Random();
		return replaceTag(sentences[rand.nextInt(sentences.length)], destinataire);
	}
	
	private static String replaceTag(String str, User destinataire) {
		String result =  str.replace("#NAME", getSurname(destinataire, true));
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
	
	private enum Command{
		MESSAGE, PERSONNE, TOURNES, TRISTE, QUESTION, JOUER, ABSENCE, SURNOM, SURNOM_ALL, RIEN, ARGENT, CAVA, COMMANDES, AMOUR, ALLSURNAME;
		
		public static Command getCommand(String str){
			String msg = str.toUpperCase();
			if (msg.contains("DIS À"))
				return MESSAGE;
			if (msg.contains("IL N'Y A PERSONNE?"))
				return PERSONNE;
			if (msg.contains("T.T"))
				return TRISTE;
			if (msg.contains("TU TOURNES"))
				return TOURNES;
			if (msg.contains("JE M'ABSENTE"))
				return ABSENCE;		
			if (msg.contains("NOUVEAU SURNOM POUR ALL"))
				 return SURNOM_ALL;
			 if (msg.contains("NOUVEAU SURNOM POUR"))
				 return SURNOM;
			 if (msg.contains("PARTIE DE DÉS"))
				 return JOUER;
			 if (msg.equals("RIEN"))
				 return RIEN;
			 if (msg.contains("ÇA VA?") || msg.contains("CA VA ?"))
					return CAVA;
			if (msg.contains("COMBIEN J'AI?") || msg.equals("COMBIEN J'AI ?"))
					return ARGENT;
			if(msg.contains("COMMANDES?") || msg.contains("COMMANDES ?"))
				return COMMANDES;
			if(msg.contains("TU M'AIMES?") || msg.contains("TU M'AIMES ?"))
				return AMOUR;
			if (msg.contains("TOUT LES SURNOMS DE"))
				return ALLSURNAME;
			if (msg.endsWith("?"))
				return QUESTION;
			return null;
		}
	}	
	
}
