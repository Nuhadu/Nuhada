package channelinstance;

import java.util.ArrayList;
import java.util.HashMap;
import jeux.Jeux;
import net.dv8tion.jda.core.entities.User;

public class ChannelInstance {

	public Jeux jeu;
	public ArrayList<User> users;
	public HashMap<User, Asking> askings;
	public HashMap<User, Absence> absences;
	public HashMap<User, MessageForYou> messages;
	public boolean greated;

	public ChannelInstance() {
		askings = new HashMap<>();
		absences = new HashMap<>();
		messages = new HashMap<>();
	}

}
