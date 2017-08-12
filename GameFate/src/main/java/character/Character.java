package character;

import java.util.List;

import character.role.Role;
import commands.Command;
import net.dv8tion.jda.core.entities.User;

public abstract class Character {

	private String name;
	private Role role;
	
	
	public Character(User u){
	}
	
	
	public Character setName( String name ){
		this.name = name;
		return this;
	}
	public Character setRole( Role role ){
		this.role = role;
		return this;
	}
	
	
	//Getter Setter
	public String getName(){ return name;}
	public Role getRoleName() {return role;}
	
	//To implements
	public abstract List<Command> getCommands();
	
	
	
}
