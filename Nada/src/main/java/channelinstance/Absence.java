package channelinstance;

import net.dv8tion.jda.core.entities.User;

public class Absence {

	private User author;
	private String reason;
	private String retour;

	public Absence(User author) {
		this.author = author;
		reason = null;
		retour = null;
	}

	public void changeRetour(String retour) {
		this.retour = retour;
	}

	public void changeReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		String str = author.getName() + " est absent.";
		if (reason != null)
			str += "\nIl a dit que c'était pour : '" + reason + "', mais je pense qu'il avait besoin d'intimité :D";
		if (retour != null)
			str += "\nIl a annoncé revenir: '" + retour + "'";
		return str;
	}

}
