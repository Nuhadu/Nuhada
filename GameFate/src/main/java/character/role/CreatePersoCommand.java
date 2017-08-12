package character.role;

import channel.Asking;
import channel.UserInstance;
import character.role.Role.RoleName;
import character.Character;
import commands.Command;
import general.FateCommandManager;
import net.dv8tion.jda.core.entities.Message;
import session.Session;

public class CreatePersoCommand extends Command {

	public final static String ID = "CREATE_PERSO";
	private final static String ROLE_STEP = "ROLE_STEP";
	
	private Character perso;
	private FateCommandManager cm;
	private Session s;
	
	public CreatePersoCommand(FateCommandManager cm, Session s) {
		this.cm = cm;
		this.s = s;
	}
	
	
	@Override
	protected void createNames() {
		names.add(ID);
		//Probably never added in user commands

	}

	@Override
	public String process(UserInstance ui, Message m) {
		String res = "Créons ton perso. Si tu as un Rôle de prédilection merci de choisir: ";
		res += "\n0. Hasard";
		int i = 1;
		for( RoleName r : RoleName.values()) {
			res += "\n" +i+". " + r.toString();
			i++;
		}
		ui.ask = new Asking(ui.user, ROLE_STEP);
		return res;
	}

	@Override
	public String getName() {
		return ID;
	}

	@Override
	public String processAsk(UserInstance ui, Message m, Asking a) {
		String content = m.getContent();
		String res = "";
		
		switch(a.step){
		case ROLE_STEP:
			RoleName r = null;
			try {
				int i = Integer.decode(content);
				if( (i >= 0) && (i < RoleName.values().length) )
					r = RoleName.values()[i-1];
				
			} catch(NumberFormatException e){
				r = RoleName.valueOf(content);
			}
			
			RoleName choice = s.obtainRole(r);
			if( r!=null && choice != r )
				res = "Navré, ce rôle n'est plus disponnible. ";
			
			res += "Vous êtes " + choice.toString();
			
			perso = Role.createCharacterFromRole(choice, ui.user);
			
			return res;
		}
		return "Non géré?";
	}

}
