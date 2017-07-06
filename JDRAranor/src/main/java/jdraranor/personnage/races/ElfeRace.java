package jdraranor.personnage.races;

import jdraranor.personnage.Statistiques;

public class ElfeRace extends Race {

	private final static String description = "Un elfe quoi.";
	private final static Statistiques stats = new Statistiques();
	
	public ElfeRace() {
		super(stats, description);
	}

	@Override
	public RaceEnum getRace() {
		return RaceEnum.ELFE;
	}

	@Override
	public String getName() {		
		return "Elfe";
	}

}
