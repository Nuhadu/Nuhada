package session;

import java.util.ArrayList;
import java.util.HashMap;

import character.role.Role.RoleName;
import net.dv8tion.jda.core.entities.User;

public class Session {

	private static HashMap<String, Session> list = new HashMap<>();
	
	private String name;
	private User creator;
	private ArrayList<User> participants;
	
	private HashMap<RoleName, ArrayList<User>> byRoles;
	
	public static void createSession(User u, String name){
		new Session(u, name);
	}
	
	public static boolean joinSession(User u, String name){
		if( !list.containsKey(name) )
			return false;
		
		if( list.get(name).participants.size() > 14 )
			return false;
		
		list.get(name).addPlayer(u);
		return true;
	}
	
	public void leaveSession(User u){
		participants.remove(u);
	}	
	
	
	private Session(User u, String name ) {		
		this.name = name;
		this.creator = u;
		list.put(name, this);
		participants = new ArrayList<>();
		participants.add(u);
		byRoles = new HashMap<>();
		for(RoleName r : RoleName.values()) {
			byRoles.put(r, new ArrayList<User>() );
		}
	}
	
	
	public void addPlayer(User u){
		participants.add(u);
	}
	
	/**
	 * Return the role of the player, witch can be the one he has chosen if there are places available.
	 * @param r
	 * @return
	 */
	public RoleName obtainRole( RoleName r) {
		ArrayList<User> servants = byRoles.get(RoleName.SERVANT);
		ArrayList<User> masters = byRoles.get(RoleName.MASTER);
		
		if(servants.size() == 7)
			return RoleName.MASTER;
		if(masters.size() == 7 )
			return RoleName.SERVANT;
		
		if( r == null ){// hasard
			if( servants.size() <= masters.size() )
				return RoleName.SERVANT;
			else 
				return RoleName.MASTER;
		}
		
		return r;
	}
	
	
	//getter/setter
	public String getName(){return name;}
	
	public User getCreator(){return creator;}
	
	//tests
	public boolean contains( User u ) { return participants.contains(u); }

	
	/**
	 * Return a text deciption of the existing sessions
	 * @return
	 */
	public static String toStringList( User u) {
		if(list.isEmpty())
			return "Aucune Session en cours";
		
		String res = "";
		int i = 1;
		for( Session s : list.values()) {
			res += "/n"+i+": " + s.name;
			if(u != null && s.participants.contains(u) ){
				res += " (Participe)";
			}
		}
		return res;
	}
	
	public static Session getSession(int i){
		return ( i>0 && i<list.size() ) ? list.values().toArray(new Session[0])[i] : null;
	}
	
	public static Session getSession(String name){
		return list.keySet().contains(name) ? list.get(name) : null;
	}
	
}
