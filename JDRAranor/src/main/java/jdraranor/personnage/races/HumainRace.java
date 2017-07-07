package jdraranor.personnage.races;

import jdraranor.personnage.Statistiques;

public class HumainRace extends Race {

	private final static String description = "Un homme quoi.";
	private final static Statistiques stats = new Statistiques();
	
	public HumainRace() {
		super(stats, description);
	}

	@Override
	public RaceEnum getRace() {
		return RaceEnum.HUMAIN;
	}

	@Override
	public String getName() {
		return "Humain";
	}

}
