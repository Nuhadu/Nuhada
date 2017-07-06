package jdraranor.jdrchannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jdraranor.UserInstance;
import jdraranor.combat.Combat;
import jdraranor.command.JDRCommand;
import jdraranor.geographie.JDRMonde;
import jdraranor.geographie.JDRNode.NodeEnum;
import jdraranor.geographie.JDRVoyage;
import jdraranor.geographie.JDRVoyage.VoyageEvent;
import jdraranor.personnage.PersoJoueur;
import jdraranor.personnage.Personnage;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class PersoChannel extends JDRChannel implements Observer{
	private PersoJoueur perso;
	
	private PersoChannel(TextChannel channel, ArrayList<JDRCommand> commands, UserInstance instance) {
		super(channel, commands);
		this.perso = instance.perso;
		if(perso.combat != null)
			perso.combat.addObserver(this);
		if(perso.voyage != null)
			perso.voyage.addObserver(this);
		 if( perso.combat != null ){
				sendMessage("Bienvenue en Aranor. Vous êtes actuellement en combat!");
				try {
					channel.sendFile(perso.combat.getMap(), null).queue();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}else if(perso.voyage != null)
			sendMessage("Bienvenue en Aranor. Vous êtes actuellement en voyage vers " + perso.voyage.destination.key.name+ ".");
		else sendMessage("Bienvenue en Aranor. Vous êtes actuellement à " + perso.getPosition().key.name+ ".");
	}
	
	public static PersoChannel build(TextChannel channel, UserInstance instance){
		ArrayList<JDRCommand> commands = new ArrayList<JDRCommand>();		
		commands.add(JDRCommand.VOYAGE);
		commands.add(JDRCommand.TEST);
		return new PersoChannel(channel, commands, instance);
	}

	@Override
	protected List<JDRCommand> getCommands(){
		List<JDRCommand> commands = perso.getSpecialCommands();
		if( commands != null)
			return commands;
		return super.getCommands();
	}
	
	@Override
	protected String answer(JDRCommand command, Message message, UserInstance instance) {
		
		String str = perso.getSpecialAnswer(command, message, instance);
		if( str != null)
			return str;
		
		switch(command){
		case VOYAGE:
			return voyage(message.getAuthor(), instance);
		case TEST:
			return test();
		default:
			return "Oublié d'implémanter la commande " + command.toString() + " ?";
		}		
	}

	@Override
	protected String asking(Message message, JDRAsking ask, UserInstance instance) {
		switch(instance.lastCommand){
		case VOYAGE:
			return  askVoyage(message.getContent(), instance);
		default:
			return "Oups asking";
		}
	}
	
	private String test(){
		perso.combat = new Combat();
		perso.combat.addObserver(this);
		try {
			channel.sendFile(perso.combat.getMap(), null).queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "test";
	}
	
	private String voyage(User author, UserInstance instance){
		instance.ask = new JDRAsking(author, Ask.DEST.toString() );
		return JDRMonde.getInstance().getFrom(perso.getPosition().key) + "\n Laquelle emprunter?";
		
	}
	
	private String askVoyage(String content, UserInstance instance){
		String direction = content.toUpperCase();
		NodeEnum dir = NodeEnum.getNode(direction);
		if(dir == null)
			return "Direction non-reconnue";
		perso.voyage = new JDRVoyage(perso.getPosition(), JDRMonde.getInstance().get(dir));
		perso.voyage.addObserver(this);
		instance.ask = null;
		return "Début Voyage";
	}
	
	private enum Ask{
		DEST, BIVOUAC, NUIT, RENCONTRE;
	}

	@Override
	public void update(Observable o, Object arg) {		
		if(o == perso.combat){
			if(arg.equals(JDRCommand.FUIR))
				perso.combat = null;
			sendMessage("Vous prenez la fuite comme un lâche.\n"+ perso.voyage.periodeString());
		}
		else if(o == perso.voyage){
			if(arg.equals(VoyageEvent.EMBUSCADE)){
				perso.combat = new Combat();
				perso.combat.addObserver(this);
				ArrayList<Personnage> list = new ArrayList<Personnage>();
				list.add(perso);
				perso.combat.setPersos(list);
				try {
					channel.sendFile(perso.combat.getMap(), null).queue();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(arg.equals(VoyageEvent.FIN)){
				JDRVoyage voyage = perso.voyage;
				perso.voyage = null;
				perso.setPosition(voyage.destination.key);
				
				
			}
		}
	}
	
}
