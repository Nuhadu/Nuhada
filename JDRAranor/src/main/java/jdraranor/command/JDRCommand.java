package jdraranor.command;

public enum JDRCommand {
	NOUVEAU_PERSO("Nouveau Perso"), QUITTER("Quitter"), SCENARIO("Scenario"), COMMANDES("Commandes"), PERSO("Perso"),VOYAGE("Voyage"), 
	TEST("Test"), FUIR("Fuir"), CAMPER("Camper"), AVANCER("Avancer");
	
	public String str;
	
	private JDRCommand(String str){
		this.str = str;
	}
	
	public static JDRCommand getCommand(String str){
		String var = str.toUpperCase();
		if(var.equals("HELP"))
			return COMMANDES;
		for(JDRCommand command : values())
			if(var.equals(command.str.toUpperCase()))
				return command;
		return null;
	}
	
}
