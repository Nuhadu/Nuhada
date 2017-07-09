package command;

import channel.Asking;
import channel.UserInstance;
import commands.Command;
import general.FateCommandManager;
import general.Session;
import net.dv8tion.jda.core.entities.Message;

public class JoinSessionCommand extends Command {

	public static final String ID = "REJOINDRE_SESSION";
	private FateCommandManager cm;
	
	public JoinSessionCommand(FateCommandManager cm) {
		this.cm = cm;
	}
	
	@Override
	public void createNames() {
		names.add(ID);
		names.add("JOIN");
		names.add("REJOINDRE");
	}

	@Override
	public String process(UserInstance ui, Message m) {
		String str = "";
		String[] words= m.getContent().split(" ");
		for( int i=1; i < words.length; i++ ){
			if( i > 1 )
				str+= " ";
			str += words[i];
		}
		
		if( str.equals("") )
			return "Join quoi? Donne moi au moins le chiffre ou le nom :/";
		
		Session session = null;
		try{
			int i = Integer.decode(str) -1;
			
			session = Session.getSession(i);
			
		} catch (NumberFormatException e){
			session = Session.getSession(str);
		}
		
		if( session == null)
			return "Cette Session n'existe pas";
		
		if( cm.setSession(session) )
			return "Vous participez maintenant à la session: " + session.getName();
		else
			return "Essayez en une autre.";
	}

	@Override
	public String getName() {
		return ID;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		return "nope";
	}

}
