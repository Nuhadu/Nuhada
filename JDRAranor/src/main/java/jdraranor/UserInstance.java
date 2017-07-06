package jdraranor;

import java.util.List;

import jdraranor.command.JDRCommand;
import jdraranor.jdrchannel.JDRAsking;
import jdraranor.personnage.PersoJoueur;
import jdraranor.personnage.Personnage;
import net.dv8tion.jda.core.entities.TextChannel;

public class UserInstance {
	
	public PersoJoueur perso;
	public List<PersoJoueur> persos;
	public TextChannel channelIn;
	public JDRCommand lastCommand;
	public JDRAsking ask;
	
	
}
