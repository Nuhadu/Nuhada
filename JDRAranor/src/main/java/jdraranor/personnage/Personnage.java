package jdraranor.personnage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import jdraranor.JDRFileManager;
import jdraranor.geographie.JDRMonde;
import jdraranor.geographie.JDRNode;
import jdraranor.geographie.JDRNode.NodeEnum;
import jdraranor.personnage.formations.Formation;
import jdraranor.personnage.races.Race;

public abstract class Personnage {
	
	//data
	private Statistiques stats;
	private Race race;
	private List<Formation> formations;
	private String name;
	private JDRNode position;
	private long jours;
	public boolean isJoueur;
	
	public Personnage(){
		stats = new Statistiques();
		formations = new ArrayList<Formation>();
		position = JDRMonde.getInstance().get(NodeEnum.QUETAIN);
		jours = 0;
		isJoueur = true;
	}
	
	public Personnage(JSONObject json){
		name = json.getString("nom");
		race = Race.getRaceFromString(json.getString("race"));
		stats = new Statistiques(json.getJSONObject("stats"));
		formations = new ArrayList<Formation>();
		position = JDRMonde.getInstance().get(NodeEnum.valueOf(json.getString("position")));
		jours = json.getLong("jours");
		JSONArray forms = json.getJSONArray("formations");
		for(int i=0; i<forms.length(); i++)
			formations.add(new Formation(forms.getJSONObject(i)));
		
		
		
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("nom", name);
		json.put("stats", stats.toJSON());
		json.put("race", race.getRace().toString());
		json.put("position", position.key.toString());
		json.put("formations", new JSONArray());
		json.put("jours", jours);
		for(Formation form : formations)
			json.getJSONArray("formations").put(form.toJSON());
		
		
		return json;
	}
	
	public void setRace(Race race){
		this.race = race;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addFormatino(Formation formation){
		formations.add(formation);
		updatePerso();
	}

	@Override
	public String toString(){
		return name + " " + race.getName()+" " +position.key.name;
	}

	public String getName() {		
		return name;
	}
	
	public JDRNode getPosition(){
		return position;
	}
	
	public void setPosition(NodeEnum pos){
		position = JDRMonde.getInstance().get(pos);
		updatePerso();
	}
	
	
	
	protected abstract void updatePerso();

	
	
	
	
}
