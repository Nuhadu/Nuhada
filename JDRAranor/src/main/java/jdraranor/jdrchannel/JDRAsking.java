package jdraranor.jdrchannel;

import net.dv8tion.jda.core.entities.User;

public class JDRAsking {
	public User author;
	public String step;
	public String var;

	public JDRAsking(User author, String step) {
		this.author = author;
		this.step = step;
		this.var = "";
	}

	public JDRAsking(User author, String step, String var) {
		this(author, step);
		this.var = var;
	}
	

}
