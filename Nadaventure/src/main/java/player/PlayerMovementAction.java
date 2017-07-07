package player;

import general.Action;

public class PlayerMovementAction implements Action {

	
	public boolean executable(Player player) {
		
		return true;
	}

	@Override
	public String execute(Player player, String args) {
		
		return "Bouge, bouge, lapinou <3";
	}

	
	
}
