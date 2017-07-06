package jdraranor.command.action;

import jdraranor.JDRAranorEngine;
import jdraranor.UserInstance;
import jdraranor.geographie.JDRMonde;
import jdraranor.geographie.JDRVoyage;
import jdraranor.geographie.JDRNode.NodeEnum;
import jdraranor.jdrchannel.JDRAsking;
import jdraranor.jdrchannel.PersoChannel;
import jdraranor.personnage.PersoJoueur;
import net.dv8tion.jda.core.entities.User;

public class VoyageAction extends JDRCommandAction {

	User user;
	UserInstance instance;

	public VoyageAction(PersoChannel perso, PersoJoueur pj, User user) {
		super(perso, pj);
		this.user = user;
		instance = JDRAranorEngine.geUserInstance(user);
	}

	@Override
	public void action() {
		instance.ask = new JDRAsking(user,null );
		channel.sendMessage( JDRMonde.getInstance().getFrom(pj.getPosition().key) + "\n Laquelle emprunter?");
	}
	
	@Override
	public void askAction(String content) {
		String direction = content.toUpperCase();
		NodeEnum dir = NodeEnum.getNode(direction);
		if(dir == null)
			channel.sendMessage("Direction non-reconnue");
		pj.voyage = new JDRVoyage(pj.getPosition(), JDRMonde.getInstance().get(dir));
		pj.voyage.addObserver(channel);
		instance.ask = null;
		channel.sendMessage("Début Voyage");
		
	}
	

	

}
