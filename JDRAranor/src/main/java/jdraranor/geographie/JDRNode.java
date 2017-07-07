package jdraranor.geographie;

import java.util.HashMap;

import javax.xml.soap.Node;

import jdraranor.geographie.JDRNode.NodeEnum;

public class JDRNode {
	public NodeEnum key;
	
	public JDRNode(NodeEnum n){
		key = n;
	}
	
	
	public enum NodeEnum{
		QUETAIN("Quetaïn"), AKERAI("Akeraï"), FEIRAL("Feïral"), WERANOI("Weranoï"), SEPHIRAI("Sephiraï"), ENETARI("Enetari");
		
		public String name;
		
		private NodeEnum(String name){
			this.name= name;
		}

		public static NodeEnum getNode(String direction) {
			for(NodeEnum n : values()){
				if(n.toString().toUpperCase().equals(direction))
					return n;
				if(n.name.toUpperCase().equals(direction))
					return n;
			}
			return null;
		}
	}
}
