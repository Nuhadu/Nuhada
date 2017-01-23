package data;

public class Sentences {
	//ANSWER
	public final static String ABSENCE = "Oh :/ pourquoi?";
	public final static String ABSENCE_INUTILE = "Mais c'était inutile de revenir X)";
	public final static String SURNOM_OK = "Oki doki, ça marche !";
	public final static String DEST_NONE = "C'est bien beau tout ça, mais je sais pas pour qui c'est.";
	public final static String DES_FURIEUX = "C'est partis pour les dés de la mort! Qui joue?";
	public final static String MESSAGE_OK = "C'est noté, je lui dirai.";
	public final static String PERSONNE_NONE = "Il y a moi!";
	public final static String PERSONNE_AFK =  "Pour l'instant que moi mais";
	public final static String TOURNE_OUI = "Mooo! *Tourne sur elle même en faisant la moue*";
	public final static String TOURNE_NON ="Cette fois, c'est pas pour moi! ;)";
	public final static String TRISTE_PITIE_TAG = "*Prends pitié de #NAME et lui donne 1 qual*";
	public final static String TRISTE_PATPAT_TAG = "*pat pat #NAME.*";
	public final static String ARGENT_COMBIEN_TAG = "Tu as #SOLDE quals.";
	public final static String ARGENT_DONNE = " Tu m'en donnes un?";
	public final static String CAVA = "Ça va et toi?";
	public final static String RIEN = "Ben m'embête pas alors!";
	public final static String ALLSURNAME_NONE = "Il n'en a pas encore et c'est bien dommage!";
	public final static String ALLSURNAME_ILYA = "Alors il y a: ";
	public final static String INTERPEL_NO = "Non. *Tire la langue*";
	public final static String INTERPEL_FENARO = "On m'a dit que tu avais une arme à feu entre les jambes. C'est vrai?";
	public final static String GREETINGBACK_MENFIN = " M'enfin bienvenue à bord!";
	public final static String MESSAGEFOR_HEY = "Hey #NAME! ";
	public final static String ASKED_INEXIST_THIB = "Désolé mon chou, mais je n'ai pas de réponses :/";
	//ASK
	public final static String ASK_NONE = "";
	public final static String ASK_SECOND = "SECOND";
	public final static String ASK_THIRD = "THIRD";
	public final static String ASK_BAD = "_BAD";
	public final static String ASK_REASON_NO = "Dis moi au moins quand tu reviens! >_<*";
	public final static String ASK_REASON_YES = "Et tu reviens quand?";
	public final static String ASK_RETOUR_NO = "Ben tant mieux! D'abord !";
	public final static String ASK_RETOUR_YES = "Ca marche! En route pour l'aventure!";
	public final static String ASK_RETOUR_BAD_TAG = "*Tire une balle dans le coeur de #NAME* \n Fallait t'en tenir à ta parole.";
	public final static String ASK_RETOUR_TAG = "Bon retour #NAME!";
	public final static String ASK_INTERPEL_TOURNES = "Mooo! *Tourne sur elle même en faisant la moue*";
	public final static String ASK_JEU_NOT_ENOUGH = "Pas assez de joueurs, qui d'autre?";	
	public final static String ASK_JEU_LANCE = "Je lance le jeu!";
	public final static String ASK_JEU_ANNULE = "Oh :/";
	public final static String ASK_JEU_JOUEURS = "J'ai les joueurs:";
	public final static String ASK_JEU_GO_ANNULE = "Go? ou Annule?";
	public final static String ASK_JEU_PAS_JOUEUR = "J'ai demandé des joueurs, pas des fantômes.\n";
	public final static String ASK_CAVA = "CAVA";
	public final static String ASK_COMBIEN = "COMBIEN";
	public final static String ASK_QUESTION_MERCI = "Héhé, je te le revaudrai!";
	public final static String ASK_QUESTION_MOQUE = "Haha, t'es trop pauvre pour ça!";
	public final static String ASK_MECHANT = "Méchant :/";
	public final static String ASK_FENARO_OUI = "Il faudra me montrer ça!";
	public final static String ASK_FENARO_NON = "Dommage, tu aurais pu être revendue un bon prix avec ça.";
	public final static String ASK_FENARO_OTHER = "Haha :D";
	public final static String ASK_FENARO_ANYWAY = "Sinon tu voulais quelque chose?";
	public final static String JEU_MANQUE = "Il manque";
	//MULTIPLE
	public final static String[] DEST_TROP =  {"Il m'faut un unique destinataire. J'suis pas un pigeon voyageur magique", "Hé oh! Un à la fois! J'suis pas un perroquet!"};
	public final static String[] GREETING_SOIR = { "Bonsoir #NAME!", "Bienvenue #NAME.", "On attendait plus que toi!","Oh non pas lui.." };
	public final static String[] GREETING_JOUR = { "Bonjour #NAME!", "Alors #NAME, on est matinal?", "*baille* 'Jour..","'Jour #NAME!" };
	public final static String[] GREETING_NOPE = { "Salut #NAME!", "Heureux d'être accueilli par une magnifique pirate?","#NAME, tu tombes à pic, y a le pont à brosser!", "*sourire accueillant*" };
	public final static String[] ANSWER_INTERPEL = { "Mui?", "C'est moi.", "Héhé, en quoi puis-je t'aider?","Si c'est pour de l'argent, j'en ai que pour moi!" };
	public final static String[] ASKED_INEXIST = { "C'est indiscret #NAME.", "Eh bien.. mettons que j'ai une réponse!", ":3","Des fois, je me demande si Miss Fortune me cherche aussi","Demande à Jack Sombrebarbe. Il sait pas tout mais il te tuera peut-être pas.","C'est pas vrai, c'est pas moi, t'as pas de preuve! Tu voulais quoi déjà?" };
	public final static String[] GREETING_NOT_JOUR = { "On vient tout juste de s'lever #NAME?","Hum.. Dois y avoir du décalage horaire avec Aranor.." };
	public final static String[] GREETING_NOT_SOIR = { "Il n'est pas si tard que ça! Flemmard!","C'est déjà le matin tu sais?", "That's no moon... That is the sun boy!" };
	//AFFINITY SELECTORS
	public final static AffinitySelector AMOUR_AFFINITY = new AffinitySelector( new String[]{"Haha!", "Aimer? On a pris la peine de t'apprendre ce verbre?", "Hum.. offre moi des quals, on en reparlera", "Pas encore", "Peut-être", "J't'aime bien", "Je t'adore!", "Faut-il être aveugle pour en douter? Crétin"});
	public final static AffinitySelector CAVA_AFFINITY = new AffinitySelector(new Integer[] {10, 30, 50, 70}, new String[] {"C'est pas comme si ça m'intéressait de toute façon.", "Vraiment *baille* passionnant", "Oh!", "Dans tout les cas, je suis contente que tu sois là!"});
	
}
