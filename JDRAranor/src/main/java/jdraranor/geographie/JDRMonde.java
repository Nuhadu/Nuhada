package jdraranor.geographie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jdraranor.geographie.JDRNode.NodeEnum;

public class JDRMonde {

	private static JDRMonde instance;
	private HashMap<NodeEnum, JDRNode> map;
	private ListChemin routes;
	
	public static JDRMonde getInstance(){
		if(instance == null)
			instance = new JDRMonde();
		return instance;
	}
	
	private JDRMonde(){
		map = new HashMap<>();
		routes = new ListChemin();
		
		for(NodeEnum n : NodeEnum.values() )
			map.put(n, new JDRNode(n));
		
		routes.put(new Chemin(NodeEnum.AKERAI, NodeEnum.QUETAIN), 5.f);
		routes.put(new Chemin(NodeEnum.QUETAIN, NodeEnum.FEIRAL), 7.f);
		routes.put(new Chemin(NodeEnum.WERANOI, NodeEnum.QUETAIN), 4.f);
		routes.put(new Chemin(NodeEnum.ENETARI, NodeEnum.WERANOI), 3.f);
		routes.put(new Chemin(NodeEnum.AKERAI, NodeEnum.FEIRAL), 8.f);
		routes.put(new Chemin(NodeEnum.SEPHIRAI, NodeEnum.QUETAIN), 10.f);
		routes.put(new Chemin(NodeEnum.AKERAI, NodeEnum.SEPHIRAI), 6.f);
		routes.put(new Chemin(NodeEnum.FEIRAL, NodeEnum.SEPHIRAI), 8.f);
	}
	
	public JDRNode get(NodeEnum n){return map.get(n);}
	
	public Float goFromTo(NodeEnum from, NodeEnum to){
		Chemin c = new Chemin(from, to);
		Float f = routes.get(c);
		if( f== null)
			return 0.f;
		return f;
	}
	
	
	private class Chemin{
		public NodeEnum node1;
		public NodeEnum node2;
		
		public Chemin(NodeEnum a, NodeEnum b){
			this.node1 = a;
			this.node2 =b;
		}
		
		public boolean has(NodeEnum n){
			return (node1==n || node2==n);
		}
		
		public String to(NodeEnum from){
			if(from == node1)
				return node2.name;
			return node1.name;
		}		
		@Override
		public boolean equals(Object o){
			if(o==this)
				return true;
			if(!(o instanceof Chemin))
				return false;
			
			Chemin b = (Chemin) o;
			if(b.node1 == node1 && b.node2 == node2)
				return true;
			if(b.node2 == node1 && b.node1 == node2)
				return true;
				
			return false;
		}
	}
	
	
	public String getFrom(NodeEnum n){
		String str = "Les routes proposées sont :";
		for(Chemin c : routes.getFrom(n)){
			str+= "\n"+c.to(n) + " ("+routes.get(c)+" jours)";
		}
		return str;
	}
	
	@SuppressWarnings({ "serial" })
	private class ListChemin extends HashMap<Chemin, Float>{
		
		public List<Chemin> getFrom(NodeEnum n){
			List<Chemin> l = new ArrayList<Chemin>();
			for(Chemin c : keySet())
				if(c.has(n))
					l.add(c);
		return l;
		}
		
		@Override
		public Float get(Object o){
			for(Chemin c : keySet())
				if(c.equals(o))
					return super.get(c);
			
			return null;
		}
	}
	
}
