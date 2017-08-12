package session;

import channel.Asking;
import channel.UserInstance;
import commands.Command;
import net.dv8tion.jda.core.entities.Message;

public class CreateSessionCommand extends Command {
	
	public final static String ID = "CREATE_SESSION";
	private final static String CREATE_STEP = "CREATE_STEP";

	@Override
	public void createNames() {
		names.add(ID);
		names.add("CREATE");
		
	}

	@Override
	public String process(UserInstance ui, Message m) {
		ui.ask = new Asking(ui.user, CREATE_STEP);
		
		return "Quel est le nom de cette nouvelle partie?";
	}

	@Override
	public String getName() {
		return ID;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		String str = m.getContent();
		switch(a.step){
		case CREATE_STEP:
			if( Session.getSession(str) != null)
				return "Cette session existe déjà. Donne m'en un autre :/";
			
			Session.createSession(ui.user, str);
			ui.ask = null;
			
			return "Votre session " + str + " a bien été créée.";
		}
		return "Non géré?";
	}

}
