package data;

public abstract class Sentences {
	/*ALL SENTENCES CONTAINING TAG NEED TO SPECIFY THE REQUIRED SOURCES
	 *  #NAME AND #SOLDE 	NEED 	_USER
	 *  #ITEM 				NEED	_ITEM
	 */
	
	
	//###ANSWER###
		//A
	public final static String ABSENCE = "Oh :/ pourquoi?";
	public final static String ABSENCE_INUTILE = "Mais c'était inutile de revenir X)";
	public final static String ALLSURNAME_ILYA = "Alors il y a: ";
	public final static String ALLSURNAME_NONE = "Il n'en a pas encore et c'est bien dommage!";	
	public final static String ARGENT_COMBIEN_USER = "Tu as #SOLDE quals.";
	public final static String ARGENT_DONNE = " Tu m'en donnes un?";
	public final static String ARGENT_MOQUE = "Haha, t'es trop pauvre pour ça!";
	public final static String ASKED_INEXIST_THIB = "Désolé mon chou, mais je n'ai pas de réponses :/";
		//C
	public final static String CADEAU_INEXIST = "Tu te moques de moi?";
	public final static String CADEAU_HAVENOT = "Tu ne devrais pas proposer ce que tu n'as pas.";
	public final static String CADEAU_TROP_ITEM = "Profites en pour me débarasser de ça. *Donne #ITEM.* J'en ai beaucoup trop.";
	public final static String CAVA = "Ça va et toi?";
		//D
	public final static String DEST_NONE = "C'est bien beau tout ça, mais je sais pas pour qui c'est.";
	public final static String DES_FURIEUX = "C'est partis pour les dés de la mort! Qui joue?";
		//E
	public final static String EAT_POMME_NADA = "*Nuhada mange la pomme joyeusement.*";
		//G	
	public final static String GREETINGBACK_MENFIN = " M'enfin bienvenue à bord!";
		//I
	public final static String INTERPEL_NO = "Non. *Tire la langue*";
	public final static String INTERPEL_FENARO = "On m'a dit que tu avais une arme à feu entre les jambes. C'est vrai?";
		//J
	public final static String JEU_MANQUE = "Il manque";
		//M	
	public final static String MERCI_USER = "Merci #NAME.";
	public final static String MESSAGEFOR_HEY_USER = "Hey #NAME! ";
	public final static String MESSAGE_OK = "C'est noté, je lui dirai.";
		//P	
	public final static String PERSONNE_NONE = "Il y a moi!";
	public final static String PERSONNE_AFK =  "Pour l'instant que moi mais";
	public final static String PREND_UNIQUEF_NADA ="Une suffira.";
		//T
	public final static String TOURNE_OUI = "Mooo! *Tourne sur elle même en faisant la moue*";
	public final static String TOURNE_NON ="Cette fois, c'est pas pour moi! ;)";
	public final static String TRISTE_PITIE_USER_ITEM = "*Prends pitié de #NAME et lui donne #ITEM.*";
	public final static String TRISTE_PATPAT_USER = "*pat pat #NAME.*";
		//R	
	public final static String RIEN = "Ben m'embête pas alors!";
		//S	
	public final static String SURNOM_OK = "Oki doki, ça marche !";
	
	//###ASK###
		//B
	public final static String ASK_BAD = "_BAD";
		//C
	public final static String ASK_CAVA = "CAVA";
	public final static String ASK_COMBIEN = "COMBIEN";
		//F
	public final static String ASK_FENARO_OUI = "Il faudra me montrer ça!";
	public final static String ASK_FENARO_NON = "Dommage, tu aurais pu être revendue un bon prix avec ça.";
	public final static String ASK_FENARO_OTHER = "Haha :D";
	public final static String ASK_FENARO_ANYWAY = "Sinon tu voulais quelque chose?";
		//I
	public final static String ASK_INTERPEL_TOURNES = "Mooo! *Tourne sur elle même en faisant la moue*";
		//J
	public final static String ASK_JEU_ANNULE = "Oh :/";
	public final static String ASK_JEU_GO_ANNULE = "Go? ou Annule?";
	public final static String ASK_JEU_JOUEURS = "J'ai les joueurs:";
	public final static String ASK_JEU_LANCE = "Je lance le jeu!";
	public final static String ASK_JEU_NOT_ENOUGH = "Pas assez de joueurs, qui d'autre?";	
	public final static String ASK_JEU_PAS_JOUEUR = "J'ai demandé des joueurs, pas des fantômes.\n";
		//M
	public final static String ASK_MECHANT = "Méchant :/";
		//N
	public final static String ASK_NONE = "";
		//Q
	public final static String ASK_QUESTION_MERCI = "Héhé, je te le revaudrai!";
		//R
	public final static String ASK_REASON_NO = "Dis moi au moins quand tu reviens! >_<*";
	public final static String ASK_REASON_YES = "Et tu reviens quand?";
	public final static String ASK_RETOUR_BAD_USER = "*Tire une balle dans le coeur de #NAME* \n Fallait t'en tenir à ta parole.";
	public final static String ASK_RETOUR_NO = "Ben tant mieux! D'abord !";	
	public final static String ASK_RETOUR_USER = "Bon retour #NAME!";
	public final static String ASK_RETOUR_YES = "Ca marche! En route pour l'aventure!";	
		//S
	public final static String ASK_SECOND = "SECOND";
		//T
	public final static String ASK_THIRD = "THIRD";
	
		
	//###MULTIPLE###
		//A
	public final static String[] ANSWER_INTERPEL = { "Mui?", "C'est moi.", "Héhé, en quoi puis-je t'aider?","Si c'est pour de l'argent, j'en ai que pour moi!" };
	public final static String[] ASKED_INEXIST = { "C'est indiscret #NAME.", "Eh bien.. mettons que j'ai une réponse!", ":3","Des fois, je me demande si Miss Fortune me cherche aussi","Demande à Jack Sombrebarbe. Il sait pas tout mais il te tuera peut-être pas.","C'est pas vrai, c'est pas moi, t'as pas de preuve! Tu voulais quoi déjà?" };
		//D
	public final static String[] DEST_TROP =  {"Il m'faut un unique destinataire. J'suis pas un pigeon voyageur magique", "Hé oh! Un à la fois! J'suis pas un perroquet!"};
		//G
	public final static String[] GREETING_JOUR = { "Bonjour #NAME!", "Alors #NAME, on est matinal?", "*baille* 'Jour..","'Jour #NAME!" };
	public final static String[] GREETING_NOPE = { "Salut #NAME!", "Heureux d'être accueilli par une magnifique pirate?","#NAME, tu tombes à pic, y a le pont à brosser!", "*sourire accueillant*" };
	public final static String[] GREETING_NOT_JOUR = { "On vient tout juste de s'lever #NAME?","Hum.. Dois y avoir du décalage horaire avec Aranor.." };
	public final static String[] GREETING_NOT_SOIR = { "Il n'est pas si tard que ça! Flemmard!","C'est déjà le matin tu sais?", "That's no moon... That is the sun boy!" };
	public final static String[] GREETING_SOIR = { "Bonsoir #NAME!", "Bienvenue #NAME.", "On attendait plus que toi!","Oh non pas lui.." };
	
	
	//###AFFINITY SELECTORS###
		//A
	public final static AffinitySelector AMOUR_AFFINITY = new AffinitySelector( new String[]{"Haha!", "Aimer? On a pris la peine de t'apprendre ce verbre?", "Hum.. offre moi des quals, on en reparlera", "Pas encore", "Peut-être", "J't'aime bien", "Je t'adore!", "Faut-il être aveugle pour en douter? Crétin"});
		//C
	public final static AffinitySelector CAVA_AFFINITY = new AffinitySelector(new Integer[] {10, 30, 50, 70}, new String[] {"C'est pas comme si ça m'intéressait de toute façon.", "Vraiment *baille* passionnant", "Oh!", "Dans tout les cas, je suis contente que tu sois là!"});
	
}
