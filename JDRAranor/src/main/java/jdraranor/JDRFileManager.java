package jdraranor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import data.FileManager;
import jdraranor.personnage.PersoJoueur;
import jdraranor.personnage.Personnage;

public class JDRFileManager extends FileManager {
	private final static String path = "src/txt/jdr/";
	private final static String persoFile = "perso.txt";
	
	public static void init(){
		initFile(path+persoFile);
	}
	
	public static void addPlayerToPerso(String userId){
		addPlayerTo(userId, path+persoFile);
	}
	
	private static void addPlayerTo(String userId, String file){
		File inputFile = new File(file);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);

			json.put(userId, new JSONArray());
			in.close();
			update(json, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int addPJ(String userId, Personnage perso){
		File inputFile = new File(path+persoFile);
		FileReader in;
		int i = -1;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);
			
			if(!json.has(userId)){
				in.close();
				addPlayerToPerso(userId);
				in = new FileReader(inputFile);
				tk = new JSONTokener(in);
				json = new JSONObject(tk);
			}
			i = json.getJSONArray(userId).length();
			json.getJSONArray(userId).put(perso.toJSON());
			in.close();
			update(json, path+persoFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public static List<PersoJoueur> getPJ(String userId){
		ArrayList<PersoJoueur> perso = new ArrayList<>();
		File inputFile = new File(path+persoFile);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);
			
			if(!json.has(userId)){
				in.close();
				addPlayerToPerso(userId);
				return perso;
			}
			
			JSONArray list = json.getJSONArray(userId);
			
			for(int i =0; i<list.length(); i++)
				perso.add(new PersoJoueur(json.getJSONArray(userId).getJSONObject(i)));			
			in.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return perso;
	}

	public static void updatePerso(int indice, Personnage perso) {
		File inputFile = new File(path+persoFile);
		FileReader in;
		try {
			in = new FileReader(inputFile);
			JSONTokener tk = new JSONTokener(in);
			JSONObject json = new JSONObject(tk);
			if(perso instanceof PersoJoueur)
				json.getJSONArray(((PersoJoueur)perso).userId).put(indice, perso.toJSON());
			in.close();
			update(json, path+persoFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
