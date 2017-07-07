package channel;

public enum Command {

	//General
	QUITTER("Quitter"), 
	COMMANDES("Commandes");
	//
	
	public String str;
	
	private Command(String str){
		this.str = str;
	}
	
	
	public static Command getCommand(String str){
		String var = str.toUpperCase();
		if(var.equals("HELP"))
			return COMMANDES;
		for(Command command : values())
			if(var.equals(command.str.toUpperCase()))
				return command;
		return null;
	}
}
