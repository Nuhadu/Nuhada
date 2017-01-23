package data;

import java.util.Random;
import java.util.StringTokenizer;

import Main.Mode;
import channelinstance.ChannelInstance;

public enum Command{
	MESSAGE, PERSONNE, TOURNES, TRISTE, QUESTION, JOUER, ABSENCE, SURNOM, SURNOM_ALL, RIEN, ARGENT, CAVA, 
	COMMANDES, AMOUR, ALLSURNAME, BONJOUR, INTERPEL, CADEAU;
	
	public static Command getCommand(String str, Mode mode){
		String msg = str.toUpperCase();
		if (interpelationOnly(str))
			return INTERPEL;
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
		if(msg.contains("JE TE DONNE"))
			return CADEAU;
		
		
		if(isGreeting(msg, mode))
			return BONJOUR;		
		if (msg.endsWith("?"))
			return QUESTION;
		return null;
	}
	
	public static boolean isCommand(String str, ChannelInstance instance){
		Command cmd = getCommand(str, new Mode(0));
		
		if( cmd == null)
			return false;
		
		int i = new Random().nextInt(100);
		//Commandes ne nécessitant pas d'interpelation
		if( (cmd == TRISTE && i > 50) || cmd == INTERPEL || cmd == PERSONNE || cmd == TOURNES || (cmd == BONJOUR && !instance.greated) )
			return true;
		
		if (interpelation(str))
			return true;		
		
		return false;
	}
	
	private static boolean interpelation(String msg) {
		String token = "";
		StringTokenizer tk = new StringTokenizer(msg.toUpperCase());
		while (tk.hasMoreTokens()) {
			token = tk.nextToken();
			if (token.equals("NADA") || token.equals("NADA,"))
				return true;
			if (token.equals("NUHADA")|| token.equals("NUHADA,"))
				return true;
		}
		return false;
	}
	
	public static boolean interpelationOnly(String str) {
		String msg = str.toUpperCase();
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
