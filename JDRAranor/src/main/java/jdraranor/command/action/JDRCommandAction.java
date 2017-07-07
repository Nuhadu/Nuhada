package jdraranor.command.action;

import jdraranor.jdrchannel.PersoChannel;
import jdraranor.personnage.PersoJoueur;

public abstract class JDRCommandAction {
	//must have
	protected PersoChannel channel;
	protected PersoJoueur pj;
		
	public JDRCommandAction(PersoChannel perso, PersoJoueur joueur){
		this.channel = perso;
		this.pj = joueur;
	}
	
	public abstract void action();
	public abstract void askAction(String content);
}
