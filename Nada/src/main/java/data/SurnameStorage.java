package data;

import java.io.File;
import java.io.FileNotFoundException;
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addSurname(String str, String author) {
		File inputFile = new File(path);

		FileReader in;
		FileWriter out;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);

			JSONObject json = new JSONObject(tk);

			if (!json.has(author)) {
				json.put(author, new JSONArray());
			}

			json.getJSONArray(author).put(str);
			in.close();
			out = new FileWriter(inputFile);
			json.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
				json.put(author, new JSONArray());
			}
			str = json.getJSONArray(author).getString(rand.nextInt(json.getJSONArray(author).length()));
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
				json.put(author, new JSONArray());
			}
			Iterator<Object> it = json.getJSONArray(author).iterator();
			
			while(it.hasNext()){
				if(!str.equals(""))
					str += ", ";
				str += it.next().toString();
			}			
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}
