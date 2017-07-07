package jdraranor.command.action;

import java.util.Random;

import jdraranor.combat.Combat;
import jdraranor.geographie.JDRVoyage;
import jdraranor.geographie.JDRVoyage.VoyageEvent;
import jdraranor.jdrchannel.PersoChannel;
import jdraranor.personnage.PersoJoueur;

public class AvancerVoyageAction extends JDRCommandAction {

	JDRVoyage voyage;
	public AvancerVoyageAction(PersoChannel perso, PersoJoueur joueur, JDRVoyage voyage) {
		super(perso, joueur);
		this.voyage = voyage;
	}

	@Override
	public void action() {
		voyage.avancer();
		/*if(voyage.avancer()){
			channel.sendMessage("Vous êtes arrivé à " +voyage.destination.key.name + ".");
			pj.voyage = null;
			return;
		}*/
		
		switch(advance()){
		case EMBUSCADE:
			pj.combat = new Combat();
			//pj.combat.addObserver(channel);
			channel.sendMessage("Vous tombez dans une embuscade. " + voyage.reposString());
		case NONE:
			channel.sendMessage("Rien ne se passe. " + voyage.periodeString()+ " " + voyage.reposString());
		case RENCONTRE:
			channel.sendMessage("Vous rencontrez quelqu'un mais on s'en fou."+ voyage.periodeString()+ " " + voyage.reposString());
		default:
			channel.sendMessage("WWUT?");
		}
		
	}
	
	public VoyageEvent advance(){
		Random rand = new Random();
		if(rand.nextInt(10) > 5)
			return VoyageEvent.EMBUSCADE;
		else 
			return VoyageEvent.NONE;
	}

	@Override
	public void askAction(String content) {
		// TODO Auto-generated method stub

	}

}
