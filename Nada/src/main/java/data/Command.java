package data;

public enum Command{
	MESSAGE, PERSONNE, TOURNES, TRISTE, QUESTION, JOUER, ABSENCE, SURNOM, SURNOM_ALL, RIEN, ARGENT, CAVA, 
	COMMANDES, AMOUR, ALLSURNAME, BONJOUR, INTERPEL;
	
	public static Command getCommand(String str){
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
		if (msg.endsWith("?"))
			return QUESTION;
		return RIEN;
	}
	
	public static boolean interpelationOnly(String msg) {
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
}	
