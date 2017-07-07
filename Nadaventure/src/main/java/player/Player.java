package player;

import java.util.ArrayList;
import java.util.List;

import general.Action;
import general.geo.Position;
import net.dv8tion.jda.core.entities.User;

public class Player {
	private User user;
	private Position position;
	
	
	public List<Action> getActions() {
		List<Action> actions = new ArrayList<>();		
		return actions;
	}
	
	
	
}
