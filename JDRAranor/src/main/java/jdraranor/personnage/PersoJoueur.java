package jdraranor.personnage;

import java.util.List;

import org.json.JSONObject;

import jdraranor.UserInstance;
import jdraranor.combat.Combat;
import jdraranor.command.JDRCommand;
import jdraranor.geographie.JDRVoyage;
import net.dv8tion.jda.core.entities.Message;

public class PersoJoueur extends Personnage {

	public String userId;
	private int indice;
	public JDRVoyage voyage;
	public Combat combat;
	
	public PersoJoueur(String userId, int indice){
		super();
		this.userId = userId;
		this.indice = indice;
	}
	
	public PersoJoueur(JSONObject json) {
		super(json);
		userId = json.getString("userId");
		if(json.has("voyage"))
			voyage = new JDRVoyage(json.getJSONObject("voyage"));
		if(json.has("combat"))
			combat = new Combat(json.getJSONObject("combat"));
	}
	
	public JSONObject toJSON(){
		JSONObject json = super.toJSON();
		json.put("userId", userId);
		if(voyage != null)
			json.put("voyage", voyage.toJSON());
		if(combat != null)
			json.put("combat", combat.toJSON());
		
		return json;
	}
	
	
	public List<JDRCommand> getSpecialCommands(){
		if(combat != null) return combat.getCommands();
		if(voyage != null) return voyage.getCommands();
		
		return null;
	}
	
	public String getSpecialAnswer(JDRCommand command, Message message, UserInstance instance){
		String str = null;
		if(combat != null){
			str=  combat.enCombat(command, message, instance);
		}
		
		if(voyage != null){
			str= voyage.enVoyage(command, message, instance);
		}
		updatePerso();
		return str;
	}
	
	public void setIndice(int i){
		indice=i;
	}
	
	@Override
	protected void updatePerso(){
		
	}

}
