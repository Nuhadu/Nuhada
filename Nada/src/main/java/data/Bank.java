package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class Bank {
	private final static String path = "src/txt/bank.txt";

	public static void initBank() {
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

	private static void addBank(String author) {
		File inputFile = new File(path);

		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);

			json.put(author, 0);
			in.close();
			update(json);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean retire(int i, String author) {
		File inputFile = new File(path);

		FileReader in;
		boolean retrait = false;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);

			if (!json.has(author)) {
				in.close();
				addBank(author);
				in = new FileReader(inputFile);
				tk = new JSONTokener(in);
				json = new JSONObject(tk);
			}

			if (i <= 0 || i <= json.getInt(author)) {
				json.put(author, json.getInt(author) - i);
				retrait = true;
			}
			in.close();
			update(json);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retrait;
	}

	public static int getSolde(String author) {
		int str = -1;
		File inputFile = new File(path);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);

			if (!json.has(author)) {
				in.close();
				addBank(author);
				in = new FileReader(inputFile);
				tk = new JSONTokener(in);
				json = new JSONObject(tk);
			}

			str = json.getInt(author);
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
