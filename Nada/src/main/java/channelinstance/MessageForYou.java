package channelinstance;

import net.dv8tion.jda.core.entities.User;

public class MessageForYou {
	public User author;
	public String message;

	public MessageForYou(User author, String message) {
		this.author = author;
		this.message = message;
	}

	@Override
	public String toString() {
		return "#NAME te dit : " + message;
	}

}
