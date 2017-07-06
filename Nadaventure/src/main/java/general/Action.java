package general;

import player.Player;
import player.PlayerMovementAction;

public interface Action {

	public static Action getAction(ActionEnum a) {
		Action action = null;
		switch(a) {
		case BOUGER:
			return new PlayerMovementAction();
		default:
			break;
			
		}
		
		return action;
	}
	
	public static ActionEnum valueof( String str) {
		return ActionEnum.valueOf(str);
	}
	
	public abstract String execute( Player player, String args );
	
	
	
	
	public enum ActionEnum {
		BOUGER;
	}
}
