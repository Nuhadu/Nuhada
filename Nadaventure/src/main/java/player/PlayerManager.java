package player;

import java.util.ArrayList;
import java.util.List;

import general.Action;

public class PlayerManager {
	private Player player;
	private List<Action> actions;
	
	
	public PlayerManager(Player p){
		player = p;
		actions = new ArrayList<>();
	}
	
	
	public void getActions() {
		actions = player.getActions();
	}
	
	
	public void answer(String str) {
		
	}
	
}
