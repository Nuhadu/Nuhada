package jdraranor.personnage.races;

import jdraranor.personnage.Statistiques;

public abstract class Race {
	
	private Statistiques stats;
	private String description;
	
	
	public Race( Statistiques stats, String description){
		this.stats = stats;
		this.description = description;
	}
	
	public abstract RaceEnum getRace();
	
	public Statistiques getStats(){
		return stats;
	}
	
	public String getDescription(){return description;}
	
	
	public static Race getRaceFromString(String str){
		RaceEnum en;
		try{
			en = RaceEnum.valueOf(str.toUpperCase());
		} catch(IllegalArgumentException e){		
			return null;
		}
		
		switch(en){
		case ELFE:
			return new ElfeRace();
		case HUMAIN:
			return new HumainRace();
		}
		return null;
	}
	
	public static String getRaceList(){
		String str = "";
		for(RaceEnum name : RaceEnum.values()){
			if(!str.equals(""))
				str+=" ";
			str += name.name;
		}
		return str;
	}
	
	public enum RaceEnum{
		HUMAIN("Humain"), ELFE("Elfe");
		
		public String name;
		
		private RaceEnum(String str){
			this.name=str;
		}
	}

	public abstract String getName();
}
