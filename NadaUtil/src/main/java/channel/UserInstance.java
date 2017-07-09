package channel;

import java.util.HashMap;

import commands.Command;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class UserInstance {
	
	private static HashMap<String, UserInstance> list = new HashMap<String, UserInstance>();

	public User user;
	public Command lastCommand;
	public CommandManager privateManager;
	//public PersoJoueur perso;
	//public List<PersoJoueur> persos;
	public TextChannel channelIn;
	public Asking ask;
	
	
	private UserInstance( User user){
		this.user = user;
		list.put(user.getId(), this);
	}
	
	public static UserInstance getUserInstance(User u) {
		if( !list.containsKey(u.getId()) ) {
			return new UserInstance(u);
		}
		else {
			return list.get(u.getId());
		}
	}
}
