package commands;

import channel.Asking;
import channel.CommandManager;
import channel.UserInstance;
import net.dv8tion.jda.core.entities.Message;

public class DisplayCommandsCommand extends Command {

	public static final String ID = "COMMANDES";
	
	CommandManager cm;
	
	public DisplayCommandsCommand( CommandManager cm) {
		this.cm = cm;
	}
	
	@Override
	public void createNames() {
		names.add(ID);
		names.add("HELP");
	}

	@Override
	public String process(UserInstance ui, Message m) {
		String res = "Commandes disponnibles: ";
		res += cm.listCommand();
		return res;
	}

	@Override
	public String getName() {
		return ID;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		// none
		return null;
	}

}
