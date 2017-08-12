package character;

import channel.Asking;
import channel.UserInstance;
import commands.Command;
import net.dv8tion.jda.core.entities.Message;

public class CreateServantCommand extends Command {

	@Override
	protected void createNames() {
		//neverUsed
	}

	@Override
	public String process(UserInstance ui, Message m) {
		String res = "";
		
		return res;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		// TODO Auto-generated method stub
		return null;
	}

}
