package jeux;

import java.util.ArrayList;

import net.dv8tion.jda.core.entities.User;

public abstract class Jeux {

	protected ArrayList<User> joueurs;
	protected String regles;
	protected boolean[] preparation;

	protected boolean start;
	protected boolean initialize;
	protected boolean seekCommand;
	protected boolean ended;

	public Jeux(ArrayList<User> joueurs, String regles) {
		this.joueurs = joueurs;
		this.regles = regles;
		this.preparation = new boolean[joueurs.size()];
		for (int i = 0; i < joueurs.size(); i++)
			preparation[i] = false;
		start = false;
		initialize = false;
		seekCommand = false;
		ended = false;
	}

	public String initialize() {
		return "Le jeu est prêt! Voulez vous lire les 'règles' ou êtes vous 'prêt'?";
	}

	public abstract String play();

	public abstract String endTurn();

	public String endGame() {
		seekCommand = false;
		ended = true;
		return "C'est la fin! Merci d'avoir joué <3";
	}

	public String receive(String msg, User author) {
		if (msg.toUpperCase().equals("STOP THAT SHIT"))
			return endGame();
		else
			return "J'ai l'impression que ce n'est pas une commande :3";
	}

	public boolean concerned(String msg, User author) {
		if (msg.equals("PRÊT"))
			return true;
		if (msg.equals("RÈGLES"))
			return true;
		if (msg.equals("STOP THAT SHIT"))
			return true;
		return false;
	}

	public String regle() {
		return regles;
	}

	public User joueurI(int i) {
		return joueurs.get(i);
	}

	public boolean userPrepare(User user) {
		preparation[joueurs.indexOf(user)] = true;
		for (int i = 0; i < joueurs.size(); i++)
			if (preparation[i] == false)
				return false;

		return true;
	}

	public String userEnAttente() {
		String str = "";
		for (int i = 0; i < joueurs.size(); i++)
			if (!preparation[i])
				str += " " + joueurs.get(i).getName();
		return str;

	}

	public boolean isInGame(User user) {
		return joueurs.contains(user);
	}

	public boolean isStarted() {
		return start;
	}

	public boolean initialized() {
		return initialize;
	}

	public boolean seekCommand() {
		return seekCommand;
	}

	public boolean ended() {
		return ended;
	}

}
