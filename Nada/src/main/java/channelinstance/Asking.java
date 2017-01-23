package channelinstance;

import net.dv8tion.jda.core.entities.User;

public class Asking {

	public User author;
	public AskType mode;
	public String var;

	public Asking(User author, AskType mode) {
		this.author = author;
		this.mode = mode;
		this.var = "";
	}

	public Asking(User author, AskType mode, String var) {
		this(author, mode);
		this.var = var;
	}
	
	public enum AskType {
		INTERPEL, QUESTION, ABSENCE, JEU;
		
		
	}
}
