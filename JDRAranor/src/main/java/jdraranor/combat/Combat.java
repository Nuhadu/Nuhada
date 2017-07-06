package jdraranor.combat;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import jdraranor.UserInstance;
import jdraranor.command.JDRCommand;
import jdraranor.personnage.Personnage;
import net.dv8tion.jda.core.entities.Message;

public class Combat extends Observable{
	private int tour;
	private Map map;
	private List<Personnage> persos;
	private HashMap<Coord, Personnage> positions;
	private long seed;
	private Random rand;
	
	private List<JDRCommand> commands;
	
	public Combat(){
		tour =0;
		seed = ThreadLocalRandom.current().nextLong();		
		init();
	}
	
	public void setPersos(List<Personnage> persos){
		this.persos = persos;
		Random r = new Random();
		Coord depart = new Coord(r.nextInt(map.maxX), r.nextInt(map.maxY));
		Stack<Coord> placement = map.voisinInoocupied(persos.size(), depart);
		for(Personnage p : persos)
			positions.put(placement.pop(), p);
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("tour", tour);
		json.put("seed", seed);
		JSONArray position = new JSONArray();
		for(Coord key : positions.keySet())
			position.put(key.toJSON());
		return json;
	}
	
	public Combat(JSONObject json){
		tour = json.getInt("tour");
		seed = json.getLong("seed");
		persos = new ArrayList<Personnage>();
		init();
	}
	
	
	private void init(){
		rand = new Random(seed);
		positions = new HashMap<Coord, Personnage>();
				
		
		map = new Map();
		commands = new ArrayList<JDRCommand>();
		commands.add(JDRCommand.FUIR);
	}
	
	public String enCombat(JDRCommand command, Message message, UserInstance instance) {
		switch(command){
		case FUIR:
			return fuir();
			default:
				return command.toString() + " non géré?";
		}
	}
	
	private String fuir(){
		setChanged();
		notifyObservers(JDRCommand.FUIR);		
		return "";
	}
	
	
	public List<JDRCommand> getCommands(){ return commands;}
	
	public File getMap() {
		BufferedImage m = map.draw();
		try {
		    File outputfile = new File("src/image/map.png");
		    ImageIO.write(m, "png", outputfile);
		    return outputfile;
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	}
	
	
	// CLASSES
	
	private class Coord{
		public int x;
		public int y;
		
		public Coord(int x, int y){
			this.x = x; this.y =y;
		}
		
		@Override
		public String toString(){
			return "("+x+","+y+")";
		}
		
		@Override
		public boolean equals(Object o){
			if(o == this) return true;
			
			if(o instanceof Coord){
				Coord c = (Coord) o;
				return (c.x == x && c.y == y);
			}
			
			return false;
		}
		
		public JSONObject toJSON(){
			JSONObject json = new JSONObject();
			json.put("x", x);
			json.put("y", y);			
			return json;
		}
	}
	
	private class Case{
		public boolean isOccupied;
		public CaseType type;
		
		
		public Case(){
			type = CaseType.getRdm(rand);
			isOccupied = false;
		}		
	}
	
	private enum CaseType{
		
		
		SOL("src/image/sol.png"), EAU("src/image/eau.png"), ALLY("src/image/ally.png"), ENNEMY("src/image/ennemy.png");
		
		public String src;
		
		CaseType(String src){
			this.src = src;
		}
		
		public static CaseType getRdm(Random rand){
			if(rand.nextInt(100)> 75)
				return EAU;
			else
				return SOL;		
		}
	}
	
	@SuppressWarnings("serial")
	private class Map extends HashMap<Coord, Case>{
		private final static int TAILLE = 20;
		private final static int CASE_SIZE = 120;
		public int maxX;
		public int maxY;
		
		public Map(int abs, int ord){
			maxX = abs;
			maxY = ord;
			for(int x =0; x<abs; x++)
				for(int y=0; y<ord; y++)
					put(new Coord(x,y), new Case());
		}
		
		public Map(){
			this(TAILLE,TAILLE);
			raffine();
		}
		
		private void raffine(){
			int defRafine = 2;
			
			List<Coord> voisin;
			for(Coord c : keySet()){
				voisin = voisin(c);
				if(get(c).type != CaseType.EAU)
					continue;
				int i =0;
				for(Coord vc : voisin)					
					if(get(vc).type == CaseType.EAU)
						i++;
				if( i < defRafine)
					get(c).type = CaseType.SOL;
			}
		}
		
		public List<Coord> voisin(Coord c){
			int modLigne;
			if(c.y%2 == 1)
				modLigne =1;
			else modLigne = -1;
			
			List<Coord> list = new ArrayList<Coord>();
			
			if(c.y > 0){//cases du dessus
				list.add(new Coord(c.x, c.y-1));
				if( (c.x >0 && modLigne == -1) || (c.x < maxX-1 && modLigne == 1))
					list.add(new Coord(c.x+modLigne, c.y-1));
			}
			if(c.y < maxY-1){//cases du dessous
				list.add(new Coord(c.x, c.y+1));
				if( (c.x >0 && modLigne == -1) || (c.x < maxX-1 && modLigne == 1))
					list.add(new Coord(c.x+modLigne, c.y+1));
			}
			//cases des côtés
			if(c.x > 0)list.add(new Coord(c.x-1, c.y));
			if(c.x < maxX-1)list.add(new Coord(c.x+1, c.y));
			return list;
		}
		
		public Stack<Coord> voisinInoocupied(int n, Coord depart){
			Stack<Coord> list = new Stack<Coord>();
			list.push(depart);
			List<Coord> voisinCentre = voisin(depart);
			Stack<Coord> queue = new Stack<Coord>();
			queue.addAll(voisin(depart));
			
			int iVoisin =0;
			while(list.size() < n){
				if(queue.isEmpty() && iVoisin < voisinCentre.size()){
					queue.addAll(voisin(voisinCentre.get(iVoisin)));
				}
				else if( queue.isEmpty()){
					System.out.println("Trop de personnages :/");
					break;
				}
				
				Coord c = queue.pop();
				if(!get(c).isOccupied){
					list.push(c);
					get(c).isOccupied = true;
				}
			}			
			return list;
		}
		
		public BufferedImage draw(){
			BufferedImage image = new BufferedImage(maxX*CASE_SIZE+CASE_SIZE/2, maxY*CASE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = image.createGraphics();
			
			for(Coord c : keySet()){
				int decal =0;
				if(c.y%2 == 1) decal = CASE_SIZE/2;
				BufferedImage mg = null;
				try{
					mg = ImageIO.read(new File(get(c).type.src));
				}catch(IOException e){
					e.printStackTrace();
				}
				g.drawImage(mg, c.x*CASE_SIZE+decal, c.y*(CASE_SIZE-14),CASE_SIZE, CASE_SIZE, null);
				
				if(get(c).isOccupied){
					Personnage perso = positions.get(c);
					try{
						mg = ImageIO.read(new File(CaseType.ALLY.src));
					}catch(IOException e){
						e.printStackTrace();
					}
					g.drawImage(mg, c.x*CASE_SIZE+decal, c.y*(CASE_SIZE-14),CASE_SIZE, CASE_SIZE, null);
				}
			}
			return image;
		}
		
		
		
		@Override
		public Case get(Object o){
			Coord key = null;
			for(Coord c: keySet())
				if(c.equals(o))
					key =c;
			return super.get(key);
		}
	}



	
}
