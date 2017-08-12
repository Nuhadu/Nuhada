package character.role;

import character.Master;
import character.Servant;
import net.dv8tion.jda.core.entities.User;
import character.Character;

public abstract class Role {

	private String name;
	
	public Role(String name ){
		this.name = name;
	}
	
	
	//Getter Setter
	public String getName(){ return name; }
	
	
	public static enum RoleName{
		MASTER, SERVANT;
	}
	
	public static Character createCharacterFromRole(RoleName r, User u){
		switch(r){
		case MASTER:
			return new Master(u);
		case SERVANT:
			return new Servant(u);
		default:
			return null;
		}
	}
	
}
