package session;

import channel.Asking;
import channel.UserInstance;
import commands.Command;
import net.dv8tion.jda.core.entities.Message;

public class SearchSessionCommand extends Command {

	public final static String ID = "SEARCH_SESSION";
	
	public SearchSessionCommand() {
	}
	
	@Override
	public void createNames() {
		names.add(ID);
		names.add("SEARCH");
	}

	@Override
	public String process(UserInstance ui, Message m) {
		String res = "Voici la liste des sessions en cours: ";
		res += Session.toStringList(ui.user);
		return res;
	}

	@Override
	public String getName() {		
		return ID;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		String res = "Non géré?";
		return res;
	}
	
	
}
