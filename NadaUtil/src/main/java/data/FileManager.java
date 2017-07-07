package data;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public abstract class FileManager {

	protected static void initFile(String path) {
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
	
	protected static void update(JSONObject json, String path) {
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
