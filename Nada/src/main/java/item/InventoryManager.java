/**
 * 
 */
package item;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author nuhad
 *
 */
public class InventoryManager {
	private final static String path = "src/txt/inventory.txt";

	public static void init() {
		File inputFile = new File(path);
		try {
			if (inputFile.createNewFile()) {
				FileWriter in;
				in = new FileWriter(inputFile);
				JSONObject json = new JSONObject();
				json.write(in);
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addAuthor(String author) {
		File inputFile = new File(path);

		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);

			json.put(author, new JSONObject());
			in.close();
			update(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addItem(Item item, String author) {
		File inputFile = new File(path);
		String key = item.getType().toString();
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(author)) {
				in.close();	
				addAuthor(author);						
				in = new FileReader(inputFile);
				tk = new JSONTokener(in);
				json = new JSONObject(tk);
			}
			if(!json.getJSONObject(author).has(key))
				json.getJSONObject(author).put(key, item.toJson());
			else{
				int quantity = json.getJSONObject(author).getJSONObject(key).getInt("quantity");
				json.getJSONObject(author).getJSONObject(key).put("quantity",  quantity + item.getQuantity());
			}
			in.close();
			update(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Item getItem(String author, Item item) {
		Item retour = null;
		File inputFile = new File(path);
		FileReader in;
		String key = item.getType().toString();
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(author))
			{
				in.close();	
				addAuthor(author);
			}			
			else if(json.getJSONObject(author).length() > 0){
				if(json.getJSONObject(author).has(key))
					retour = Item.fromJson(key, json.getJSONObject(author).getJSONObject(key));
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public static boolean retireItem(Item item, String authorId){
		boolean retour = false;
		File inputFile = new File(path);
		FileReader in;
		String key = item.getType().toString();
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(authorId))
			{
				in.close();	
				addAuthor(authorId);
			}			
			else if(json.getJSONObject(authorId).has(key)){
					Item pick = Item.fromJson(key, json.getJSONObject(authorId).getJSONObject(key) );
					if( pick.getQuantity() >= item.getQuantity()){
						json.getJSONObject(authorId).getJSONObject(key).put("quantity", pick.getQuantity()-item.getQuantity());
						retour = true;
					}
				in.close();
				update(json);
			}
			else in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public static List<Item> getAllItem(String author) {
		ArrayList<Item> str =  new ArrayList<Item>();
		File inputFile = new File(path);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(author)) {
				in.close();	
				addAuthor(author);
			}
			else if(json.getJSONObject(author).length() > 0) {
				for(String key : json.getJSONObject(author).keySet())				
					str.add(Item.fromJson(key, json.getJSONObject(author).getJSONObject(key)));
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static void update(JSONObject json) {
		File inputFile = new File(path);
		FileWriter out;
		try {
			out = new FileWriter(inputFile);
			json.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
