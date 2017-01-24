package data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class SurnameStorage {

	private final static String path = "src/txt/surnom.txt";

	public static void initSurnmae() {
		File inputFile = new File(path);
		try {
			if (inputFile.createNewFile()) {
				FileWriter in;
				in = new FileWriter(inputFile);
				JSONObject json = new JSONObject();
				json.write(in);
				in.close();
				addSurname("mon petit", "ALL");
				addSurname("mon braillard", "ALL");
				addSurname("Bob", "ALL");
				addSurname("matelot", "ALL");
				addSurname("moussaillon", "ALL");
				addSurname("petit malin", "ALL");
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

			json.put(author, new JSONArray());
			in.close();
			update(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addSurname(String str, String author) {
		File inputFile = new File(path);

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

			json.getJSONArray(author).put(str);
			in.close();
			update(json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSurname(String author) {
		String str = "";
		Random rand = new Random();
		File inputFile = new File(path);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(author))
			{
				in.close();	
				addAuthor(author);
			}
			else if(json.getJSONArray(author).length() > 0){
				str = json.getJSONArray(author).getString(rand.nextInt(json.getJSONArray(author).length()));
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String getAllSurname(String author) {
		String str = "";
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
			else if(json.getJSONArray(author).length() > 0) {
				Iterator<Object> it = json.getJSONArray(author).iterator();
				
				while(it.hasNext()){
					if(!str.equals(""))
						str += ", ";
					str += it.next().toString();
				}			
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
