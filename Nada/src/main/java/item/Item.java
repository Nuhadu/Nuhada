package item;

import java.util.Random;
import java.util.StringTokenizer;

import Main.Mode;
import data.Affinity;
import data.Bank;
import data.Sentences;

public class Item {
	
	private ItemType type;
	private int quantity;
	
	private Item(ItemType type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}
	
	private Item(ItemType type){
		this(type,0);
	}
	
	public static Item decodeItem(String str){
		
		Item item = null;
		ItemType supposed = ItemType.supposedItemType(str);
		if(supposed == null)
			return null;
		
		switch(supposed){
		case ARGENT:
		case POMME:
			item = getEasy(str, supposed);
			break;
		}
		
		return item;
	}
	
	
	private static Item getEasy(String str, ItemType type){
		Item item = null;		
		Mode mode = new Mode();
		mode.i = null;
		mode.str = null;
		findTokens(str, mode);
		
		if(!ItemType.isType(mode.str, type) || mode.i == null)
			return null;
		
		item = new Item(type, mode.i);
		
		return item;
	}
	
	private static void findTokens(String str, Mode mode){
		StringTokenizer tk = new StringTokenizer(str);
		try{
			mode.i = Integer.decode(tk.nextToken());
		} catch(NumberFormatException e){
			mode.i = null;
		}
		mode.str = tk.nextToken().toUpperCase();
	}
	
	public static String cadeauItemFrom(Item item, String authorId){
		String str = Sentences.MERCI_USER;
		if(item.quantity <= 0){
			Affinity.changeAfinity(item.quantity*2-1, authorId);
			return Sentences.CADEAU_INEXIST;
		}
		
			switch(item.getType()){
			case ARGENT :
					if(Bank.retire(item.quantity, authorId))
						Affinity.changeAfinity(item.quantity*2, authorId);
					else
						return Sentences.ARGENT_MOQUE;
				break;
			case POMME:
				Affinity.changeAfinity(1, authorId);
				if( item.quantity > 1)
					str += " " +Sentences.PREND_UNIQUEF_NADA + "\n" + Sentences.EAT_POMME_NADA;
				else				
					str += "\n" + Sentences.EAT_POMME_NADA;
				break;
			}
		return str;
	}

	public Item cadeauItemTo(String authorId){
		switch(type){
		case ARGENT :
			Bank.retire(-quantity, authorId);
			break;
		case POMME:
			//nothing for the time being
			break;
				
		}
		return this;
	}
	public static Item cadeauItemTo(Item item, String authorId){
		return item.cadeauItemTo(authorId);
	}
	
	public static Item getRdmItem(){
		return new Item(ItemType.getRdmType(), 1);
	}
	
	
	private void setQuantity(int i) {
		quantity = i;
	}
	
	private ItemType getType(){
		return this.type;
	}
	
	public enum ItemType{
		ARGENT ("qual", "quals"), POMME ("pomme", "pommes");
		
		private String name;
		private String pluriel;
		
		ItemType(String name, String pluriel){
			this.name= name;
			this.pluriel= pluriel;
		}
		
		public static ItemType supposedItemType(String str){
			String msg = str.toUpperCase();
			for( ItemType type : values())
				if(msg.contains(type.name.toUpperCase()) || msg.contains(type.pluriel.toUpperCase()))
					return type;
			return null;
		}
		
		public static boolean isType(String str, ItemType type){
			String msg = str.toUpperCase();
			return (msg.equals(type.name.toUpperCase()) || msg.equals(type.pluriel.toUpperCase()));
		}
		
		@Override
		public String toString(){
			return name;
		}
		
		public String toString(int quantity){
			return (quantity > 1) ? pluriel : name;
		}
		
		public static ItemType getRdmType(){
			return ItemType.values()[ new Random().nextInt(ItemType.values().length)];
		}
	}
	
	@Override
	public String toString(){
		return ""+this.quantity+" " +this.type.toString(this.quantity);
	}
	
	
}
