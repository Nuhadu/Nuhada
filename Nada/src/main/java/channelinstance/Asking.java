package channelinstance;

import net.dv8tion.jda.core.entities.User;

public class Asking {

	public User author;
	public int mode;
	public String var;

	public Asking(User author, int mode) {
		this.author = author;
		this.mode = mode;
		this.var = "";
	}

	public Asking(User author, int mode, String var) {
		this(author, mode);
		this.var = var;
	}
}
