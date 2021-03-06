package data;

import java.util.Random;
import java.util.StringTokenizer;

import Main.Mode;
import channelinstance.ChannelInstance;

public enum Command{
	MESSAGE, PERSONNE, TOURNES, TRISTE, QUESTION, JOUER, ABSENCE, SURNOM, SURNOM_ALL, RIEN, ARGENT, CAVA, 
	COMMANDES, AMOUR, ALLSURNAME, BONJOUR, INTERPEL, CADEAU, INVENTAIRE;
	
	public static Command getCommand(String str, Mode mode){
		String msg = str.toUpperCase();
		if (interpelationOnly(str))
			return INTERPEL;
		
		msg = withoutInterpelation(msg);
		//EQUALS
		if (msg.equals("T.T"))
			return TRISTE;
		if (msg.equals("TU TOURNES"))
			return TOURNES;
		if (msg.equals("JE M'ABSENTE"))
			return ABSENCE;		
		if (msg.equals("NOUVEAU SURNOM POUR ALL"))
			 return SURNOM_ALL;
		if (msg.equals("PARTIE DE DÉS"))
			 return JOUER;
		 if (msg.equals("RIEN"))
			 return RIEN;
		 if (msg.equals("ÇA VA?") || msg.equals("CA VA ?"))
			return CAVA;
		 if (msg.equals("COMBIEN J'AI?") || msg.equals("COMBIEN J'AI ?"))
			return ARGENT;
		 if(msg.equals("COMMANDES?") || msg.equals("COMMANDES ?"))
			return COMMANDES;
		 if(msg.equals("TU M'AIMES?") || msg.equals("TU M'AIMES ?"))
			return AMOUR;
		 if(msg.equals("INVENTAIRE") || msg.equals("QU'EST-CE QUE J'AI?") || msg.equals("QU'EST-CE QUE J'AI ?"))
			 return INVENTAIRE;
		 //BEGIN WITH
		if (msg.startsWith("DIS À"))
			return MESSAGE;
		if (msg.startsWith("IL N'Y A PERSONNE?"))
			return PERSONNE;
		 if (msg.startsWith("NOUVEAU SURNOM POUR"))
			 return SURNOM;
		if (msg.startsWith("TOUT LES SURNOMS DE"))
			return ALLSURNAME;
		if(msg.startsWith("JE TE DONNE"))
			return CADEAU;
		
		//PARTICULAR
		if(isGreeting(msg, mode))
			return BONJOUR;		
		if (msg.endsWith("?"))
			return QUESTION;
		return null;
	}
	
	public static boolean isCommand(String str, ChannelInstance instance){
		Command cmd = getCommand(str, new Mode(0));
		
		if (interpelation(str))
			return true;
		
		int i = new Random().nextInt(100);
		//Commandes ne nécessitant pas d'interpelation
		if( (cmd == TRISTE && i > 50) || cmd == INTERPEL || cmd == PERSONNE || cmd == TOURNES || (cmd == BONJOUR && !instance.greated) )
			return true;

		
		return false;
	}
	
	private static boolean interpelation(String msg) {
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg.toUpperCase());
		if (tk.hasMoreTokens()) {
			token = tk.nextToken();
			if (token.equals("NADA") || token.equals("NADA,"))
				return true;
			if (token.equals("NUHADA")|| token.equals("NUHADA,"))
				return true;
			if(token.equals("NA!")|| token.equals("NA!,"))
				return true;
		}
		return false;
	}
	
	private static String withoutInterpelation(String msg){
		String result = "";
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg.toUpperCase());
		while (tk.hasMoreTokens()) {
			token = tk.nextToken();
			if (token.equals("NADA") || token.equals("NADA,"))
				continue;
			if (token.equals("NUHADA")|| token.equals("NUHADA,"))
				continue;
			if(token.equals("NA!")|| token.equals("NA!,"))
				continue;
			if(!result.equals(""))
				result += " ";
			result += token;
		}
		return result;
	}
	
	public static boolean interpelationOnly(String str) {
		String msg = str.toUpperCase();
		if (msg.equals("NADA"))
			return true;
		if (msg.equals("NUHADA"))
			return true;
		if (msg.equals("NA!"))
			return true;

		if (msg.equals("NADA!"))
			return true;
		if (msg.equals("NUHADA!"))
			return true;		

		if (msg.equals("NADA?"))
			return true;
		if (msg.equals("NUHADA?"))
			return true;
		if (msg.equals("NA!?"))
			return true;

		if (msg.equals("NADA ?"))
			return true;
		if (msg.equals("NUHADA ?"))
			return true;
		if (msg.equals("NA! ?"))
			return true;

		if (msg.equals("NADA !"))
			return true;
		if (msg.equals("NUHADA !"))
			return true;
		
		if (msg.equals("NADA,"))
			return true;
		if (msg.equals("NUHADA,"))
			return true;
		if (msg.equals("NA!,"))
			return true;
		
		return false;
	}
	
	public static boolean isGreeting(String msg, Mode mode) {
		
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg);
		if (tk.hasMoreTokens()) {
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
}	
