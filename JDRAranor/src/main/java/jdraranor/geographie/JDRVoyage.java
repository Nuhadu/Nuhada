package jdraranor.geographie;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import org.json.JSONObject;

import jdraranor.UserInstance;
import jdraranor.command.JDRCommand;
import jdraranor.command.JDRCommandHolder;
import jdraranor.geographie.JDRNode.NodeEnum;
import net.dv8tion.jda.core.entities.Message;

public class JDRVoyage extends Observable implements JDRCommandHolder{
	private JDRNode oldLocation;
	public JDRNode destination;
	private float joursRestants;
	public float jourDeVoyage;
	private List<JDRCommand> commands;
	private  Periode periode;
	private boolean repos;
	
	
	private JDRVoyage(){
		commands = new ArrayList<JDRCommand>();
		commands.add(JDRCommand.AVANCER);
	}
	
	public JDRVoyage(JDRNode old, JDRNode dest){
		this();
		oldLocation = old;
		destination = dest;
		joursRestants = JDRMonde.getInstance().goFromTo(old.key, dest.key);
		jourDeVoyage = 1;		
		periode = Periode.JOUR;		
		repos = true;
	}
	
	public JDRVoyage(JSONObject json){
		this();
		oldLocation = JDRMonde.getInstance().get( NodeEnum.valueOf(json.getString("old")));
		destination = JDRMonde.getInstance().get( NodeEnum.valueOf(json.getString("dest")));
		joursRestants = new Float(json.getDouble("joursR"));
		jourDeVoyage = new Float(json.getDouble("joursV"));
		periode = Periode.valueOf(json.getString("periode"));
		repos = json.getBoolean("repos");
		
		if(periode == Periode.NUIT)
			commands.add(JDRCommand.CAMPER);
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("old", oldLocation.key.toString());
		json.put("dest", destination.key.toString());
		json.put("joursR", joursRestants);
		json.put("joursV", jourDeVoyage);
		json.put("periode", periode.toString());
		json.put("repos", repos);
		
		return json;
	}
	
	
	
	
	public String enVoyage(JDRCommand command, Message message, UserInstance instance) {
		switch(command){
		case CAMPER:
			return camper();
		case AVANCER:
			return avancer();
			default:
				return command.toString() + " non géré?";
		}
	}
	
	private String camper(){
		repos = true;
		changePeriode();
		return "Vous camper pour la nuit.\n Il ne se passe rien: " + periodeString();
	}
	
	public String avancer(){
		if(periode == Periode.NUIT)
			repos = false;
		
		changePeriode();
		if(joursRestants == 0){
			setChanged();
			notifyObservers(VoyageEvent.FIN);
			
			return "Vous êtes arrivé à " +destination.key.name + ".";
		}
		
		switch(advance()){
		case EMBUSCADE:
			setChanged();
			notifyObservers(VoyageEvent.EMBUSCADE);			
			return "Vous tombez dans une embuscade. " + reposString();
		case NONE:
			return "Rien ne se passe. " + periodeString()+ " " + reposString();
		case RENCONTRE:
			return "Vous rencontrez quelqu'un mais on s'en fou."+ periodeString()+ " " + reposString();
		default:
			return "WWUT?";
		
		}
	}
	
	public String reposString(){
		if(repos)
			return "";
		else return "Vous êtes exténué.";
	}
	
	public String periodeString(){
		switch(periode){
		case JOUR:
			return "Jour "+((int)jourDeVoyage)+".";
		case NUIT:
			return "La nuit est proche, camper?";
		}
		
		return "";
	}
	
	private void changePeriode(){
		switch(periode){
		case JOUR:
			jourDeVoyage+=0.6;
			periode = Periode.NUIT;
			commands.add(JDRCommand.CAMPER);
			break;
		case NUIT:
			jourDeVoyage+=0.4;
			periode = Periode.JOUR;
			commands.remove(JDRCommand.CAMPER);
			joursRestants --;
			break;		
		}
	}
	
	public List<JDRCommand> getCommands(){
		return commands;
	}
	
	public VoyageEvent advance(){
		Random rand = new Random();
		if(rand.nextInt(10) > 5)
			return VoyageEvent.EMBUSCADE;
		else 
			return VoyageEvent.NONE;
	}
	
	public float finJour(){
		joursRestants--;
		jourDeVoyage++;
		
		if(joursRestants == 0)
			return -1;
		else
			return jourDeVoyage;		
	}
	
	public enum VoyageEvent{
		NONE, EMBUSCADE, RENCONTRE, FIN;
	}
	
	public enum Periode{
		JOUR, NUIT;
	}


}
