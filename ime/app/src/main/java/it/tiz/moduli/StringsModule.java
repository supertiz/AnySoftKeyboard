package it.tiz.moduli;

import com.google.gson.Gson;

import java.util.Map;

public class StringsModule {
	public static final String SPACE_STRING=" ";
	
	private StringsModule(){}
	
	public static boolean isEmpty(String s){
		return s == null || s.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> fromStringToJson(String s) {
		return new Gson().fromJson(s, Map.class);
	}
}
