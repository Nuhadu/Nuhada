package channel;

import net.dv8tion.jda.core.entities.User;

public class Asking {

	
	public User author;
	public String step;
	public String var;

	public Asking(User author, String step) {
		this.author = author;
		this.step = step;
		this.var = "";
	}

	public Asking(User author, String step, String var) {
		this(author, step);
		this.var = var;
	}
}
