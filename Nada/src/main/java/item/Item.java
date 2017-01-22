package item;

import java.util.StringTokenizer;

import data.Affinity;
import net.dv8tion.jda.core.entities.User;

public class Item {
	
	private ItemType type;
	private int quantity = 0;
	
	private Item(ItemType type){
		this.type = type;
	}
	
	public static Item decodeItem(String str){
		
		Item item = null;
		
		if(str.toUpperCase().contains("QUAL")){
			StringTokenizer tk = new StringTokenizer(str);
			try{
			int number = Integer.decode(tk.nextToken());
			item = new Item(ItemType.ARGENT);
			item.setQuantity(number);
			} catch(NumberFormatException e){
				return null;
			}
		}
		
		return item;
	}
	

	
	public static String cadeauItem(Item item, String author){
		String str = "merci";
			switch(item.getType()){
			case ARGENT :
				Affinity.changeAfinity(item.quantity*2, author);
				break;
				default :
					
			}
		return str;
	}
	
	
	private void setQuantity(int i) {
		quantity = i;
	}
	
	private ItemType getType(){
		return this.type;
	}
	
	public enum ItemType{
		ARGENT;
	}
}
