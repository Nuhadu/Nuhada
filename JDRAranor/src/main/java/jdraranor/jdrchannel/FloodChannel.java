package jdraranor.jdrchannel;

import java.util.ArrayList;

import jdraranor.UserInstance;
import jdraranor.command.JDRCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class FloodChannel extends JDRChannel {

	private FloodChannel(TextChannel channel, ArrayList<JDRCommand> commands) {
		super(channel, commands);
	}
	
	public static FloodChannel build(TextChannel channel){
		ArrayList<JDRCommand> commands = new ArrayList<JDRCommand>();
		
		return new FloodChannel(channel, commands);
	}

	@Override
	protected String answer(JDRCommand command, Message message, UserInstance instance) {
		// TODO Auto-generated method stub
		return "Pas encore fait";
	}

	@Override
	protected String asking(Message message, JDRAsking ask, UserInstance instance) {
		// TODO Auto-generated method stub
		return  "Pas encore fait";
	}
	
	
	//Ne fait RIEN du tout
	@Override
	public void answer( Message message, UserInstance instance){
	}

}
