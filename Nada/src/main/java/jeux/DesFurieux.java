<<<<<<< HEAD
package jeux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import net.dv8tion.jda.core.entities.User;

public class DesFurieux extends Jeux {

	final static String regles = "J'espère pour toi que tu sais compter! \nChaque joueur possède une couleur, il y a deux dés d'une même couleur pour chacun d'entre eux. Chaque tour, les dés sont lancés en même temps. Il faudra alors te saisir le plus rapidement de ceux nécessaires pour obtenir 10 en aditionnant leurs scores et éviter d'attraper un dé de valeur interdite (annoncé en début de tour s'il y en a un).\n Attention: Tu dois boire si:"
			+ "\n\t*Si tu prends au moins un dé mais que tu n'atteinds pas ou dépasse 10 (excepté si tu obtiens 20).\n\t*Si un de tes adversaires parvient à attteindre le score pile.\n\t*Si un de tes adversaires obtient un 20, tu écopes alors d'un malus de 3 au prochain tour.\n\t*Si un de tes adversaires attrape quatre dés de la même valeur\n\t*Si, lorsqu'apparaissent deux paires différentes, tu n'es pas le premier à écrire: 'A boire et le diable avait réglé leur sort!'"
			+ "\n\t*Si tu pronnonces cette phrase au mauvais moment.\n\t*Si tu prends les deux dés d'une même couleur, autre que la tienne, et qu'ils font 10.\n\t*Si tu attrapes un dé interdit. Si tu en attrapes deux tu as un malus de 3 au tour suivant. Prêt?";

	private int dices;
	private int colors;
	private int valeurInterdite;
	private boolean waitingRetry;
	private ArrayList<Integer> values;
	private int[] table;
	private HashMap<User, ArrayList<String>> dicesTaken;
	private String[] fautes; // Pas atteint 10, pas atteint 20, mal annoncée
								// double parie, valeur interdite, valeur
								// interdite 2, 10 de couleur adverse
	private String[] reussites;// atteint 10, atteint 20, Annoncé double paire,
								// 4 dés,
	private int[] malus, malusP;
	private boolean[] pass;
	private boolean[] pris;

	public DesFurieux(ArrayList<User> joueurs) {
		super(joueurs, regles);
		dices = joueurs.size() * 2;
		colors = joueurs.size();
		dicesTaken = new HashMap<>();
		values = new ArrayList<Integer>();
		pris = new boolean[dices];
		malus = new int[] { 0, 0, 0, 0, 0, 0 };
		malusP = new int[] { 0, 0, 0, 0, 0, 0 };
		for (User user : joueurs)
			dicesTaken.put(user, new ArrayList<String>());
	}

	@Override
	public String initialize() {
		table = new int[] { 0, 0, 0, 0, 0, 0 };
		fautes = new String[] { "", "", "", "", "", "" };
		reussites = new String[] { "", "", "", "" };
		pass = new boolean[] { false, false, false, false, false, false };
		values.clear();

		initialize = true;
		String str = " Les couleurs des joueurs sont: ";

		int i = 0;
		for (User joueur : joueurs) {
			dicesTaken.get(joueur).clear();
			pris[i] = false;
			pris[i + colors] = false;
			str += " " + joueur.getName() + " " + getColor(i);
			i++;
		}
		return str + " " + super.initialize();
	}

	@Override
	public String play() {
		start = true;

		String str = "Les dés sont jetés :\n";
		Random rand = new Random();
		int dé = -1;
		for (int i = 0; i < dices; i++) {
			dé = rand.nextInt(6);
			values.add(dé + 1);
			table[dé]++;
			str += dé + 1 + " " + getColor(i) + "\n";
		}

		str += "Annoncez vos prises avec: prends 4 rouge, ou équivalent. Si vous ne voulez plus de dés tapez 'passe'.";
		seekCommand = true;
		return str;
	}

	private String getColor(int i) {
		switch (i % colors) {
		case 0:
			return "rouge";
		case 1:
			return "bleu";
		case 2:
			return "gris";
		case 3:
			return "vert";
		case 4:
			return "jaune";
		}
		return "WHUT";
	}

	private int getIndiceColor(String color) {
		int fact = -1;
		if (color.equals("ROUGE"))
			fact = 0;
		else if (color.equals("ROUGE"))
			fact = 1;
		else if (color.equals("BLEU"))
			fact = 2;
		else if (color.equals("GRIS"))
			fact = 3;
		else if (color.equals("VERT"))
			fact = 4;
		else if (color.equals("JAUNE"))
			fact = 5;
		return fact;
	}

	private int getDice(int number, String color) {

		int fact = -1;
		if (color.equals("ROUGE"))
			fact = 0;
		else if (color.equals("BLEU"))
			fact = 1;
		else if (color.equals("GRIS"))
			fact = 2;
		else if (color.equals("VERT"))
			fact = 3;
		else if (color.equals("JAUNE"))
			fact = 4;

		if (fact == -1)
			return -1;
		if (fact + 1 > joueurs.size())
			return -1;

		if (values.get(fact) == number && !pris[fact])
			return fact;
		else if (values.get(fact + colors) == number)
			return fact + colors;
		else
			return -2;

	}

	@Override
	public String endTurn() {
		int[] boissons = { 0, 0, 0, 0, 0, 0 };
		StringTokenizer st;
		seekCommand = false;
		String str = "Fin de la manche! C'est l'heure de compter!";

		for (User joueur : joueurs) {
			str += "\n" + joueur.getName();
			int total = 0;
			int[] colors = { 0, 0, 0, 0, 0, 0 };
			int[] values = { 0, 0, 0, 0, 0, 0 };

			for (String take : dicesTaken.get(joueur)) {
				st = new StringTokenizer(take);
				int i = Integer.decode(st.nextToken());
				values[i - 1]++;
				String color = st.nextToken();
				total += i;
				colors[getIndiceColor(color.toUpperCase())] += i;
			}
			str += " a obtenu " + total + " avec un malus de " + malus[joueurs.indexOf(joueur)];
			// Faute
			if (total > 0 && total + malus[joueurs.indexOf(joueur)] < 10) {
				fautes[0] += joueur.getName() + " ";
				boissons[joueurs.indexOf(joueur)]++;
			} else if (total + malus[joueurs.indexOf(joueur)] > 10 && total != 20) {
				fautes[1] += joueur.getName() + " ";
				boissons[joueurs.indexOf(joueur)]++;
			}
			if (valeurInterdite > 0) {
				if (values[valeurInterdite - 1] >= 1) {
					fautes[3] += joueur.getName() + " ";
					boissons[joueurs.indexOf(joueur)]++;
				}
				if (values[valeurInterdite - 1] >= 2)
					fautes[4] += joueur.getName() + " ";
			}
			for (int i = 0; i < this.colors; i++) {
				if (colors[i] == 10 && !joueurs.get(i).equals(joueur)) {
					fautes[5] += joueur.getName() + " ";
					boissons[joueurs.indexOf(joueur)]++;
					malusP[joueurs.indexOf(joueur)] -= 3;
					break;
				}

			}
			// reussite
			if (total + malus[joueurs.indexOf(joueur)] == 10) {
				reussites[0] += joueur.getName() + " ";
				for (int i = 0; i < this.colors; i++)
					if (i != joueurs.indexOf(joueur))
						boissons[i]++;
			}
			if (total + malus[joueurs.indexOf(joueur)] == 20) {
				reussites[1] += joueur.getName() + " ";
				for (int i = 0; i < this.colors; i++)
					if (i != joueurs.indexOf(joueur)) {
						boissons[i]++;
						malusP[i] -= 3;
					}
			}
			for (int i = 0; i < 6; i++) {
				if (values[i] >= 4) {
					reussites[2] += joueur.getName() + " ";
					for (int j = 0; j < this.colors; j++)
						if (j != joueurs.indexOf(joueur))
							boissons[j]++;
					break;
				}
			}
		}

		for (User joueur : joueurs) {
			if (fautes[2].contains(joueur.getName()))
				boissons[joueurs.indexOf(joueur)]++;
			if (reussites[2].contains(joueur.getName()))
				for (int i = 0; i < joueurs.size(); i++)
					if (i != joueurs.indexOf(joueur))
						boissons[i]++;
		}

		for (int i = 0; i < this.colors; i++) {
			malus[i] = malusP[i];
			malusP[i] = 0;
		}

		str += "\nFAUTES\n\t*N'ont pas formé 10 ni 20: " + fautes[0] + fautes[1];
		str += "\n\t*Ont mal placé la double paire :" + fautes[2];
		str += "\n\t*Ont attrapté un dé interdit :" + fautes[3];
		str += "\n\t*Ont attrapé un 2eme dé interdit :" + fautes[4];
		str += "\n\t*Ont fait 10 avec une couleur adverse :" + fautes[5];
		str += "\nVICTOIRES\n\t*Ont formé 10 :" + reussites[0];
		str += "\n\t*Ont formé 20 :" + reussites[1];
		str += "\n\t*A déclaré la double paire en premier:" + reussites[2];
		str += "\n\t*Ont attrapé 4 dés de la même valeur :" + reussites[3];

		str += "\n\n BOISSONS ET MALUS :";
		int i = 0;
		for (User joueur : joueurs) {
			str += "\n" + joueur.getName() + " " + boissons[i] + " coups et malus de " + malus[i];
			i++;
		}
		str += "\nPour un autre tour, dîtes 'Reine Nuhada'";
		seekCommand = true;
		waitingRetry = true;
		return str;

	}

	@Override
	public String endGame() {
		return super.endGame();

	}

	@Override
	public String receive(String message, User author) {
		String msg = message.toUpperCase();
		if (msg.equals("A BOIRE ET LE DIABLE AVAIT RÉGLÉ LEUR SORT!")) {
			if (!reussites[2].equals("")) {
				return "Désolé " + author.getName() + ", " + reussites[2] + " t'as déjà devancé!";
			} else {
				int j = 0;
				for (int i = 0; i < 6; i++) {
					if (table[i] >= 2)
						j++;
					if (j >= 2) {
						reussites[2] += author.getName();
						return "Félicitation tu es le premier à l'avoir écrit!";
					}
				}
				fautes[2] += author.getName();
				return "Félicitation, tu t'es honteusement trompé!";
			}
		}
		if (msg.startsWith("PRENDS")) {
			if (pass[joueurs.indexOf(author)])
				return "Désolé, " + author.getName() + " mais tu as déjà dit que tu passais !";
			StringTokenizer st = new StringTokenizer(message);
			if (st.countTokens() >= 3) {
				String token1 = st.nextToken();
				int token2 = Integer.decode(st.nextToken());
				String token3 = st.nextToken();

				if (token1.toUpperCase().equals("PRENDS"))
					token1 = author.getName() + " prends le dé ";
				else
					token1 = author.getName() + token1 + "? le dé ";

				if (getDice(token2, token3.toUpperCase()) == -1)
					return "Cette couleur n'existe pas, bougre d'âne!";
				else if (getDice(token2, token3.toUpperCase()) == -2) {
					return "Ce chiffre n'existe même pas, tricheur!";
				} else if (!pris[getDice(token2, token3.toUpperCase())]) {
					dicesTaken.get(author).add(token2 + " " + token3.toLowerCase());
					pris[getDice(token2, token3.toUpperCase())] = true;
					// values.remove(getDice(token2,token3.toUpperCase()));
					boolean reste = false;
					for (int i = 0; i < this.dices; i++)
						if (!pris[i]) {
							reste = true;
							break;
						}

					if (!reste) {
						return token1 + token2 + " " + token3.toLowerCase() + "\n" + endTurn();
					}
					return token1 + token2 + " " + token3.toLowerCase();
				} else
					return "Ce dé a déjà été ramassé, dommage :/";
			}
		} else if (msg.equals("PASSE")) {
			pass[joueurs.indexOf(author)] = true;
			boolean allPass = true;
			for (int i = 0; i < joueurs.size(); i++) {
				if (!pass[i]) {
					allPass = false;
					break;
				}
			}

			if (allPass)
				return author.getName() + " ne désire plus prendre de dés\n" + endTurn();
			else
				return author.getName() + " ne désire plus prendre de dés";
		}

		if (waitingRetry) {
			if (msg.equals("REINE NUHADA") || msg.equals("PUTE")) {
				waitingRetry = false;
				initialize();
				return play();
			}
		}
		return super.receive(msg, author);
	}

	@Override
	public boolean concerned(String message, User author) {
		String msg = message.toUpperCase();
		if (msg.contains("A BOIRE ET LE DIABLE AVAIT RÉGLÉ LEUR SORT!"))
			return true;
		if (msg.contains("PRENDS"))
			return true;
		if (msg.contains("REINE NUHADA"))
			return true;
		if (msg.contains("PUTE"))
			return true;
		if (msg.equals("PASSE"))
			return true;
		return super.concerned(msg, author);
	}

}
=======
package jeux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import net.dv8tion.jda.core.entities.User;

public class DesFurieux extends Jeux {

	final static String regles = "J'espère pour toi que tu sais compter! \nChaque joueur possède une couleur, il y a deux dés d'une même couleur pour chacun d'entre eux. Chaque tour, les dés sont lancés en même temps. Il faudra alors te saisir le plus rapidement de ceux nécessaires pour obtenir 10 en aditionnant leurs scores et éviter d'attraper un dé de valeur interdite (annoncé en début de tour s'il y en a un).\n Attention: Tu dois boire si:"
			+ "\n\t*Si tu prends au moins un dé mais que tu n'atteinds pas ou dépasse 10 (excepté si tu obtiens 20).\n\t*Si un de tes adversaires parvient à attteindre le score pile.\n\t*Si un de tes adversaires obtient un 20, tu écopes alors d'un malus de 3 au prochain tour.\n\t*Si un de tes adversaires attrape quatre dés de la même valeur\n\t*Si, lorsqu'apparaissent deux paires différentes, tu n'es pas le premier à écrire: 'A boire et le diable avait réglé leur sort!'"
			+ "\n\t*Si tu pronnonces cette phrase au mauvais moment.\n\t*Si tu prends les deux dés d'une même couleur, autre que la tienne, et qu'ils font 10.\n\t*Si tu attrapes un dé interdit. Si tu en attrapes deux tu as un malus de 3 au tour suivant. Prêt?";

	private int dices;
	private int colors;
	private int valeurInterdite;
	private boolean waitingRetry;
	private ArrayList<Integer> values;
	private int[] table;
	private HashMap<User, ArrayList<String>> dicesTaken;
	private String[] fautes; // Pas atteint 10, pas atteint 20, mal annoncée
								// double parie, valeur interdite, valeur
								// interdite 2, 10 de couleur adverse
	private String[] reussites;// atteint 10, atteint 20, Annoncé double paire,
								// 4 dés,
	private int[] malus, malusP;
	private boolean[] pass;
	private boolean[] pris;

	public DesFurieux(ArrayList<User> joueurs) {
		super(joueurs, regles);
		dices = joueurs.size() * 2;
		colors = joueurs.size();
		dicesTaken = new HashMap<>();
		values = new ArrayList<Integer>();
		pris = new boolean[dices];
		malus = new int[] { 0, 0, 0, 0, 0, 0 };
		malusP = new int[] { 0, 0, 0, 0, 0, 0 };
		for (User user : joueurs)
			dicesTaken.put(user, new ArrayList<String>());
	}

	@Override
	public String initialize() {
		table = new int[] { 0, 0, 0, 0, 0, 0 };
		fautes = new String[] { "", "", "", "", "", "" };
		reussites = new String[] { "", "", "", "" };
		pass = new boolean[] { false, false, false, false, false, false };
		values.clear();

		initialize = true;
		String str = " Les couleurs des joueurs sont: ";

		int i = 0;
		for (User joueur : joueurs) {
			dicesTaken.get(joueur).clear();
			pris[i] = false;
			pris[i + colors] = false;
			str += " " + joueur.getName() + " " + getColor(i);
			i++;
		}
		return str + " " + super.initialize();
	}

	@Override
	public String play() {
		start = true;

		String str = "Les dés sont jetés :\n";
		Random rand = new Random();
		int dé = -1;
		for (int i = 0; i < dices; i++) {
			dé = rand.nextInt(6);
			values.add(dé + 1);
			table[dé]++;
			str += dé + 1 + " " + getColor(i) + "\n";
		}

		str += "Annoncez vos prises avec: prends 4 rouge, ou équivalent. Si vous ne voulez plus de dés tapez 'passe'.";
		seekCommand = true;
		return str;
	}

	private String getColor(int i) {
		switch (i % colors) {
		case 0:
			return "rouge";
		case 1:
			return "bleu";
		case 2:
			return "gris";
		case 3:
			return "vert";
		case 4:
			return "jaune";
		}
		return "WHUT";
	}

	private int getIndiceColor(String color) {
		int fact = -1;
		if (color.equals("ROUGE"))
			fact = 0;
		else if (color.equals("ROUGE"))
			fact = 1;
		else if (color.equals("BLEU"))
			fact = 2;
		else if (color.equals("GRIS"))
			fact = 3;
		else if (color.equals("VERT"))
			fact = 4;
		else if (color.equals("JAUNE"))
			fact = 5;
		return fact;
	}

	private int getDice(int number, String color) {

		int fact = -1;
		if (color.equals("ROUGE"))
			fact = 0;
		else if (color.equals("BLEU"))
			fact = 1;
		else if (color.equals("GRIS"))
			fact = 2;
		else if (color.equals("VERT"))
			fact = 3;
		else if (color.equals("JAUNE"))
			fact = 4;

		if (fact == -1)
			return -1;
		if (fact + 1 > joueurs.size())
			return -1;

		if (values.get(fact) == number && !pris[fact])
			return fact;
		else if (values.get(fact + colors) == number)
			return fact + colors;
		else
			return -2;

	}

	@Override
	public String endTurn() {
		int[] boissons = { 0, 0, 0, 0, 0, 0 };
		StringTokenizer st;
		seekCommand = false;
		String str = "Fin de la manche! C'est l'heure de compter!";

		for (User joueur : joueurs) {
			str += "\n" + joueur.getName();
			int total = 0;
			int[] colors = { 0, 0, 0, 0, 0, 0 };
			int[] values = { 0, 0, 0, 0, 0, 0 };

			for (String take : dicesTaken.get(joueur)) {
				st = new StringTokenizer(take);
				int i = Integer.decode(st.nextToken());
				values[i - 1]++;
				String color = st.nextToken();
				total += i;
				colors[getIndiceColor(color.toUpperCase())] += i;
			}
			str += " a obtenu " + total + " avec un malus de " + malus[joueurs.indexOf(joueur)];
			// Faute
			if (total > 0 && total + malus[joueurs.indexOf(joueur)] < 10) {
				fautes[0] += joueur.getName() + " ";
				boissons[joueurs.indexOf(joueur)]++;
			} else if (total + malus[joueurs.indexOf(joueur)] > 10 && total != 20) {
				fautes[1] += joueur.getName() + " ";
				boissons[joueurs.indexOf(joueur)]++;
			}
			if (valeurInterdite > 0) {
				if (values[valeurInterdite - 1] >= 1) {
					fautes[3] += joueur.getName() + " ";
					boissons[joueurs.indexOf(joueur)]++;
				}
				if (values[valeurInterdite - 1] >= 2)
					fautes[4] += joueur.getName() + " ";
			}
			for (int i = 0; i < this.colors; i++) {
				if (colors[i] == 10 && !joueurs.get(i).equals(joueur)) {
					fautes[5] += joueur.getName() + " ";
					boissons[joueurs.indexOf(joueur)]++;
					malusP[joueurs.indexOf(joueur)] -= 3;
					break;
				}

			}
			// reussite
			if (total + malus[joueurs.indexOf(joueur)] == 10) {
				reussites[0] += joueur.getName() + " ";
				for (int i = 0; i < this.colors; i++)
					if (i != joueurs.indexOf(joueur))
						boissons[i]++;
			}
			if (total + malus[joueurs.indexOf(joueur)] == 20) {
				reussites[1] += joueur.getName() + " ";
				for (int i = 0; i < this.colors; i++)
					if (i != joueurs.indexOf(joueur)) {
						boissons[i]++;
						malusP[i] -= 3;
					}
			}
			for (int i = 0; i < 6; i++) {
				if (values[i] >= 4) {
					reussites[2] += joueur.getName() + " ";
					for (int j = 0; j < this.colors; j++)
						if (j != joueurs.indexOf(joueur))
							boissons[j]++;
					break;
				}
			}
		}

		for (User joueur : joueurs) {
			if (fautes[2].contains(joueur.getName()))
				boissons[joueurs.indexOf(joueur)]++;
			if (reussites[2].contains(joueur.getName()))
				for (int i = 0; i < joueurs.size(); i++)
					if (i != joueurs.indexOf(joueur))
						boissons[i]++;
		}

		for (int i = 0; i < this.colors; i++) {
			malus[i] = malusP[i];
			malusP[i] = 0;
		}

		str += "\nFAUTES\n\t*N'ont pas formé 10 ni 20: " + fautes[0] + fautes[1];
		str += "\n\t*Ont mal placé la double paire :" + fautes[2];
		str += "\n\t*Ont attrapté un dé interdit :" + fautes[3];
		str += "\n\t*Ont attrapé un 2eme dé interdit :" + fautes[4];
		str += "\n\t*Ont fait 10 avec une couleur adverse :" + fautes[5];
		str += "\nVICTOIRES\n\t*Ont formé 10 :" + reussites[0];
		str += "\n\t*Ont formé 20 :" + reussites[1];
		str += "\n\t*A déclaré la double paire en premier:" + reussites[2];
		str += "\n\t*Ont attrapé 4 dés de la même valeur :" + reussites[3];

		str += "\n\n BOISSONS ET MALUS :";
		int i = 0;
		for (User joueur : joueurs) {
			str += "\n" + joueur.getName() + " " + boissons[i] + " coups et malus de " + malus[i];
			i++;
		}
		str += "\nPour un autre tour, dîtes 'Reine Nuhada'";
		seekCommand = true;
		waitingRetry = true;
		return str;

	}

	@Override
	public String endGame() {
		return super.endGame();

	}

	@Override
	public String receive(String message, User author) {
		String msg = message.toUpperCase();
		if (msg.equals("A BOIRE ET LE DIABLE AVAIT RÉGLÉ LEUR SORT!")) {
			if (!reussites[2].equals("")) {
				return "Désolé " + author.getName() + ", " + reussites[2] + " t'as déjà devancé!";
			} else {
				int j = 0;
				for (int i = 0; i < 6; i++) {
					if (table[i] >= 2)
						j++;
					if (j >= 2) {
						reussites[2] += author.getName();
						return "Félicitation tu es le premier à l'avoir écrit!";
					}
				}
				fautes[2] += author.getName();
				return "Félicitation, tu t'es honteusement trompé!";
			}
		}
		if (msg.startsWith("PRENDS")) {
			if (pass[joueurs.indexOf(author)])
				return "Désolé, " + author.getName() + " mais tu as déjà dit que tu passais !";
			StringTokenizer st = new StringTokenizer(message);
			if (st.countTokens() >= 3) {
				String token1 = st.nextToken();
				int token2 = Integer.decode(st.nextToken());
				String token3 = st.nextToken();

				if (token1.toUpperCase().equals("PRENDS"))
					token1 = author.getName() + " prends le dé ";
				else
					token1 = author.getName() + token1 + "? le dé ";

				if (getDice(token2, token3.toUpperCase()) == -1)
					return "Cette couleur n'existe pas, bougre d'âne!";
				else if (getDice(token2, token3.toUpperCase()) == -2) {
					return "Ce chiffre n'existe même pas, tricheur!";
				} else if (!pris[getDice(token2, token3.toUpperCase())]) {
					dicesTaken.get(author).add(token2 + " " + token3.toLowerCase());
					pris[getDice(token2, token3.toUpperCase())] = true;
					// values.remove(getDice(token2,token3.toUpperCase()));
					boolean reste = false;
					for (int i = 0; i < this.dices; i++)
						if (!pris[i]) {
							reste = true;
							break;
						}

					if (!reste) {
						return token1 + token2 + " " + token3.toLowerCase() + "\n" + endTurn();
					}
					return token1 + token2 + " " + token3.toLowerCase();
				} else
					return "Ce dé a déjà été ramassé, dommage :/";
			}
		} else if (msg.equals("PASSE")) {
			pass[joueurs.indexOf(author)] = true;
			boolean allPass = true;
			for (int i = 0; i < joueurs.size(); i++) {
				if (!pass[i]) {
					allPass = false;
					break;
				}
			}

			if (allPass)
				return author.getName() + " ne désire plus prendre de dés\n" + endTurn();
			else
				return author.getName() + " ne désire plus prendre de dés";
		}

		if (waitingRetry) {
			if (msg.equals("REINE NUHADA") || msg.equals("PUTE")) {
				waitingRetry = false;
				initialize();
				return play();
			}
		}
		return super.receive(msg, author);
	}

	@Override
	public boolean concerned(String message, User author) {
		String msg = message.toUpperCase();
		if (msg.contains("A BOIRE ET LE DIABLE AVAIT RÉGLÉ LEUR SORT!"))
			return true;
		if (msg.contains("PRENDS"))
			return true;
		if (msg.contains("REINE NUHADA"))
			return true;
		if (msg.contains("PUTE"))
			return true;
		if (msg.equals("PASSE"))
			return true;
		return super.concerned(msg, author);
	}

}
>>>>>>> refs/remotes/origin/master
