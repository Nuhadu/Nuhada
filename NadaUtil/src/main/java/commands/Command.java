package commands;

import java.util.ArrayList;

import channel.Asking;
import channel.UserInstance;
import net.dv8tion.jda.core.entities.Message;

public abstract class Command {
	
	protected ArrayList<String> names;
	
	public Command() {
		names = new ArrayList<>();
		createNames();
	}

	public boolean isThisCommand(String str){
		for(String name : names){
			if( str.toUpperCase().startsWith(name) )
				return true;
		}
		
		return false;
	};
	
	abstract protected void createNames();
	
	abstract public String process(UserInstance ui, Message m);
	
	abstract public String getName();
	
	abstract public String processAsk(UserInstance ui, Message m, Asking a);
	
}
