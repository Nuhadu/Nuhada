package jdraranor.jdrchannel;

import java.util.ArrayList;

import jdraranor.JDRAranorEngine;
import jdraranor.JDRFileManager;
import jdraranor.UserInstance;
import jdraranor.command.JDRCommand;
import jdraranor.personnage.PersoJoueur;
import jdraranor.personnage.Personnage;
import jdraranor.personnage.races.Race;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class AccueilChannel extends JDRChannel {

	private AccueilChannel(TextChannel channel, ArrayList<JDRCommand> commands) {
		super(channel, commands);
	}
	
	public static AccueilChannel build(TextChannel channel){
		ArrayList<JDRCommand> commands = new ArrayList<JDRCommand>();
		commands.add(JDRCommand.NOUVEAU_PERSO);
		commands.add(JDRCommand.PERSO);
		
		return new AccueilChannel(channel, commands);
	}
	
	@Override
	protected String answer(JDRCommand command, Message message, UserInstance instance){
		switch(command){
			case NOUVEAU_PERSO:
				return nouveauPerso(message.getAuthor(), instance);
			case PERSO:
				return prendrePerso(message, instance);
		default:
			return "Oublié d'implémanter la commande " + command.toString() + " ?";
		}
		
	}
	
	@Override
	protected String asking(Message message, JDRAsking ask, UserInstance instance) {
		if(instance.ask.step != null)
			if(Ask.valueOf(instance.ask.step)==Ask.PERSO)
				return lancerPartie(message, instance);
		switch(instance.lastCommand){
		case NOUVEAU_PERSO:
			return nouveauPerso(message, instance);
		case PERSO:
			return askPerso(message, instance);
		default:
			return "Oups asking";
		}
	}
	
	private String nouveauPerso(User user, UserInstance instance){
		instance.perso = new PersoJoueur(user.getId(),-1);
		instance.ask = new JDRAsking(user, Ask.NAME.toString());
		
		return "Indiquer son nom:";
	}
	
	private String nouveauPerso(Message message, UserInstance instance){
		switch( Ask.valueOf(instance.ask.step)){
		case NAME:
			instance.perso.setName(message.getContent());
			instance.ask.step = Ask.RACE.toString();
			return "Choisir une race parmis: " + Race.getRaceList();
		case RACE:
			Race race = Race.getRaceFromString(message.getContent());
			if(race == null)
				return "Cette race n'existe pas. Choisir parmis: "+ Race.getRaceList();
			instance.perso.setRace(race);
			instance.ask.step = Ask.VALIDE_RACE.toString();
			return race.getDescription() + "\n Choisir cette race: oui, non?";
		case VALIDE_RACE:
			String msg = message.getContent().toUpperCase();
			if(msg.equals("OUI"))
			{
				instance.ask.step = Ask.PERSO.toString();
				int i =JDRFileManager.addPJ(message.getAuthor().getId(), instance.perso);
				instance.perso.setIndice(i);
				instance.persos = null;
				return "Voilà, votre personnage est fini! Lancer la partie? Oui, non?";
			}else if(msg.equals("NON")){
				instance.ask.step = Ask.RACE.toString();
				String str = "Choisir une race parmis: " + Race.getRaceList();
				return str;
			} else return "Réponse non comprise. Choisir cette race: oui, non?";
		}
		return "Oups nouveauPerso";
	}
	
	private String prendrePerso(Message message, UserInstance instance){
		if(instance.persos == null){
			instance.persos = JDRFileManager.getPJ(message.getAuthor().getId());
			if(instance.persos == null)
				return "Encore faudrait-il en avoir créé un";
		}
		
		if(instance.persos.isEmpty())
			return "Encore faudrait-il en avoir créé un";
		
		String str = "Vous avez :";
		int i =1;
		for(Personnage perso : instance.persos){
			str+="\n"+i+". "+perso;
			i++;
		}
		str += "\nLequel désirez vous jouer? (nombre correspondant)";
		instance.ask = new JDRAsking(message.getAuthor(), Ask.GET_PERSO.toString());
		return str;
	}
	
	private String askPerso(Message message, UserInstance instance){		
		int i =0;
		try{i = Integer.decode(message.getContent());} catch(NumberFormatException e){return "Ceci n'est pas un nombre.. Essaye encore";}
		if( i < 1 || i > instance.persos.size())
			return "Ce personnage n'existe pas, réessaye";
		instance.perso = instance.persos.get(i-1);
		instance.ask.step = Ask.PERSO.toString();
		return "Vous avez choisi : "+ instance.perso+ "\nLancer la partie? Oui, non?";
	}
	
	private String lancerPartie(Message message, UserInstance instance){
		String msg = message.getContent().toUpperCase();
		if(msg.equals("OUI")){
			instance.ask = null;
			JDRAranorEngine.launchGame(instance, message.getAuthor());
			return "GO !";
		}
		
		if(msg.equals("NON")){
			instance.ask = null;
			return "Oh.. Que puis-je d'autre pour vous?";
		}
		
		return "Je n'ai pas compris :/";
	}
	
	private enum Ask{
		NAME, RACE, VALIDE_RACE, GET_PERSO, PERSO;
	}
	


	

}
