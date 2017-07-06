import java.util.ArrayList;
import java.util.HashMap;

import net.dv8tion.jda.core.entities.User;

public class Session {

	private static HashMap<String, Session> list;
	
	private String name;
	private User creator;
	private ArrayList<User> participants;
	
	
	public static void createSession(User u, String name){
		new Session(u, name);
	}
	
	public static void joinSession(User u, String name){
		list.get(name).addPlayer(u);
	}
	
	public void leaveSession(User u){
		//TODO
		
		participants.remove(u);
	}
	
	
	
	
	
	private Session(User u, String name ) {
		if( list == null) {
			list = new HashMap<>();
		}
		
		this.name = name;
		this.creator = u;
		list.put(name, this);
	}
	
	
	public void addPlayer(User u){
		//TODO
		
		participants.add(u);
	}
	
	
}
